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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.internetengineering.exception.DBException;
import net.internetengineering.exception.DataIllegalException;
import net.internetengineering.server.StockMarket;
import net.internetengineering.utils.HSQLUtil;

/**
 *
 * @author Hamed Ara
 */
@WebServlet("/transaclimit")
public class TransacLimit extends HttpServlet{
        @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
                try {
                    String id = StockMarket.getInstance().isTestMode()?request.getParameter("id"): request.getRemoteUser();
                    Long amount = Long.parseLong(request.getParameter("amount"));
                    if (id == null || id.isEmpty() || amount == null ||amount<=0)
                            throw new DataIllegalException("Mismatched Parameters");
                    StockMarket.getInstance().setTransacLimitation(amount);
                    out.println("Transaction limitation set to: "+amount);
			
		}catch (DataIllegalException ex){
			out.println(ex.getMessage());
		} catch (NumberFormatException ex){
                    out.print("Invalid Number Format");
                    Logger.getLogger(DepositHandler.class.getName()).log(Level.SEVERE, null, ex);
                }

	}
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doPost(request,response);
    }

}