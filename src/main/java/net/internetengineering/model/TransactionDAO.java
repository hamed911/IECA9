/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.model;

import java.sql.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.internetengineering.domain.Transaction;
import net.internetengineering.exception.DBException;
import net.internetengineering.utils.HSQLUtil;


/**
 *
 * @author Hamed Ara
 */
public class TransactionDAO {
    private static final String createTransactionTableQuery = "create table transaction(" +
                        "    tr_id bigint IDENTITY PRIMARY KEY," +
                        "    buyer varchar(80) not null," +
                        "    seller varchar(80) not null," +
                        "    instrument varchar(80) not null," +
                        "    typeOfTrade varchar(80) not null," +
                        "    quantity bigint not null," +
                        "    price bigint not null," +
                        "    tTime date default now" +
                        ")";
    private static final String dropIfExistQuery = "drop table transaction if exists";
    private static final String insertQuery = "insert into transaction "
            + "(buyer, seller, instrument, typeOfTrade, quantity, price) "
            + "values (?, ?, ?, ?, ?, ?)";
    private static final String selectQuery = "select TR_ID,BUYER,BUYERNAME,BUYERFAMILY,BUYERBALANCE,SELLER,NAME as SELLERNAME,FAMILY as SELLERFAMILY,BALANCE as SELLERBALANCE,INSTRUMENT,TYPEOFTRADE,QUANTITY,PRICE,TTime" +
                                                " from" +
                                                    " (select TR_ID,BUYER,NAME as BUYERNAME,FAMILY as BUYERFAMILY,BALANCE as BUYERBALANCE,SELLER,INSTRUMENT,TYPEOFTRADE,QUANTITY,PRICE,TTIME" +
                                                        " from transaction t join customer u on t.BUYER = u.ID) as t1" +
                                                " join customer u2 on t1.SELLER = u2.ID order by TR_ID";

    private static String searchQuery = "select * from (select TR_ID,BUYER,BUYERNAME,BUYERFAMILY,BUYERBALANCE,SELLER,NAME as SELLERNAME,FAMILY as SELLERFAMILY,BALANCE as SELLERBALANCE,INSTRUMENT,TYPEOFTRADE,QUANTITY,PRICE,TTIME" +
                                                " from" +
                                                    " (select TR_ID,BUYER,NAME as BUYERNAME,FAMILY as BUYERFAMILY,BALANCE as BUYERBALANCE,SELLER,INSTRUMENT,TYPEOFTRADE,QUANTITY,PRICE,TTIME" +
                                                        " from transaction t join customer u on t.BUYER = u.ID) as t1" +
                                                " join customer u2 on t1.SELLER = u2.ID order by TR_ID) as res where TR_ID is not null ";

    private static final String resetSearchQuery = searchQuery;

    
    public static void createTransaction(Transaction t) throws DBException{
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;
        try {
            dbConnection = HSQLUtil.getInstance().openConnectioin();
            preparedStatement = dbConnection.prepareStatement(insertQuery);
            preparedStatement.setString(1, t.buyer);
            preparedStatement.setString(2, t.seller);
            preparedStatement.setString(3, t.instrument);
            preparedStatement.setString(4, t.typeOfTrade);
            preparedStatement.setLong(5, Long.parseLong(t.quantity));
            preparedStatement.setLong(6, Long.parseLong(t.price));
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DBException("Unable to execute insert in Transaction table.",ex);
        }finally{
            try {
                if(preparedStatement!=null && !preparedStatement.isClosed())
                    preparedStatement.close();
                if(dbConnection!=null && !dbConnection.isClosed())
                    dbConnection.close();
            } catch (SQLException ex) {
                throw new DBException("Unable to close connection in Transaction table.",ex);
            }
        }
    }

    public static ResultSet getSearchResult(String buyerID, String buyerName, String buyerFamily, String sbuyerBalance, String ebuyerBalance, String sellerID, String sellerName, String sellerFamily, String ssellerBalance, String esellerBalance, String instrument, String mytype, String startQuantity, String endQuantity, String startPrice, String endPrice, String startDate, String endDate) throws DBException{
        Connection dbConnection = null;
        Statement statement = null;  
        try {
            dbConnection= HSQLUtil.getInstance().openConnectioin();
            statement = dbConnection.createStatement();
            
            searchQuery = resetSearchQuery;

            if(buyerID!=null && !buyerID.isEmpty()) 
                searchQuery += " and BUYER = '"+buyerID+"'";
            if(buyerName!=null && !buyerName.isEmpty())
                searchQuery += " and BUYERNAME = '"+buyerName+"'";
            if(buyerFamily!=null && !buyerFamily.isEmpty())
                searchQuery += " and BUYERFAMILY = '"+buyerFamily+"'";

            if(sellerID!=null && !sellerID.isEmpty()) 
                searchQuery += " and SELLER = '"+sellerID+"'";
            if(sellerName!=null && !sellerName.isEmpty())
                searchQuery += " and SELLERNAME = '"+sellerName+"'";
            if(sellerFamily!=null && !sellerFamily.isEmpty())
                searchQuery += " and SELLERFAMILY = '"+sellerFamily+"'";

            if(instrument!=null && !instrument.isEmpty())
                searchQuery += " and INSTRUMENT = '"+instrument+"'";
            if(mytype!=null && !mytype.isEmpty())
                searchQuery += " and TYPEOFTRADE = '"+mytype+"'";
             
            if(sbuyerBalance!=null && !sbuyerBalance.isEmpty())
                searchQuery+= " and BUYERBALANCE >= "+ Long.parseLong(sbuyerBalance);
            if(ebuyerBalance!=null && !ebuyerBalance.isEmpty())
                searchQuery+= " and BUYERBALANCE <= "+ Long.parseLong(ebuyerBalance);

            if(ssellerBalance!=null && !ssellerBalance.isEmpty())
                searchQuery+= " and SELLERBALANCE >= "+ Long.parseLong(ssellerBalance);
            if(esellerBalance!=null && !esellerBalance.isEmpty())
                searchQuery+= " and SELLERBALANCE <= "+ Long.parseLong(esellerBalance);

            if(startQuantity!=null && !startQuantity.isEmpty())
                searchQuery+= " and QUANTITY >= "+ Long.parseLong(startQuantity);
            if(endQuantity!=null && !endQuantity.isEmpty())
                searchQuery+= " and QUANTITY <= "+ Long.parseLong(endQuantity);

            if(startPrice!=null && !startPrice.isEmpty())
                searchQuery+= " and PRICE >= "+ Long.parseLong(startPrice);
            if(endPrice!=null && !endPrice.isEmpty())
                searchQuery+= " and PRICE <= "+ Long.parseLong(endPrice);


            if(startDate!=null && !startDate.isEmpty())
                searchQuery += " and TTIME >= DATE'"+startDate.split("T")[0]+"'";
            if(endDate!=null && !endDate.isEmpty())
                searchQuery += " and TTIME <= DATE'"+endDate.split("T")[0]+"'";
                   


            return statement.executeQuery(searchQuery);

        } catch (SQLException ex) {
            throw new DBException("Unable to execute select in Transaction table.",ex);
        }finally{
            try {
                if(statement!=null && !statement.isClosed())
                    statement.close();
                if(dbConnection!=null && !dbConnection.isClosed())
                    dbConnection.close();
            } catch (SQLException ex) {
                throw new DBException("Unable to close connection in Transaction table.",ex);
            }
        } 
    }

    public static ResultSet getAllTransactions() throws DBException{
        Connection dbConnection = null;
        Statement statement = null;
        try {
            dbConnection= HSQLUtil.getInstance().openConnectioin();
            statement = dbConnection.createStatement();
            
            return statement.executeQuery(selectQuery);

        } catch (SQLException ex) {
            throw new DBException("Unable to execute select in Transaction table.",ex);
        }finally{
            try {
                if(statement!=null && !statement.isClosed())
                    statement.close();
                if(dbConnection!=null && !dbConnection.isClosed())
                    dbConnection.close();
            } catch (SQLException ex) {
                throw new DBException("Unable to close connection in Transaction table.",ex);
            }
        }
    }
    
    public static void dropTableIfExist(Connection dbConnection) throws SQLException{
        dbConnection.createStatement().execute(dropIfExistQuery);
    }
    
    public static void createTransactionTable(Connection dbConnection) throws SQLException{
        dbConnection.createStatement().execute(createTransactionTableQuery);
    }

}
