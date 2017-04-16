
package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;

/**
 * @author Think
 * 第四模块，引文分析
 */
@Named(value = "refStatic")
@SessionScoped
public class RefStatic implements Serializable {
    private ResultSet year_count;
    
    public RefStatic() {
    }
    
    public void setAllData(){
        Connection conn = null;
        Statement stat = null;
        String sql;
        try{
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select r_year,count(*) from paper_references group by r_year order by r_year";
            year_count = stat.executeQuery(sql);
            
        }catch(Exception e){
            
        }finally{
            
        }
        
    }
    
}
