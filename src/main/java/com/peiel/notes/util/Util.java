package com.peiel.notes.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class Util {

    public static String[] getDecodeToken(String token) {
        String[] result = new String[]{null, null};
        try {
            String tmpString = new String(Base64.decodeBase64(token));
            String[] tmp = tmpString.split("&");
            result[0] = tmp[0];
            result[1] = tmp[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isEmpty(Object o) {
        if (o == null)
            return true;
        if (o instanceof String) {
            if (Objects.equals(o, ""))
                return true;
        }
        if (o instanceof List) {
            if (((List) o).size() == 0)
                return true;
        }
        return false;
    }

    public static JSON jsonSuccess() {
        JSONObject jo = new JSONObject();
        jo.put("code", 200);
        jo.put("msg", "请求成功");
        return jo;
    }

    public static JSON jsonSuccess(Map<String, Object> params) {
        JSONObject jo = new JSONObject();
        jo.put("code", 200);
        jo.put("msg", "请求成功");
        jo.putAll(params);
        return jo;
    }

    public static JSON jsonSuccess(String msg) {
        JSONObject jo = new JSONObject();
        jo.put("code", 200);
        jo.put("msg", msg);
        return jo;
    }

    public static JSON jsonSuccess(String msg, Map<String, Object> params) {
        JSONObject jo = new JSONObject();
        jo.put("code", 200);
        jo.put("msg", msg);
        jo.putAll(params);
        return jo;
    }

    public static JSON jsonFail() {
        JSONObject jo = new JSONObject();
        jo.put("code", 400);
        jo.put("msg", "参数错误");
        return jo;
    }

    public static JSON jsonFail(String msg) {
        JSONObject jo = new JSONObject();
        jo.put("code", 50001);
        jo.put("msg", msg);
        return jo;
    }

    public static JSON jsonFail(int code, String msg) {
        JSONObject jo = new JSONObject();
        jo.put("code", code);
        jo.put("msg", msg);
        return jo;
    }

    public static JSON jsonFail(int code, String msg, Map<String, Object> params) {
        JSONObject jo = new JSONObject();
        jo.put("code", code);
        jo.put("msg", msg);
        jo.putAll(params);
        return jo;
    }

    public static double dealPriceUnit(double price) {
        double priceUnit = 0;
        double price1 = getDouble2(price / 100000000);
        double price2 = getDouble2(price / 10000);
        if (price1 >= 1) {
            priceUnit = price1;
        } else if (price2 >= 1) {
            priceUnit = price2;
        } else {
            priceUnit = price;
        }
        return priceUnit;
    }

    public static String dealPriceUnitString(double price) {
        String priceUnitString = "元";
        double price1 = getDouble2(price / 100000000);
        double price2 = getDouble2(price / 10000);
        if (price1 >= 1) {
            priceUnitString = "亿元";
        } else if (price2 >= 1) {
            priceUnitString = "万元";
        }
        return priceUnitString;
    }

    public static void setAndCountPrice(Map map) {
        double day_price = Double.parseDouble(map.get("day_price").toString());
        map.put("day_price", Util.dealPriceUnit(day_price));
        map.put("dayPriceUnit", Util.dealPriceUnitString(day_price));
        double price = day_price * Double.parseDouble(map.get("house_area").toString());
        map.put("price", Util.dealPriceUnit(price));
        map.put("priceUnit", Util.dealPriceUnitString(price));
    }

    public static void main(String[] args) {
        List<String> buildingIdsOwm = new ArrayList<String>();
        List<String> buildingIds = new ArrayList<String>();
        buildingIds.add("10");
        buildingIds.add("19");
        buildingIds.add("18");
        buildingIds.add("17");
        buildingIds.add("16");
        buildingIds.add("1");
        buildingIds.add("3");
        buildingIds.add("4");
        buildingIds.add("33");

        buildingIdsOwm.add("18");
        buildingIdsOwm.add("16");
        buildingIdsOwm.add("3");
        System.out.println(buildingIds);
        System.out.println(buildingIdsOwm);
        buildingIdsOwm.retainAll(buildingIds);//取交际
        buildingIds.removeAll(buildingIdsOwm);//取差值
        System.out.println(buildingIds);
        System.out.println(buildingIdsOwm);
    }

    public static double getDouble2(Double price) {
        return new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
