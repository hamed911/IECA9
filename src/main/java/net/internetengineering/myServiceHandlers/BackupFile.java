/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.myServiceHandlers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.internetengineering.domain.Transaction;
import net.internetengineering.exception.DBException;
import net.internetengineering.model.TransactionDAO;
import net.internetengineering.utils.CSVFileWriter;

/**
 *
 * @author Hamed Ara
 */
@WebServlet("/backupfile")
public class BackupFile extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
  
        response.setContentType("text/html");  
        PrintWriter out = response.getWriter();  
        String filename = "backup.csv";   
        String filepath = "./";   
        response.setContentType("APPLICATION/OCTET-STREAM");   
        response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");   
         
        try {
            CSVFileWriter.writeCsvFile( getTransactions(TransactionDAO.getAllTransactions()) );
        } catch (DBException ex) {
            out.print(ex.getMessage());
            Logger.getLogger(BackupFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            out.print("DB Error occured.");
            Logger.getLogger(BackupFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileInputStream fileInputStream = new FileInputStream(filepath + filename);  

        int i;   
        while ((i=fileInputStream.read()) != -1) {  
            out.write(i);   
        }   
        fileInputStream.close();   
        out.close();   
    }
    
    private List<Transaction> getTransactions(ResultSet rs) throws SQLException, DBException{
        ArrayList<Transaction> ts = new ArrayList<Transaction>();
        while(rs.next()){
            Transaction t = new Transaction(rs.getString("buyer"),rs.getString("seller"), rs.getString("instrument"),
                    rs.getString("typeOfTrade"), rs.getString("quantity"), rs.getString("price"));
            ts.add(t);
        }
        if(ts.isEmpty())
            throw new DBException("No Transaction is found");
        return ts;
    }
    
}
