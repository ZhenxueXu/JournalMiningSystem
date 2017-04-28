/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.VisualMap;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Think
 */
@Named(value = "test2")
@SessionScoped
public class Test2 implements Serializable {

   
    public Test2() {
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
            sql = "select j_year as 年,sum(j_citation_frequency) as 被引次数 from journal_info group by j_year order by j_year";
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
}
