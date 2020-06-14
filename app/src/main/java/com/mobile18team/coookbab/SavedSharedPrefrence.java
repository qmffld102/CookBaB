package com.mobile18team.coookbab;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SavedSharedPrefrence {static final String PREF_USER_NAME = "username";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
}
