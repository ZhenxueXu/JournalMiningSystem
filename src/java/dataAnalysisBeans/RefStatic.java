
package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Think
 * 第四模块，引文分析
 */
@Named(value = "refStatic")
@SessionScoped
public class RefStatic implements Serializable {
    private ResultSet year_count;       //逐年引文数量，只统计有年份记录的引文，因为有一部分参考文献没有年份记录，比如参考书籍等
    private int total;                  //有年份记录的参考文献总数，用来计算引文率
    private List<Integer> quantity;     //z
    private List<Float>  rate;
    private List<Float> sumRate;
    private List<String> year;
    
    public RefStatic() {
    }
    
    public void setAllData(){
        Connection conn = null;
        Statement stat = null;
        ResultSet localSet = null;
        String sql;
        try{
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select r_year,count(*) from paper_references where r_year REGEXP('[0-9]{4}') group by r_year order by r_year DESC";
            year_count = stat.executeQuery(sql);
            sql = "select count(*) as total from paper_references where r_year REGEXP('[0-9]{4}')";
            localSet = stat.executeQuery(sql);
            total = localSet.getInt(1);
            year = new ArrayList<String>();
            quantity = new ArrayList<Integer>();
            rate = new ArrayList<Float>();
            sumRate = new ArrayList<Float>();
            int i = 1;
            int sum = 0;
            while(year_count.next()){
                //显示20年的数据
                
                if(i<20){
                    year.add(year_count.getString(1));
                    quantity.add(year_count.getInt(2));
                    rate.add(year_count.getInt(2) / (float) total);
                    if (sumRate.size() <= 0) {
                        sumRate.add(rate.get(0));
                    } else {
                        sumRate.add(sumRate.get(sumRate.size() - 1) + rate.get(rate.size() - 1));
                    }        
                }else{
                    sum += year_count.getInt(2);                    
                }
                i++;               
            }
            year.add(year_count.getString(1)+"以前");
            rate.add(sum/(float)total);
            sumRate.add((float)100);
        }catch(Exception e){
            
        }finally{
            
        }
        
    }
    
}