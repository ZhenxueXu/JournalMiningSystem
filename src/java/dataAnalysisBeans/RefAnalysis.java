package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;

/**
 * @author Think 第三模块，被引统计
 */
@Named(value = "refAnalysis")
@SessionScoped
public class RefAnalysis implements Serializable {

    public RefAnalysis() {
    }

    public String getData() {
        ResultSet year_citation;   //期刊每年总被引次数
        ResultSet citationSort;    //论文被引次数排序
        Connection conn = null;
        Statement stat = null;
        String sql;
        ResultSet local = null;
        GsonOption option = new GsonOption();
        CategoryAxis xAxis = new CategoryAxis();
        Line line = new Line();
        Line line1 = new Line();

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
        return option.toString();
    }

}
