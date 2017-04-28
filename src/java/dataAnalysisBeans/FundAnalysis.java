
package dataAnalysisBeans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import JDBCUtils.JDBCUtils;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Named(value = "fundAnalysis")
@SessionScoped
public class FundAnalysis implements Serializable {
    private List<Map.Entry<String,Integer>> publish;
  
    public FundAnalysis() {
    }
    
    public void setAllData(){
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        try{
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select distinct j_fund as 基金 ,count(*) as 发文量"
                    + "from paper_fund "
                    + "group by j_fund "
                    + "order by count(*) desc ";
            rs = stat.executeQuery(sql);            //查询各基金的发文总量
            publish = new ArrayList<>();
            int i = 1;
            while(rs.next()){
                if(i<=20){
                    /*-- start 发文总量排名前二十的基金，每年发文量统计，用于折线图 --*/
                    sql = "select j_year as 年,count(*) as 发文量 from paper_fund,journal_info"
                            + "where  paper_fund.j_number = journal_info.j_number and j_fund = '"+rs.getString(1)+"'"
                            + "group by j_fund,j_year"
                            + "order by j_year";
                    try (Statement stat2 = conn.createStatement(); ResultSet rs2 = stat2.executeQuery(sql)) {
                        while(rs.next()){
                            /**
                             * 所需数据处理区域
                             * rs.getString(1) ---基金名
                             * rs2.getString(1) ----年
                             * rs2.getInt(2)  -----发文量
                             */
                        }
                    }
                }
                /*-- end 发文量前二十，分年统计--*/
                
                // 以下用于记录各基金的发文总量量
                Map.Entry<String,Integer> rowData ;
                rowData = new AbstractMap.SimpleEntry<>(rs.getString(1), rs.getInt(2));
                publish.add(rowData);               
            }
            
            /*-- start 基金被引统计 --*/
            sql = "select j_fund as 基金 ,sum(journal_info.j_citation_frequency) as 被引次数, avg(journal_info.j_citation_frequency) as 篇均被引次数"
                    + "from paper_fund,journal_info"
                    + "where  paper_fund.j_number = journal_info.j_number        "
                    + "group by j_fund"
                    + "order by 被引次数 desc";
            rs = stat.executeQuery(sql);
            
            // -- start 国家级基金发文被引统计  格式（年，发文量，被引次数，篇均被引次数）--//
            sql = "select distinct j_year as 年,count(*) as 发文量 ,sum(journal_info.j_citation_frequency) as 被引次数, avg(journal_info.j_citation_frequency) as 篇均被引次数"
                    + "from paper_fund,journal_info"
                    + "where  paper_fund.j_number = journal_info.j_number and j_fund regexp('国家') and j_fund not regexp('国家重点实验室')  "
                    + "group by j_year"
                    + "order by j_year";
            rs = stat.executeQuery(sql);
            
            // -- start 省部级基金发文被引统计 格式（年，发文量，被引次数，篇均被引次数） --//
            
            sql = "select distinct j_year ,count(*) as 发文量 ,sum(journal_info.j_citation_frequency) as 被引次数, avg(journal_info.j_citation_frequency) as 篇均被引次数"
                    + "from paper_fund,journal_info"
                    + "where  paper_fund.j_number = journal_info.j_number and j_fund regexp('省|北京|上海|重庆')"
                    + "group by j_year"
                    + "order by j_year";
            rs = stat.executeQuery(sql);
            
            
        }catch(Exception e){
            
        }finally{
            JDBCUtils.close(rs, stat, conn);
        }
        
    }
    
}
