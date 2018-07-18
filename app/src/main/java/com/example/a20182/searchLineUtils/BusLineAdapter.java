package com.example.a20182.searchLineUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a20182.R;
import com.example.a20182.searchLineUtils.BusLineInfo;

import java.util.ArrayList;
import java.util.List;

public class BusLineAdapter extends BaseAdapter{
    private List<BusLineInfo> datas = new ArrayList<>();
    private LayoutInflater inflater;
    private static final int RESOURCE = R.layout.busline_item;
    public static List<View> viewlist = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public BusLineAdapter(Context context, List<BusLineInfo> datas) {
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
            vh.tv_title = (TextView) convertView.findViewById(R.id.linename_tv);
            vh.tv_start= (TextView) convertView.findViewById(R.id.start_tv);
            vh.tv_end= (TextView) convertView.findViewById(R.id.end_tv);
            vh.tv_time = (TextView) convertView.findViewById(R.id.linetime_tv);
            vh.iv_shift = (ImageView) convertView.findViewById(R.id.shift_iv);
            convertView.setTag(vh);
            viewlist.add(convertView);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        BusLineInfo bean = (BusLineInfo) getItem(position);
        vh.tv_title.setText(bean.getBusLineName());
        vh.tv_start.setText(bean.getOriginStation());
        vh.tv_end.setText(bean.getTerminalStation());
        vh.tv_time.setText(bean.getFirstBusTime()+"-"+bean.getLastBusTime());

        if(bean.getChild()!=null)vh.iv_shift.setImageResource(R.drawable.shift);
        vh.iv_shift.setTag(position);
        vh.iv_shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = (Integer) v.getTag();
                BusLineInfo bean = (BusLineInfo) getItem(index);
                View vw = viewlist.get(index);
                TextView tv_name = (TextView)vw.findViewById(R.id.linename_tv);
                TextView tv_start = (TextView)vw.findViewById(R.id.start_tv);
                TextView tv_end = (TextView)vw.findViewById(R.id.end_tv);
                TextView tv_time = (TextView)vw.findViewById(R.id.linetime_tv);
                Log.i("output---------",index+ "   " + tv_name.getText().toString());
                BusLineInfo another = bean.getChild()!=null?bean.getChild():null;
                if(another!=null) {
                    if(!bean.getIsChild()) {
                        tv_start.setText(another.getOriginStation());
                        tv_end.setText(another.getTerminalStation());
                        tv_time.setText(another.getFirstBusTime() + "-" + another.getLastBusTime());
                        bean.setIsChild(true);
                    }else{
                        tv_start.setText(bean.getOriginStation());
                        tv_end.setText(bean.getTerminalStation());
                        tv_time.setText(bean.getFirstBusTime()+"-"+bean.getLastBusTime());
                        bean.setIsChild(false);
                    }
                }
            }
        });

        return convertView;
    }

    private class ViewHolder{
        public TextView tv_title;
        public TextView tv_start;
        public TextView tv_end;
        public TextView tv_time;
        public ImageView iv_shift;
    }

    public void setData(List<BusLineInfo> datas){
        //dismissLoadingDialog();
        this.datas = datas;
    }

}
