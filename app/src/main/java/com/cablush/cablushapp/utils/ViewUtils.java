package com.cablush.cablushapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cablush.cablushapp.R;

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

    /**
     * Inflate the "custom_title" layout.
     */
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

    /**
     * Check if the editText is not empty, and "mark" if it is empty.
     */
    public static boolean checkNotEmpty(Context context, EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError(context.getString(R.string.msg_required));
            return false;
        }
        return true;
    }

    /**
     * Check if at least one checkbox is checked, and "mark" the textView if not.
     */
    public static boolean checkOneChecked(Context context, TextView textView,
                                          CheckBox... checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            if (!checkBox.isChecked()) {
                textView.setError(context.getString(R.string.msg_select_one));
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a valid option is selected, and 'mark' the textView if not.
     */
    public static boolean checkSelected(Context context, TextView textView, AdapterView adapter) {
        if (getSelectedItem(context, adapter).isEmpty()) {
            textView.setError(context.getString(R.string.msg_select_one));
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
