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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.internetengineering.domain.Customer;
import net.internetengineering.exception.DBException;
import net.internetengineering.server.StockMarket;
import net.internetengineering.utils.HSQLUtil;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author parisa
 */
@WebServlet("/getcustomers")
public class GetCustomers extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out= response.getWriter();
        Connection dbConnection = null;
            try {
                
                dbConnection = HSQLUtil.getInstance().openConnectioin();
                ArrayList<Customer> cList = StockMarket.getInstance().getCustomers(dbConnection);
                JSONArray customers = new JSONArray();
                ArrayList<String> allRoles = StockMarket.getInstance().getRoles(dbConnection);
                
            for (Customer c : cList) {
                
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("id", c.getId());
                map.put("name", c.getName());
                map.put("family", c.getFamily());
                map.put("email", c.getEmail());
                map.put("money", c.getMoney());
                
                Map<String,Object> roles = new HashMap<String, Object>();
                
                for (String role : allRoles) {
                    int x = 0;
                    for (int i = 0; i < c.getRoles().size(); i++) {
                        if(role.equals(c.getRoles().get(i).name)){
                            roles.put(c.getRoles().get(i).name,true);
                            x = 1;
                        }
                    }
                    if(x != 1)
                        roles.put(role,false);
                }
                
                map.put("roles2", roles);
                
                JSONArray rl = new JSONArray();
                for (int i = 0; i < c.getRoles().size(); i++) {
                    rl.put(c.getRoles().get(i).name);
                }
                
                map.put("roles", rl);
                
                customers.put(map);
            }
                
                response.getWriter().print(customers);
                response.setContentType("application/json");
            
            } catch (SQLException ex) {
                out.print("Database Error happend");
                Logger.getLogger(DepositHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DBException ex) {
                out.println(ex.getMessage());
                Logger.getLogger(GetCustomer.class.getName()).log(Level.SEVERE, null, ex);
            } finally{
                try {
                    if(dbConnection!=null&&!dbConnection.isClosed())
                        dbConnection.close();
                } catch (SQLException ex) {
                }
            }
            
    }
}
