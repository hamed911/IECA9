package net.internetengineering.myServiceHandlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
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
import net.internetengineering.exception.DataIllegalException;
import net.internetengineering.server.StockMarket;
import net.internetengineering.domain.dealing.Instrument;
import net.internetengineering.domain.dealing.SellingOffer;
import net.internetengineering.domain.dealing.BuyingOffer;
import net.internetengineering.exception.DBException;
import net.internetengineering.utils.HSQLUtil;
import net.internetengineering.utils.JsonBuilder;

 

@WebServlet("/getinstrument")
public class GetInstrument extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out= response.getWriter();
        Connection dbConnection = null;
        try{
            dbConnection = HSQLUtil.getInstance().openConnectioin();
            List<Instrument> instruments = StockMarket.getInstance().getInstruments(dbConnection);
            
            List<Object> myList = new ArrayList<Object>();
            JSONArray myList1 = new JSONArray();

            for (int i = 0; i < instruments.size(); i++) {
                Map<String,Object> map = new HashMap<String, Object>();
                JSONObject map1 = new JSONObject();

                
                map.put("name", instruments.get(i).getSymbol());
                map1.put("name", instruments.get(i).getSymbol());

                map.put("quantity", instruments.get(i).getQuantity());
                map1.put("quantity", instruments.get(i).getQuantity().toString());

                List<Object> sells = new ArrayList<Object>();
                JSONArray sells1 = new JSONArray();

                List<Object> buys = new ArrayList<Object>();
                JSONArray buys1 = new JSONArray();
                
                for (int j = 0; j < instruments.get(i).getSellingOffers().size(); j++) {
                    Map<String,Object> sell = new HashMap<String, Object>();
                    JSONObject sell1 = new JSONObject();

                    sell.put("id", instruments.get(i).getSellingOffers().get(j).getID());
                    sell.put("quantity", instruments.get(i).getSellingOffers().get(j).getQuantity());
                    sell.put("price", instruments.get(i).getSellingOffers().get(j).getPrice());
                    sell.put("type", instruments.get(i).getSellingOffers().get(j).getType());

                    sell1.put("id", instruments.get(i).getSellingOffers().get(j).getID());
                    sell1.put("quantity", instruments.get(i).getSellingOffers().get(j).getQuantity().toString());
                    sell1.put("price", instruments.get(i).getSellingOffers().get(j).getPrice().toString());
                    sell1.put("type", instruments.get(i).getSellingOffers().get(j).getType());

                    sells1.put(sell1);
                }
                
                for (int j = 0; j < instruments.get(i).getBuyingOffers().size(); j++) {
                    Map<String,Object> buy = new HashMap<String, Object>();
                    JSONObject buy1 = new JSONObject();

                    buy.put("id", instruments.get(i).getBuyingOffers().get(j).getID());
                    buy.put("quantity", instruments.get(i).getBuyingOffers().get(j).getQuantity());
                    buy.put("price", instruments.get(i).getBuyingOffers().get(j).getPrice());
                    buy.put("type", instruments.get(i).getBuyingOffers().get(j).getType());

                    buy1.put("id", instruments.get(i).getBuyingOffers().get(j).getID());
                    buy1.put("quantity", instruments.get(i).getBuyingOffers().get(j).getQuantity().toString());
                    buy1.put("price", instruments.get(i).getBuyingOffers().get(j).getPrice().toString());
                    buy1.put("type", instruments.get(i).getBuyingOffers().get(j).getType());

                    buys1.put(buy1);
                }

                map.put("sellingOffers", sells);
                map.put("buyingOffers", buys);

                map1.put("sellingOffers", sells1);
                map1.put("buyingOffers", buys1);

                myList.add(map);
                myList1.put(map1);
            }
            
                response.getWriter().print(myList1);
                response.setContentType("application/json");
        }catch (SQLException ex) {
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
