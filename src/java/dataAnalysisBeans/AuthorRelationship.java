package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.code.Layout;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Graph;
import com.github.abel533.echarts.series.force.Link;
import com.github.abel533.echarts.series.force.Node;
import com.github.abel533.echarts.style.LineStyle;
import com.google.gson.Gson;
import info.GraphLink;
import info.GraphNormal;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者合作关系分析
 *
 * @author Think
 */
@Named(value = "authorRelationship")
@SessionScoped
public class AuthorRelationship implements Serializable {
    private List<GraphLink> links;
    private List<Node> nodes;
    private Map<String,List> graphData;
    private int minTime = 20;

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }
    
    

    public AuthorRelationship() {
    }

    public String getData() {
        String data = "";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        Map<String, Integer> author_times = new HashMap<>();
        graphData = new HashMap<>();
        links = new ArrayList<>();
        nodes = new ArrayList<>();
        GsonOption option = new GsonOption();
        Graph graph = new Graph();
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select distinct a.j_author,sum(b.j_citation_frequency) as times "
                    + "from paper_author as a,journal_info as b "
                    + "where a.j_number = b.j_number "                 
                    + "group by a.j_author "
                    + "having times>"+minTime +" "
                    + "order by times desc";
            rs = stat.executeQuery(sql);
            int max = -1;
            while (rs.next()) {
                if (max == -1) {
                    max = rs.getInt(2);
                }
                author_times.put(rs.getString(1), rs.getInt(2));
                Node node = new Node();
                node.setName(rs.getString(1));
                node.setValue(rs.getInt(2));
                node.setSymbolSize(2 + rs.getInt(2) * 30.0 / max);
                nodes.add(node);
                graph.data(node);
            }

            sql = "select distinct a.j_author,b.j_author,count(*) as 合作频次 "
                    + "from paper_author as a,paper_author as b  "
                    + "where a.j_number = b.j_number and a.j_author <> b.j_author and a.j_author < b.j_author  "
                    + "group by a.j_author,b.j_author "
                    + "order by a.j_number";
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                GraphLink link = new GraphLink();
                link.setValue(rs.getInt(3));
                link.setSource(rs.getString(1));
                link.setTarget(rs.getString(2));
                LineStyle linestyle  = new LineStyle();
//                GraphNormal normal = new GraphNormal();
//                normal.setWidth((float) (2+rs.getInt(3)*4.0/max));
//                linestyle.normal(normal);
//                link.lineStyle(linestyle);
                links.add(link);
                graph.links(link);
            }

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
        Gson gson = new Gson();
        //option.series(graph);
       
        graphData.put("nodes", nodes);
        graphData.put("links", links);
        return gson.toJson(graphData);
    }

}
