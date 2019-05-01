package com.example.lmqmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.xh.customnotification.action.main";
        public static String INIT_ACTION = "com.xh.customnotification.action.init";
        public static String PREV_ACTION = "com.xh.customnotification.action.prev";
        public static String PLAY_ACTION = "com.xh.customnotification.action.play";
        public static String NEXT_ACTION = "com.xh.customnotification.action.next";
        public static String STARTFOREGROUND_ACTION = "com.xh.customnotification.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.xh.customnotification.action.stopforeground";
        public static String PAUSE_ACTION = "com.xh.customnotification.action.PAUSE_ACTION";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
