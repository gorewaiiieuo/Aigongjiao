package com.example.guohuayu.Game.FlappyBird.config;

public class Config {

	public final static int TO_MAIN_VIEW = 1;
	public final static int END_GAME = 0;
	
	public final static int LOADING_GAME_INTERVAL = 2000;
	
	public final static int SPEED = (int) (Constants.SCREEN_WIDTH * 5 / 480);
	
	public final static float COLUMN_Y_GAP = Constants.SCREEN_HEIGHT * 173 / 854;
	public final static float COLUMN_X_GAP = Constants.SCREEN_WIDTH / 2 + 50;
	
	public final static double v0 = Constants.SCREEN_HEIGHT * 23 / 854;
	public final static double g = Constants.SCREEN_HEIGHT * 3 / 854;
	public final static double t = 0.6;//ʱ����
	
}
