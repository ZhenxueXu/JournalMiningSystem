package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.series.Graph;
import com.github.abel533.echarts.series.force.Link;
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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named(value = "keywordCoAppearAnalysis")
@SessionScoped
public class KeywordCoAppearAnalysis implements Serializable {

    private Graph graph;
    private List<GraphLink> links;
    private Map<String, Node> nodeList = new HashMap<>();
    private Map<String, List> data;
    private int minTimes;
    int maxvalue = -1;

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
                    maxvalue = node.value() >= maxvalue ? node.value() : maxvalue;
                    nodeList.put(node.name(), node);
                } else {
                    Node node = new Node();
                    node.name(rs.getString(1)).value(nodeList.get(rs.getString(1)).value() + rs.getInt(3));
                    maxvalue = node.value() >= maxvalue ? node.value() : maxvalue;
                    nodeList.put(rs.getString(1), node);
                }
                if (nodeList.get(rs.getString(2)) == null) {
                    Node node = new Node();
                    node.name(rs.getString(2)).value(rs.getInt(3));
                    maxvalue = node.value() >= maxvalue ? node.value() : maxvalue;
                    nodeList.put(node.name(), node);
                } else {
                    Node node = new Node();
                    node.name(rs.getString(2)).value(nodeList.get(rs.getString(2)).value() + rs.getInt(3));
                    maxvalue = node.value() >= maxvalue ? node.value() : maxvalue;
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
List<GraphLink> returnLink;
    public String getData() {
        if (data == null) {
            setAllData();
        }
        returnLink = new ArrayList<>();
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
            returnNode.add(nodeList.get(item).symbolSize(nodeList.get(item).value() * 20.0 / maxvalue + 2));
        }
        data.put("links", returnLink);
        data.put("nodes", returnNode);
        Gson gson = new Gson();
        return gson.toJson(data);
    }
    ///筛选

    private Map<String, List<String>> graphData;
    private List<Link> linksList;
    private Map<String, Boolean> visit;
    private Graph graph2;

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
            sql = "select  a.keyword, b.keyword ,count(*) as 共现次数 "
                    + "from  paper_keywords a, paper_keywords b "
                    + "where a.j_number=b.j_number and a.keyword<b.keyword  "
                    + "group by a.keyword,b.keyword  "
                    + "having 共现次数 > " + minTimes + " "
                    + "order by count(*) desc";
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

    private String serchName = "";
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
            FacesMessage message = new FacesMessage("对不起！没有您查询的关键词，请输入正确的关键词和共现次数");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            tempVisit.put(nodeName, true);
            System.out.println(tempVisit.get(nodeName));
            for (String item : graphData.get(nodeName)) {
                if (tempVisit.get(item) == false) {
                    DFSTrave(item, depth + 1);
                    Node node = new Node();
                    node = nodeList.get(item);
                    graph.data(node);
                    childGraphNodes.add(node);
                }
            }
        }
    }

    public String getGraph2() {
        childGraphNodes = new ArrayList<>();
        tempVisit = new HashMap<>();
        tempVisit.putAll(visit);
        Node node = new Node();
        node = nodeList.get(serchName);
        childGraphNodes.add(node);
        DFSTrave(serchName, 1);       
        data.put("nodes", childGraphNodes);
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public String returnData() {
        if (serchName == null || serchName.equals("")) {
            return getData();
        } else {               
            setGraphData();
            return getGraph2();

        }
    }

}
