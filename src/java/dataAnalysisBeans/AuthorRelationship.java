package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Graph;
import com.github.abel533.echarts.series.force.Link;
import com.github.abel533.echarts.series.force.Node;
import com.github.abel533.echarts.style.LineStyle;
import com.google.gson.Gson;
import info.GraphLink;
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
    private Map<String, List> totalGraphData;
    private int minTime = 20;
    private Map<String, Node> nodesMap = new HashMap<>();
    private Map<String, List<String>> graphData;
    private Map<String, Boolean> visit;
    private String serchname = "";
    List<Node> childGraphNodes;
    List<Link> childGraphLinks;

    public int getMinTime() {

        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public AuthorRelationship() {
    }

    public void setData() {
        graphData = new HashMap<>();
        visit = new HashMap<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        Map<String, Integer> author_times = new HashMap<>();
        totalGraphData = new HashMap<>();
        links = new ArrayList<>();
        nodes = new ArrayList<>();
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select distinct a.j_author,sum(b.j_citation_frequency) as times "
                    + "from paper_author as a,journal_info as b "
                    + "where a.j_number = b.j_number "
                    + "group by a.j_author "
                    + "having times>" + minTime + " "
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
                nodesMap.put(node.name(), node);
                nodes.add(node);
            }

            sql = "select distinct a.j_author,b.j_author,count(*) as 合作频次 "
                    + "from paper_author as a,paper_author as b  "
                    + "where a.j_number = b.j_number and a.j_author <> b.j_author and a.j_author < b.j_author  "
                    + "group by a.j_author,b.j_author "
                    + "order by a.j_number";
            rs = stat.executeQuery(sql);
            List<String> child = new ArrayList<>();
            while (rs.next()) {
                GraphLink link = new GraphLink();
                link.setValue(rs.getInt(3));
                link.setSource(rs.getString(1));
                link.setTarget(rs.getString(2));
//                LineStyle linestyle = new LineStyle();
//                GraphNormal normal = new GraphNormal();
//                normal.setWidth((float) (2+rs.getInt(3)*4.0/max));
//                linestyle.normal(normal);
//                link.lineStyle(linestyle);
                links.add(link);
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
//                graph.links(link);
            }

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }

        //option.series(graph);
    }

    public String getSerchname() {
        return serchname;
    }

    public void setSerchname(String serchname) {
        this.serchname = serchname;
    }

    Map<String, Boolean> tempVisit;

    public void DFSTrave(String nodeName, int depth) {
        tempVisit.put(nodeName, true);
//        System.out.println(tempVisit.get(nodeName));
        for (String item : graphData.get(nodeName)) {
            if (tempVisit.get(item) == false) {
                DFSTrave(item, depth + 1);
                if (nodesMap.get(item) != null) {
                    Node node = new Node();
                    node = nodesMap.get(item);
                    childGraphNodes.add(node);
                }
            }

        }

    }

    public void getGraph() {
        childGraphNodes = new ArrayList<>();
        tempVisit = new HashMap<>();
        tempVisit.putAll(visit);
        Node node = new Node();
        if (!serchname.equals("")) {
            if (nodesMap.get(serchname) != null) {
                node = nodesMap.get(serchname);
                childGraphNodes.add(node);
                //graph.data(node);
                DFSTrave(serchname, 1);
            } else {
                FacesMessage message = new FacesMessage("对不起！没有您查询的作者，请输入正确的名字和被引频次");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }

    }

    public String getGraphData() {
        setData();
        getGraph();
        if (serchname.equals("")) {
            totalGraphData.put("nodes", nodes);
        }else{
            totalGraphData.put("nodes", childGraphNodes);
        }
        totalGraphData.put("links", links);     

        Gson gson = new Gson();
        return gson.toJson(totalGraphData);
    }

}
