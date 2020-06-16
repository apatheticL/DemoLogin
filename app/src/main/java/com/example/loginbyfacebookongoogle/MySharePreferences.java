package com.example.loginbyfacebookongoogle;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.Edits;

public class MySharePreferences {
    private static MySharePreferences instance;
    private SharedPreferences sharedPreferences=null;
    private SharedPreferences.Editor editor = null;
    public static MySharePreferences getInstance(Context context){
        if (instance==null){
            synchronized (MySharePreferences.class){
                if (instance==null){
                    instance = new MySharePreferences(context);
                }
            }
        }
        return instance;
    }
    public MySharePreferences(Context context){
        sharedPreferences = context.getSharedPreferences(context.getPackageName(),context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public MySharePreferences putInt( String key, int value){
        try {
            editor.putInt(key,value);
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
        return this;
    }
    public int getInt(String key){
        try{
            return sharedPreferences.getInt(key,0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
//    public MySharePreferences putJson(String key, Object value){
//        try{
//
//        }
//    }
}
