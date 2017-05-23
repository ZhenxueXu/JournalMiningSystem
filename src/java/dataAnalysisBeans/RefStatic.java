package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.data.WordCloudData;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.google.gson.Gson;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Think 第四模块，引文分析
 */
@Named(value = "refStatic")
@SessionScoped
public class RefStatic implements Serializable {

    private ResultSet year_count;       //根据引文发表的年限，统计论文数量，只统计有年份记录的引文，因为有一部分参考文献没有年份记录，比如参考书籍等
    private int total;                  //有年份记录的参考文献总数，用来计算引文率   
    private List<Float> sumRate;        //累计引文率
    private GsonOption option = new GsonOption();
    private List<Map<String, Object>> data = new ArrayList<>();

    //以下是引用期刊趋势分析数据
    private String minYear;
    private String maxYear;
    List<String> yearData;
    List<List<WordCloudData>> trendData;

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
    Map<String,String> menu;

    public Map<String, String> getMenu() {
        return menu;
    }

    public void setMenu(Map<String, String> menu) {
        this.menu = menu;
    }

    public RefStatic() {
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
            sql="select distinct j_year from journal_info order by j_year";
            menu = new HashMap<>();
            rs = stat.executeQuery(sql);
            while(rs.next()){
                menu.put(rs.getString(1), rs.getString(1));
            }

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
        setAllData();
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
            sql = "select j_year, r_journal,count(*) as 被引次数  "
                    + "from paper_references,journal_info "
                    + "where r_journal<>'' and paper_references.j_number = journal_info.j_number  "
                    + "group by j_year,r_journal  "
                    + "order by j_year,被引次数 desc";
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
                temp.add(new WordCloudData(rs.getString(2), rs.getInt(3)));
            }
            trendData.add(temp);
            yearData.add(year);

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }

    }

    public String getYearData() {
        if (this.yearData == null) {
            setTimeline();
        }
        Gson gson = new Gson();
        return gson.toJson(yearData);
    }

    public void setYearData(List<String> yearData) {

        this.yearData = yearData;
    }

    public String getTrendData() {
        if (this.trendData == null) {
            setTimeline();
        }
        Gson gson = new Gson();
        return gson.toJson(trendData);

    }

    public void setTrendData(List<List<WordCloudData>> trendData) {
        this.trendData = trendData;
    }

    public void setAllData() {
        DecimalFormat df = new DecimalFormat("#.####");

        CategoryAxis xAxis = new CategoryAxis();
        Bar bar = new Bar();
        Pie pie = new Pie();
        Connection conn = null;
        Statement stat = null;
        ResultSet localSet = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select count(*) as total from paper_references where r_year REGEXP('[0-9]{4}')";
            localSet = stat.executeQuery(sql);      //局部变量，用于临时处理
            localSet.next();

            total = localSet.getInt(1);
            sumRate = new ArrayList<>();
            int i = 1;
            int sum = 0;
            String year = "";
            //引文的年代统计分析
            sql = "select r_year,count(*) from paper_references where r_year REGEXP('[0-9]{4}') group by r_year order by r_year DESC";
            year_count = stat.executeQuery(sql);
            while (year_count.next()) {  //显示20年的数据
                if (i < 20) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("year", year_count.getString(1));
                    row.put("amount", year_count.getInt(2));
                    float rate = Float.parseFloat(df.format(year_count.getInt(2) / 1.0 / total));
                    row.put("rate", rate);
                    xAxis.data(year_count.getString(1));
                    bar.data(year_count.getInt(2));
                    PieData piedata = new PieData(year_count.getString(1), rate);
                    pie.data(piedata);
                    if (i == 1) {
                        row.put("sumrate", rate);
                        sumRate.add(rate);
                    } else {
                        row.put("sumrate", sumRate.get(sumRate.size() - 1) + rate);
                        sumRate.add(sumRate.get(sumRate.size() - 1) + rate);
                    }
                    data.add(row);
                    year = year_count.getString(1);
                } else {
                    sum += year_count.getInt(2);
                }
                i++;
            }
            Map<String, Object> row = new HashMap<>();
            if (sum > 0) {
                float rate = Float.parseFloat(df.format(sum / 1.0 / total));
                row.put("year", year + "以前");
                row.put("amount", sum);
                row.put("rate", rate);
                xAxis.data(year + "以前");
                bar.data(sum);
                PieData piedata = new PieData(year + "以前", rate);
                pie.data(piedata);

                row.put("sumrate", 100);
                data.add(row);
            }
            option.series(bar).series(pie).xAxis(xAxis);


            /*end 引文年代统计分析*/
            // start 每年的引文数量和篇均引文数量
        } catch (Exception e) {

        } finally {
            JDBCUtils.close(localSet, stat, conn);
        }

    }

    public String getOption() {
        return option.toString();
    }

    public List<Map<String, Object>> getRefRateTable() {
        return data;
    }

    public String getRefData() {
        GsonOption option = new GsonOption();
        Bar bar = new Bar();
        Line line = new Line();
        CategoryAxis xAxis = new CategoryAxis();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select j_year as 年份,count(*) as 引文数量,count(*)/count(distinct A.j_number) as 篇均引文量 from journal_info A,paper_references B where A.j_number=B.j_number group by j_year order by j_year";
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                xAxis.data(rs.getString(1));
                bar.data(rs.getInt(2));
                line.data(rs.getFloat(3));
            }
            option.series(bar).series(line).xAxis(xAxis);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
        return option.toString();
    }

    public String getRefTime() {
        Gson gson = new Gson();
        List<WordCloudData> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select r_journal,count(*) as 被引次数 "
                    + "from paper_references,journal_info "
                    + "where r_journal<>'' and paper_references.j_number = journal_info.j_number and j_year between '" + minYear + "' and '" + maxYear + "' "
                    + "group by r_journal "
                    + "order by 被引次数 desc";
            rs = stat.executeQuery(sql);
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                list.add(new WordCloudData(rs.getString(1), rs.getInt(2)));
            }

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
        return gson.toJson(list);
    }

    public List<Map> getTableData() {
        List<Map> data = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select r_journal,count(*) as 被引次数 "
                    + "from paper_references where r_journal<>'' "
                    + "group by r_journal "
                    + "order by 被引次数 desc";
            rs = stat.executeQuery(sql);
            data = JDBCUtils.getResultList(rs);

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
        return data;
    }

}
