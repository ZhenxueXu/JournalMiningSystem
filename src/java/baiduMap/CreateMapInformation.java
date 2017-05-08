/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baiduMap;

import JDBCUtils.JDBCUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Think
 */
public class CreateMapInformation {

    public static void createInformation() {
        Connection conn = null;

        Statement stat = null;
        Statement stat2 = null;
        //List<String> sqlArray = null;
        ResultSet rs = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            conn.setAutoCommit(false);
            stat = conn.createStatement();
            sql = "select distinct j_address from paper_address";
            rs = stat.executeQuery(sql);
            Map<String, Double> map = new HashMap<>();
            //sqlArray = new ArrayList<>();
           stat2 = conn.createStatement();
            while (rs.next()) {                 
                map = baiduMap.LngAndLatUtil.getLngAndLat(rs.getString(1));
               //System.out.println(map.get("lng") + "," + map.get("lat"));
                if (map.get("lng") != null) {
                    stat2.addBatch("insert into adress_lng_lat values('" + rs.getString(1) + "' , " + map.get("lng") + " , " + map.get("lat") + ")");
                }

            }
            stat2.executeBatch();
            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                stat2.close();
            } catch (SQLException ex) {
                Logger.getLogger(CreateMapInformation.class.getName()).log(Level.SEVERE, null, ex);
            }
            JDBCUtils.close(rs, stat, conn);
        }

    }

}
