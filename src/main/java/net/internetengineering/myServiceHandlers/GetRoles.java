/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.myServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.internetengineering.server.StockMarket;
import net.internetengineering.exception.DBException;
import net.internetengineering.utils.HSQLUtil;


/**
 *
 * @author parisa
 */
 

@WebServlet("/getroles")
public class GetRoles extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out= response.getWriter();
        Connection dbConnection = null;
        try{
            dbConnection = HSQLUtil.getInstance().openConnectioin();
            ArrayList<String> roles = StockMarket.getInstance().getRoles(dbConnection);
            
            
            JSONArray myList1 = new JSONArray();

            for (String role : roles) {
                myList1.put(role);
            }
            
                response.getWriter().print(myList1);
                response.setContentType("application/json");
        }catch (SQLException ex) {
            out.print("Error in retrieving data from DB.");
            Logger.getLogger(AddCustomer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DBException ex) {
            out.print("Error in creating DB connection");
            Logger.getLogger(AddCustomer.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(dbConnection!=null&&!dbConnection.isClosed())
                    dbConnection.close();
            } catch (SQLException ex) {
            }
        }
    }
    
}



