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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Hamed Ara
 */
@WebServlet("/getcustomer")
public class GetCustomer extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out= response.getWriter();
        Connection dbConnection = null;
            try {
                String id = request.getRemoteUser();
                
                if (id == null || id.isEmpty()) {
                    throw new DataIllegalException("Mismatched Parameters");
                }
                dbConnection = HSQLUtil.getInstance().openConnectioin();
                Customer c = StockMarket.getInstance().getCustomer(id,dbConnection);
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("id", c.getId());
                map.put("name", c.getName());
                map.put("family", c.getFamily());
                map.put("email", c.getEmail());
                map.put("money", c.getMoney());
                
                JSONArray roles = new JSONArray();
                for (int i = 0; i < c.getRoles().size(); i++) {
                    roles.put(c.getRoles().get(i).name);
                }  
                
                map.put("roles", roles);
                
                JsonBuilder.writeToJSON(map, response);
            }catch (DataIllegalException ex){
                    out.println(ex.getMessage());
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
