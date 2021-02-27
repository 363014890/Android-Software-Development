package com.cs478.gzhang37.musiccentral;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.cs478.gzhang37.musicinfo.MusicInfo;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MusicCentralService extends IntentService {


    public MusicCentralService() {
        super("Downloader");
    }


    protected static final Integer SONG_NAMES = 0;
    protected static final Integer SINGER_NAMES = 1;
    protected static final Integer DOWNLOAD_URI = 2;
    private final String [] songNames = {
            "モビルスーツ戦",
            "梵高先生",
            "流川枫与苍井空",
            "我的世界因你而改变",
            "安和桥"
    };

    private final String [] singerNames = {
            "三枝成彰",
            "李志",
            "黑撒",
            "丢火车",
            "宋东野"
    };

    private final Uri[] songUri = {
            Uri.parse("https://drive.google.com/uc?export=download&id=1gOILK0cm6-a2hAHkd_fzRSuTBLC_E71S"),
            Uri.parse("https://drive.google.com/uc?export=download&id=1gNBVg319LSnFVsMrs3rJeTiwrBG-KuOs"),
            Uri.parse("https://drive.google.com/uc?export=download&id=1gM2IPYTl55OMEYtr-nT221MUZoxWb04A"),
            Uri.parse("https://drive.google.com/uc?export=download&id=1gIGJgpJ4w_K2LTZpGGprzn06b9-fQHdv"),
            Uri.parse("https://drive.google.com/uc?export=download&id=1gGI-sILI4okVHbtGRNd0S1hOtwB-8TkL")
    };

    private final Uri[] songPictures = {
            Uri.parse("https://drive.google.com/uc?export=download&id=1gaEFc3Ez6Ho0JUBmmEauUOA6CVcc8HVu"),
            Uri.parse("https://drive.google.com/uc?export=download&id=1gQLRHFyz1sYFbpP4fBHy8Z0zd4AiddVf"),
            Uri.parse("https://drive.google.com/uc?export=download&id=1ga5fbIse8nvj743D_e_Wt_hsI9Vp1WvC"),
            Uri.parse("https://drive.google.com/uc?export=download&id=1gQy4khy48rCJ2bOHKDiuZCWdAie-C_Qu"),
            Uri.parse("https://drive.google.com/uc?export=download&id=1gb8kRbMzgimoTH0N2ffglegdJMvOgKXs")
    };
    private MediaPlayer mPlayer;
    private Notification notification ;
    private static final int NOTIFICATION_ID = 1;
    private static String CHANNEL_ID = "Music player style" ;
    private Bitmap onePic;
    private String picLock = "lock";
    private Bitmap[] allPics = new Bitmap[5];
    private boolean handled = false;

    MusicInfo.Stub infoGetter = new MusicInfo.Stub() {

        @Override
        public String getSingerName(int id) {
            return singerNames[id];
        }

        @Override
        public String getSongUrl(int id) {
            return songUri[id].toString();
        }

        @Override
        public String getSongTitle(int id) {
            return songNames[id];
        }

        @Override
        public Bitmap getOneSongPicture() {
            synchronized (picLock){
                try{
                    if(!handled)
                        picLock.wait();
                    handled = false;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return onePic;
            }
        }

        @Override
        public Bitmap[] getAllSongPicture() {
            synchronized (picLock){
                try{
                    if(!handled)
                        picLock.wait();
                    handled = false;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return allPics;
            }
        }

        @Override
        public Map<Integer,String[]> getAllSongsInfo() {
            Map<Integer,String[]> allInfo = new HashMap<Integer, String[]>();
            String[] songs = new String[5];
            String[] singers = new String[5];
            String[] downloads = new String[5];
            for(int i = 0; i < songNames.length; i ++){
                songs[i] = getSongTitle(i);
                singers[i] = getSingerName(i);
                downloads[i] = getSongUrl(i);
            }
            allInfo.put(SONG_NAMES,songs);
            allInfo.put(SINGER_NAMES,singers);
            allInfo.put(DOWNLOAD_URI,downloads);
            return allInfo;
        }

        @Override
        public void play(int id) {
            if(null!=mPlayer){
                mPlayer.stop();
            }
            Intent i = new Intent(getApplicationContext(),MusicCentralService.class);
            i.putExtra("song",id);
            i.putExtra("play",1);
            startForegroundService(i);
        }

        @Override
        public void pause() {
            synchronized (mPlayer){
                if(null != mPlayer){
                    mPlayer.pause();
                }
            }
        }

        @Override
        public void stop() {
            synchronized (mPlayer){
                if(null != mPlayer){
                    mPlayer.stop();
                }
            }
        }
    };



    @Override
    public void onCreate(){
        super.onCreate();

        this.createNotificationChannel();

        /*final Intent notificationIntent = new Intent(getApplicationContext(), MusicCentralService.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this,0,
                notificationIntent,0);*/
        notification = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true).setContentTitle("Service Running")
                .setContentText("Click to Access MusicClient")
                //.setFullScreenIntent(pendingIntent, false)
                .build();

        startForeground(NOTIFICATION_ID,notification);
    }

    public void createNotificationChannel(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Music player notification";
            String description = "The channel for music player notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



    @Override
    public void onDestroy(){
        if (null != mPlayer) {

            mPlayer.stop();
            mPlayer.release();

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return infoGetter;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent.getIntExtra("download_pic",0)==1){
            synchronized (picLock){
                try{
                    System.out.println();
                    URL pic = new URL(songPictures[intent.getIntExtra("id",0)].toString());
                    onePic =  BitmapFactory.decodeStream((InputStream) pic.getContent());
                    handled = true;
                    picLock.notify();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }


        }
        else if(intent.getIntExtra("download_pic",0)==2){
            synchronized (picLock){
                try{
                    for(int i = 0; i<5;i++){
                        URL pic = new URL(songPictures[i].toString());
                        allPics[i] =  BitmapFactory.decodeStream((InputStream) pic.getContent());
                    }
                    handled = true;
                    picLock.notify();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        else if(intent.getIntExtra("play",0)== 1){
            int songID = intent.getIntExtra("song",0);
            mPlayer = MediaPlayer.create(this,songUri[songID]);
            if(null != mPlayer){
                mPlayer.setLooping(false);
                mPlayer.start();
            }
        }
    }
}
