/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.data.WordCloudData;
import com.google.gson.Gson;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 关键字分析
 *
 * @author Think
 */
@Named(value = "keywordAnalysis")
@SessionScoped
public class KeywordAnalysis implements Serializable {

    private String minYear;
    private String maxYear;
    List<String> yearData;
    List<List<WordCloudData>> trendData;

    public KeywordAnalysis() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select min(j_year),max(j_year)from journal_info";  //数据年限
            rs = stat.executeQuery(sql);
            if (rs.next()) {
                minYear = rs.getString(1);
                maxYear = rs.getString(2);
            }

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
    }

    public String getMinYear() {
        return minYear;
    }

    public void setMinYear(String minYear) {
        this.minYear = minYear;
    }

    public String getMaxYear() {        
        return maxYear;
    }

    public void setMaxYear(String maxYear) {
        this.maxYear = maxYear;
    }

    public String getAllData() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        String data = "";
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select keyword as 关键字,count(*) as 词频 "
                    + "from paper_keywords,journal_info "
                    + "where paper_keywords.j_number = journal_info.j_number and journal_info.j_year between '" + minYear + "' and '" + maxYear + "' "
                    + "group by keyword  "
                    + "order by 词频 desc";
            rs = stat.executeQuery(sql);
            Gson gson = new Gson();
            List<WordCloudData> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new WordCloudData(rs.getString(1), rs.getInt(2)));
            }
            data = gson.toJson(list);

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
        return data;
    }

    public void setTimeline() {
        yearData = new ArrayList<>();
        trendData = new ArrayList<>();
        List<WordCloudData> temp = null;
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select j_year, keyword as 关键字,count(*) as 词频 "
                    + "from paper_keywords,journal_info "
                    + "where paper_keywords.j_number = journal_info.j_number "
                    + "group by j_year,keyword  "
                    + "order by j_year,词频 desc";
            rs = stat.executeQuery(sql);
            String year = "";
            while (rs.next()) {
                if (!year.equals(rs.getString(1))) {                    
                    if (temp != null) {
                        trendData.add(temp);
                        yearData.add(year);
                    }
                    year = rs.getString(1);
                    temp = new ArrayList<>();
                }
                temp.add(new WordCloudData(rs.getString(2),rs.getInt(3)));
            }
            trendData.add(temp);
            yearData.add(year);

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }

    }

    public String getYearData() {
        if(this.yearData==null){
            setTimeline();
        }
        Gson gson = new Gson();
        return gson.toJson(yearData);
    }

    public void setYearData(List<String> yearData) {
        
        this.yearData = yearData;
    }

    public String getTrendData() {
        if(this.trendData==null){
            setTimeline();
        }
        Gson gson = new Gson();
        return gson.toJson(trendData);
        
    }

    public void setTrendData(List<List<WordCloudData>> trendData) {
        this.trendData = trendData;
    }
    

}
