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
import javax.servlet.http.*;
import net.internetengineering.exception.DataIllegalException;
import net.internetengineering.model.TransactionDAO;
import net.internetengineering.utils.JsonBuilder;
import net.internetengineering.exception.DBException;
import net.internetengineering.utils.HSQLUtil;


import java.sql.*;

 

@WebServlet("/index1.html/search")
public class Search extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        JSONArray transactions = new JSONArray();

        String buyerID = request.getParameter("buyerID");
        String buyerName = request.getParameter("buyerName");
        String buyerFamily = request.getParameter("buyerFamily");
        String sbuyerBalance = request.getParameter("sbuyerBalance");
        String ebuyerBalance = request.getParameter("ebuyerBalance");

        String sellerID = request.getParameter("sellerID");
        String sellerName = request.getParameter("sellerName");
        String sellerFamily = request.getParameter("sellerFamily");
        String ssellerBalance = request.getParameter("ssellerBalance");
        String esellerBalance = request.getParameter("esellerBalance");

        String instrument = request.getParameter("instrument");
        String mytype = request.getParameter("type");

        String startQuantity = request.getParameter("startQuantity");
        String endQuantity = request.getParameter("endQuantity");

        String startPrice = request.getParameter("startPrice");
        String endPrice = request.getParameter("endPrice");

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        try{

            //int rowCount = (int)TransactionDAO.getTransactionsRowCount().get;
            //ResultSet rs = TransactionDAO.getAllTransactions();
            ResultSet rs = TransactionDAO.getSearchResult(buyerID, buyerName, buyerFamily, sbuyerBalance, ebuyerBalance, sellerID, sellerName, sellerFamily, ssellerBalance, esellerBalance, instrument, mytype, startQuantity, endQuantity, startPrice, endPrice, startDate, endDate);

            if (rs != null) {
                while (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    JSONObject map = new JSONObject();

                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {

                        int type = rsmd.getColumnType(i);
                        String name = rsmd.getColumnName(i);
                        //if(!name.equals("TR_ID")){
                            if (type == Types.VARCHAR || type == Types.CHAR || type == Types.TIMESTAMP || type == Types.DATE) {
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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
    
}
