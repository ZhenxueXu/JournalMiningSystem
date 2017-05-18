package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.google.gson.Gson;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Think 第三模块，被引统计
 */
@Named(value = "refAnalysis")
@SessionScoped
public class RefAnalysis implements Serializable {

    private String box;
    private GsonOption option;
    private GsonOption option2;  //频次分布图option
    private CategoryAxis xAxis;
    private Line line;
    private Line line1;
    private Map<String, List> bdata;

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
        CategoryAxis Axis = new CategoryAxis();
        int[] times = {0};
        Pie pie = new Pie();
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
        } catch (Exception e) {

        } finally {
            JDBCUtils.close(null, stat, conn);
        }
        line.name("每年期刊被引次数");
        line1.name("每年论文被引次数排序");
        option.xAxis(xAxis);
        option.series(line);
        option.series(line1);
        pie.data(times);
        option2 = new GsonOption();
        option2.xAxis(Axis).series(pie);
        System.out.println(option2.toString());

    }

    public String getFrequency() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        CategoryAxis xAxis = new CategoryAxis();
        Line line = new Line();
        GsonOption option = new GsonOption();
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select j_citation_frequency,count(*) "
                    + "from journal_info "
                    + "group by j_citation_frequency "
                    + "order by j_citation_frequency ";
            rs = stat.executeQuery(sql);
            while(rs.next()){
                xAxis.data(rs.getInt(1));
                line.data(rs.getInt(2));
            }
            option.series(line).xAxis(xAxis);

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
       
        

        return option.toString();
    }

    public String getBoxdata() {
        Gson gson = new Gson();
        return gson.toJson(bdata);
    }

    public void setBoxData() {
        List<List<Integer>> boxdata;
        boxdata = new ArrayList<>();
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

        List<List<Double>> box = new ArrayList<>();
        List<List<Integer>> out = new ArrayList<>();
        int count = 0;
        List<List<Integer>> outitem = new ArrayList<>();
        for (List<Integer> item : boxdata) {

            List<Double> boxitem = new ArrayList<>();
            if (item.size() < 5) {
                //utitem.addAll(item);
            } else {
                int Q1 = (int) Math.ceil(item.size() * 0.25) - 1;
                Q1 = item.get(Q1);
                int Q2 = item.size() / 2;
                double mid = item.size() % 2 == 0 ? (item.get(Q2) + item.get(Q2 - 1)) / 2.0 : item.get(Q2);
                int Q3 = (int) Math.floor(item.size() * 0.75) - 1;
                Q3 = item.get(Q3);
                double IQR = (Q3 - Q1) * 1.5;
                int min = item.get(0);
                int max = item.get(item.size() - 1);
                double Q4, Q5;
                if (min < Q1 - IQR) {
                    boxitem.add(Q1 - IQR);
                    Q4 = Q1 - IQR;
                } else {
                    boxitem.add(min / 1.0);
                    Q4 = min;
                }
                for (int n : item) {
                    if (n < Q4) {
                        List<Integer> j = new ArrayList<>();
                        j.add(count);
                        j.add(n);
                        outitem.add(j);
                    }

                }
                boxitem.add(Q1 / 1.0);
                boxitem.add(mid / 1.0);
                boxitem.add(Q3 / 1.0);
                if (max > Q3 + IQR) {
                    boxitem.add(Q3 + IQR);
                    Q5 = Q3 + IQR;
                } else {
                    boxitem.add(max / 1.0);
                    Q5 = max;
                }
                for (int n : item) {
                    if (n > Q5) {
                        List<Integer> j = new ArrayList<>();
                        j.add(count);
                        j.add(n);
                        outitem.add(j);
                    }

                }

            }
            box.add(boxitem);
            count++;
        }
        bdata = new HashMap<>();
        bdata.put("boxplotdata", box);
        bdata.put("outdata", outitem);

    }

    public String getOption() {
        return option.toString();
    }

    public String getOption2() {
        return option2.toString();
    }

}
