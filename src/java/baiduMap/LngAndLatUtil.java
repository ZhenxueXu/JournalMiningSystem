package baiduMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
public class LngAndLatUtil {

    public static Map<String, Double> getLngAndLat(String address) {
        Map<String, Double> map = new HashMap<String, Double>();
        try {
            address = java.net.URLEncoder.encode(address, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = "http://api.map.baidu.com/geocoder/v2/?address=" + address + "&output=json&ak=TW5tsYOwBZAGaWeLCOx77mfYeHTI2nHc";
        String json = loadJSON(url);                
        JsonParser parser = new JsonParser();
        JsonObject obj =  (JsonObject) parser.parse(json);
        if (obj.get("status").toString().equals("0")) {
            double lng = obj.getAsJsonObject("result").getAsJsonObject("location").get("lng").getAsDouble();//obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
            double lat =  obj.getAsJsonObject("result").getAsJsonObject("location").get("lat").getAsDouble();
            map.put("lng", lng);
            map.put("lat", lat);
            //System.out.println("经度："+lng+"---纬度："+lat);
        } else {
            System.out.println("未找到相匹配的经纬度！");
        }
        return map;
    }
    public static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return json.toString();
    }

}
