package com.lahoriagency.cikolive.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences pref;
    private Context context;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "Ciko";

    public SessionManager(Context con) {
        context = con;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveIntValue(String key, int i) {
        editor.putInt(key, i);
        editor.apply();
    }



    public void saveFireBaseToken(String token) {
        editor.putString(Const.TOKEN, token);
        editor.apply();
    }

    public int getIntValue(String key) {
        return pref.getInt(key, -1);
    }

    public String getFireBaseToken() {
        String token = pref.getString(Const.TOKEN, "");
        if (!token.isEmpty()) {
            return token;
        }
        return null;
    }


    public void saveBooleanValue(String key, boolean b) {

        editor.putBoolean(key, b);
        editor.apply();
    }

    public Boolean getBooleanValue(String key) {

        return pref.getBoolean(key, false);

    }



    public void clear() {
        editor.clear();
        editor.apply();
    }


}
