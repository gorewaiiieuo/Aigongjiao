package com.example.a20182.searchStationUtils;

/*
*对象数组存储站点信息，即经过的公交路线名
 */

import com.amap.api.services.core.LatLonPoint;

import java.util.List;

public class BusStationInfo {
    private String busStationName;
    private LatLonPoint latlonPoint;
    private List<String> busLines;

    public BusStationInfo(String busStationName,
                          LatLonPoint latlonPoint,
                          List<String> busLines)
    {
        this.busStationName = busStationName;
        this.latlonPoint = latlonPoint;
        this.busLines = busLines;
    }

    public String getBusStationName(){
        return this.busStationName;
    }

    public List<String> getBusLines(){
        return this.busLines;
    }

    public LatLonPoint getLatLon(){
        return this.latlonPoint;
    }

    public String getLinesText(){
        List<String> lines = this.busLines;
        String text = "";
        for(String str:lines)
        {
            if(text.equals(""))text = str;
            else text += "-" + str;
        }
        return text;
    }

}
