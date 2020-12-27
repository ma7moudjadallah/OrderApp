package com.mahmoud.orderfirebaseapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;



import java.util.Locale;

public class SharedPreferenceApp {
    SharedPreferences preferences;
    Editor editor =null;

    public SharedPreferenceApp(Context context) {
        this.preferences = context.getSharedPreferences("mudyaf",0);
        editor = preferences.edit();
    }

    public static SharedPreferenceApp getInstance(Context context) {
        return new SharedPreferenceApp(context);
    }

    public void saveText(String Key, String Value) {
        this.editor.putString(Key, Value).apply();
    }

    public void saveNumber(String Key, int Value) {
        this.editor.putInt(Key, Value).apply();
    }
    public void saveNumberLong(String Key, long Value) {
        this.editor.putLong(Key, Value).apply();
    }

    public void saveBoolean(String Key, boolean Value) {
        this.editor.putBoolean(Key, Value).apply();
    }
    public void clear() {
        this.editor.clear().apply();
    }

    public String getText(String Key, String defaultValue) {

        return this.preferences.getString(Key, defaultValue);
    }

    public int getNumber(String Key, int defaultValue) {
        return this.preferences.getInt(Key, defaultValue);
    }
    public long getNumberLong(String Key, long defaultValue) {
        return this.preferences.getLong(Key, defaultValue);
    }

    public boolean getBoolean(String Key, boolean defaultValue) {
        return this.preferences.getBoolean(Key, defaultValue);
    }
}
