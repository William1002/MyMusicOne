package com.example.mymusicone.view;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.example.mymusicone.R;

public class MainActivity extends Activity {
	/**
	 * 双向滑动菜单布局
	 */
	private BidirSlidingLayout bidirSldingLayout;
	private MusicService musicService;
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			musicService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			musicService = ((MusicService.MyBinder) service).getService();
			System.out.println("getServiced");
		}
	};
	private ListView listView;
	private Button btnPlay, next, back, btnMode;
	private SeekBar seekBar;

	private Handler mHandler;
	private boolean isPlaying = false;
	private boolean isBinded = false;

	/**
	 * 顺序模式
	 */
	private final static String ORDER_MODE = "ORDER_MODE";
	/**
	 * 随机模式
	 */
	private final static String RANDOM_MODE = "RANDOM_MODE";
	/**
	 * 用于记录模式,默认是顺序模式
	 */
	public String MODE = ORDER_MODE;

	int playState, position, duration, current;
	List<Map<String, Object>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bidirSldingLayout = (BidirSlidingLayout) findViewById(R.id.bidir_sliding_layout);
		listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(new MyAdapter(this));
		btnPlay = (Button) findViewById(R.id.play);
		next = (Button) findViewById(R.id.next);
		back = (Button) findViewById(R.id.back);
		btnMode = (Button) findViewById(R.id.mode);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		list = MusicInfo.GetMusicData(this);

		bidirSldingLayout.setScrollEvent(listView);

		Intent intent = new Intent(MainActivity.this, MusicService.class);
		if (!isBinded) {
			bindService(intent, conn, BIND_AUTO_CREATE);
			isBinded = true;
		}

		mHandler = new Handler();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//isPlaying = true;
				MainActivity.this.position = position;
				musicService.setMODE(MODE);
				// 开始线程
				mHandler.post(seekBarThread);
				duration = (Integer) list.get(position).get("DURATION");
				seekBar.setMax(100);
				Intent intent = new Intent(MainActivity.this,
						MusicService.class);
				intent.putExtra("STATE", "PLAY");
				intent.putExtra("CURRENT", 0);
				intent.putExtra("POSITION", position);
				startService(intent);

			}
		});
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					musicService.seekTo((progress * musicService.getDuration()) / 100);

				}
			}
		});

		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPlaying) {
					Log.v("tag","playing....");
					musicService.musicStart();
					isPlaying = false;
					Intent intent = new Intent(MainActivity.this,
							MusicService.class);
					intent.putExtra("STATE", "PLAY");
					startService(intent);
					btnPlay.setText("暂停");
					mHandler.post(seekBarThread);

				} else {
					musicService.musicPause();
					isPlaying = true;
					Intent intent = new Intent(MainActivity.this,
							MusicService.class);
					intent.putExtra("STATE", "PAUSE");
					startService(intent);
					btnPlay.setText("播放");
					mHandler.removeCallbacks(seekBarThread);
				}
			}
		});
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						MusicService.class);
				intent.putExtra("STATE", "PLAY");
				if (position < list.size()) {
					intent.putExtra("POSITION", ++position);
				}
				startService(intent);
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						MusicService.class);
				intent.putExtra("STATE", "PLAY");
				if (position > 0) {
					intent.putExtra("POSITION", --position);
				}
				startService(intent);
			}
		});
		btnMode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MODE == ORDER_MODE) {
					Log.v("tag","ORDER_MODE");
					Intent intent = new Intent(MainActivity.this,
							MusicService.class);
					intent.putExtra("STATE", "PLAY");
					musicService.setMODE(MODE);
					MODE = RANDOM_MODE;
					btnMode.setText("转为顺序");
				} else if (MODE == RANDOM_MODE) {
					Log.v("tag","RANDOM_MODE");
					Intent intent = new Intent(MainActivity.this,
							MusicService.class);
					intent.putExtra("STATE", "PLAY");
					musicService.setMODE(MODE);
					MODE = ORDER_MODE;
					btnMode.setText("转为随机");
					
				}

			}
		});
	}

	// 此线程用来更新进度条的
	Runnable seekBarThread = new Runnable() {
		@Override
		public void run() {

			seekBar.setProgress((musicService.getCurrentPosition() * 100)
					/ musicService.getDuration());
			mHandler.postDelayed(seekBarThread, 500);
		}
	};
	

}
