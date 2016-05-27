package net.internetengineering.myServiceHandlers;

import net.internetengineering.domain.dealing.BuyingOffer;
import net.internetengineering.exception.DataIllegalException;
import net.internetengineering.server.StockMarket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.internetengineering.exception.DBException;
import net.internetengineering.utils.HSQLUtil;
import net.internetengineering.utils.JsonBuilder;

public class BuyOrder{
	public static void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		MyLogger logger = new MyLogger(new ArrayList<String>());
                PrintWriter out = response.getWriter();
                Connection dbConnection=null;
                try {
                        String id = StockMarket.getInstance().isTestMode()?request.getParameter("id"): request.getRemoteUser();
                        String instrument = request.getParameter("instrument");
                        String type = request.getParameter("type");
                        Long price = Long.parseLong(request.getParameter("price"));
                        Long quantity = Long.parseLong(request.getParameter("quantity"));
                        BuyingOffer buyingOffer = new BuyingOffer(0L,price,quantity,type,id);
                        if(instrument==null || instrument.isEmpty())
                                throw new DataIllegalException("Mismatched Parameters");
                        buyingOffer.validateVariables();
                        dbConnection = HSQLUtil.getInstance().openConnectioin();
                        StockMarket.getInstance().executeBuyingOffer(out,buyingOffer,instrument,dbConnection,false);
                } catch (DataIllegalException e) {
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("Error",e.getMessage() );
                    JsonBuilder.writeToJSON(map, response);
                } catch (DBException ex) {
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("Error",ex.getMessage() );
                    JsonBuilder.writeToJSON(map, response);
                } catch (SQLException ex) {
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("Error","DBConnection error" );
                    JsonBuilder.writeToJSON(map, response);
                    Logger.getLogger(SellOrder.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                    try {
                        if(dbConnection!=null&&!dbConnection.isClosed())
                            dbConnection.close();
                    } catch (SQLException ex) {
                    }
                }
	}

	public static void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

}
