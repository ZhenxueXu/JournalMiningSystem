/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inputBeans;

import JDBCUtils.JDBCUtils;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;

/**
 *
 * @author Think
 */
@Named(value = "deleteData")
@SessionScoped
public class DeleteData implements Serializable {

    
    public DeleteData() {
    }
    public String delete(){
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        try{
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "delete from journal_info";
            stat.executeUpdate(sql);
            sql = "delete from adress_lng_lat";
            stat.executeUpdate(sql);
            sql = "delete from paper_address";
            stat.executeUpdate(sql);
            sql = "delete from paper_author";
            stat.executeUpdate(sql);
            sql = "delete from paper_fund";
            stat.executeUpdate(sql);
            sql = "delete from paper_keywords";
            stat.executeUpdate(sql);
            sql = "delete from paper_references";
            stat.executeUpdate(sql);
            sql = "delete from references_author";
            stat.executeUpdate(sql);
            
        }catch(Exception e){
            
        }finally{
            JDBCUtils.close(rs, stat, conn);
        }
        return "deleteInfo.xhtml";
    }
    
}
