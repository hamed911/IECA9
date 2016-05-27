package net.internetengineering.myServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.internetengineering.exception.DataIllegalException;
import net.internetengineering.model.TransactionDAO;
import net.internetengineering.utils.JsonBuilder;
import net.internetengineering.exception.DBException;
import net.internetengineering.utils.HSQLUtil;


import java.sql.*;
import javax.servlet.http.HttpServlet;

 

@WebServlet("/admin/reports/getreport")
public class GetReport extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        JSONArray transactions = new JSONArray();
        try{

            //int rowCount = (int)TransactionDAO.getTransactionsRowCount().get;
            ResultSet rs = TransactionDAO.getAllTransactions();

            if (rs != null) {
                while (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    JSONObject map = new JSONObject();

                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {

                        int type = rsmd.getColumnType(i);
                        String name = rsmd.getColumnName(i);
                        //if(!name.equals("TR_ID")){
                            if (type == Types.VARCHAR || type == Types.CHAR || type == Types.TIMESTAMP) {
                                //out.print(rs.getString(i));
                                map.put( name, rs.getString(i));
                            } else {
                                //out.print(rs.getLong(i));
                                map.put( name, rs.getLong(i));
                            }
                        //}
                    }
                    transactions.put(map);
                    //out.println();
                }
            }
            
        } catch (DBException ex) {
            out.print(ex.getMessage());
            
        }catch(Exception e){
           out.print(e.getMessage());
           
        }
        
        System.out.println("OK!");
        response.getWriter().print(transactions);
        response.setContentType("application/json");

    }
    
}
