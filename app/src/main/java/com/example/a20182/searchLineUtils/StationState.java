package com.example.a20182.searchLineUtils;

public class StationState {
    private String title;
    private int state;
    private boolean isfocus = false;

    public StationState(String title,int state){
        this.title = title;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setIsfocus(boolean isfocus){this.isfocus = isfocus;}

    public boolean getIsfocus(){return isfocus;}
}
