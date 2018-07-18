package com.example.a20182.searchStationUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a20182.R;

import java.util.ArrayList;
import java.util.List;

public class stationAdapter extends BaseAdapter{
    private List<BusStationInfo> datas = new ArrayList<>();
    private LayoutInflater inflater;
    private static final int RESOURCE = R.layout.busstation_item;

    public stationAdapter(Context context, List<BusStationInfo> datas) {
        this.datas = datas;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = inflater.inflate(RESOURCE, null);
            vh.tv_title = (TextView) convertView.findViewById(R.id.stationName_tv);
            vh.tv_lines = (TextView) convertView.findViewById(R.id.crossLine_tv);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        BusStationInfo bean = (BusStationInfo) getItem(position);
        vh.tv_title.setText(bean.getBusStationName());
        vh.tv_lines.setText(bean.getLinesText());
        return convertView;
    }

    private class ViewHolder{
        public TextView tv_title;
        public TextView tv_lines;
    }

    public void setData(List<BusStationInfo> datas){
        //dismissLoadingDialog();
        this.datas = datas;
    }
}
