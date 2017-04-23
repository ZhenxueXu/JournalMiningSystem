/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyServlets;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.VisualMap;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.VisualMapType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author Think
 */
@WebServlet(name = "ReferencedTimes", urlPatterns = {"/ReferencedTimes"})
public class ReferencedTimes extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
         request.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
           out.print(getData());
           out.close();
        }
    }
    
    public String getData(){
        
        Connection conn = null;
        Statement stat = null;
        String sql;
        ResultSet local = null;
        GsonOption option = new GsonOption();
        CategoryAxis xAxis = new CategoryAxis();
        Line line = new Line();
        VisualMap visualmap = new VisualMap();
        int max = 0;
        int min = 100000;
        try{
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();            
            sql = "select j_year,sum(j_citation_frequency) from journal_info group by j_year order by j_year";
            local = stat.executeQuery(sql);
            while(local.next()){
               xAxis.data(local.getString(1));
               line.data(local.getInt(2));
               if(local.getInt(2)>=max) max = local.getInt(2);
               if(local.getInt(2)<=min) min = local.getInt(2);
            }
                       
        }catch(Exception e){
            
        }finally{
            JDBCUtils.close(local, stat, conn);
        }   
        line.name("期刊总被引次数变化");
        option.xAxis(xAxis);
        option.series(line);
        while(max%10!=0){
            max++;
        }
        while(min%10!=0){
            min--;
        }
        
        //visualmap.max(max).min(min).type(VisualMapType.valueOf("piecewise")).splitNumber(5);
        System.out.println(option.toString());
        return option.toString();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
