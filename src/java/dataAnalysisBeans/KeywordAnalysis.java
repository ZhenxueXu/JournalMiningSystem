/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.data.WordCloudData;
import com.github.abel533.echarts.series.Series;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *关键字分析
 * @author Think
 */
@Named(value = "keywordAnalysis")
@SessionScoped
public class KeywordAnalysis implements Serializable {

  
    public KeywordAnalysis() {
    }
    
    public String getAllData(){
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        String data = "";
        try{
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select keyword as 关键字,count(*) as 词频\n"
                    + "from paper_keywords\n"
                    + "group by keyword\n"
                    + "order by 词频 desc limit 200";
            rs = stat.executeQuery(sql);
            Gson gson = new Gson();
            List<WordCloudData> list = new ArrayList<>();
            while(rs.next()){
                list.add(new WordCloudData(rs.getString(1),rs.getInt(2)));
            }
            data = gson.toJson(list);
            
        }catch(Exception e){
            
        }finally{
            JDBCUtils.close(rs, stat, conn);
        }
        return data;
    }
    
}
