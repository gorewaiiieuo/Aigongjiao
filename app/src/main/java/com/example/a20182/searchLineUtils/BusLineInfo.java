package com.example.a20182.searchLineUtils;

/*
*对象数组存储公交路线信息
* 特别的，将往返公交路线合并让显示更为清晰
 */

import com.amap.api.services.busline.BusLineItem;

import java.util.List;

public class BusLineInfo {
    private String busLineName;
    private String busLineType;
    private String originStation;
    private String terminalStation;
    private List<String> busStations;
    private String firstBusTime;
    private String lastBusTime;
    private BusLineInfo child;
    private boolean isChild = false;
    private String busLineId;
    private BusLineItem info;

    public BusLineInfo(String busLineName,
                       String busLineType,
                       String originStation,
                       String terminalStation,
                       List<String> busStations,
                       String firstBusTime,
                       String lastBusTime,
                       String busLineId,
                       BusLineItem item)
    {
        this.busLineName = busLineName;
        this.busLineType = busLineType;
        this.originStation = originStation;
        this.terminalStation = terminalStation;
        this.busStations = busStations;
        this.firstBusTime = firstBusTime;
        this.lastBusTime = lastBusTime;
        this.busLineId = busLineId;
        this.info = item;
    }

    public String getBusLineName(){
        return this.busLineName;
    }

    public String getOriginStation(){
        return this.originStation;
    }

    public String getTerminalStation(){
        return this.terminalStation;
    }

    public String getBusLineType(){
        return this.busLineType;
    }

    public List<String> getBusStations(){
        return this.busStations;
    }

    public String getFirstBusTime(){
        return this.firstBusTime;
    }

    public BusLineItem getBusLine(){
        return this.info;
    }

    public String getLastBusTime(){
        return this.lastBusTime;
    }

    public void setChild(BusLineInfo child){
        this.child = child;
    }

    public BusLineInfo getChild(){
        return this.child;
    }

    public String getBusLineId(){
        return this.busLineId;
    }

    public void setIsChild(boolean isChild){
        this.isChild = isChild;
    }

    public boolean getIsChild(){
        return this.isChild;
    }

}
