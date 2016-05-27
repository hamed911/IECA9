package net.internetengineering.myServiceHandlers;

import net.internetengineering.domain.dealing.TransactionType;
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
import net.internetengineering.exception.DBException;
import net.internetengineering.utils.HSQLUtil;

@WebServlet("/deposit")
public class DepositHandler extends HttpServlet{
        @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Connection dbConnection = null;
                try {
                        String id = StockMarket.getInstance().isTestMode()?request.getParameter("id"): request.getRemoteUser();
			Long amount = Long.parseLong(request.getParameter("amount"));
			if (id == null || id.isEmpty() || amount == null)
				throw new DataIllegalException("Mismatched Parameters");
                        dbConnection = HSQLUtil.getInstance().openConnectioin();
			if (StockMarket.getInstance().containCustomer(id,dbConnection)){
				StockMarket.getInstance().executeFinancialTransaction(id, TransactionType.DEPOSIT,amount,dbConnection);
				out.println("Successful");
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
                } finally{
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