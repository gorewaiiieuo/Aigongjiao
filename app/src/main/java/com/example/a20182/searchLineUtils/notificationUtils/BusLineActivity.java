package com.example.a20182.searchLineUtils.notificationUtils;

/*
* 实时公交情况的显示类，
* 包含高德2D地图显示公交路线和
* 自定义水平方向列表显示实时数据
*/

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.amap.api.maps2d.AMap;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.overlay.BusLineOverlay;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.core.LatLonPoint;
import com.example.a20182.R;
import com.example.a20182.searchLineUtils.BusLineInfo;
import com.example.a20182.searchLineUtils.HorizontalListView;
import com.example.a20182.searchLineUtils.StationState;
import com.example.a20182.searchLineUtils.lineAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.a20182.FragmentSetting.isClock;
import static com.example.a20182.FragmentSetting.isEarlier;
import static com.example.a20182.FragmentSetting.isShark;
import static com.example.a20182.FragmentSetting.isWindow;
import static com.example.a20182.searchLineUtils.SearchLineActivity.buslineInfo;
import static com.example.a20182.searchLineUtils.SearchLineActivity.markedBusline;
import static com.example.a20182.searchLineUtils.notificationUtils.ServiceSubclass.stopShark;

public class BusLineActivity extends AppCompatActivity implements AMap.OnMapLoadedListener{

    private AMap aMap;
    private MapView mapView;
    private BusLineOverlay buslineOverlay;
    private BusLineItem busline;
    public static boolean isFirstLoad = true;
    private HorizontalListView show_name_station;
    private lineAdapter showStationNameAdapter;
    private List<StationState> stationstate;
    private int drawpos=0;
    private int curpos=0;
    private int preStation=-1;
    private View mView;

    Timer timer0=new Timer();
    final TimerTask task0 = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //关闭震动提醒
                    if(stopShark){
                        VibrateUtil.vibrateCancle(BusLineActivity.this);
                        stopShark = false;
                    }
                    if(curpos != drawpos) {
                        curpos = drawpos;
                        //当实时站点为预设站点时开始提醒
                        if((!isEarlier && preStation==curpos)
                                ||(isEarlier && preStation-1==curpos)){
                            clockAlert();
                        }
                        //实时情况的绘制，通过更新数据源的标记变量改变图标
                        if (curpos > 0 && curpos < stationstate.size()) {
                            stationstate.get(0).setState(R.drawable.plot1);
                            if (curpos == 1) stationstate.get(curpos).setState(R.drawable.bus2);
                            else if (curpos == stationstate.size() - 1) {
                                stationstate.get(curpos).setState(R.drawable.bus3);
                                stationstate.get(curpos - 1).setState(R.drawable.plot2);
                            } else {
                                stationstate.get(curpos).setState(R.drawable.bus2);
                                stationstate.get(curpos - 1).setState(R.drawable.plot2);
                            }
                        }else{
                            curpos=drawpos=0;
                            stationstate.get(0).setState(R.drawable.bus1);
                            stationstate.get(stationstate.size() - 1).setState(R.drawable.plot3);
                        }
                        showStationNameAdapter.notifyDataSetChanged();
                    }

                }
            });
        }
    };
    Timer timer1=new Timer();
    final TimerTask task1 = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    drawpos+=1;
                }
                });//drawpos即获取到的真实位置，此处为模拟数据
            }
        };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_line);
        mView = View.inflate(this, R.layout.activity_bus_line, null);
        show_name_station = (HorizontalListView) this.findViewById(R.id.show_name_station);
        mapView = (MapView) findViewById(R.id.busMap);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        //Log.i("onCreate","---------------");

        Intent intent = getIntent();
        int pos = intent.getIntExtra("position",-1);
        if(pos!=-1) {

            if(buslineInfo==null){
                buslineInfo = markedBusline;
            }
            BusLineInfo info = buslineInfo.get(pos);

            //判断buslineinfo获取的busline的往返情况，即SearchStationActivity中的选择情况
            if (info.getIsChild()) {
                busline = info.getChild().getBusLine();
            } else {
                busline = info.getBusLine();
            }

            buslineOverlay = new BusLineOverlay(this, aMap, busline);
            buslineOverlay.removeFromMap();
            buslineOverlay.addToMap();
            buslineOverlay.zoomToSpan();
            //首次加载时zoomToSpan函数无效，以下处理这个问题
            if (isFirstLoad) {
                int size = busline.getBusStations().size();
                LatLng first = convertToLatLng(busline.getBusStations().get(0).getLatLonPoint());
                LatLng middle = convertToLatLng(busline.getBusStations().get((int) (size / 2)).getLatLonPoint());
                LatLng last = convertToLatLng(busline.getBusStations().get(size - 1).getLatLonPoint());
                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(first).include(middle).include(last)
                        .build();
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 500));
                isFirstLoad = false;
            }

            stationstate = new ArrayList<>();
            for (int n = 0; n < busline.getBusStations().size(); n++) {
                String name = busline.getBusStations().get(n).getBusStationName();
                StationState state = new StationState(name, R.drawable.plot2);
                if (n == 0) state.setState(R.drawable.bus1);
                else if (n == busline.getBusStations().size() - 1) state.setState(R.drawable.plot3);
                stationstate.add(state);
            }

            //自定义的水平方向列表，并设置点击事件监听实现到站提醒
            showStationNameAdapter = new lineAdapter(this, stationstate);
            showStationNameAdapter.notifyDataSetChanged();
            show_name_station.setAdapter(showStationNameAdapter);
            show_name_station.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    final View vw = view;
                    final ConfirmDialog confirmDialog =
                            new ConfirmDialog(mView.getContext(), "设置到站提醒", "确认", "取消");
                    confirmDialog.show();
                    confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            //设置前初始化提醒事件的状态
                            MediaPlayUtil.stopRing();
                            if (stopShark) VibrateUtil.vibrateCancle(BusLineActivity.this);
                            confirmDialog.dismiss();
                            preStation = position;
                            for (StationState item : stationstate) {
                                item.setIsfocus(false);
                            }
                            stationstate.get(position).setIsfocus(true);
                            showStationNameAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void doCancel() {
                            confirmDialog.dismiss();
                        }
                    });

                }
            });

            timer0.schedule(task0, 0, 1000);
            timer1.schedule(task1, 2000, 3000);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("onStart","-------------");
        //new SendContact().execute();
    }

    @Override
    public void onMapLoaded() {
            //aMap.moveCamera(CameraUpdateFactory.zoomTo(80));
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            //aMap.moveCamera(CameraUpdateFactory.zoomTo(100));
        }
    }

    private LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    private void clockAlert(){
        //响铃提醒判断
        if(isClock){
            MediaPlayUtil.playRing(this);
        }
        //震动提醒
        if(isShark) {
            VibrateUtil.vibrate(this, new long[]{1000, 1000, 1000, 1000}, 0);
        }
        //弹窗提醒
        if(isWindow){
            Intent intent = new Intent();
            intent.setAction("windowService");
            intent.setPackage(getPackageName());
            startService(intent);
        }

        preStation = -1;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class SendContact extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            URL url = null;
            //把JSON数据转换成String类型使用输出流向服务器写
            try {
                url = new URL("http://mpforz.ticp.net:36037/WSServer/WSServlet");
                urlConnection = (HttpURLConnection) url.openConnection();//打开http连接
                urlConnection.setConnectTimeout(3000);//连接的超时时间
                urlConnection.setUseCaches(false);//不使用缓存
                //urlConnection.setFollowRedirects(false);是static函数，作用于所有的URLConnection对象。
                urlConnection.setInstanceFollowRedirects(true);//是成员函数，仅作用于当前函数,设置这个连接是否可以被重定向
                urlConnection.setReadTimeout(3000);//响应的超时时间
                urlConnection.setDoInput(true);//设置这个连接是否可以写入数据
                urlConnection.setDoOutput(true);//设置这个连接是否可以输出数据
                urlConnection.setRequestMethod("POST");//设置请求的方式
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");//设置消息的类型
                //urlConnection.connect();// 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                String jsonstr = "123";//把JSON对象按JSON的编码格式转换为字符串

                OutputStream out = urlConnection.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
                bw.write(jsonstr);//把json字符串写入缓冲区中
                bw.flush();//刷新缓冲区，把数据发送出去，这步很重要
                out.close();
                bw.close();//使用完关闭

                int code = urlConnection.getResponseCode();
                if (code == 200 && ((url.toString()).equals(urlConnection.getURL().toString()))) {
                    //获取服务器响应后返回的数据
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String rstr = null;
                    StringBuffer buffer = new StringBuffer();
                    while ((rstr = br.readLine()) != null) {//BufferedReader特有功能，一次读取一行数据
                        buffer.append(rstr);
                    }
                    System.out.println(buffer.toString());
                    in.close();
                    br.close();

                } else {
                    //上传失败
                }

            } catch (ConnectException e) {
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String str) {

        }
    }
}
