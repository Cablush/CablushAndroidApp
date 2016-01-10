package com.cablush.cablushapp.model.persistence;

import android.database.Cursor;
import android.util.Log;

import java.util.Date;

/**
 * Created by oscar on 12/12/15.
 */
public abstract class AppBaseDAO {

    protected final String TAG = getClass().getName();

    protected CablushDBHelper dbHelper;

    /**
     *
     * @param cursor
     * @param column
     * @param <T>
     * @return
     */
    protected <T> T readCursor(Cursor cursor, String column, Class<T> clazz) {
        try {
            int index = cursor.getColumnIndexOrThrow(column);

            T objRet = null;
            if (cursor.getType(index) == Cursor.FIELD_TYPE_NULL) {
                objRet = null;
            } else if (clazz.equals(Long.class)) {
                objRet = (T) Long.valueOf(cursor.getLong(index));
            } else if (clazz.equals(Integer.class)) {
                objRet = (T) Integer.valueOf(cursor.getInt(index));
            } else if (clazz.equals(Double.class)) {
                objRet = (T) Double.valueOf(cursor.getDouble(index));
            } else if (clazz.equals(String.class)) {
                objRet = (T) cursor.getString(index);
            } else if (clazz.equals(Boolean.class)) {
                objRet = (T) Boolean.valueOf(cursor.getInt(index) == 1 ? true : false);
            } else if (clazz.equals(Date.class)) {
                Long time = Long.valueOf(cursor.getLong(index));
                objRet = (T) new Date(time);
            }
            return objRet;
        } catch (Exception e) {
            Log.e(TAG, "Exception on readCursor()", e);
        }
        return null;
    }

    /**
     *
     * @param cursor
     * @param column
     * @param enumtype
     * @param <T>
     * @return
     */
    protected <T extends  Enum<T>> T readEnum(Cursor cursor, String column, Class<T> enumtype) {
        String name = readCursor(cursor, column, String.class);
        return name != null ? Enum.valueOf(enumtype, name): null;
    }
}
