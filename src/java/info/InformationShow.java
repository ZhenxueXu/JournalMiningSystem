/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info;

import JDBCUtils.JDBCUtils;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Think
 */
@Named(value = "informationShow")
@SessionScoped
public class InformationShow implements Serializable {

    /**
     * Creates a new instance of InformationShow
     */
    public InformationShow() {
    }
    
    private  List<Map> tableData = new ArrayList<>();
    public List<Map> getTable() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        Map<String, Map<String, Object>> table = new HashMap<>();
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select a.j_number,a.j_title,a.j_citation_frequency,a.j_year,j_page2,a.j_others_citation,a.j_abstract "
                    + "from journal_info a "                    
                    + "order by a.j_number";
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", rs.getString(1));
                item.put("title", rs.getString(2));
                item.put("times", rs.getString(3));
                item.put("year", rs.getString(4));
                item.put("pages", rs.getString(5));
                item.put("others", rs.getString(6));
                item.put("abstract", rs.getString(7));
                table.put(rs.getString(1), item);
            }
            sql = "select a.j_number,keyword "
                    + "from journal_info a,paper_keywords b "
                    + "where a.j_number = b.j_number "
                    + "order by a.j_number";
            rs = stat.executeQuery(sql);
            String id = "";
            String keyword = "";
            while (rs.next()) {
                if (!id.equals(rs.getString(1))) {
                    if (!id.equals("")) {
                        table.get(id).put("keywords", keyword);
                    }
                    id = rs.getString(1);
                    keyword = "";
                }
                keyword = rs.getString(2) + " " + keyword;
            }

            sql = "select a.j_number,j_author "
                    + "from journal_info a,paper_author b "
                    + "where a.j_number = b.j_number "
                    + "order by a.j_number";
            rs = stat.executeQuery(sql);
            id = "";
            String author = "";
            while (rs.next()) {
                if (!id.equals(rs.getString(1))) {
                    if (!id.equals("")) {
                        table.get(id).put("authors", author);
                    }
                    id = rs.getString(1);
                    author = "";
                }
                author = rs.getString(2) + " " + author;
            }
            sql = "select a.j_number,j_fund "
                    + "from journal_info a,paper_fund b "
                    + "where  a.j_number = b.j_number "
                    + "order by a.j_number";
            rs = stat.executeQuery(sql);
            id = "";
            String fund = "";
            while (rs.next()) {
                if (!id.equals(rs.getString(1))) {
                    if (!id.equals("")) {
                        table.get(id).put("funds", fund);
                    }
                    id = rs.getString(1);
                    fund = "";
                }
                fund = rs.getString(2) + " " + fund;
            }

            sql = "select a.j_number,j_address "
                    + "from journal_info a,paper_address b "
                    + "where a.j_number = b.j_number "
                    + "order by a.j_number";
            rs = stat.executeQuery(sql);
            id = "";
            String address = "";
            while (rs.next()) {
                if (!id.equals(rs.getString(1))) {
                    if (!id.equals("")) {
                        table.get(id).put("address", address);
                    }
                    id = rs.getString(1);
                    address = "";
                }
                address = rs.getString(2) + " " + address;
            }
           
            for(Map.Entry<String, Map<String, Object>> row:table.entrySet()){
                tableData.add(row.getValue());
            }

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
        return tableData;
    }
    
}
