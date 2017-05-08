package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.data.MapData;
import com.google.gson.Gson;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Named(value = "affiliationHeatMap")
@SessionScoped
public class AffiliationHeatMap implements Serializable {

    public AffiliationHeatMap() {
        setAllData();
    }
    private List<MapData> data;

    public void setAllData() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select distinct paper_address.j_address,sum(j_citation_frequency),j_lng,j_lat "
                    + "from paper_address,journal_info,adress_lng_lat "
                    + "where paper_address.j_number=journal_info.j_number and paper_address.j_address = adress_lng_lat.j_address "
                    + "group by j_address  "
                    + "order by sum(j_citation_frequency) desc";
            rs = stat.executeQuery(sql);
            data = new ArrayList<>();            
            while(rs.next()){
                List<Float> item = new ArrayList<>();
                item.add((float)rs.getDouble(3));
                item.add((float)rs.getDouble(4));
                item.add((float)rs.getInt(2));
                MapData mapdata = new MapData(rs.getString(1),item);
                data.add(mapdata);
            }

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }

    }

    public String test() {
        baiduMap.CreateMapInformation.createInformation();
        return null;
    }
    public String getMapData(){
        Gson gson = new Gson();
        //System.out.println(gson.toJson(data));
        return gson.toJson(data);
    }

}
