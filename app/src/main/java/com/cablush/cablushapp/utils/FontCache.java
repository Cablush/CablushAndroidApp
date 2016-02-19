package com.cablush.cablushapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by oscar on 28/12/15.
 */
public class FontCache {

    private static final String TAG = FontCache.class.getSimpleName();

    private static Hashtable<String, Typeface> fontCache = new Hashtable<>();

    public static Typeface get(Context context, String name) {
        Typeface tf = fontCache.get(name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            } catch (Exception ex) {
                Log.e(TAG, "Error creating Font from Asset.", ex);
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }

    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     * @param context to work with assets
     * @param defaultFontNameToOverride for example "monospace"
     * @param customFontFileNameInAssets file name of the font from assets
     */
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = get(context, customFontFileNameInAssets);

            replaceFont(defaultFontNameToOverride, customFontTypeface);
        } catch (Exception ex) {
            Log.e(TAG, "Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride, ex);
        }
    }

    private static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Map<String, Typeface> newMap = new HashMap<>();
            newMap.put("sans-serif", newTypeface);
            try {
                final Field staticField = Typeface.class.getDeclaredField("sSystemFontMap");
                staticField.setAccessible(true);
                staticField.set(null, newMap);
            } catch (NoSuchFieldException ex) {
                Log.e(TAG, "Error replacing font.", ex);
            } catch (IllegalAccessException ex) {
                Log.e(TAG, "Error replacing font.", ex);
            }
        } else {
            try {
                final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
                staticField.setAccessible(true);
                staticField.set(null, newTypeface);
            } catch (NoSuchFieldException ex) {
                Log.e(TAG, "Error replacing font.", ex);
            } catch (IllegalAccessException ex) {
                Log.e(TAG, "Error replacing font.", ex);
            }
        }
    }

}
