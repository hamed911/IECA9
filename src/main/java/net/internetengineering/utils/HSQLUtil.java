/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.internetengineering.exception.DBException;

/**
 *
 * @author Hamed Ara
 */
public class HSQLUtil {
    private static HSQLUtil mysqlUtil;
    static String user;
    static String url;
    static String driver;
    static String pass;
    
    private HSQLUtil() throws DBException {
        Properties config = new Properties();
        try {
            InputStream stream = HSQLUtil.class.getResourceAsStream("/config.properties"); 
            config.load(stream);
            user = config.getProperty("db_user", "");
            pass = config.getProperty("db_pass", "");
            url = config.getProperty("db_url", "");
            driver = config.getProperty("db_driver", "");
            Class.forName(driver);
            
        } catch (IOException ex) {
            Logger.getLogger(HSQLUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new DBException("can not load config's properties",ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HSQLUtil.class.getName()).log(Level.SEVERE, null, ex);
            throw new DBException("Unable to load 'db_driver'",ex);
        } 
        
    }
    
    public static HSQLUtil getInstance() throws DBException{
        if(mysqlUtil==null){
            mysqlUtil = new HSQLUtil();
        }
        return mysqlUtil;
    }
    public Connection openConnectioin() throws DBException{
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException ex) {
            throw new DBException("Unable to open new connection",ex);
        }
    }
    
}
