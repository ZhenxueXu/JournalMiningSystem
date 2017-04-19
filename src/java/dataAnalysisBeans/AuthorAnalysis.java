/*
 * 第五模块，科研作者分析
	作者影响力(h指数)
	核心作者（普赖斯公式）
	作者发文排名
	作者被引排名

 */
package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Think
 */
@Named(value = "authorAnalysis")
@SessionScoped
public class AuthorAnalysis implements Serializable {
    private List<Map<String,Integer>> author_hIndex;

    public AuthorAnalysis() {
    }
    
    public void setAllData(){
        String sql;
        Connection conn = null;
        Statement stat = null;
        ResultSet local = null;
        try{
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "call call proce_core_author";         //proce_core_author   为数据库里定义的查询候选核心作者的存储过程
            local = stat.executeQuery(sql);
            
        }catch(Exception e){
            
        }finally{
            JDBCUtils.close(local, stat, conn);
        }
        
    }         
}