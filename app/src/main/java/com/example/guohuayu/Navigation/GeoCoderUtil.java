package com.example.guohuayu.Navigation;

/*
*地理逆编码工具类
*输入经纬度返回具体地理数据
*/

import android.content.Context;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

public class GeoCoderUtil implements GeocodeSearch.OnGeocodeSearchListener {
    private GeocodeSearch geocodeSearch;
    private GeoCoderAddressListener geoCoderAddressListener;
    private GeoCoderLatLngListener geoCoderLatLngListener;

    private static GeoCoderUtil geoCoderUtil;
    public static GeoCoderUtil getInstance(Context context) {
        if (null == geoCoderUtil) {
            geoCoderUtil = new GeoCoderUtil(context);
        }
        return geoCoderUtil;
    }

    private GeoCoderUtil(Context context) {
        geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(this);
    }


    /**
     * 经纬度转地址描述
     **/
    public void geoAddress(LatLonPoint latLonPoint, GeoCoderAddressListener geoCoderAddressListener) {
        if (latLonPoint == null) {
            geoCoderAddressListener.onAddressResult("");
            return;
        }
        this.geoCoderAddressListener = geoCoderAddressListener;

        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求

    }
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode != 1000 || result == null || result.getRegeocodeAddress() == null) {
            geoCoderAddressListener.onAddressResult("");
            return;
        }
        RegeocodeAddress regeocodeAddress = result.getRegeocodeAddress();
        String addressDesc = regeocodeAddress.getCityCode();
        if (regeocodeAddress.getAois().size() > 0) {
            addressDesc += regeocodeAddress.getAois().get(0).getAoiName();
        }
        geoCoderAddressListener.onAddressResult(addressDesc);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
        if (geocodeResult == null || geocodeResult.getGeocodeAddressList() == null
                || geocodeResult.getGeocodeAddressList().size() <= 0) {
            geoCoderLatLngListener.onLatLonResult(null);
            return;
        }
        GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
        geoCoderLatLngListener.onLatLonResult(address.getLatLonPoint());

    }

    public interface GeoCoderAddressListener {
        void onAddressResult(String result);
    }

    public interface GeoCoderLatLngListener {
        void onLatLonResult(LatLonPoint latLonPoint);
    }

}