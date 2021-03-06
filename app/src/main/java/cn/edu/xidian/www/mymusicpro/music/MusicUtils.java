package cn.edu.xidian.www.mymusicpro.music;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.edu.xidian.www.mymusicpro.settings.SettingDat;

/**
 * Created by chenyudong on 2017/5/7.
 */

public class MusicUtils {
    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */

    private static String TAG = "MusicUtils";

    private static List<SongInfo> local_music_list = new ArrayList<>();

    private static List<SongInfo> music_url_list = new ArrayList<>();




    public static List<SongInfo> getMusicData() {
        return local_music_list;
    }
    public static List<SongInfo> getMusicUrlList() {
        return music_url_list;
    }



    public static void SearchLocalMusic(Context context) {
        Log.i(TAG, "getMusicData: start.");
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            local_music_list.clear();
            while (cursor.moveToNext()) {
                SongInfo song = new SongInfo();

                song.song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

//                Log.i(TAG, "getMusicData: song:" + song.song + "singer:" + song.singer + "path:" + song.path
//                        + "duration:" + song.duration + "size:" + song.size);
                if (song.size > 1024 * SettingDat.file_size) {
                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                    if (song.song.contains("-")) {
                        String[] str = song.song.split("-");
                        song.singer = str[0];
                        song.song = str[1];
                    }
                    local_music_list.add(song);
                }
            }
            // 释放资源
            cursor.close();
        }
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }

    }

    public static void InitUrlMusic(List<SongInfo> songInfo) {
        Log.i(TAG, "InitUrlMusic: ");
        music_url_list = songInfo;
    }
}