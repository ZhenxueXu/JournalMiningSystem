
package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;

/**
 * @author Think
 * 第三模块，被引统计
 */
@Named(value = "refAnalysis")
@SessionScoped
public class RefAnalysis implements Serializable {
    private ResultSet year_citation;   //期刊每年总被引次数
    private ResultSet citationSort;    //论文被引次数排序

    public RefAnalysis() {
    }
    
    public void setData(){
        Connection conn = null;
        Statement stat = null;
        String sql;
        try{
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select j_year,sum(j_citation_frequency) from journal_info group by j_year order by j_year";
            year_citation = stat.executeQuery(sql);
            sql = "j_number,j_title,j_citation_frequency from journal_info order by j_citation_frequency DESC";  //用来处理频次分布，高引，低引
            citationSort = stat.executeQuery(sql);
            
            
        }catch(Exception e){
            
        }finally{
            JDBCUtils.close(null, stat, conn);
        }       
    }

    public ResultSet getYear_citation() {
        return year_citation;
    }

    public ResultSet getCitationSort() {
        return citationSort;
    }
    
    
}
