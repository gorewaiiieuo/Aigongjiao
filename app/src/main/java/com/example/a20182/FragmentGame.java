package com.example.a20182;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.a20182.Game.FlappyBird.FlappyBirdActivity;
import com.example.a20182.Game.Tetris.TetrisActivityAW;


/**
 * A simple {@link Fragment} subclass.
 * 小游戏界面，点击图标进入游戏
 */
public class FragmentGame extends Fragment implements View.OnClickListener {
    private ImageView startTetrisIv, startFlappybirdIv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        startTetrisIv = view.findViewById(R.id.startTetris_iv);
        startFlappybirdIv = view.findViewById(R.id.startFlappybird_iv);

        startTetrisIv.setOnClickListener(this);
        startFlappybirdIv.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startTetris_iv:
                Intent tetirsIntent = new Intent(getContext().getApplicationContext(), TetrisActivityAW.class);
                startActivity(tetirsIntent);
                break;
            case R.id.startFlappybird_iv:
                Intent flappyIntent = new Intent(getContext().getApplicationContext(), FlappyBirdActivity.class);
                startActivity(flappyIntent);
                break;
        }
    }
}
