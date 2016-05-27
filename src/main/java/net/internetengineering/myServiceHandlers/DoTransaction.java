package net.internetengineering.myServiceHandlers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.HttpServlet;

/**
 * Created by Hamed Ara on 4/8/2016.
 */
@WebServlet("/transaction")
public class DoTransaction extends HttpServlet  {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String order = request.getParameter("order");
        if(order!=null && order.equals("buy"))
            BuyOrder.doPost(request,response);
        else if(order!=null && order.equals("sell"))
            SellOrder.doPost(request,response);
        else
            request.getRequestDispatcher("page-not-found.jsp").forward(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String order = request.getParameter("order");
        if(order!=null && order.equals("buy"))
            BuyOrder.doGet(request,response);
        else if(order!=null && order.equals("sell"))
            SellOrder.doGet(request,response);
        else
            request.getRequestDispatcher("page-not-found.jsp").forward(request, response);
    }
}
