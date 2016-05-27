package net.internetengineering.domain;

import java.sql.Connection;
import java.sql.SQLException;
import net.internetengineering.domain.dealing.Instrument;
import net.internetengineering.domain.dealing.Offering;
import net.internetengineering.domain.dealing.TransactionType;

import java.util.ArrayList;
import java.util.List;
import net.internetengineering.exception.DBException;
import net.internetengineering.model.CustomerDAO;
import net.internetengineering.model.InstrumentDAO;


public class Customer {
    private String id;
    private String name;
    private String family;
    private String email;
    private String password;
    private Account customerAccount;
    private List<Instrument> instruments;
    private List<Role> roles;

    public Customer(String id, String name, String family, String email, String password) {
        this.id = id;
        this.name = name;
        this.family = family;
        this.password = password;
        this.email = email;
        this.customerAccount = new Account();
        this.instruments = new ArrayList<Instrument>();
        this.roles = new ArrayList<Role>();
    }
    

    public String getId() {
            return id;
    }

    public List<Instrument> getInstruments(){
        return instruments;
    }

    public void executeTransaction(TransactionType type, Long amount, Connection dbConnection) throws SQLException{
            customerAccount.executeTransaction(type,amount);
            CustomerDAO.updateBalance(id, customerAccount.getBalance(), dbConnection);
    }

    public Long getMoney(){
            return this.customerAccount.getBalance();
    }

    public Boolean hasEnoughMoney(Long amount){
            return customerAccount.isEnoughMoney(amount);
    }

    public Boolean hasEnoughStock(String instrumentName,Offering offer){
            for(Instrument i : instruments){
                    if(i.symbolIsMatched(instrumentName))
                            if(i.HasQuantity(offer.getQuantity()))
                                    return true;	
            }
            return false;
    }

    public void updateInstruments(String type,Long count,String name,Connection dbConnection) throws SQLException, DBException{
            boolean flag = false;
            List<Instrument> instruments = InstrumentDAO.findByCustomerID(id, dbConnection);
            for(Instrument i : instruments){
                    if(i.symbolIsMatched(name)){
                            i.changeQuantity(type, count);
                            InstrumentDAO.updateQuantity(id, name, i.getQuantity(),false, dbConnection);
                            flag = true;
                            break;
                    }
            }
            if(!flag){
//                    instruments.add(new Instrument(name, count));
                    InstrumentDAO.insertNewInstrument(id, name, count,false, dbConnection);
            }
    }

    public String getName() {
        return name;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }
    
    public String getFamily() {
        return family;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    
    public void addRole(Role r){
        roles.add(r);
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
    
}

