package com.example.guohuayu.Game.FlappyBird.util;

import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

import com.example.guohuayu.Game.FlappyBird.FlappyBirdActivity;
import com.example.a20182.R;

public class SoundPlayer {

	private SoundPool soundPool;
	private FlappyBirdActivity mainActivity;
	private HashMap<Integer, Integer> map;
	
	public SoundPlayer(FlappyBirdActivity mainActivity) {
		this.mainActivity = mainActivity;
		map = new HashMap<Integer, Integer>();
		soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
	}
	
	public void initSounds() {
		map.put(1, soundPool.load(mainActivity, R.raw.flappy, 1));//����
		map.put(2, soundPool.load(mainActivity, R.raw.pass, 1));//ͨ������
		map.put(3, soundPool.load(mainActivity, R.raw.hit, 1));//ײ��
		map.put(4, soundPool.load(mainActivity, R.raw.die, 1));//����
		map.put(5, soundPool.load(mainActivity, R.raw.swooshing, 1));//�л�
	}
	
	public void playSound(int sound, int loop) {
		soundPool.play(sound, 1, 1, 1, loop, 1.0f);
	}
	
}
