package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.google.gson.Gson;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Think 第三模块，被引统计
 */
@Named(value = "refAnalysis")
@SessionScoped
public class RefAnalysis implements Serializable {

    private String box;
    private List<List> boxdata;
    private GsonOption option;
    private CategoryAxis xAxis;
    private Line line;
    private Line line1;

    public RefAnalysis() {
        setData();
        setBoxData();
    }

    public void setData() {
        ResultSet year_citation;   //期刊每年总被引次数
        ResultSet citationSort;    //论文被引次数排序
        Connection conn = null;
        Statement stat = null;
        String sql;
        ResultSet local = null;
        option = new GsonOption();
        xAxis = new CategoryAxis();
        line = new Line();
        line1 = new Line();

        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select j_year,sum(j_citation_frequency) from journal_info group by j_year order by j_year";
            year_citation = stat.executeQuery(sql);

            while (year_citation.next()) {
                xAxis.data(year_citation.getString(1));
                line.data(year_citation.getInt(2));
            }

            sql = "select j_number,j_title,j_citation_frequency from journal_info order by j_citation_frequency DESC";  //用来处理频次分布，高引，低引
            citationSort = stat.executeQuery(sql);
            while (citationSort.next()) {
                line1.data(citationSort.getInt(2));
            }

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(null, stat, conn);
        }
        line.name("每年期刊被引次数");
        line1.name("每年论文被引次数排序");
        option.xAxis(xAxis);
        option.series(line);
        option.series(line1);
        System.out.println(option.toString());
    }

    public String getBoxdata() {
        Gson gson = new Gson();
        return gson.toJson(boxdata);
    }

    public void setBoxData() {

        Connection conn = null;
        Statement stat = null;
        ResultSet local = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select j_year,j_citation_frequency from journal_info order by j_year,j_citation_frequency";   //盒图数据
            local = stat.executeQuery(sql);
            List<Integer> item = new ArrayList<>();
            boxdata = new ArrayList<>();
            String year = "";
            int i = 1;
            while (local.next()) {
                if (i++ == 1) {
                    item = new ArrayList<>();
                    year = local.getString(1);
                }
                if (!year.equals(local.getString(1))) {
                    boxdata.add(item);
                    item = new ArrayList<>();
                    year = local.getString(1);
                }
                item.add(local.getInt(2));
            }
            boxdata.add(item);

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(local, stat, conn);
        }

    }

    public String getOption() {
        return option.toString();
    }

}
