
package dataAnalysisBeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import JDBCUtils.JDBCUtils;

/**
 *机构分析
 * 1、机构发文排名
 * 2、机构被引排名
 * @author Think
 */
@Named(value = "affiliationAnalysis")
@SessionScoped
public class AffiliationAnalysis implements Serializable {
    private ResultSet aff_publish;                      //机构发文量，格式（单位，发文总量）
    private ResultSet aff_ref;                          //机构被引次数， 格式（单位，总被引，篇均被引）

    public AffiliationAnalysis() {
    }
    public void setAllData(){
        Connection conn = null;
        Statement stat = null;
        ResultSet local = null;
        String sql = null;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            /*--start 单位发文统计 --*/
            sql = "select j_address , count(*) as 发文量"
                    + "from journal_info,paper_address"
                    + "where journal_info.j_number = paper_address.j_number"
                    + "group by j_address"
                    + "order by 发文量 DESC";
            aff_publish = stat.executeQuery(sql);
            
            /*--start 单位被引统计 --*/
            sql = "select j_address , sum(journal_info.j_citation_frequency) as 被引次数,avg(journal_info.j_citation_frequency) as 篇均被引次数"
                    + "from journal_info,paper_address"
                    + "where journal_info.j_number = paper_address.j_number"
                    + "group by j_address"
                    + "order by 被引次数 DESC,篇均被引次数 DESC";
            aff_ref = stat.executeQuery(sql);
            
            
        }catch(Exception e){
            
        }finally{
            JDBCUtils.close(local, stat, conn);
    }
    }
}
