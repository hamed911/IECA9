package net.internetengineering.server;

import net.internetengineering.domain.Customer;
import net.internetengineering.domain.dealing.*;
import net.internetengineering.exception.DataIllegalException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.internetengineering.domain.Transaction;
import net.internetengineering.exception.DBException;
import net.internetengineering.model.CustomerDAO;
import net.internetengineering.model.InstrumentDAO;
import net.internetengineering.model.InstrumentOfferingDAO;
import net.internetengineering.model.OfferingDAO;

/**
 * Created by Hamed Ara on 2/18/2016.
 */
public class StockMarket {
    private static StockMarket stockMarket=null;
    private static boolean testMode=false;
    //private static List<Instrument> instruments;
    //private static HashMap<String, Customer> customers;

    private StockMarket(){
//        instruments = new ArrayList<Instrument>();
//        customers = new HashMap<String, Customer>();
    }

    public static StockMarket getInstance(){
        if(stockMarket==null)
            stockMarket=new StockMarket();
        return stockMarket;
    }

    public void addNewCustomer(Customer newOne,Connection dbConnection) throws SQLException{
        CustomerDAO.addNewCustomer(newOne, dbConnection);
    }

    public Boolean containCustomer(String id ,Connection dbConnection) throws SQLException{
        return CustomerDAO.containCustomer(id, dbConnection);
    }

    public void executeFinancialTransaction(String id, TransactionType type, Long amount,Connection dbConnection) throws SQLException, DBException{
        CustomerDAO.findByID(id, dbConnection).executeTransaction(type, amount,dbConnection);
    }

    public Boolean customerHasEnoughMoney(String id,Long amount,Connection dbConnection)throws Exception{
        return CustomerDAO.findByID(id, dbConnection).hasEnoughMoney(amount);
    }

    public void executeSellingOffer(PrintWriter out, SellingOffer offer, String symbol, Connection dbConnection,boolean isOwner) throws DataIllegalException, DBException, SQLException{
        if(isOwner)
            addOrUpdateInstrumentByOwner(symbol,offer,dbConnection);
        Instrument instrument = loadVerifiedParameters(offer,symbol,dbConnection);
        Customer customer = CustomerDAO.findByID(offer.getID(),dbConnection);
        if(!customer.hasEnoughStock(symbol, offer) && !offer.isAdminOffer()){
            throw new DataIllegalException("Not enough share");
        }
        OfferingDAO.insertNewOffering(offer,symbol, dbConnection);
        if(isOwner)
            InstrumentOfferingDAO.insertInstrOffer(offer.getID(), symbol, offer.getDbID(), dbConnection);
        instrument.executeSellingByType(out,offer,dbConnection);
        if(offer.getQuantity()>0 && !isOwner)
            InstrumentOfferingDAO.insertInstrOffer(instrument.getCustomerID(), symbol, offer.getDbID(), dbConnection);
    }

    public void executeBuyingOffer(PrintWriter out, BuyingOffer offer, String symbol,Connection dbConnection, boolean isOwner) throws DataIllegalException, SQLException, DBException{
        Customer customer = CustomerDAO.findByID(offer.getID(),dbConnection);
        if(!customer.hasEnoughMoney(offer.getPrice() * offer.getQuantity())){
            throw  new DataIllegalException("Not enough money");
        }
        if(isOwner)
            deleteOrUpdateInstrumentByOwner(symbol,offer,dbConnection);
        else
            customer.executeTransaction(TransactionType.WITHDRAW, offer.getPrice()*offer.getQuantity(),dbConnection);
        Instrument instrument = loadVerifiedParameters(offer, symbol,dbConnection);
        OfferingDAO.insertNewOffering(offer,symbol ,dbConnection);
        instrument.executeBuyingByType(out, offer,dbConnection);
        if(offer.getQuantity()>0 &&!isOwner)
            InstrumentOfferingDAO.insertInstrOffer(instrument.getCustomerID(), symbol, offer.getDbID(), dbConnection);
    }

    private void addOrUpdateInstrumentByOwner(String symbol,Offering offer,Connection dbConnection) throws SQLException, DBException {
    	boolean flag = false;
    	for(Instrument i : getInstruments(dbConnection)){
            if(i.symbolIsMatched(symbol)){
                i.changeQuantity("add", offer.getQuantity());
                InstrumentDAO.updateQuantity(offer.getID(), symbol, i.getQuantity(),true, dbConnection);
                flag = true;
                break;
            }
        }
    	if(!flag)
//    		instruments.add(new Instrument(symbol, offer.getQuantity()));
            InstrumentDAO.insertNewInstrument(offer.getID(), symbol, offer.getQuantity(),true, dbConnection);
        
    }
    private void deleteOrUpdateInstrumentByOwner(String symbol,Offering offer, Connection dbConnection) throws SQLException, DBException {
        for(Instrument i : getInstruments(dbConnection)){
            if(i.symbolIsMatched(symbol)){
                i.changeQuantity("delete", offer.getQuantity());
                Boolean availdable = offer.getQuantity()>=0? true: false;
                InstrumentDAO.updateQuantity(offer.getID(), symbol, i.getQuantity(),availdable, dbConnection);
                return;
            }
        }
    }

    private Instrument loadVerifiedParameters(Offering offer, String symbol,Connection dbConnection) throws DataIllegalException, SQLException, DBException {
        Instrument instrument=null;
        if(!containCustomer(offer.getID(),dbConnection))
            throw new DataIllegalException("Unknown user id");
        if((instrument= getSymbol(symbol,dbConnection))==null)
            throw new DataIllegalException("Invalid symbol id");
        
        return instrument;
    }


//    private Boolean customerIsRegistered(Offering offer,Connection dbConnection){
//        try {
//            CustomerDAO.findByID(offer.getID(), dbConnection);
//            return true;
//        } catch (SQLException ex) {
//            return false;
//        } catch (DBException ex) {
//            return false;
//        }
//        
//    }

    public Customer getCustomer(String id,Connection dbConnection) throws SQLException, DBException{
        return CustomerDAO.findByID(id, dbConnection);
    }

    private Instrument getSymbol(String inst,Connection dbConnection) throws SQLException, DBException{
        for(Instrument instrument : getInstruments(dbConnection))
            if( instrument.symbolIsMatched(inst))
                return instrument;
        return null;
    }
//    public void initializeInstruments(){
//        Instrument ins1 = new Instrument("Peugeot",242L);
//        ins1.initialOfferLists();
//        instruments.add(ins1);
//        ins1 = new Instrument("Renault",8585L);
//        ins1.initialOfferLists();
//        instruments.add(ins1);
//    }

    public List<Instrument> getInstruments(Connection dbConnection) throws SQLException, DBException {
        return InstrumentDAO.getForeSaleInstruments(dbConnection);
    }

    public HashMap<String, Long> getDepositRequests(){
        return depositRequests;
    }

    public void addDepositRequest(String id, Long amount){
        if(!depositRequests.containsKey(id))
            depositRequests.put(id, amount);
        else
            depositRequests.put(id,depositRequests.get(id)+ amount);
    }

    public static void changeCustomerProperty(SellingOffer sOffer, BuyingOffer bOffer, Long price, Long count, String symbol,Connection dbConnection) throws SQLException, DBException{
        Customer seller = CustomerDAO.findByID(sOffer.getID(),dbConnection);
        Customer buyer = CustomerDAO.findByID(bOffer.getID(), dbConnection);

        seller.executeTransaction(TransactionType.DEPOSIT, price*count,dbConnection);
        seller.updateInstruments("delete", count, symbol,dbConnection);

//        buyer.executeTransaction(TransactionType.WITHDRAW, price*count);
        buyer.updateInstruments("add", count, symbol,dbConnection);

    }

    public void setTestMode(boolean state){
        testMode = state;
    }
    
    public boolean isTestMode(){
        return testMode;
    }
    
    public boolean authenticateCustomer(String id, String pass, Connection dbConnection) throws SQLException {
        return CustomerDAO.authenticateCustomer(id, pass, dbConnection);
    }
    
    public ArrayList<Customer> getCustomers(Connection dbConnection) throws SQLException, DBException {
        return CustomerDAO.getAllCustomers(dbConnection);
    }
    
    public void addNewSymbol(String symbol , SellingOffer offer){
        if(!newSymbols.containsKey(symbol)){
            ArrayList<SellingOffer> newOffer = new ArrayList<SellingOffer>();
            newOffer.add(offer);
            newSymbols.put(symbol, newOffer);
        }else
            newSymbols.get(offer).add(offer);
    }
    
    public SellingOffer getApprovedNewSymbol(String symbol, SellingOffer offer){
        if(!newSymbols.containsKey(symbol))
            return null;
        if(newSymbols.get(offer)==null || newSymbols.get(offer).isEmpty())
            return null;
        for( int i=0; i< newSymbols.get(offer).size(); i++){
            SellingOffer record = newSymbols.get(offer).get(i);
            if(record.getID().equals(offer.getID()) &&record.getQuantity().equals(offer.getQuantity())
                    &&record.getPrice().equals(offer.getPrice())){
                newSymbols.get(offer).remove(i);
                return record;
            }
        }
        return null;
    }
    
    private static HashMap<String,Long> depositRequests = new HashMap<String, Long>();
    private static List<Transaction> heavyTransactions = new ArrayList<Transaction>();
    private static HashMap<String,List<SellingOffer>> newSymbols = new HashMap<String,List< SellingOffer>>();
    private static Long TransacLimit = Long.MAX_VALUE;
}
