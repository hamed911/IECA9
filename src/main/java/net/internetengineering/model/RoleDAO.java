/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import net.internetengineering.domain.Role;
import net.internetengineering.exception.DBException;

/**
 *
 * @author Hamed Ara
 */
public class RoleDAO {
    private final static String dropIfExistQuery = "drop table role if exists";
    private final static String createRoleTableQuery = "create table role (" +
            "    name varchar(80) not null," +
            "    deposit boolean not null," +
            "    transaction boolean not null," +
            "    confdeposit boolean not null," +
            "    conftransact boolean not null," +
            "    addsymbol boolean not null," +
            "    transactlimit boolean not null," +
            "    confnewsymbol boolean not null," +
            "    report boolean not null," +
            "    rolemanager boolean not null," +
            "    userprofiles boolean not null," +
            "    backup boolean not null," +
            "    primary key (name)" +
            ");";
    private final static String insertRoles = "insert into role values ('admin',true,true,true,true,true,true,true,true,true,true,true);\n" +
            "insert into role values ('typical',true,true,false,false,false,false,false,false,false,false,false);" +
            "insert into role values ('officer',false,false,true,true,false,false,false,false,false,false,false);" +
            "insert into role values ('owner',false,false,false,false,true,false,false,false,false,false,false);";
    private final static String selectByCidQuery ="select * from role r where r.name =?";
    
    public static void dropTableIfExist(Connection dbConnection) throws SQLException{
        dbConnection.createStatement().execute(dropIfExistQuery);
    }
    
    public static void createRoleTable(Connection dbConnection) throws SQLException{
        dbConnection.createStatement().execute(createRoleTableQuery);
        insertRoles(dbConnection);
    }
    private static void insertRoles(Connection dbConnection) throws SQLException{
        dbConnection.createStatement().execute(insertRoles);
    }
    
    public static Role findByRoleName(String name, Connection dbConnection) throws SQLException{
        PreparedStatement preparedStatement = dbConnection.prepareStatement(selectByCidQuery);
        preparedStatement.setString(1, name);
        ResultSet rs = preparedStatement.executeQuery();
        if(rs.next()){
            Role r = new Role(name, rs.getBoolean("deposit"), rs.getBoolean("transaction"),
                    rs.getBoolean("confdeposit"), rs.getBoolean("conftransact"), rs.getBoolean("addsymbol"),
                    rs.getBoolean("transactlimit"),rs.getBoolean("confnewsymbol"), rs.getBoolean("report"),
                    rs.getBoolean("rolemanager"),rs.getBoolean("userprofiles"), rs.getBoolean("backup"));
            return r;
        }
        throw new SQLException("Unable to find role '"+name+"'");
    }
}
