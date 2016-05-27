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
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.JSONArray;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.internetengineering.domain.Customer;
import net.internetengineering.exception.DBException;
import net.internetengineering.exception.DataIllegalException;
import net.internetengineering.server.StockMarket;
import net.internetengineering.utils.HSQLUtil;
import net.internetengineering.utils.JsonBuilder;

/**
 *
 * @author Hamed Ara
 */
@WebServlet("/getcustomerins")
public class GetCustomerIns extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out= response.getWriter();
        Connection dbConnection = null;
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                throw new DataIllegalException("Mismatched Parameters");
            }
            dbConnection = HSQLUtil.getInstance().openConnectioin();
            if (!StockMarket.getInstance().containCustomer(id,dbConnection)) {
                throw new DataIllegalException("Invalid ID");
            }
            Customer c = StockMarket.getInstance().getCustomer(id,dbConnection);

            JSONArray list = new JSONArray();
            for (int i = 0; i <  c.getInstruments().size(); i++ ) {
                list.put(i,c.getInstruments().get(i).getSymbol());
            }


            response.getWriter().print(list);
            response.setContentType("application/json");

        }catch (DataIllegalException ex){
                out.println(ex.getMessage());
        } catch (SQLException ex) {
            out.print("Error in saving new customer in DB.");
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
