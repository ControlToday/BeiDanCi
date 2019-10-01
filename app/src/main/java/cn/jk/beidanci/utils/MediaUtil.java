package cn.jk.beidanci.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;

import cn.jk.beidanci.InitApplication;

/**
 * Created by Administrator on 2017/7/11.
 */

public class MediaUtil {
    static MediaPlayer mediaPlayer = new MediaPlayer();

    public static boolean display(String url, final Context context) {
        HttpProxyCacheServer proxy = ((InitApplication) context.getApplicationContext()).getProxy(context);
        final String proxyUrl = proxy.getProxyUrl(url);
        if (!proxy.isCached(proxyUrl)) {
            if (!NetWorkUtil.isOnline(context)) {
                return false;
            }
        }
        mediaPlayer.reset();
        try {


            mediaPlayer.setDataSource(proxyUrl);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer player) {
                    player.start();

                }
            });


        } catch (Exception e) {
            Log.e("error", e.toString());
            return false;
        }
        return true;
    }

}
