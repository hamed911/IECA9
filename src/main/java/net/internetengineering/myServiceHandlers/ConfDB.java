/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.myServiceHandlers;

/**
 *
 * @author Hamed Ara
 */
//STEP 1. Import required packages
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.internetengineering.domain.Transaction;
import net.internetengineering.exception.DBException;
import net.internetengineering.model.CustomerDAO;
import net.internetengineering.model.CustomerRoleDAO;
import net.internetengineering.model.InstrumentDAO;
import net.internetengineering.model.InstrumentOfferingDAO;
import net.internetengineering.model.OfferingDAO;
import net.internetengineering.model.RoleDAO;
import net.internetengineering.model.TransactionDAO;
import net.internetengineering.server.StockMarket;
import net.internetengineering.utils.HSQLUtil;

@WebServlet("/confdb")
public class ConfDB extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {   
        PrintWriter out = response.getWriter();
        Connection dbConnection = null;
        Statement statement = null;
        try{
            if(request.getParameter("test")!=null)
                StockMarket.getInstance().activeTestMode();
            dbConnection = HSQLUtil.getInstance().openConnectioin();
            dropTablesIfExist(dbConnection);
            createTables(dbConnection);
            out.print("DataBase configured successfully.");
            System.out.println("DataBase configured successfully.");
        }catch (SQLException ex) {
            out.print("Error in Database");
            Logger.getLogger(ConfDB.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception e){
           out.print("Internal Error :(");
           Logger.getLogger(ConfDB.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            try {
                if(dbConnection!=null&&!dbConnection.isClosed())
                    dbConnection.close();
            } catch (SQLException ex) {
            }
        }
    }
    protected void dropTablesIfExist(Connection dbConnection) throws SQLException{
        InstrumentOfferingDAO.dropTableIfExist(dbConnection);
        InstrumentDAO.dropTableIfExist(dbConnection);
        OfferingDAO.dropTableIfExist(dbConnection);
        CustomerRoleDAO.dropTableIfExist(dbConnection);
        RoleDAO.dropTableIfExist(dbConnection);
        CustomerDAO.dropTableIfExist(dbConnection);
        TransactionDAO.dropTableIfExist(dbConnection);
    }
    protected void createTables(Connection dbConnection) throws SQLException{
        RoleDAO.createRoleTable(dbConnection);
        CustomerDAO.createCustomerTable(dbConnection);
        CustomerRoleDAO.createRoleTable(dbConnection);
        InstrumentDAO.createInstrumentTable(dbConnection);
        OfferingDAO.createOfferingTable(dbConnection);
        InstrumentOfferingDAO.createInstrOfferTable(dbConnection);
        TransactionDAO.createTransactionTable(dbConnection);
        CustomerDAO.insertAdmin(dbConnection);
    }
}