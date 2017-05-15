/*
 * 第五模块，科研作者分析
	作者影响力(h指数)
	核心作者（普赖斯公式）
	作者发文排名
	作者被引排名

 */
package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.code.Position;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Graph;
import com.github.abel533.echarts.series.force.Link;
import com.github.abel533.echarts.series.force.Node;
import com.github.abel533.echarts.style.itemstyle.Normal;
import com.google.gson.Gson;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Think
 */
@Named(value = "authorAnalysis")
@SessionScoped
public class AuthorAnalysis implements Serializable {

    private List<Map.Entry<String, Integer>> hIndex;  //h指数
    private Set<String> core_author;                //核心作者
    private ResultSet publish1;                      //发文量统计，格式（作者[string]，发文量[int]）
    private ResultSet publish2;                      //发文量完整数据，用于表格
    private ResultSet Quote;
    private ResultSet ref;                          //被引统计，格式（作者[string]，被引总计[int]）
    private int min_h_index = 5;                        //用于筛选核心作者的最低h指数
    private ResultSet co_publish;
    private GsonOption option1;
    private CategoryAxis xAxis1;
    private Bar bar1;
    private GsonOption option2;
    private CategoryAxis xAxis2;
    private Bar bar2;
    private List<Map> AuthorPublishdata;
     private List<Map> AuthorQuotedata;
    private String minYear = "2010";
    private String maxYear ="2015";
   


    public AuthorAnalysis() {
        setAllData();
        getHIdex();
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
    
    

    public void setMin_h_index(int min_h_index) {
        this.min_h_index = min_h_index;
    }

    public void setAllData() {
        min_h_index = 10;
        Connection conn = null;
        Statement stat = null;
        ResultSet local = null;
        option1 = new GsonOption();
        xAxis1 = new CategoryAxis();
        bar1 = new Bar();
        option2 = new GsonOption();
        xAxis2 = new CategoryAxis();
        bar2 = new Bar();
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            String sql;
            //--start 发文量统计图---//
            sql = "select j_author as 作者, count(*) as 发文量 "
                    + " from journal_info a,paper_author b "
                    + " where a.j_number = b.j_number "
                    + " group by b.j_author "
                    + " having 发文量>1 "
                    + " order by 发文量 desc";
            publish1 = stat.executeQuery(sql);
            int shu = 1;
            int mapcount = 1;
            Map<Integer, List<Integer>> data = new HashMap<>();
            Map<Integer, List<String>> xdata = new HashMap<>();
            List<Integer> datalist = new ArrayList<>();
            List<String> xlist = new ArrayList<>();
            while (publish1.next()) {
                if (shu % 25 == 0) {
                    data.put(mapcount, datalist);
                    xdata.put(mapcount, xlist);
                    mapcount++;
                    datalist = new ArrayList<>();
                    xlist = new ArrayList<>();
                }
                datalist.add(publish1.getInt(2));
                xlist.add(publish1.getString(1));
                shu++;
            }
            bar2.data(data);
            xAxis2.data(xdata);

            //--start 发文量统计表---//
            sql = "select j_author as Author, count(*) as PublishCount "
                    + " from journal_info a,paper_author b "
                    + " where a.j_number = b.j_number "
                    + " group by b.j_author "
                    + " order by PublishCount desc";
            publish2 = stat.executeQuery(sql);
            AuthorPublishdata = new ArrayList<>();
            AuthorPublishdata = JDBCUtils.getResultList(publish2);

            //--start 被引统计 --//     
            sql = "select j_author as 作者, sum(j_citation_frequency) as 被引总计 "
                    + " from journal_info,paper_author "
                    + " where journal_info.j_number = paper_author.j_number "
                    + " group by paper_author.j_author"
                    + " order by 被引总计 desc ";
            ref = stat.executeQuery(sql);
            shu = 1;
            mapcount = 1;
            data = new HashMap<>();
            xdata = new HashMap<>();
            datalist = new ArrayList<>();
            xlist = new ArrayList<>();
            while (ref.next()) {
                if (shu % 25 == 0) {
                    data.put(mapcount, datalist);
                    xdata.put(mapcount, xlist);
                    mapcount++;
                    datalist = new ArrayList<>();
                    xlist = new ArrayList<>();
                }
                datalist.add(ref.getInt(2));
                xlist.add(ref.getString(1));
                shu++;
            }
            bar1.data(data);
            xAxis1.data(xdata);
                        //--start 被引量统计表---//
            sql = "select j_author as 作者, sum(j_citation_frequency) as 被引总计 "
                    + " from journal_info,paper_author "
                    + " where journal_info.j_number = paper_author.j_number "
                    + " group by paper_author.j_author"
                    + " order by 被引总计 desc ";
            Quote = stat.executeQuery(sql);
            AuthorQuotedata = new ArrayList<>();
            AuthorQuotedata = JDBCUtils.getResultList(Quote);
            // -- star 合作发文量 --//
            sql = "select 合作人数 , count(*) as 发文量 from ("
                    + "     select count(*) as 合作人数, a.j_number as 编号"
                    + "     from journal_info a,paper_author b"
                    + "     where a.j_number = b.j_number"
                    + "     group by b.j_number"
                    + "     order by 合作人数 desc) as a"
                    + "group by 合作人数";
            co_publish = stat.executeQuery(sql);

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(local, stat, conn);

        }
        bar2.name("发文统计");
        option2.xAxis(xAxis2);
        option2.series(bar2);
        bar1.name("被引统计");
        option1.xAxis(xAxis1);
        option1.series(bar1);

    }

    public void getHIdex() {


        Connection conn = null;
        Statement stat = null;
        ResultSet local = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select j_author as 作者,paper_author.j_number as 论文编号,j_citation_frequency as 被引次数 "
                    + " from journal_info,paper_author "
                    + " where journal_info.j_number = paper_author.j_number "
                    + " order by 作者,被引次数 DESC ";
            local = stat.executeQuery(sql);
            String currentAuthor = "";
            int h_index = 0;
            int row = 1;
            Map<String, Integer> current = new HashMap<>();
            while (local.next()) {
                if (!local.getString(1).equals(currentAuthor)) {
                    current.put(currentAuthor, h_index);
                    currentAuthor = local.getString(1);
                    row = 1;
                    h_index = local.getInt(3) >= row ? 1 : 0;
                    row++;
                } else {
                    if (local.getInt(3) >= row) {
                        h_index = row++;
                    }
                }
            }
            current.put(currentAuthor, h_index);                //放入最后一个作者
            hIndex = new ArrayList<>(current.entrySet());
            sql = "call proce_core_author()";
            local = stat.executeQuery(sql);                   //根据普赖斯公式选出的候选核心作者
            core_author = new HashSet<>();
            while (local.next()) {
                if (current.get(local.getString(1)) >= min_h_index) {
                    core_author.add(local.getString(1));
                }
            }

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(local, stat, conn);
        }

    }

    public String getAuthorBeiYinData() {
        //被引统计      
        return option1.toString();
    }

    public String getAuthorFaWenData() {
        //发文图表统计        
        return option2.toString();
    }

    public List<Map> getAuthorQuotedata() {
        return AuthorQuotedata;
    }

 
    public List<Map> getAuthorPublishdata() {
        return AuthorPublishdata;
    }

    public String getHBar() {
        Bar bar = new Bar();
        CategoryAxis xAxis = new CategoryAxis();
        GsonOption option = new GsonOption();
        Collections.sort(hIndex, (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) -> o2.getValue() - o1.getValue()); //根据h指数排序
        for (Map.Entry<String, Integer> item : hIndex) {
            xAxis.data(item.getKey());
            bar.data(item.getValue());
        }
        option.xAxis(xAxis).series(bar);
        return option.toString();
    }

    public String getCoreAuthorInfo() {
        Map<String, Map<String, Integer>> author_keyword = new HashMap();
        Map<String, Integer> keywords = new HashMap<>();
        Set<String> key = new HashSet<>();
        Graph graph = new Graph();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select distinct j_orgin from journal_info";
            rs = stat.executeQuery(sql);
            String journalName ="";
            if(rs.next()){
                journalName = rs.getString(1);
            }
           
            for (String author : core_author) {
                Node node = new Node();
                Normal normal=new Normal();
                normal.show(true).position(Position.top);
                node.name(author).symbolSize(25);
                Link link = new Link();
                link.source(journalName).target(author);
                graph.data(node).links(link);
                sql = "select j_author,keyword,count(*) "
                        + "from paper_author,paper_keywords "
                        + "where paper_author.j_number=paper_keywords.j_number and j_author = " + "'" + author + "' "
                        + "group by j_author,keyword "
                        + "order by count(*) desc";
                rs = stat.executeQuery(sql);
                keywords = new HashMap<>();
                while (rs.next()) {
                    keywords.put(rs.getString(2), rs.getInt(3));
                    key.add(rs.getString(2));
                    link = new Link();
                    link.source(author).target(rs.getString(2));
                    graph.links(link);
                }
                author_keyword.put(author, keywords);
            }
            for (String k : key) {
                Node node = new Node();
                node.name(k).symbolSize(2);
                graph.data(node);
            }

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }

        Gson gson = new Gson();
        return gson.toJson(graph);
    }

}
