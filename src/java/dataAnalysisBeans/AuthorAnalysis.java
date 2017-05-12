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
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private ResultSet ref;                          //被引统计，格式（作者[string]，被引总计[int]）
    private int min_h_index;                        //用于筛选核心作者的最低h指数
    private ResultSet co_publish;
    private GsonOption option1;
    private CategoryAxis xAxis1;
    private Bar bar1;
    private GsonOption option2;
    private CategoryAxis xAxis2;
    private Bar bar2;
    private List<Map> AuthorPublishdata ;


    public AuthorAnalysis() {

        setAllData();
    }

    public void setMax_h_index(int min_h_index) {
        this.min_h_index = min_h_index;
    }

    public void setAllData() {
        min_h_index = 5;
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
            //用于计算h指数
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
            hIndex = new ArrayList<Map.Entry<String, Integer>>(current.entrySet());
            Collections.sort(hIndex, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o2.getValue() - o1.getValue();
                }

            });
            /**
             * 在此处对输出格式进一步处理 画图数据格式处理
             */

            sql = "call proce_core_author()";
            local = stat.executeQuery(sql);                   //根据普赖斯公式选出的候选核心作者
            core_author = new HashSet<>();
            while (local.next()) {
                if (current.get(local.getString(1)) >= min_h_index) {
                    core_author.add(local.getString(1));
                }
            }

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
                if (shu % 20 == 0) {
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
            //System.out.println(AuthorPublishdata);


            //--start 被引统计 --//     
            sql = "select j_author as 作者, sum(j_citation_frequency) as 被引总计 "
                    + " from journal_info,paper_author "
                    + " where journal_info.j_number = paper_author.j_number "
                    + " group by paper_author.j_author"
                    + " order by 被引总计 desc ";
            ref = stat.executeQuery(sql);

            for (int i = 1; i <= 20; i++) {

                if (ref.next()) {
                    xAxis1.data(ref.getString(1));
                    bar1.data(ref.getInt(2));//被引统计
                }

            }

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

    public String getAuthorBeiYinData() {
        //被引统计      
        return option1.toString();
    }

    public String getAuthorFaWenData() {
        //发文图表统计        
        return option2.toString();
    }

    public List<Map> getAuthorPublishdata() {
        return AuthorPublishdata;
    }


 
}
