package com.example.guohuayu.Navigation;

/*
*POI搜索结构列表适配器
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.a20182.R;

public class SearchAdapter extends BaseAdapter{
    private List<LocationBean> datas = new ArrayList<>();
    private LayoutInflater inflater;
    private static final int RESOURCE = R.layout.list_item;

    public SearchAdapter(Context context, List<LocationBean> datas) {
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
            vh.tv_title = (TextView) convertView.findViewById(R.id.address);
            vh.tv_text = (TextView) convertView.findViewById(R.id.addressDesc);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        LocationBean bean = (LocationBean) getItem(position);
        vh.tv_title.setText(bean.getTitle());
        vh.tv_text.setText(bean.getContent());
        return convertView;
    }

    private class ViewHolder{
        public TextView tv_title;
        public TextView tv_text;
    }

    public void setData(List<LocationBean> datas){
        //dismissLoadingDialog();
        this.datas = datas;
    }
}
