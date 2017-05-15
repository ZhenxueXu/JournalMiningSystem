package text;

import com.google.gson.Gson;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named(value = "test2")
@SessionScoped
public class Test2 implements Serializable {

    public Test2() {
    }
    private List<List<Double>> data;   //需要返回前端的数据
    private int minYear = 2006;
    private int maxYear = 2013;

    public int getMinYear() {
        return minYear;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    public int getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }
    
    

    public void setAllData() {
        String[] address = {"武汉大学经济管理学院", "华侨大学计算机学院", "厦门大学计算机学院"};  //需要绘图的地址
        data = new ArrayList<>();
        for (String addr : address) {
            List<Double> item = new ArrayList<>();    //每个数据点的数据
            Map<String, Double> map = new HashMap<>();  //获取每个地址转换后的经纬度
            map = baiduMap.LngAndLatUtil.getLngAndLat(addr);
            item.add(map.get("lng"));    //经度
            item.add(map.get("lat"));    // 纬度
            item.add(500.0);             //值
            data.add(item);
        }
    }

    public String getMapData() {
        //转换为json格式数据返回前端
        Gson gson = new Gson();
        System.out.println(gson.toJson(data));
        return gson.toJson(data);
    }

}
