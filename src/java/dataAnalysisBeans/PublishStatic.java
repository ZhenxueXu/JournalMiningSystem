
package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;

/**
 *
 * @author XZX
 * 发文量统计分析
 */
@Named(value = "publishStatic")
@SessionScoped
public class PublishStatic implements Serializable {
    private ResultSet year_count = null;     //逐年载文数量变化
    private ResultSet year_pages = null;     //逐年页数
    private ResultSet year_avgPages = null;  //逐年篇均页数

    public PublishStatic() {
    }
    
    public void setAllData(){
        Connection conn = null;
        Statement stat = null ;
        String sql;
        try{
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select j_year , count(j_number) from journal_info group by j_year";
            year_count = stat.executeQuery(sql);
            sql = "select j_year,sum(j_page2) from journal_info group by j_year";
            year_pages = stat.executeQuery(sql);
            sql = "select j_year,avg(j_page2) from journal_info group by j_year";
            year_avgPages = stat.executeQuery(sql);         
        }catch(Exception e){
            
        }finally{
            JDBCUtils.close(null, stat, conn);
            
        }
    }
    
    
           
}
