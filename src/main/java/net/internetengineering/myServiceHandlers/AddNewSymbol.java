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
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
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
import net.internetengineering.utils.JsonBuilder;

/**
 *
 * @author Hamed Ara
 */
@WebServlet("/addnewsymbol")
public class AddNewSymbol extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {   
        PrintWriter out = response.getWriter();
        Connection dbConnection = null;
        try {
            String id = StockMarket.getInstance().isTestMode()?request.getParameter("id"): request.getRemoteUser();
            String symbol = request.getParameter("symbol");
            Long price = Long.parseLong(request.getParameter("price"));
            Long quantity = Long.parseLong(request.getParameter("amount"));
            SellingOffer sellingOffer = new SellingOffer(0L,price, quantity, "GTC", id);
            if (symbol == null || symbol.isEmpty())
                throw new DataIllegalException("Mismatched Parameters");
            sellingOffer.validateVariables();
            dbConnection = HSQLUtil.getInstance().openConnectioin();
            StockMarket.getInstance().executeSellingOffer(out, sellingOffer, symbol,dbConnection,true);
            out.print("Your request for adding a new symbol is saved.");
        } catch (DataIllegalException e) {
            out.print(e.getMessage());
        } catch (DBException ex) {
            out.print(ex.getMessage());
            Logger.getLogger(SellOrder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            out.print("DBConnection error" );
            Logger.getLogger(SellOrder.class.getName()).log(Level.SEVERE, null, ex);
        }catch (NumberFormatException e) {
            out.print("Invalid Number Format." );
            Logger.getLogger(SellOrder.class.getName()).log(Level.SEVERE, null, e);
        } catch (Exception e) {
            out.print("Internal Error happend" );
            Logger.getLogger(SellOrder.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            try {
                if(dbConnection!=null&&!dbConnection.isClosed())
                    dbConnection.close();
            } catch (SQLException ex) {
            }
        }
    }
}
