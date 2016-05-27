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
import net.internetengineering.domain.dealing.TransactionType;
import net.internetengineering.exception.DBException;
import net.internetengineering.exception.DataIllegalException;
import net.internetengineering.server.StockMarket;
import net.internetengineering.utils.HSQLUtil;

/**
 *
 * @author Hamed Ara
 */
@WebServlet("/approvedeposit")
public class ApproveDeposit extends HttpServlet{
        @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Connection dbConnection = null;
                try {
                        String id = StockMarket.getInstance().isTestMode()?request.getParameter("id"): request.getRemoteUser();
			String cid = request.getParameter("cid");
			if (id == null || id.isEmpty() || cid == null)
				throw new DataIllegalException("Mismatched Parameters");
                        dbConnection = HSQLUtil.getInstance().openConnectioin();
			if (StockMarket.getInstance().containCustomer(id,dbConnection)){
                            Long amount= StockMarket.getInstance().getDepositRequests().get(cid);
                            if(amount==null)
                                throw new DataIllegalException("No request for the customer");
                            StockMarket.getInstance().executeFinancialTransaction(cid, TransactionType.DEPOSIT,amount,dbConnection);
                            StockMarket.getInstance().getDepositRequests().remove(cid);
                            out.println("'"+amount+"'$ is added to '"+cid+"' successfully");
			}
			else {
				throw new DataIllegalException("Unknown user id");
			}
//			request.setAttribute("successes", logger.getAndFlushMyLogger());
			StockMarket.getInstance().getDepositRequests().remove(id);
		}catch (DataIllegalException ex){
			out.println(ex.getMessage());
//			request.setAttribute("errors", logger.getAndFlushMyLogger());
		} catch (SQLException ex) {
                    out.print("Database Error happend");
                    Logger.getLogger(DepositHandler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (DBException ex) {
                    out.println(ex.getMessage());
                    Logger.getLogger(DepositHandler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NumberFormatException ex){
                    out.print("Invalid Number Format");
                    Logger.getLogger(DepositHandler.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                    try {
                        if(dbConnection!=null&&!dbConnection.isClosed())
                            dbConnection.close();
                    } catch (SQLException ex) {
                    }
                }
//		request.getRequestDispatcher("show-info.jsp").forward(request, response);

	}
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doPost(request,response);
    }

}
