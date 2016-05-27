/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.internetengineering.domain.Customer;
import net.internetengineering.domain.Role;
import net.internetengineering.domain.dealing.TransactionType;
import net.internetengineering.exception.DBException;
import net.internetengineering.utils.HashUtil;

/**
 *
 * @author Hamed Ara
 */
public class CustomerDAO {
    private final static String dropIfExistQuery = "drop table customer if exists";
    private final static String createCustomerTableQuery = "create table customer (" +
                "    id varchar(80) not null," +
                "    name varchar(80) not null," +
                "    family varchar(80) not null," +
                "    email varchar(100) not null," +
                "    password varchar(200) not null," +
                "    balance bigint default 0 not null ," +
                "    primary key (id)" +
                ");";
    private final static String insertQuery = "insert into customer values (?, ?,?,?,?,?)";
    private final static String selectByIdQuery = "select * from customer c where c.id=?";
    private final static String selectByIdAndPassQuery = "select * from customer c where c.id=? and c.password=?";
    private final static String updateBalance = "update customer set balance = ? where id =?";
    
    public static void dropTableIfExist(Connection dbConnection) throws SQLException{
        dbConnection.createStatement().execute(dropIfExistQuery);
    }
    
    public static void createCustomerTable(Connection dbConnection) throws SQLException{
        Statement statement= dbConnection.createStatement();
        statement.execute(createCustomerTableQuery);
    }
    
    public static void updateBalance(String cid,Long balance, Connection dbConnection) throws SQLException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(updateBalance);
        preparedStatement.setLong(1, balance);
        preparedStatement.setString(2, cid);
        preparedStatement.execute();
    }
    
    public static void insertAdmin(Connection dbConnection) throws SQLException{
//        Customer admin = new Customer("1", "admin", "admin","admin@stockmarket.com",HashUtil.md5( "password"));
        Customer admin = new Customer("1", "admin", "admin","admin@stockmarket.com", "password");
        admin.addRole(RoleDAO.findByRoleName("admin", dbConnection));
        addNewCustomer(admin, dbConnection);
    }
    
    public static void addNewCustomer(Customer c, Connection dbConnection) throws SQLException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(insertQuery);
        preparedStatement.setString(1, c.getId());
        preparedStatement.setString(2, c.getName());
        preparedStatement.setString(3, c.getFamily());
        preparedStatement.setString(4, c.getEmail());
        preparedStatement.setString(5, c.getPassword());
        preparedStatement.setLong(6, c.getMoney());
        preparedStatement.execute();
        for(Role r: c.getRoles())
            CustomerRoleDAO.insertRole(c.getId(), r.name, dbConnection);
    }
    
    public static Customer findByID(String id,Connection dbConnection) throws SQLException, DBException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(selectByIdQuery);
        preparedStatement.setString(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()){
            Customer c = new Customer(rs.getString("id"),rs.getString("name"), rs.getString("family"),
                    rs.getString("email"),rs.getString("password"));
            c.executeTransaction(TransactionType.DEPOSIT, rs.getLong("balance"),dbConnection);
            c.setInstruments(InstrumentDAO.findByCustomerID(id, dbConnection));
            c.setRoles(CustomerRoleDAO.findByCustomerID(id, dbConnection));
            return c;
        }else
            throw new DBException("CustomerID '"+id+"' is unknown" );
    }
    
    public static Boolean containCustomer(String id, Connection dbConnection) throws SQLException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(selectByIdQuery);
        preparedStatement.setString(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next())
            return true;
        else
            return false;
    }

    public static boolean authenticateCustomer(String id, String pass, Connection dbConnection) throws SQLException {
        PreparedStatement preparedStatement = dbConnection.prepareStatement(selectByIdAndPassQuery);
        preparedStatement.setString(1, id);
//        preparedStatement.setString(2, HashUtil.md5(pass));
        preparedStatement.setString(2, pass);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next())
            return true;
        else
            return false;
    }
}
