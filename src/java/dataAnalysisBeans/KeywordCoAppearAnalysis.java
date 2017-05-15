package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.series.Graph;
import com.github.abel533.echarts.series.force.Node;
import com.github.abel533.echarts.style.LineStyle;
import com.google.gson.Gson;
import info.GraphLink;
import info.GraphNormal;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

@Named(value = "keywordCoAppearAnalysis")
@SessionScoped
public class KeywordCoAppearAnalysis implements Serializable {

    private Graph graph;
    private List<GraphLink> links;
    private Map<String, Node> nodeList = new HashMap<>();
    private Map<String, List> data;
    private int minTimes;
    int maxvalue=-1;

    public int getMinTimes() {
        return minTimes;
    }

    public void setMinTimes(int minTimes) {
        this.minTimes = minTimes;
    }

    public KeywordCoAppearAnalysis() {
        minTimes = 1;
    }

    public void setAllData() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        graph = new Graph();
        links = new ArrayList<>();
        data = new HashMap<>();
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();

            sql = "select  a.keyword, b.keyword ,count(*) as 共现次数 "
                    + "from  paper_keywords a, paper_keywords b "
                    + "where a.j_number=b.j_number and a.keyword<b.keyword  "
                    + "group by a.keyword,b.keyword  "
                    + "order by count(*) desc";
            rs = stat.executeQuery(sql);
            int max = -1;
            while (rs.next()) {
                if (max == -1) {
                    max = rs.getInt(3);
                }

                if (nodeList.get(rs.getString(1)) == null) {
                    Node node = new Node();
                    node.name(rs.getString(1)).value(rs.getInt(3));
                    maxvalue = node.value()>=maxvalue? node.value():maxvalue;
                    nodeList.put(node.name(), node);
                } else {
                    Node node = new Node();
                    node.name(rs.getString(1)).value(nodeList.get(rs.getString(1)).value() + rs.getInt(3));
                    maxvalue = node.value()>=maxvalue? node.value():maxvalue;
                    nodeList.put(rs.getString(1), node);
                }
                if (nodeList.get(rs.getString(2)) == null) {
                    Node node = new Node();
                    node.name(rs.getString(2)).value(rs.getInt(3));
                    maxvalue = node.value()>=maxvalue? node.value():maxvalue;
                    nodeList.put(node.name(), node);
                } else {
                    Node node = new Node();
                    node.name(rs.getString(2)).value(nodeList.get(rs.getString(2)).value() + rs.getInt(3));
                    maxvalue = node.value()>=maxvalue? node.value():maxvalue;
                    nodeList.put(rs.getString(2), node);
                }

                GraphLink link = new GraphLink();
                link.setSource(rs.getString(1));
                link.setTarget(rs.getString(2));
                link.setValue(rs.getInt(3));
                LineStyle linestyle = new LineStyle();
                GraphNormal normal = new GraphNormal();
                normal.setWidth((float) (rs.getInt(3) * 5.0 / max));
                linestyle.normal(normal);
                link.lineStyle(linestyle);
                links.add(link);
                graph.links(link);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(rs, stat, conn);
        }

    }

    public String getGraph() {
        if (graph == null) {
            setAllData();
        }
        Gson gson = new Gson();
        System.out.println(gson.toJson(graph));
        return gson.toJson(graph);
    }

    public String getData() {
        if (data == null) {
            setAllData();
        }

        List<GraphLink> returnLink = new ArrayList<>();
        List<Node> returnNode = new ArrayList<>();        
        Set<String> nodeName = new HashSet<>();
        for (GraphLink item : links) {
            if (item.getValue() > minTimes) {
                returnLink.add(item);
                nodeName.add(item.getSource().toString());
                nodeName.add(item.getTarget().toString());
            } else {
                break;
            }
        }
        for (String item : nodeName) {            
            returnNode.add(nodeList.get(item).symbolSize(nodeList.get(item).value()*20.0/maxvalue+2));
        }
        data.put("links", returnLink);
        data.put("nodes", returnNode);
        Gson gson = new Gson();
        return gson.toJson(data);
    }

}
