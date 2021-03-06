package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.Label;
import com.github.abel533.echarts.VisualMap;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.code.Position;
import com.github.abel533.echarts.code.VisualMapType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.itemstyle.Normal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author XZX 发文量统计分析
 */
@Named(value = "publishStatic")
@SessionScoped
public class PublishStatic implements Serializable {

    public PublishStatic() {
    }

    public String getData() {
        ResultSet year_count = null;     //逐年载文数量变化
        ResultSet year_pages = null;     //逐年页数
        ResultSet year_avgPages = null;  //逐年篇均页数

        Connection conn = null;
        Statement stat = null;
        String sql;
        ResultSet local = null;
        GsonOption option = new GsonOption();
        CategoryAxis yAxis = new CategoryAxis();
        CategoryAxis xAxis = new CategoryAxis();
        Bar line = new Bar();
        Bar line1 = new Bar();
        Bar line2 = new Bar();
        Bar line3 = new Bar();
    

        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select j_year , count(j_number) from journal_info group by j_year";
            year_count = stat.executeQuery(sql);
            while (year_count.next()) {
                xAxis.data(year_count.getString(1));
              yAxis.data(year_count.getString(1));
                line.data(year_count.getInt(2));
            }
            sql = "select j_year,sum(j_page2) from journal_info group by j_year";
            year_pages = stat.executeQuery(sql);
            while (year_pages.next()) {
                line1.data(year_pages.getInt(2));
            }
            sql = "select j_year,avg(j_page2) from journal_info group by j_year";
            year_avgPages = stat.executeQuery(sql);
            DecimalFormat df = new DecimalFormat("#.####");
            while (year_avgPages.next()) {
                line2.data(Double.parseDouble(df.format(year_avgPages.getFloat(2))));
                line3.data(Double.parseDouble(df.format(1.0/year_avgPages.getInt(2))));
            }
        } catch (Exception e) {

        } finally {
            JDBCUtils.close(null, stat, conn);

        }

        line.name("每年载文数量");
        line1.name("每年总页数");
        line2.name("每年篇均页数");
        option.yAxis(yAxis);
        option.xAxis(xAxis);
        option.series(line);
        option.series(line1);
        option.series(line2);
        option.series(line3);
        //System.out.println(option.toString());
        return option.toString();
   
    }

}
