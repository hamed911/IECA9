/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import net.internetengineering.domain.dealing.BuyingOffer;
import net.internetengineering.domain.dealing.Instrument;
import net.internetengineering.domain.dealing.Offering;
import net.internetengineering.domain.dealing.SellingOffer;
import net.internetengineering.exception.DBException;

/**
 *
 * @author Hamed Ara
 */
public class InstrumentDAO {
    private final static String dropIfExistQuery = "drop table instrument if exists";
    private final static String createInstrumentTableQuery = "create table instrument (" +
                    "    customer_id varchar(80) not null," +
                    "    symbol varchar(100) not null," +
                    "    quantity bigint not null," +
                    "    for_sale boolean not null,"+
                    "    primary key (customer_id,symbol)," +
                    "    constraint customer_id_fk foreign key(customer_id) references customer(id) on delete cascade" +
                    ")";
    private final static String selectByCidQuery ="select * from instrument i where i.customer_id =?"; 
    private final static String updateQuantInstrument = "UPDATE instrument set quantity=? where customer_id=? and symbol=? and for_sale=?";
    private final static String insertInstrument = "insert into instrument values (?, ?, ?,?);";
    private final static String selectForeSaleInstruments = "select * from instrument i where i.quantity> 0 and i.for_sale=TRUE";
    
    public static void dropTableIfExist(Connection dbConnection) throws SQLException{
        dbConnection.createStatement().execute(dropIfExistQuery);
    }
    
    public static void createInstrumentTable(Connection dbConnection) throws SQLException{
        dbConnection.createStatement().execute(createInstrumentTableQuery);
    }
    
    public static List<Instrument> getForeSaleInstruments(Connection dbConnection) throws SQLException, DBException{
        ResultSet rs=dbConnection.createStatement().executeQuery(selectForeSaleInstruments);
        List<Instrument> insts = new ArrayList<Instrument>();
        while(rs.next()){
            Instrument i = new Instrument(rs.getString("customer_id"),rs.getString("symbol"), rs.getLong("quantity"));
            List<Offering> offers = InstrumentOfferingDAO.selectByCidAndSymbol(rs.getString("customer_id"), rs.getString("symbol"), dbConnection);
            for(Offering o : offers)
                if(o instanceof SellingOffer)
                    i.addSellingOffer((SellingOffer)o);
                else if(o instanceof BuyingOffer)
                    i.addBuyingOffer((BuyingOffer)o);
            insts.add(i);
        }
        return insts;
    }
    
    public static List<Instrument> findByCustomerID(String cid, Connection dbConnection) throws SQLException, DBException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(selectByCidQuery);
        preparedStatement.setString(1, cid);
        ResultSet rs = preparedStatement.executeQuery();
        List<Instrument> insts = new ArrayList<Instrument>();
        while(rs.next()){
            Instrument i = new Instrument(rs.getString("customer_id"),rs.getString("symbol"), rs.getLong("quantity"));
            List<Offering> offers = InstrumentOfferingDAO.selectByCidAndSymbol(cid, rs.getString("symbol"), dbConnection);
            for(Offering o : offers)
                if(o instanceof SellingOffer)
                    i.addSellingOffer((SellingOffer)o);
                else if(o instanceof BuyingOffer)
                    i.addBuyingOffer((BuyingOffer)o);
            insts.add(i);
        }
        return insts;
    }
    public static void updateQuantity(String cid,String symbol,Long newQuant,boolean forSale, Connection dbConnection) throws SQLException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(updateQuantInstrument);
        preparedStatement.setLong(1, newQuant);
        preparedStatement.setString(2, cid);
        preparedStatement.setString(3, symbol);
        preparedStatement.setBoolean(4, forSale);
        preparedStatement.executeUpdate();
    }
    
    public static void insertNewInstrument(String cid,String symbol,Long quantity,boolean foreSale,Connection dbConnection) throws SQLException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(insertInstrument);
        preparedStatement.setString(1, cid);
        preparedStatement.setString(2, symbol);
        preparedStatement.setLong(3, quantity);
        preparedStatement.setBoolean(4, foreSale);
        preparedStatement.executeUpdate();
    }
    
}
