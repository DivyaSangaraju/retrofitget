package com.example.tudaapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPrefs(Context ctx){
        prefs = ctx.getSharedPreferences("Tuda", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.apply();
    }
    public void setfcm(String fcmtoken){

        editor.putString("fcmtoken",fcmtoken);

        editor.commit();
    }

    public String getfcm(){return prefs.getString("fcmtoken","");}

    public void ClearAll(){
        editor.clear();
        editor.apply();
    }
}
