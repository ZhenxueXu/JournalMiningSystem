package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import java.sql.*;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/*
*第一模块，上传数据的概述
*
 */

@Named(value = "journalAbstract")
@RequestScoped
public class JournalAbstract {

    private String JName;      //期刊来源
    private String years;      //数据年限
    private int paperCount;  //上传的期刊文献总数据量
    private int refCount;     //总的引文数据量
    private int refTimes;      //总的被引次数
    private double aveRefTimes;   //篇均被引次数
    private int h_index;

    public JournalAbstract() {
        setAllData();
    }

    public String getJName() {
        return JName;
    }

    public String getYears() {
        return years;
    }

    public int getPaperCount() {
        return paperCount;
    }

    public int getRefCount() {
        return refCount;
    }

    public int getRefTimes() {
        return refTimes;
    }

    public double getAveRefTimes() {
        return aveRefTimes;
    }

    public int getH_index() {
        return h_index;
    }

    public void setAllData() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            String sql = "select distinct j_orgin from journal_info";  //文章来源
            rs = stat.executeQuery(sql);
            String temp = "";
            while (rs.next()) {
                temp = temp + rs.getString(1) + "、";
            }
            JName = temp.substring(0, temp.length() - 1);
            sql = "select min(j_year),max(j_year)from journal_info";  //数据年限
            rs = stat.executeQuery(sql);
            if (rs.next()) {
                years = rs.getString(1) + " - " + rs.getString(2);
            }
            sql = "select  count(j_number) from journal_info";        //论文总量
            rs = stat.executeQuery(sql);
            if (rs.next()) {
                paperCount = rs.getInt(1);
            }
            sql = "select  count(*) from paper_references";
            rs = stat.executeQuery(sql);
            if (rs.next()) {
                refCount = rs.getInt(1);
            }
            sql = "select  sum(j_citation_frequency) from journal_info";
            rs = stat.executeQuery(sql);
            if (rs.next()) {
                refTimes = rs.getInt(1);
            }
            aveRefTimes = ((double)refTimes) / paperCount;
            //计算h指数
            sql = "select j_number, j_citation_frequency from journal_info order by j_citation_frequency DESC";
            rs = stat.executeQuery(sql);
            h_index = 0;
            while (rs.next()) {
                h_index++;
                if (h_index >= rs.getInt(2)) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            JDBCUtils.close(rs, stat, conn);

        }
    }

    @Override
    public String toString() {
        return "JournalAbstract{" + "JName=" + JName + ", years=" + years + ", paperCount=" + paperCount + ", refCount=" + refCount + ", refTimes=" + refTimes + ", aveRefTimes=" + aveRefTimes + ", h_index=" + h_index + '}';
    }
    
    

}
