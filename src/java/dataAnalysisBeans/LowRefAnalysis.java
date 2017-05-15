/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.data.WordCloudData;
import com.github.abel533.echarts.json.GsonOption;
import com.google.gson.Gson;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Think
 */
@Named(value = "lowRefAnalysis")
@SessionScoped
public class LowRefAnalysis implements Serializable {

    private int maxTimes = 5;
    private List<List<WordCloudData>> data;
    private GsonOption option;
    
    public LowRefAnalysis() {
    }

    public int getMaxTimes() {
        setData();
        return maxTimes;
    }

    public void setMaxTimes(int maxTimes) {
        this.maxTimes = maxTimes;
    }
    
     public void setData() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        data = new ArrayList<>();

        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select keyword,count(*) "
                    + "from journal_info,paper_keywords "
                    + "where j_citation_frequency<=" + maxTimes + "  and journal_info.j_number = paper_keywords.j_number "
                    + "group by keyword "
                    + "order by count(*) desc ";
            rs = stat.executeQuery(sql);
            List<WordCloudData> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new WordCloudData(rs.getString(1), rs.getInt(2)));
            }
            data.add(list);
            sql = "select j_author,count(*) "
                    + "from journal_info,paper_author "
                    + "where j_citation_frequency<=" + maxTimes + "  and journal_info.j_number = paper_author.j_number  "
                    + "group by j_author "
                    + "order by count(*) desc";
            rs = stat.executeQuery(sql);
            list = new ArrayList<>();
            while (rs.next()) {
                list.add(new WordCloudData(rs.getString(1), rs.getInt(2)));
            }
            data.add(list);

            sql = "select j_fund,count(*) "
                    + "from journal_info,paper_fund "
                    + "where j_citation_frequency<=" + maxTimes + "  and journal_info.j_number = paper_fund.j_number "
                    + "group by j_fund "
                    + "order by count(*) desc";
            rs = stat.executeQuery(sql);
            list = new ArrayList<>();
            while (rs.next()) {
                list.add(new WordCloudData(rs.getString(1), rs.getInt(2)));
            }
            data.add(list);

            sql = "select j_address,count(*) "
                    + "from journal_info,paper_address "
                    + "where j_citation_frequency<=" + maxTimes + "  and journal_info.j_number = paper_address.j_number "
                    + "group by j_address "
                    + "order by count(*) desc";
            rs = stat.executeQuery(sql);
            list = new ArrayList<>();
            while (rs.next()) {
                list.add(new WordCloudData(rs.getString(1), rs.getInt(2)));
            }
            data.add(list);
            sql = "select r_year,count(*) as times "
                    + "from paper_references,journal_info "
                    + "where paper_references.j_number = journal_info.j_number and journal_info.j_citation_frequency <=" + maxTimes + "  and r_year<> 'null' "
                    + "group by r_year "
                    + "order by times desc ";
            rs = stat.executeQuery(sql);
            list = new ArrayList<>();
            while (rs.next()) {
                list.add(new WordCloudData(rs.getString(1), rs.getInt(2)));
            }
            data.add(list);

            sql = "select r_journal,count(*) "
                    + "from paper_references,journal_info "
                    + "where paper_references.j_number = journal_info.j_number and journal_info.j_citation_frequency <=" + maxTimes + " and r_journal<>'' "
                    + "group by r_journal "
                    + "order by count(*) desc";
            rs = stat.executeQuery(sql);
            list = new ArrayList<>();
            while (rs.next()) {
                list.add(new WordCloudData(rs.getString(1), rs.getInt(2)));
            }
            data.add(list);
            //            sql = "select j_year,count(*) "
            //                    + "from journal_info "
            //                    + "where j_citation_frequency > 20  "
            //                    + "group by j_year";
            //            rs = stat.executeQuery(sql);
            //            Line line = new Line();
            //            CategoryAxis xAxis = new CategoryAxis();
            //            option = new GsonOption();
            //            while(rs.next()){
            //                line.data(rs.getInt(2));
            //                xAxis.data(rs.getString(1));
            //            }
            //            option.series(line).xAxis(xAxis);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
    }

    public String getData() {
        if(data==null){
            setData();
        }
        Gson gson = new Gson();
        return gson.toJson(data);
    }
    
}
