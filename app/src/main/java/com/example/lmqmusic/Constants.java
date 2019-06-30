package com.example.lmqmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.customnotification.action.main";
        public static String INIT_ACTION = "com.customnotification.action.init";
        public static String PREV_ACTION = "com.customnotification.action.prev";
        public static String PLAY_ACTION = "com.customnotification.action.play";
        public static String NEXT_ACTION = "com.customnotification.action.next";
        public static String STARTFOREGROUND_ACTION = "com.customnotification.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.customnotification.action.stopforeground";
        public static String PAUSE_ACTION = "com.customnotification.action.PAUSE_ACTION";

    }

    public interface Extra{
        public static String FAVORITE = "FAVORITE";
        public static String PLAYLIST = "PLAYLIST";
    }


    public static int NOTIFICATION_ID = 101;

}
