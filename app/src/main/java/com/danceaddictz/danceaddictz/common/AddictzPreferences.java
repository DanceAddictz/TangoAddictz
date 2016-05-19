package com.danceaddictz.danceaddictz.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by abhrte on 14/2/16.
 */
public class AddictzPreferences {

    private static final String PREFERENCES_LAT = "lat";
    private static final String PREFERENCES_LNG = "lng";
    private static SharedPreferences sharedPreferences = null;

    public static void storeLocation(Context context, LatLng location) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREFERENCES_LAT, Double.doubleToRawLongBits(location.latitude));
        editor.putLong(PREFERENCES_LNG, Double.doubleToRawLongBits(location.longitude));
        Log.d(PREFERENCES_LAT, "Saving " + location.latitude);
        Log.d(PREFERENCES_LNG, "Saving " + location.longitude);
        editor.apply();
    }

    public static LatLng getLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Long lat = prefs.getLong(PREFERENCES_LAT, Long.MAX_VALUE);
        Long lng = prefs.getLong(PREFERENCES_LNG, Long.MAX_VALUE);
        if (lat != Long.MAX_VALUE && lng != Long.MAX_VALUE) {
            Double latDbl = Double.longBitsToDouble(lat);
            Double lngDbl = Double.longBitsToDouble(lng);
            Log.d(PREFERENCES_LAT, "Reading " + latDbl);
            Log.d(PREFERENCES_LNG, "Reading " + lngDbl);
            return new LatLng(latDbl, lngDbl);
        }
        return null;
    }

    public static SharedPreferences getSharedPrefernces(Context context) {
        if (context != null) {
            if (sharedPreferences == null) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            }
        }
        return sharedPreferences;
    }

    public static String getString(String key, Context context) {
        return getSharedPrefernces(context).getString(key, "");

    }

    public static boolean getBoolean(String key, Context context) {
        return getSharedPrefernces(context).getBoolean(key, false);
    }

    public static void putBoolean(String key, boolean value, Context context) {
        getSharedPrefernces(context).edit().putBoolean(key, value).apply();
    }

    public static void putString(String key, String value, Context context) {
        getSharedPrefernces(context).edit().putString(key, value).apply();

    }

    public static void putInt(String key, int value, Context context) {
        getSharedPrefernces(context).edit().putInt(key, value).apply();
    }

    public static int getInt(String key, Context context) {
        return getSharedPrefernces(context).getInt(key, 2);
    }

    public static void clearData(Context context) {
        getSharedPrefernces(context).edit().clear().apply();
    }

    public static void clearStringData(String key, Context context) {
        getSharedPrefernces(context).edit().remove(key).apply();
    }

    public static void removeData(Context context) {
        AddictzPreferences.putInt(AddictzConstants.ID, -1, context);
        AddictzPreferences.putInt(AddictzConstants.LOGIN_TYPE, -1, context);
        AddictzPreferences.putString(AddictzConstants.USER_NAME, "", context);
        AddictzPreferences.putString(AddictzConstants.EMAIL, "", context);
        AddictzPreferences.putString(AddictzConstants.AUTH_KEY, "", context);
        AddictzPreferences.putString(AddictzConstants.REST_KEY, "", context);
        AddictzPreferences.putString(AddictzConstants.FACEBOOK_ID, "", context);
        AddictzPreferences.putString(AddictzConstants.GOOGLE_ID, "", context);
        AddictzPreferences.putString(AddictzConstants.MOBILE, "", context);
        AddictzPreferences.putString(AddictzConstants.GENDER, "", context);

    }
}
