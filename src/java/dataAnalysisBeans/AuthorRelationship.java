package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.code.Layout;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Graph;
import com.github.abel533.echarts.series.force.Link;
import com.github.abel533.echarts.series.force.Node;
import com.google.gson.Gson;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者合作关系分析
 *
 * @author Think
 */
@Named(value = "authorRelationship")
@SessionScoped
public class AuthorRelationship implements Serializable {

    public AuthorRelationship() {
    }

    public String getData() {
        String data = "";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        Map<String, Integer> author_times = new HashMap<>();
        GsonOption option = new GsonOption();
        Graph graph = new Graph();
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select distinct a.j_author,sum(b.j_citation_frequency) as times "
                    + "from paper_author as a,journal_info as b "
                    + "where a.j_number = b.j_number "
                    + "group by a.j_author "
                    + "order by a.j_author ";
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                author_times.put(rs.getString(1), rs.getInt(2));
                Node node = new Node();
                node.setName(rs.getString(1));
                node.setValue(rs.getInt(2));
                node.setSymbolSize(rs.getInt(2) / 10.0);
                graph.data(node);
            }

            sql = "select distinct a.j_author,b.j_author "
                    + "from paper_author as a,paper_author as b "
                    + "where a.j_number = b.j_number and a.j_author <> b.j_author and a.j_author < b.j_author "
                    + "order by a.j_number";
            rs = stat.executeQuery(sql);
            while (rs.next()) {
                Link link = new Link();
                link.setSource(rs.getString(1));
                link.setTarget(rs.getString(2));
                graph.links(link);
            }

        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
        Gson gson = new Gson();
        //option.series(graph);
        //
        data = gson.toJson(graph);
        System.out.println(data);
        return data;
    }

}
