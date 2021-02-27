// MusicInfo.aidl
package com.cs478.gzhang37.musicinfo;

// Declare any non-default types here with import statements

interface MusicInfo {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getSingerName(int id);
    String getSongUrl(int id);
    String getSongTitle(int id);
    Bitmap getOneSongPicture();
    Bitmap[] getAllSongPicture();
    Map getAllSongsInfo();
    void play(int id);
    void pause();
    void stop();


}
