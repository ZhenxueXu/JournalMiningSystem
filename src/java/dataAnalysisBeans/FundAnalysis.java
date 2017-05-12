package dataAnalysisBeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named(value = "fundAnalysis")
@SessionScoped
public class FundAnalysis implements Serializable {


    //机构发文变量
    private GsonOption option1;
    private List<Map> publish;
    //机构被引变量
    private GsonOption option2;
    private List<Map> quote;
    //各级基金量
    private GsonOption option3;
    private GsonOption option4;
    private GsonOption option5;

    public FundAnalysis() {
        setAllData();
    }

    public void setAllData() {

        Bar bar1 = new Bar();
        CategoryAxis xAxis1 = new CategoryAxis();
        Line line = new Line();
        Bar bar2 = new Bar();
        CategoryAxis xAxis2 = new CategoryAxis();
        List<Integer> list;
        List<Integer> list1;
        List<String> xlist;
        Map<Integer, List<Integer>> data;
        Map<Integer, List<Integer>> data1;
        Map<Integer, List<String>> xdata;

        Connection conn = null;
        Statement stat = null;

        ResultSet rs = null;
        ResultSet rs1 = null;
        Line line11 = new Line();
        Line line12 = new Line();
        Line line21 = new Line();
        Line line22 = new Line();
        Line line31 = new Line();
        Line line32 = new Line();
        CategoryAxis xAxis3 = new CategoryAxis();
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select distinct j_fund as 基金,count(*) as 发文量 "
                    + "from paper_fund "
                    + "group by j_fund "
                    + "order by count(*) desc ";
            rs = stat.executeQuery(sql);            //查询各基金的发文总量

            //机构发文柱状图
            list = new ArrayList();
            xlist = new ArrayList();
            data = new HashMap();
            xdata = new HashMap();
            int count = 1;
            int pagecount = 1;

            while (rs.next()) {
                list.add(rs.getInt(2));
                xlist.add(rs.getString(1));
                count++;
                if (count % 25 == 0) {
                    data.put(pagecount, list);
                    xdata.put(pagecount, xlist);
                    list = new ArrayList();
                    xlist = new ArrayList();
                    pagecount++;
                }
            }

            bar1.data(data);
            xAxis1.data(xdata);

            //机构发文表格
            rs = stat.executeQuery(sql);
            publish = JDBCUtils.getResultList(rs);

            /*-- start 基金被引统计 --*/
            sql = "select j_fund as 基金 ,sum(journal_info.j_citation_frequency) as 被引次数, avg(journal_info.j_citation_frequency) as 篇均被引次数 "
                    + "from paper_fund,journal_info "
                    + "where  paper_fund.j_number = journal_info.j_number "
                    + "group by j_fund "
                    + "order by 被引次数 desc ";
            rs = stat.executeQuery(sql);


            //机构被引柱状图
            list = new ArrayList();
            list1 = new ArrayList();
            xlist = new ArrayList();
            data = new HashMap();
            data1 = new HashMap();
            xdata = new HashMap();
            count = 1;
            pagecount = 1;

            while (rs.next()) {
                list.add(rs.getInt(2));
                list1.add(rs.getInt(3));
                xlist.add(rs.getString(1));
                count++;
                if (count % 25 == 0) {
                    data1.put(pagecount, list1);
                    data.put(pagecount, list);
                    xdata.put(pagecount, xlist);
                    list = new ArrayList();
                    list1 = new ArrayList();
                    xlist = new ArrayList();
                    pagecount++;
                }
            }

            bar2.data(data);
            line.data(data1);
            xAxis2.data(xdata);

            //机构被引表格
            rs = stat.executeQuery(sql);
            quote = JDBCUtils.getResultList(rs);


            // -- start 国家级基金发文被引统计  格式（年，发文量，被引次数，篇均被引次数）--//
            sql = "select distinct j_year as 年,count(*) as 发文量 ,sum(journal_info.j_citation_frequency) as 被引次数, avg(journal_info.j_citation_frequency) as 篇均被引次数 "
                    + "from paper_fund,journal_info "
                    + "where  paper_fund.j_number = journal_info.j_number and j_fund regexp('国家') and j_fund not regexp('国家重点实验室')  "
                    + "group by j_year "
                    + "order by j_year ";
            rs = stat.executeQuery(sql);

            while (rs.next()) {
                line11.data(rs.getInt(2));
                line21.data(rs.getInt(3));
                line31.data(rs.getInt(4));
                xAxis3.data(rs.getInt(1));

            }

            // -- start 省部级基金发文被引统计 格式（年，发文量，被引次数，篇均被引次数） --//
            sql = "select distinct j_year ,count(*) as 发文量 ,sum(journal_info.j_citation_frequency) as 被引次数, avg(journal_info.j_citation_frequency) as 篇均被引次数 "
                    + "from paper_fund,journal_info "
                    + "where  paper_fund.j_number = journal_info.j_number and j_fund regexp('省|北京|上海|重庆') "
                    + "group by j_year "
                    + "order by j_year ";
            rs1 = stat.executeQuery(sql);
            while (rs1.next()) {
                line12.data(rs1.getInt(2));
                line22.data(rs1.getInt(3));
                line32.data(rs1.getInt(4));
            }


            // -- start 省部级基金发文被引统计 格式（年，发文量，被引次数，篇均被引次数） --//
            sql = "select distinct j_year ,count(*) as 发文量 ,sum(journal_info.j_citation_frequency) as 被引次数, avg(journal_info.j_citation_frequency) as 篇均被引次数"
                    + "from paper_fund,journal_info"
                    + "where  paper_fund.j_number = journal_info.j_number and j_fund regexp('省|北京|上海|重庆')"
                    + "group by j_year"
                    + "order by j_year";
            rs = stat.executeQuery(sql);


        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
        option1 = new GsonOption();
        option1.xAxis(xAxis1);
        option1.series(bar1);
        option2 = new GsonOption();
        option2.xAxis(xAxis2);
        option2.series(bar2);
        option2.series(line);
        option3 = new GsonOption();
        option3.series(line11);
        option3.series(line12);
        option3.xAxis(xAxis3);
        option4 = new GsonOption();
        option4.series(line21);
        option4.series(line22);
        option4.xAxis(xAxis3);
        option5 = new GsonOption();
        option5.series(line31);
        option5.series(line32);
        option5.xAxis(xAxis3);

    }

    public GsonOption getOption1() {
        return option1;
    }

    public List<Map> getPublish() {
        return publish;
    }

    public List<Map> getQuote() {
        return quote;
    }

    public GsonOption getOption2() {
        return option2;
    }

    public GsonOption getOption3() {
        return option3;
    }

    public GsonOption getOption4() {
        return option4;
    }

    public GsonOption getOption5() {
        return option5;
    }


}
