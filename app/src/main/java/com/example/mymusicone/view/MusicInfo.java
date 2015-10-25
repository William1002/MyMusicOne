package com.example.mymusicone.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class MusicInfo {
	public static List<Map<String, Object>> GetMusicData(Context ctx) {
		List<Map<String, Object>> musicdata = new ArrayList<Map<String, Object>>();
		// 获得一个一个ContentResolver实例
		/**
		 * file, sqlite3, Preferences, ContectResolver与
		 * ContentProvider前三种数据操作方式都只是针对本应用内数据，
		 * 程序不能通过这三种方法去操作别的应用内的数据。
		 * 
		 * android中提供ContectResolver与
		 * ContentProvider来操作别的应用程序的数据。
		 * 使用方式:
		 * 一个应用实现ContentProvider来提供内容给别的应用来操作，
		 * 一个应用通过ContentResolver来操作别的应用数据，当然在自己的应用中也可以。
		 */
		ContentResolver mResolver = ctx.getContentResolver();
		// 查询
		Cursor cursor = mResolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,// 哪一个库名+表表名
				null,// 那一列
				null,// 查询条件
				null,// 与第二个参数相关，只有第二个参数有“？”时才不为null
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);// 排序
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				// 歌曲ID：MediaStore.Audio.Media._ID
				int id = cursor.getInt(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));

				// 歌曲 的名称：MediaStore.Audio.Media.TITL
				String tilte = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

				// 歌曲的专辑名：MediaStore.Audio.Media.ALBUM
				String album = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));

				// 歌曲的歌手名： MediaStore.Audio.Media.ARTIST
				String artist = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

				// 歌曲文件的全路径 ：MediaStore.Audio.Media.DATA
				String url = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

				// 歌曲文件的名称：MediaStroe.Audio.Media.DISPLAY_NAME

				String display_name = cursor
						.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));

				// 歌曲文件的发行日期：MediaStore.Audio.Media.YEAR
				String year = cursor.getString(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));

				// 歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
				// int duration = cursor
				// .getInt(cursor
				// .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

				int duration = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

				// 歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
				float size = cursor.getLong(cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
		
				if (url.endsWith(".mp3") || url.endsWith(".MP3")) {
					Map<String, Object> map = new HashMap<String, Object>();
					// map.put("ID", id);
					map.put("TITLE", tilte);
					map.put("ARTIST", artist);
					map.put("DURATION", duration);
					// map.put("display_name", display_name);
					// map.put("YEAR", year);
					map.put("URL", url);
					// map.put("ALBUM", album);
					// map.put("SIZE", size);
					musicdata.add(map);
				}
			}
		}
		return musicdata;
	}
}
