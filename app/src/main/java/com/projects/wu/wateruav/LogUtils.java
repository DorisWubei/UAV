package com.projects.wu.wateruav;

import android.util.Log;

public class LogUtils {

    private static final boolean isEnableLog = true;

    public final static void error(String tag, String strContent) {
        if (isEnableLog) {
            Log.e(tag, strContent);
        }
    }

    public final static void info(String tag, String strContent){
        if(isEnableLog){
            Log.i(tag, strContent);
        }
    }


    public final static void warning(String tag, String strContent){
        if(isEnableLog){
            Log.w(tag, strContent);
        }
    }


    public final static void verbose(String tag, String strContent){
        if(isEnableLog){
            Log.v(tag, strContent);
        }
    }

    public final static void debug(String tag, String strContent){
        if (isEnableLog) {
            Log.d(tag, strContent);
        }
    }
}