/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.internetengineering.utils;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import java.util.List;

/**
 *
 * @author Hamed Ara
 */
public class JsonBuilder {
    public static void writeToJSON(Map<String,Object>map, HttpServletResponse response) throws IOException{
        JSONObject json = new JSONObject(map);
        response.getWriter().print(json);
        response.setContentType("application/json");
    }

    public static void writeListToJSON(List<Object> myList, HttpServletResponse response) throws IOException{
        PrintWriter out= response.getWriter();
        
            for(int x = 0 ; x < myList.size() ; x++){
                JSONObject json = new JSONObject(myList.get(x));

                response.getWriter().print(json);
                out.println(json);
                out.println(myList.get(x));
            }
            
            response.setContentType("application/json");
    }
    
}
