package com.aries.template.xiaoyu.dapinsocket;

import android.content.Context;
import android.content.SharedPreferences;


public class AddressSettingSharedPreference {
    private static SharedPreferences shared;
    public final static String WARNING_CONF = "garea_socket_demo";

    private final static String TAG = "AddressSettingSharedPreference";
    public final static String ADDRESS = "address";
    public final static String IP = "ip";



    public static String getAddrs(Context context, String key) {
        if (shared == null) {
            shared = context.getSharedPreferences(WARNING_CONF, Context.MODE_PRIVATE);
        }
        return shared.getString(key, "");
    }

    public static String get(Context context, String key, String defValue) {
        if (shared == null) {
            shared = context.getSharedPreferences(WARNING_CONF, Context.MODE_PRIVATE);
        }
        return shared.getString(key, defValue);
    }

    public static void setAddrs(Context context, String key, String addrs) {
        try{
            if (shared == null) {
                shared = context.getSharedPreferences(WARNING_CONF, Context.MODE_PRIVATE);
            }
            shared.edit().putString(key, addrs).commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
