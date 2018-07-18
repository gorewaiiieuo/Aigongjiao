package com.example.a20182.routeDisplayAdapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a20182.R;

/**
 * Created by Administrator on 2018/7/3.
 * 用于显示选择方案后的线路导航列表
 */

public class routeDisaplayAdapter extends RecyclerView.Adapter{

    public static enum ITEM_TYPE{
        ITEM_TYPE_START,
        ITEM_TYPE_WALK_AND_END,
        ITEM_TYPE_BUS,
        ITEM_TYPE_TRAIN,
        ITEM_TYPE_TOTAL
    }

    private String[] routeInfo;
    //为了支持item展开收缩
    private boolean isHide;//收缩
    private boolean isOpen;//展开
    private HideOrShowCallBack hideOrShowCallBack;

    public void setHideOrShowCallBack(HideOrShowCallBack hideOrShowCallBack){
        this.hideOrShowCallBack = hideOrShowCallBack;
    }

    public interface HideOrShowCallBack{
        void hide();

        void open();
    }
    public routeDisaplayAdapter(String[] routeInfo){
        this.routeInfo = routeInfo;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == ITEM_TYPE.ITEM_TYPE_START.ordinal()){
            View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.route_start_item,viewGroup,false);

            RecyclerView.ViewHolder routeHolder = new RouteStartHolder(view);

            return routeHolder;
        }
        if(viewType == ITEM_TYPE.ITEM_TYPE_WALK_AND_END.ordinal()){
            View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.route_walk_and_end_item,viewGroup,false);

            RecyclerView.ViewHolder routeWalkAndEndHolder = new RouteWalkAndEndHolder(view);

            return routeWalkAndEndHolder;
        }
        if(viewType == ITEM_TYPE.ITEM_TYPE_BUS.ordinal()){
            View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.route_bus_item,viewGroup,false);

            RecyclerView.ViewHolder routeBusHolder = new RouteBusHolder(view);

            return routeBusHolder;
        }
        if(viewType == ITEM_TYPE.ITEM_TYPE_TOTAL.ordinal()){
            View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.route_total_item,viewGroup,false);

            RecyclerView.ViewHolder routeTotalHolder = new RouteTotalHolder(view);

            return routeTotalHolder;
        }
        else{
            View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.route_train_item,viewGroup,false);

            RecyclerView.ViewHolder routeTrainHolder = new RouteTrainHolder(view);

            return routeTrainHolder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof RouteStartHolder){
            RouteStartHolder routeStartHolder = (RouteStartHolder) viewHolder;

            routeStartHolder.tv_walkitem_orginal.setText(routeInfo[position].substring(0,routeInfo[position].indexOf("\n")));
            routeStartHolder.tv_walkinfo.setText(routeInfo[position].substring(routeInfo[position].indexOf("\n") + 1));
        }
        if(viewHolder instanceof RouteBusHolder){
            final RouteBusHolder routeBusHolder = (RouteBusHolder) viewHolder;
            final String[] route = routeInfo[position].split("\n");

            routeBusHolder.tv_bus_start.setText(route[0]);
            routeBusHolder.tv_busInfo.setText(route[1]);
            routeBusHolder.tv_bus_des.setText(route[3]);

            String hideStationInfo = "";
            String openStationInfo = "";
            //设置经过站点的展开隐藏
            if(Integer.valueOf(route[1].charAt(route[1].indexOf("站") - 1)) > 0){//除起点终点还有站
                String[] pastStations = route[2].split(" ");
                int length = pastStations.length;
                for(int i = 0; i < length; i++){
                    openStationInfo += pastStations[i];
                    if(i != length - 1){
                        openStationInfo += "\n";
                    }
                }
                setHideList();
                //**********************************
                System.out.println("该线路经过的站点有： " + openStationInfo);
            }else{ //仅有起点终点两站
                setRealList();
            }


            if(hideOrShowCallBack != null){
                final String finalOpenStationInfo = openStationInfo;
                routeBusHolder.tv_busInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //************************************//
//                        if(Integer.valueOf(route[1].charAt(route[1].indexOf("站") - 1)) > 0){
                            if(isOpen){
                                hideOrShowCallBack.hide();
                                routeBusHolder.tv_busStationInfo.setText("");
                                System.out.println("isOpen" + finalOpenStationInfo);
                                return;
                            }
                            if(isHide){
                                hideOrShowCallBack.open();
                                routeBusHolder.tv_busStationInfo.setText(finalOpenStationInfo);
                                System.out.println("isHide" + finalOpenStationInfo);
                                return;
                            }
//                        }
                    }
                });
            }
        }
        if(viewHolder instanceof RouteTrainHolder){
            final RouteTrainHolder routeTrainHolder = (RouteTrainHolder) viewHolder;

            String[] route = routeInfo[position].split("\n");

            routeTrainHolder.tv_train_start.setText(route[0]);
            routeTrainHolder.tv_trainInfo.setText(route[1]);
            routeTrainHolder.tv_train_des.setText(route[3]);

            String hideStationInfo = "";
            String openStationInfo = "";
            //设置经过站点的展开隐藏
            if(Integer.valueOf(route[1].charAt(route[1].indexOf("站") - 1)) > 0){//除起点终点还有站
                String[] pastStations = route[2].split(" ");
                int length = pastStations.length;
                for(int i = 0; i < length; i++){
                    openStationInfo += pastStations[i];
                    if(i != length - 1){
                        openStationInfo += "\n";
                    }
                }
                setHideList();
            }else{ //仅有起点终点两站
                setRealList();
            }


            if(hideOrShowCallBack != null){
                final String finalOpenStationInfo = openStationInfo;
                routeTrainHolder.tv_trainInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //************************************//
//                        if(Integer.valueOf(route[1].charAt(route[1].indexOf("站") - 1)) > 0){
                        if(isOpen){
                            hideOrShowCallBack.hide();
                            routeTrainHolder.tv_trainStationInfo.setText("");
                            System.out.println("isOpen" + finalOpenStationInfo);
                            return;
                        }
                        if(isHide){
                            hideOrShowCallBack.open();
                            routeTrainHolder.tv_trainStationInfo.setText(finalOpenStationInfo);
                            System.out.println("isHide" + finalOpenStationInfo);
                            return;
                        }
//                        }
                    }
                });
            }
//            routeTrainHolder.tv_train_start.setText("这是一个轨道起点");
        }
        if(viewHolder instanceof RouteWalkAndEndHolder){
            RouteWalkAndEndHolder routeWalkAndEndHolder = (RouteWalkAndEndHolder) viewHolder;

            String[] route = routeInfo[position].split("\n");
            if(position == routeInfo.length - 1) {
                String[] preStep = routeInfo[position - 1].split("\n");
                routeWalkAndEndHolder.tv_walkend_start.setText(preStep[3]);
                routeWalkAndEndHolder.tv_walkendInfo.setText(route[1]);
                routeWalkAndEndHolder.tv_walkend_des.setText("终点");
            }else if(position == 0){
                String[] nextStep = routeInfo[position + 1].split("\n");
                routeWalkAndEndHolder.tv_walkend_start.setText(route[0]);
                routeWalkAndEndHolder.tv_walkendInfo.setText(route[1]);
                routeWalkAndEndHolder.tv_walkend_des.setText(nextStep[0]);
            } else{
                String[] preStep = routeInfo[position - 1].split("\n");
                String[] nextStep = routeInfo[position + 1].split("\n");
                routeWalkAndEndHolder.tv_walkend_start.setText(preStep[3]);
                routeWalkAndEndHolder.tv_walkendInfo.setText(route[1]);
                routeWalkAndEndHolder.tv_walkend_des.setText(nextStep[0]);
            }
//            routeWalkAndEndHolder.tv_walkend_start.setText("这是一个步行中转起点或终点");
        }
        if(viewHolder instanceof RouteTotalHolder){
            RouteTotalHolder routeTotalHolder = (RouteTotalHolder) viewHolder;
            String[] route = routeInfo[position].split("\n");

            routeTotalHolder.tv_totalRoute.setText(route[0]);
            routeTotalHolder.tv_totalRouteInfo.setText(route[1]);
        }
    }
    /*
    *     识别item类型并加载对应itemLayout
    * */

    @Override
    public int getItemViewType(int position) {
        if(routeInfo[position].contains("步行大约")){
            return ITEM_TYPE.ITEM_TYPE_START.ordinal();
        }
        else if(routeInfo[position].contains( "·")){
            return ITEM_TYPE.ITEM_TYPE_TOTAL.ordinal();
        }
        else if(routeInfo[position].contains("号线")){
            return ITEM_TYPE.ITEM_TYPE_TRAIN.ordinal();
        }
        else if(routeInfo[position].contains("路(") || routeInfo[position].contains("专线")){
            return ITEM_TYPE.ITEM_TYPE_BUS.ordinal();
        }
        else if(routeInfo[position].contains( "分钟，步行约")){
            return ITEM_TYPE.ITEM_TYPE_WALK_AND_END.ordinal();
        }
//        else{
//            return ITEM_TYPE.ITEM_TYPE_WALK_AND_END.ordinal();
//        }
       return super.getItemViewType(position);

    }


    @Override
    public int getItemCount() {
        return routeInfo == null ? 0 : routeInfo.length;
    }

    //隐藏
    public void setHideList(){
//        this.routeInfo = newRouteInfo;
//        notifyDataSetChanged();
        this.isHide = true;
        this.isOpen = false;
    }

    //展开
    public void setOpenList() {
//        this.routeInfo = openRouteInfo;
        this.isOpen = true;
        this.isHide = false;
        //notifyDataSetChanged();
    }

    //不需要隐藏/展开
    public void setRealList() {
//        this.routeInfo = realRouteInfo;
        //notifyDataSetChanged();
        this.isHide = false;
        this.isOpen = false;
    }

    private class RouteStartHolder extends RecyclerView.ViewHolder {
        private TextView tv_walkitem_orginal,tv_walkinfo;
        public RouteStartHolder(View itemView) {
            super(itemView);

            tv_walkitem_orginal = itemView.findViewById(R.id.tv_walkitem_orginal);
            tv_walkinfo = itemView.findViewById(R.id.tv_walkinfo);
        }
    }

    private class RouteWalkAndEndHolder extends RecyclerView.ViewHolder {
        private TextView tv_walkend_start,tv_walkendInfo,tv_walkend_des;
        public RouteWalkAndEndHolder(View itemView) {
            super(itemView);

            tv_walkend_start = itemView.findViewById(R.id.tv_walkend_start);
            tv_walkendInfo = itemView.findViewById(R.id.tv_walkend_Info);
            tv_walkend_des = itemView.findViewById(R.id.tv_walkend_des);
        }
    }

    private class RouteBusHolder extends RecyclerView.ViewHolder {
        private TextView tv_bus_start,tv_busInfo, tv_bus_des, tv_busStationInfo;
        public RouteBusHolder(View itemView) {
            super(itemView);

            tv_bus_start = itemView.findViewById(R.id.tv_bus_start);
            tv_busInfo = itemView.findViewById(R.id.tv_busInfo);
            tv_busStationInfo = itemView.findViewById(R.id.tv_busStationInfo);
            tv_bus_des = itemView.findViewById(R.id.tv_bus_des);
        }
    }

    private class RouteTrainHolder extends RecyclerView.ViewHolder {
        private TextView tv_train_start,tv_trainInfo,tv_trainStationInfo, tv_train_des;
        public RouteTrainHolder(View itemView) {
            super(itemView);

            tv_train_start = itemView.findViewById(R.id.tv_train_start);
            tv_trainInfo = itemView.findViewById(R.id.tv_trainInfo);
            tv_trainStationInfo = itemView.findViewById(R.id.tv_trainStationInfo);
            tv_train_des = itemView.findViewById(R.id.tv_train_des);
        }
    }

    private class RouteTotalHolder extends RecyclerView.ViewHolder {
        private TextView tv_totalRoute, tv_totalRouteInfo;
        public RouteTotalHolder(View itemView) {
            super(itemView);

            tv_totalRoute = itemView.findViewById(R.id.tv_totalRoute);
            tv_totalRouteInfo = itemView.findViewById(R.id.tv_totalRouteInfo);
        }
    }
}
