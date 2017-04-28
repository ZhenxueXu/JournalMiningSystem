
package JDBCUtils;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class JDBCUtils {
    private static Properties prop = null;
    
    private JDBCUtils(){
        
    }
    /**
    * 静态代码块，加载数据库配置文件
    */
   static{
       try{
        prop=new Properties();
        prop.load(new FileReader(JDBCUtils.class.getClassLoader().getResource("jdbc-config.properties").getPath()));
       }catch(Exception e){
           e.printStackTrace();
           throw new RuntimeException();
       }
   }
   
   /**
    * 获取连接
    */
   public static Connection getConn() throws Exception{ 
       Class.forName(prop.getProperty("driver"));
       return DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("user"), prop.getProperty("password"));
   }
   
   /**
    * 关闭连接
    */
   public static void close(ResultSet rs,Statement stat,Connection conn){
       if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                rs=null;
            }
        }
        if(stat!=null){
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                stat=null;
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                conn=null;
            }
        }
   }
   public static List getResultList( ResultSet result){
       List list = new ArrayList();
       Map rowData = new HashMap();
       ResultSetMetaData md = null;
       try{
           md = result.getMetaData();
           int columnCount = md.getColumnCount();
           while(result.next()){
               rowData = new HashMap(columnCount);
               for(int i = 1;i <= columnCount;i++){
                   rowData.put(md.getColumnName(i), result.getObject(i));
               }
            list.add(rowData);
           }          
       }catch(SQLException e){
           e.printStackTrace();
       }
        return list;
   }
   
   
   
}
    
