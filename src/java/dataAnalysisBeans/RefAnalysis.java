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

            //频次分布图数据处理
            sql = "select j_number,j_title,j_citation_frequency from journal_info order by j_citation_frequency  desc limit 1";
            local = stat.executeQuery(sql);
            int max = 1;
            if (local.next()) {
                max = local.getInt(3);

            }
            int i = 1;
            int group = 1;

            if (max <= 100) {
                while (max % 5 != 0) {
                    max++;
                }
                group = max / 5;
                for (int j = 0; j < max; j = j + 5) {
                    if (j == 0) {
                        Axis.data("0-5");
                    } else {
                        Axis.data((j + 1) + "-" + (j + 5));
                    }
                }

            } else if (max > 100 && max <= 200) {
                while (max % 10 != 0) {
                    max++;
                }
                group = max / 10;
                for (int j = 0; j < max; j = j + 10) {
                    if (j == 0) {
                        Axis.data("0-10");
                    } else {
                        Axis.data((j + 1) + "-" + (j + 10));
                    }
                }
            } else {
                while (max % 20 != 0) {
                    max++;
                }
                group = max / 20;
                for (int j = 0; j < max; j = j + 20) {
                    if (j == 0) {
                        Axis.data("0-20");
                    } else {
                        Axis.data((j + 1) + "-" + (j + 20));
                    }
                }
            }
            times = new int[group];
            sql = "select j_number,j_title,j_citation_frequency from journal_info order by j_citation_frequency ";  //用来处理频次分布，高引，低引
            citationSort = stat.executeQuery(sql);
            while (citationSort.next()) {
                if (max <= 100) {
                    times[(citationSort.getInt(3) - 1) / 5]++;
                } else if (max > 100 && max <= 200) {
                    times[(citationSort.getInt(3) - 1) / 10]++;
                } else {
                    times[(citationSort.getInt(3) - 1) / 20]++;
                }
                //line1.data(citationSort.getInt(2));
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
                double Q4,Q5;
                if (min < Q1 - IQR) {
                    boxitem.add(Q1 - IQR);
                   Q4 = Q1-IQR;
                } else {
                    boxitem.add(min / 1.0);
                    Q4 = min;
                }
                 for (int n : item) {
                    if (n <Q4) {
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
