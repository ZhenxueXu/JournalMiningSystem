package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.series.Graph;
import com.github.abel533.echarts.series.force.Link;
import com.github.abel533.echarts.series.force.Node;
import com.google.gson.Gson;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named(value = "graphTrave")
@SessionScoped
public class GraphTrave implements Serializable {

    private Map<String, List<String>> graphData;
    private List<Link> linksList;
    private Map<String, Boolean> visit;

    ;
    public GraphTrave() {
        setGraphData();
    }

    private Graph graph ;

    public void setGraphData() {
        graphData = new HashMap<>();
        graph = new Graph();
        visit = new HashMap<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select distinct a.j_author,b.j_author,count(*) as 合作频次 "
                    + "from paper_author as a,paper_author as b  "
                    + "where a.j_number = b.j_number and a.j_author <> b.j_author and a.j_author < b.j_author  "
                    + "group by a.j_author,b.j_author "
                    + "order by a.j_author";
            rs = stat.executeQuery(sql);
            List<String> child = new ArrayList<>();
            linksList = new ArrayList<>();
            while (rs.next()) {
                if (graphData.get(rs.getString(1)) == null) {
                    child = new ArrayList<>();
                    child.add(rs.getString(2));
                    graphData.put(rs.getString(1), child);
                } else {
                    graphData.get(rs.getString(1)).add(rs.getString(2));
                }
                if (graphData.get(rs.getString(2)) == null) {
                    child = new ArrayList<>();
                    child.add(rs.getString(1));
                    graphData.put(rs.getString(2), child);
                } else {
                    graphData.get(rs.getString(2)).add(rs.getString(1));
                }
                visit.put(rs.getString(1), false);
                visit.put(rs.getString(2), false);
                Link link = new Link();
                link.source(rs.getString(1)).target(rs.getString(2));
                graph.links(link);
                linksList.add(link);

            }

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
    }
    private String serchName = "yy";
    List<Node> childGraphNodes;
    List<Link> childGraphLinks;

    public String getSerchName() {
        return serchName;
    }

    public void setSerchName(String serchName) {
        this.serchName = serchName;
    }

    Map<String, Boolean> tempVisit;
    public void DFSTrave(String nodeName, int depth) {                
        if (graphData.get(nodeName) == null) {
            FacesMessage message = new FacesMessage("对不起！没有您查询的作者，请输入正确的名字");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            tempVisit.put(nodeName, true);
            System.out.println(tempVisit.get(nodeName));
            for (String item : graphData.get(nodeName)) {
                if (tempVisit.get(item) == false) {
                    DFSTrave(item, depth + 1);
                    Node node = new Node();
                    node.name(item);
                    node.value(depth);
                    graph.data(node);
                    childGraphNodes.add(node);
                }
            }
        }
    }

    public String getGraph() {
        childGraphNodes = new ArrayList<>();        
        tempVisit= new HashMap<>();
        tempVisit.putAll(visit);
        Node node = new Node();
        node.name(serchName);
        node.value(0);
        node.symbolSize(30);
        graph.data(node);
        DFSTrave(serchName, 1);
        Gson gson = new Gson();
        
        return gson.toJson(graph);
    }

}
