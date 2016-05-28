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
import net.internetengineering.domain.dealing.SellingOffer;
import net.internetengineering.exception.DBException;
import net.internetengineering.exception.DataIllegalException;
import net.internetengineering.server.StockMarket;
import net.internetengineering.utils.HSQLUtil;

/**
 *
 * @author Hamed Ara
 */
@WebServlet("/apptransac")
public class ApproveTransactions extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {   
        PrintWriter out = response.getWriter();
        try {
            StockMarket.getInstance().confirmHeavyTransactions(out);
            out.print("All the transactions are confirmed.");
        } catch (DBException ex) {
            out.print(ex.getMessage());
            Logger.getLogger(SellOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            out.print("Internal Error happend" );
            Logger.getLogger(SellOrder.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
