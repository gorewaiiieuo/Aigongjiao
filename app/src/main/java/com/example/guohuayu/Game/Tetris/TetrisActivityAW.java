package com.example.guohuayu.Game.Tetris;

import com.example.guohuayu.Game.Tetris.GameUtils.NextBlockView;
import com.example.a20182.R;
import com.example.guohuayu.Game.Tetris.GameUtils.BlockUnit;
import com.example.guohuayu.Game.Tetris.GameUtils.TetrisViewAW;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/*
*  FLAPPYBIRD游戏启动界面
* */

public class TetrisActivityAW extends Activity {
    private NextBlockView nextBlockView;
    private TetrisViewAW tetrisViewAW;
    private TextView gameStatusTip;
    public TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris_activity_aw);
        nextBlockView = (NextBlockView) findViewById(R.id.nextBlockView1);
        tetrisViewAW = (TetrisViewAW) findViewById(R.id.tetrisViewAW1);
        tetrisViewAW.setFather(this);
        gameStatusTip = (TextView) findViewById(R.id.game_staus_tip);
        score = (TextView) findViewById(R.id.score);
    }

    public void setNextBlockView(List<BlockUnit> blockUnits, int div_x) {
        nextBlockView.setBlockUnits(blockUnits, div_x);
    }

    /**
     * 开始游戏
     *
     * @param view
     */
    public void startGame(View view) {
        tetrisViewAW.startGame();
        gameStatusTip.setText("游戏运行中");
    }

    /**
     * 暂停游戏
     */
    public void pauseGame(View view) {
        tetrisViewAW.pauseGame();
        gameStatusTip.setText("游戏已暂停");
    }

    /**
     * 继续游戏
     */
    public void continueGame(View view) {
        tetrisViewAW.continueGame();
        gameStatusTip.setText("游戏运行中");
    }

    /**
     * 停止游戏
     */
    public void stopGame(View view) {
        tetrisViewAW.stopGame();
        score.setText("" + 0);
        gameStatusTip.setText("游戏已停止");
    }

    /**
     * 向左滑动
     */
    public void toLeft(View view) {
        tetrisViewAW.toLeft();
    }

    /**
     * 向右滑动
     */
    public void toRight(View view) {
        tetrisViewAW.toRight();
    }

    /**
     * 向右滑动
     */
    public void toRoute(View view) {
        tetrisViewAW.route();
    }

    /**
     * 加速下落
     * */
    public void toFall(View view){tetrisViewAW.fall();}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tetrisViewAW != null) {
            tetrisViewAW.stopGame();
        }
    }
}
//if (Math.abs(snake.x - snakeBuf2.x) < 2&&Math.abs(snake.y - snakeBuf2.y) < 2) {
//	return false;
//}
