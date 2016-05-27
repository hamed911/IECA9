/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.model;

import java.sql.*;
import java.util.List;
import net.internetengineering.domain.dealing.BuyingOffer;
import net.internetengineering.domain.dealing.Offering;
import net.internetengineering.domain.dealing.SellingOffer;
import net.internetengineering.exception.DBException;

/**
 *  //kind : 0=sell     1=buy
 * @author Hamed Ara
 */
public class OfferingDAO {
    private final static String dropIfExistQuery = "drop table offering if exists";
    private final static String createOfferingTableQuery = "create table offering(" +
                    "    db_id bigint IDENTITY PRIMARY KEY," +
                    "    customer_id varchar(80) not null," +
                    "    price bigint not null," +
                    "    quantity bigint not null," +
                    "    type varchar(30) not null," +
                    "    kind integer not null," +
                    "    constraint custome_id_fk foreign key(customer_id) references customer(id) on delete cascade" +
                    ")";
    private final static String selectFieldsQuery = "select * from offering where db_id=? ";
    private final static String insertOfferingQuery = "insert into offering (customer_id, price, quantity, type, kind) values (?, ?, ?, ?, ?)";
    private final static String getMaxDBID = "SELECT MAX(o.db_id) as max_db FROM offering o";
    private final static String deleteOffringByDBID = "delete from offering o where o.db_id=?";
    private final static String updateQuantityOfOffering = "update offering o set o.quantity =? where o.db_id =?";
    
    public static void dropTableIfExist(Connection dbConnection) throws SQLException{
        dbConnection.createStatement().execute(dropIfExistQuery);
    }
    
    public static void createOfferingTable(Connection dbConnection) throws SQLException{
        dbConnection.createStatement().execute(createOfferingTableQuery);
    }
    
    public static Offering selectByOfferID(Long oid,Connection dbConnection) throws SQLException, DBException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(selectFieldsQuery);
        preparedStatement.setLong(1, oid);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()){
            Offering o = null;
            if( rs.getInt("kind")==0)
                o = new SellingOffer(rs.getLong("db_id"),rs.getLong("price"), rs.getLong("quantity"), rs.getString("type"), rs.getString("customer_id"));
            else
                o = new BuyingOffer(rs.getLong("db_id"),rs.getLong("price"), rs.getLong("quantity"), rs.getString("type"), rs.getString("customer_id"));
            return o;
        }else
            throw new DBException("Unknown OfferID '"+oid+"'");
    }
    
    public static void insertNewOffering(Offering o, String symbol, Connection dbConnection) throws SQLException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(insertOfferingQuery);
        preparedStatement.setString(1, o.getID());
        preparedStatement.setLong(2, o.getPrice());
        preparedStatement.setLong(3, o.getQuantity());
        preparedStatement.setString(4, o.getType());
        preparedStatement.setInt(5, (o instanceof SellingOffer)?0:1);
        preparedStatement.executeUpdate();
        Long dbID = getMaximomDBID(dbConnection);
        o.setDbID(dbID);
//        InstrumentOfferingDAO.insertInstrOffer(o.getID(), symbol, dbID, dbConnection);
    }
    
    public static void updateQuantityOfOffering(Offering o, Connection dbConnection)throws SQLException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(updateQuantityOfOffering);
        preparedStatement.setLong(1, o.getQuantity());
        preparedStatement.setLong(2, o.getDbID());
        preparedStatement.executeUpdate();
    }
    
    public static void deleteOfferingByDBID(Long dbID,Connection dbConnection) throws SQLException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(deleteOffringByDBID);
        preparedStatement.setLong(1, dbID);
        preparedStatement.executeUpdate();
    }
    
    public static Long getMaximomDBID(Connection dbConnection) throws SQLException{
        ResultSet rs= dbConnection.createStatement().executeQuery(getMaxDBID);
        if(rs.next())
            return rs.getLong("max_db");
        else 
            throw new SQLException("Unable to Calculate Max Instrument DBID");
    }
}
