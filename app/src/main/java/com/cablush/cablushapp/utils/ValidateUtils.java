package com.cablush.cablushapp.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.cablush.cablushapp.R;

import java.util.Collection;
import java.util.Date;

/**
 * Created by oscar on 14/02/16.
 */
public class ValidateUtils {

    /**
     * Check if a string is empty.
     */
    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Check if a string is not blank.
     */
    public static boolean isNotBlank(String string) {
        return string != null && !string.trim().isEmpty();
    }

    /**
     * Check if a date is not empty.
     */
    public static boolean isNotEmpty(Date date) {
        return date != null && date.getTime() > 0;
    }

    /**
     * Check if a collection is not empty.
     */
    public static boolean isNotEmpty(Collection collection) {
        return collection != null || !collection.isEmpty();
    }

    /**
     * Check if a password is valid.
     */
    public static boolean isValidPassword(String password) {
        return isNotBlank(password) && password.length() >= 6;
    }

    /**
     * Check if a email is valid.
     */
    public static boolean isValidEmail(String email, boolean required) {
        if (required) {
            return !isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return isEmpty(email) || Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Check if a web url is valid.
     */
    public static boolean isValidWebUrl(String webUrl, boolean required) {
        if (required) {
            return !isEmpty(webUrl) && Patterns.WEB_URL.matcher(webUrl).matches();
        }
        return isEmpty(webUrl) || Patterns.WEB_URL.matcher(webUrl).matches();
    }

    /**
     * Check if a phone number is valid.
     */
    public static boolean isValidPhoneNumber(String phoneNumber, boolean required) {
        if (required) {
            return !isEmpty(phoneNumber) && Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return isEmpty(phoneNumber) || Patterns.PHONE.matcher(phoneNumber).matches();
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
        if (ViewUtils.getSelectedItem(context, adapter).isEmpty()) {
            textView.setError(context.getString(R.string.msg_select_one));
            return false;
        }
        return true;
    }

    public static abstract class TextWatcherValidator implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public abstract void afterTextChanged(Editable s);
    }

}
