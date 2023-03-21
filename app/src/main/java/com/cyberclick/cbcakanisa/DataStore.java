package com.cyberclick.cbcakanisa;

import android.content.Context;
import android.content.SharedPreferences;


public class DataStore {
    private static DataStore mInstance;
    private static Context mCtx;
    private static final String SHARED_PREF_NAME = "mysharedpref7";
    private static final String KEY_LAST_POST = "last_post_id";

    private DataStore(Context context) {
        mCtx = context;
    }

    public static synchronized DataStore getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataStore(context);
        }
        return mInstance;
    }

    public boolean setLastPostId(Integer lastPostId) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_LAST_POST, lastPostId);
        editor.apply();

        return true;
    }

    public int getLastPostId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_LAST_POST, 0);
    }

}
