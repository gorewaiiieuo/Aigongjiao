package com.example.a20182.searchLineUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.a20182.R;

import java.util.ArrayList;
import java.util.List;

public class lineAdapter extends BaseAdapter{
    private List<StationState> datas = new ArrayList<>();
    private LayoutInflater inflater;
    private static final int RESOURCE = R.layout.stationname_item;

    public lineAdapter(Context context, List<StationState> datas) {
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
            vh.tv_title = (TextViewVertical) convertView.findViewById(R.id.stationname);
            vh.iv_busline = (ImageView) convertView.findViewById(R.id.drawLine_iv);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        StationState bean = (StationState) getItem(position);

        vh.tv_title.setText(bean.getTitle());
        if(bean.getIsfocus())vh.tv_title.setTextARGB(255,255,0,0);
        vh.iv_busline.setImageResource(bean.getState());

        return convertView;
    }

    private class ViewHolder{
        public TextViewVertical tv_title;
        public ImageView iv_busline;
    }

    public void setData(List<StationState> datas){
        //dismissLoadingDialog();
        this.datas = datas;
    }
}
