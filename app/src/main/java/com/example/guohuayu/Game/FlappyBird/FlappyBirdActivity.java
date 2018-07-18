package com.example.guohuayu.Game.FlappyBird;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.example.guohuayu.Game.FlappyBird.config.Config;
import com.example.guohuayu.Game.FlappyBird.util.SoundPlayer;
import com.example.guohuayu.Game.FlappyBird.view.LoadingView;
import com.example.guohuayu.Game.FlappyBird.view.MainView;

public class FlappyBirdActivity extends Activity {

    private LoadingView loadingView;
    private MainView mainView;

    private SoundPlayer soundPlayer;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            if(msg.what == Config.TO_MAIN_VIEW) {
                toMainView();
            }
            if(msg.what == Config.END_GAME) {
                endGame();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.soundPlayer = new SoundPlayer(this);
        this.soundPlayer.initSounds();
        this.loadingView = new LoadingView(this, soundPlayer);
        this.setContentView(loadingView);
    }

    public void toMainView() {
        if(this.mainView == null) {
            this.mainView = new MainView(this, soundPlayer);
        } else {
            this.mainView = null;
            this.mainView = new MainView(this, soundPlayer);
        }
        this.setContentView(this.mainView);
        this.loadingView = null;
    }

    public void endGame() {
        if(this.mainView != null) {
            this.mainView.setThreadFlag(false);
        }
        if(this.loadingView != null) {
            this.loadingView.setThreadFlag(false);
        }
        this.finish();
    }

    public Handler getHandler() {
        return this.handler;
    }
}