package text;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.VisualMap;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.code.VisualMapType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;






@Named(value = "test2")
@SessionScoped
public class Test2 implements Serializable {

   
    public Test2() {
    }
    public String getData(){
        
        Connection conn = null;
        Statement stat = null;
        String sql;
        ResultSet local = null;
        GsonOption option = new GsonOption();
        CategoryAxis xAxis = new CategoryAxis();
        Line line = new Line();
 

        try{
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();            
            sql = "select j_year,sum(j_citation_frequency) from journal_info group by j_year order by j_year";
            local = stat.executeQuery(sql);
            while(local.next()){
               xAxis.data(local.getString(1));
               line.data(local.getInt(2));

            }
                       
        }catch(Exception e){
            
        }finally{
            JDBCUtils.close(local, stat, conn);
        }   
        line.name("期刊总被引次数变化");
        option.xAxis(xAxis);
        option.series(line);

        System.out.println(option.toString());
        return option.toString();
    }
}
    
