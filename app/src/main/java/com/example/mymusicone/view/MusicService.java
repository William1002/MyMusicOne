package com.example.mymusicone.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
	MediaPlayer mediaPlayer = new MediaPlayer();
	List<Map<String, Object>> list;
	// 接收哪一个音乐被点击
	int position, current, duration;
	// 音乐的地址
	String url;

	State state;

	String MODE;

	public void setMODE(String MODE) {
		this.MODE = MODE;
	}

	public class MyBinder extends Binder {
		MusicService getService() {
			return MusicService.this;
		}

	}

	MyBinder myBinder = new MyBinder();

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("onBind");
		return myBinder;
	}

	public boolean onUnbind(Intent intent) {
		System.out.println("onUnbind");
		return false;

	}

	public void play(String url) {

		mediaPlayer.reset();
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		mediaPlayer.pause();
	}

	/**
	 * 返回总时间
	 * 
	 * @return
	 */
	public int getDuration() {
		return mediaPlayer.getDuration();
	}

	/**
	 * 返回当前进度
	 * 
	 * @return
	 */
	public int getCurrentPosition() {
		return mediaPlayer.getCurrentPosition();
	}

	/**
	 * 调整进度
	 * 
	 * @param current
	 */
	public void seekTo(int current) {
		mediaPlayer.seekTo(current);
	}

	/**
	 * 音乐播放
	 */
	public void musicStart() {
		mediaPlayer.start();
	}

	/**
	 * 音乐暂停
	 */
	public void musicPause() {
		mediaPlayer.pause();
	}

	@Override
	public void onCreate() {

		super.onCreate();
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		list = MusicInfo.GetMusicData(this);
		String state = intent.getStringExtra("STATE");

		// TODO 应用的暂停和播放键没有获得position，所以每次点击播放时position总是为0
		position = intent.getIntExtra("POSITION", 0);

		url = list.get(position).get("URL").toString();
		playState(state);
		playMode(MODE);

	}

	public void playState(String state) {
		if (state.equals("PLAY")) {
			play(url);
		} else if (state.equals("PAUSE")) {
			pause();
		}
	}

	public void playMode(final String MODE) {
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// 随机
				if (MODE.equals("RANDOM_MODE")) {
					Random random = new Random();
					position = random.nextInt(list.size() - 1);
					play(list.get(position).get("URL").toString());

					System.out.println("随机");
					// 顺序
				} else if (MODE.equals("ORDER_MODE")) {
					if (position == list.size()) {
						position = 0;
						play(list.get(position).get("URL").toString());

						System.out.println("顺序，position=max");
					} else if (position < list.size()) {
						++position;
						play(list.get(position).get("URL").toString());
						System.out.println("顺序，position!=max");
					}
				}
			}
		});
	}

	public void reStart() {
		mediaPlayer.start();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

}
