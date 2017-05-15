package dataAnalysisBeans;

import JDBCUtils.JDBCUtils;
import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.data.MapData;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Scatter;
import com.google.gson.Gson;
import info.AssociationRules;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 引用期刊的关联分析
 *
 * @author Think
 */
@Named(value = "refJournalAssociation")
@SessionScoped
public class RefJournalAssociation implements Serializable {

    private List<List<String>> data;
    private List<AssociationRules> arlist = new ArrayList<>();
    private GsonOption option;
    private double min_supCount = 0.15;
    private double min_confidence = 0.6;
    int total;
    List<List<List<String>>> frequentItemsets ;       //频繁项集
    List<Map<List<String>, Integer>> supAcount ;      //支持度计数

    public RefJournalAssociation() {
         
        
    }

    public double getMin_supCount() {
        return min_supCount;
    }

    public void setMin_supCount(double min_supCount) {
        this.min_supCount = min_supCount;
    }

    public double getMin_confidence() {       
        return min_confidence;
    }

    public void setMin_confidence(double min_confidence) {
        this.min_confidence = min_confidence;
    }

    public List<AssociationRules> getArlist() {
        return arlist;
    }

    public void setData() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();

            sql = "select j_number, r_journal "
                    + "from paper_references where r_journal<>'' "
                    + "group by j_number,r_journal  "
                    + "order by j_number,r_number";
            rs = stat.executeQuery(sql);
            data = new ArrayList<>();
            String itemId = "";
            List<String> item = null;
            total = 0;
            while (rs.next()) {
                if (!itemId.equals(rs.getString(1))) {
                    if (item != null) {
                        data.add(item);
                        total++;
                    }
                    item = new ArrayList<>();
                    itemId = rs.getString(1);
                }
                item.add(rs.getString(2));
            }
        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
        apriori(min_supCount);
    }

    public void apriori(double min_sup) {

        List<List<String>> L1 = new ArrayList<>();                           //一项频繁集                         
        Map<List<String>, Integer> count1 = new HashMap<>();                  //一项集支持度计数
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        String sql;
        try {
            conn = JDBCUtils.getConn();
            stat = conn.createStatement();
            sql = "select distinct r_journal,count(*) as 被引次数 "
                    + "from paper_references where r_journal<>'' "
                    + "group by r_journal "
                    + "order by r_journal ";
            rs = stat.executeQuery(sql);

            while (rs.next()) {
                if (rs.getInt(2) / 1.0 / total >= min_sup) {
                    List<String> item = new ArrayList<>();
                    item.add(rs.getString(1));
                    L1.add(item);
                    count1.put(item, rs.getInt(2));

                }
            }
        } catch (Exception e) {

        } finally {
            JDBCUtils.close(rs, stat, conn);
        }
        supAcount = new ArrayList<>(); 
        frequentItemsets = new ArrayList<>();
        frequentItemsets.add(L1);
        supAcount.add(count1);
        int len = L1.size();
        List<List<String>> preLk = L1;
        List<List<String>> Ck = new ArrayList<>();
        Map<List<String>, Integer> kCount = new HashMap<>();

        for (int i = 2; i < len; i++) {
            kCount = new HashMap<>();
            Ck = new ArrayList<>();
            Ck = getNextCandidateSet(preLk);
            List<List<String>> Lk = new ArrayList<>();
            for (int j = 0; j < Ck.size(); j++) {
                int supCount = 0;
                for (List<String> item : data) {
                    if (item.containsAll(Ck.get(j))) {
                        supCount++;
                    }
                }
                if (supCount / 1.0 / total >= min_sup) {
                    Lk.add(Ck.get(j));
                    kCount.put(Ck.get(j), supCount);
                }
            }
            if (Lk == null || Lk.size() < 1) {
                break;
            }
            preLk = Lk;
            frequentItemsets.add(Lk);
            supAcount.add(kCount);
            for (List<String> l : Lk) {
                System.out.print(l + " " + ":" + kCount.get(l) + "\n");
            }
        }
        produceAssociationRules(min_confidence);
        
    }

    public List<List<String>> getNextCandidateSet(List<List<String>> preFrequentSet) {

        List<List<String>> kCandidateSet = new ArrayList<>();
        int preK = preFrequentSet.get(0).size();               //记录k-1项集的项数
        if (preK == 1) {
            for (int i = 0; i < preFrequentSet.size(); i++) {
                for (int j = i + 1; j < preFrequentSet.size(); j++) {
                    List<String> tempLine = new ArrayList<>();
                    tempLine.addAll(preFrequentSet.get(i));
                    tempLine.addAll(preFrequentSet.get(j));
                    kCandidateSet.add(tempLine);
                }
            }
        } else {
            for (int i = 0; i < preFrequentSet.size(); i++) {
                for (int j = i + 1; j < preFrequentSet.size(); j++) {
                    if (preFrequentSet.get(i).subList(0, preK - 1).containsAll(preFrequentSet.get(j).subList(0, preK - 1))) {
                        List<String> tempLine = new ArrayList<>();
                        tempLine.addAll(preFrequentSet.get(i));
                        tempLine.add(preFrequentSet.get(j).get(preK - 1));
                        if (!hasInfrequentSubset(preFrequentSet, tempLine)) {
                            kCandidateSet.add(tempLine);
                        }
                    }
                }
            }
        }
        return kCandidateSet;
    }

    public boolean hasInfrequentSubset(List<List<String>> preFrequentSet, List<String> cItem) {
        boolean flag = false;
        int len = cItem.size();
        for (int i = 0; i < len; i++) {
            List<String> tempLine = new ArrayList<>();
            tempLine.addAll(cItem.subList(0, i));
            tempLine.addAll(cItem.subList(i + 1, len));
            if (!preFrequentSet.contains(tempLine)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public void produceAssociationRules(double min_conf) {
        option = new GsonOption();
        Legend legend = new Legend();
        legend.right(10);
        for (int i = 1; i < frequentItemsets.size(); i++) {
            List<MapData> scatter = new ArrayList<>();
            Scatter series = new Scatter();
            for (int j = 0; j < frequentItemsets.get(i).size(); j++) {
                int len = frequentItemsets.get(i).get(j).size();
                int num = len == 0 ? 0 : 1 << (len);
                for (int k = 1; k < num - 1; k++) {
                    List<String> subSet = new ArrayList<>();
                    int index = k;
                    for (int n = 0; n < len; n++) {
                        if ((index & 1) == 1) {		//每次判断index最低位是否为1，为1则把集合set的第j个元素放到子集中
                            subSet.add(frequentItemsets.get(i).get(j).get(n));
                        }
                        index >>= 1;		//右移一位
                    }//求出真子集
                    //产生关联规则
                    List<String> tempLine = new ArrayList<>();
                    tempLine.addAll(frequentItemsets.get(i).get(j));
                    double support;
                    double confidence;
                    support = supAcount.get(len - 1).get(tempLine) / 1.0 / total;
                    confidence = supAcount.get(len - 1).get(tempLine) / 1.0 / supAcount.get(subSet.size() - 1).get(subSet);
                    tempLine.removeAll(subSet);
                    if (confidence >= min_conf) {
                        AssociationRules ar = new AssociationRules(subSet + " => " + tempLine, support, confidence);
                        List<Double> temp = new ArrayList<>();
                        temp.add(support);
                        temp.add(confidence);
                        MapData point = new MapData(ar.getRule(), temp);     //画图数据
                        series.data(point);
                        scatter.add(point);
                        arlist.add(ar);
                        System.out.println(ar.toString());
                    }
                }
            }
            if ( scatter.size() > 0) {                
                series.name((i + 1) + "项频繁集");
                legend.data((i + 1) + "项频繁集");
                option.series(series);
            }

        }
        option.legend(legend);
    }

    public String getOption() {
        setData();
        Gson gson = new Gson();
        System.out.println(gson.toJson(option));
        return gson.toJson(option);
    }

}
