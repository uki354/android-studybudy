package com.example.studdybuddy;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;

public class JwtTokenManager {

    private static SharedPreferences prefs;
    public static final String JWT_TOKEN_KEY = "token";

    public static String getTokenFromDevice(Activity activity){
        setPrefs(activity);
        return prefs.getString(JWT_TOKEN_KEY, "");
    }

    public static void storeTokenToDevice(Activity activity){
        setPrefs(activity);
        prefs.edit().putString(JWT_TOKEN_KEY, "").apply();
    }

    private static void setPrefs(Activity activity){
        prefs = activity.getSharedPreferences("myPrefs", MODE_PRIVATE);
    }
}
