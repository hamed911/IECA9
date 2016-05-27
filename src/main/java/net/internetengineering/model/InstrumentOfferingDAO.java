/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import net.internetengineering.domain.dealing.Instrument;
import net.internetengineering.domain.dealing.Offering;
import net.internetengineering.exception.DBException;

/**
 *
 * @author Hamed Ara
 */
public class InstrumentOfferingDAO {
    private final static String dropIfExistQuery = "drop table instr_offer if exists";
    private final static String createInstrOfferTableQuery = "create table instr_offer(" +
                "    instr_cust_id varchar(80) not null," +
                "    instr_symbol varchar(100) not null," +
                "    offer_id bigint not null," +
                "    primary key (instr_cust_id, instr_symbol, offer_id )," +
                "    constraint instr_fk foreign key(instr_cust_id, instr_symbol) references instrument(customer_id,symbol) on delete cascade," +
                "    constraint offer_id_fk foreign key(offer_id) references offering(db_id) on delete cascade" +
                ")";
    private final static String selectByCidAndSymbolQuery = "select * from instr_offer where instr_cust_id=? and instr_symbol=?";
    private final static String insertNewInstOfferQuery = "insert into instr_offer values (?, ?, ?)";
    
    public static void dropTableIfExist(Connection dbConnection) throws SQLException{
        dbConnection.createStatement().execute(dropIfExistQuery);
    }
    
    public static void createInstrOfferTable(Connection dbConnection) throws SQLException{
        dbConnection.createStatement().execute(createInstrOfferTableQuery);
    }
    
    public static List<Offering> selectByCidAndSymbol(String cid,String symbol, Connection dbConnection) throws SQLException, DBException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(selectByCidAndSymbolQuery);
        preparedStatement.setString(1, cid);
        preparedStatement.setString(2, symbol);
        ResultSet rs = preparedStatement.executeQuery();
        ArrayList<Offering> offers = new ArrayList<Offering>();
        while(rs.next()){
            Long oid= rs.getLong("offer_id");
            offers.add( OfferingDAO.selectByOfferID(oid, dbConnection));
        }
        return offers;
    }
    
    public static void insertInstrOffer(String cid,String symbol,Long offerID, Connection dbConnection) throws SQLException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(insertNewInstOfferQuery);
        preparedStatement.setString(1, cid);
        preparedStatement.setString(2, symbol);
        preparedStatement.setLong(3, offerID);
        preparedStatement.executeUpdate();
    }
}
