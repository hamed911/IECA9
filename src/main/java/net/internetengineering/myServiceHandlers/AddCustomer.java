package net.internetengineering.myServiceHandlers;

import net.internetengineering.domain.Customer;
import net.internetengineering.exception.DataIllegalException;
import net.internetengineering.server.StockMarket;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import net.internetengineering.domain.Role;
import net.internetengineering.exception.DBException;
import net.internetengineering.model.RoleDAO;
import net.internetengineering.utils.HSQLUtil;
import net.internetengineering.utils.HashUtil;

@WebServlet("/add")
public class AddCustomer extends HttpServlet{
        @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            PrintWriter out= response.getWriter();
            Connection dbConnection = null;
            try {
                String id = request.getParameter("id");
                String name = request.getParameter("name");
                String family = request.getParameter("family");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                if (id == null|| name == null || email==null || password==null|| family == null||  
                        id.isEmpty() || name.isEmpty() || family.isEmpty()|| email.isEmpty() || password.isEmpty()) {
                    throw new DataIllegalException("Mismatched Parameters");
                }
                dbConnection = HSQLUtil.getInstance().openConnectioin();
                if (StockMarket.getInstance().containCustomer(id,dbConnection)) {
                    throw new DataIllegalException("Repeated id");

                } else {
//                    Customer c = new Customer(id, name, family,email,HashUtil.md5(password) );
                    Customer c = new Customer(id, name, family,email,password );
                    c.addRole(RoleDAO.findByRoleName("typical", dbConnection));
                    StockMarket.getInstance().addNewCustomer(c,dbConnection);
                    out.println("New user is added");
                }
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
        @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doPost(request,response);
	}
}