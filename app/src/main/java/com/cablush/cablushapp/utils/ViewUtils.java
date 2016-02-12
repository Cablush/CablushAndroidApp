package com.cablush.cablushapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cablush.cablushapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by oscar on 26/12/15.
 */
public class ViewUtils {

    /**
     * Get a message from Strings with parameters by their IDs
     */
    public static String getMessage(Context context, int resId, int... argsResIds) {
        Object[] args = new Object[argsResIds.length];
        for (int i = 0; i < argsResIds.length; i++) {
            args[i] = context.getString(argsResIds[i]);
        }
        return context.getString(resId, args);
    }

    public static View getCustomTitleView(LayoutInflater inflater, String title, int iconId) {
        View view = inflater.inflate(R.layout.custom_title, null);
        ImageView iconImageView = (ImageView) view.findViewById(R.id.icon);
        iconImageView.setImageResource(iconId);
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setText(title.toUpperCase());
        return view;
    }

    /**
     * Get the selected item from an AdapterView, or empty if the item is the "empty selection" item.
     */
    public static String getSelectedItem(Context context, AdapterView adapter) {
        return adapter.getSelectedItem().equals(context.getString(R.string.empty_selection))
                ? ""
                : adapter.getSelectedItem().toString().toLowerCase();
    }

    public static String readRawTextFile(Context context, int id) {
        InputStream inputStream = context.getResources().openRawResource(id);

        InputStreamReader in = new InputStreamReader(inputStream);
        BufferedReader buf = new BufferedReader(in);

        String line;

        StringBuilder text = new StringBuilder();
        try {
            while (( line = buf.readLine()) != null) text.append(line);
        } catch (IOException e) {
            return null;
        }

        return text.toString();
    }

    public static boolean checkNotEmpty(Context context, EditText editText){
        if(editText.getText().toString().isEmpty()){
            editText.setError(context.getString(R.string.favor_preencher));
            return false;
        }
        return true;
    }

    /**
     * Make ViewUtils a utility class by preventing instantiation.
     */
    private ViewUtils() {
        throw new AssertionError();
    }
}
