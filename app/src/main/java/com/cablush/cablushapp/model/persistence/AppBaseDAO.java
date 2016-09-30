package com.cablush.cablushapp.model.persistence;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.cablush.cablushapp.CablushApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by oscar on 12/12/15.
 */
public abstract class AppBaseDAO {

    protected final String TAG = getClass().getName();

    protected CablushDBHelper dbHelper;

    protected Context context = CablushApp.getInstance().getApplicationContext();

    interface IColumns <T extends Enum<T>> {
        String getColumnName();
        String getColumnNameWithTable();
        String getColumnAlias();
        String getColumnDefinition();
        Boolean getPrimaryKey();
    }

    static String createTable(String table, Class<? extends IColumns<?>> columnsClass) {
        StringBuilder primaryKey = new StringBuilder();
        primaryKey.append(" PRIMARY KEY (");
        StringBuilder createTable = new StringBuilder();
        createTable.append("CREATE TABLE ").append(table).append(" (");
        for(IColumns column : columnsClass.getEnumConstants()) {
            if (column.getPrimaryKey()) {
                primaryKey.append(column.getColumnName()).append(", ");
            }
            createTable.append(column.getColumnDefinition()).append(", ");
        }
        primaryKey.replace(primaryKey.lastIndexOf(","), primaryKey.length(), ")");
        createTable.append(primaryKey.toString()).append(");");
        return createTable.toString();
    }

    static String[] getColumnsProjectionWithAlias(Class<? extends IColumns<?>>... columnsClasses) {
        List<String> projColumns = new ArrayList<>();
        for (Class<? extends IColumns<?>> iColumnClass : Arrays.asList(columnsClasses)) {
            for (IColumns<?> column : iColumnClass.getEnumConstants()) {
                projColumns.add(column.getColumnNameWithTable() + " AS " + column.getColumnAlias());
            }
        }
        return projColumns.toArray(new String[projColumns.size()]);
    }

    static String getGroupBy(Class<? extends IColumns<?>>... columnsClasses) {
        StringBuilder groupBy = new StringBuilder();
        for (Class<? extends IColumns<?>> iColumnClass : Arrays.asList(columnsClasses)) {
            for (IColumns<?> column : iColumnClass.getEnumConstants()) {
                groupBy.append(column.getColumnAlias()).append(", ");
            }
        }
        groupBy.replace(groupBy.lastIndexOf(","), groupBy.length(), "");
        return groupBy.toString();
    }

    /**
     * Read a column from a cursor returning a object of class.
     *
     * @param cursor The cursor to be read.
     * @param column The name of column.
     * @param <T> The class to be returned.
     * @return The object read or null if something goes wrong.
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
        } catch (Exception ex) {
            Log.e(TAG, "Exception reading the cursor.", ex);
        }
        return null;
    }

    /**
     * Read an enum from a cursor returning a enum instance of the class.
     *
     * @param cursor The cursor to be read.
     * @param column The name of column.
     * @param enumtype The type of the enum.
     * @return The enum instance read or null if something goes wrong.
     */
    protected <T extends  Enum<T>> T readEnum(Cursor cursor, String column, Class<T> enumtype) {
        String name = readCursor(cursor, column, String.class);
        return name != null ? Enum.valueOf(enumtype, name): null;
    }
}
