package com.cablush.cablushapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.model.domain.Localizavel;
import com.cablush.cablushapp.model.domain.Loja;
import com.cablush.cablushapp.model.domain.Pista;

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
     * Get the codigo estado by its position in the string-array.
     */
    public static String getCodigoEstado(Context context, int position) {
        return context.getResources().getStringArray(R.array.states_values)[position];
    }

    /**
     * Get the position in the string-array of a codigo estado.
     */
    public static int getPositionEstado(Context context, String codigoEstado) {
        String[] codes = context.getResources().getStringArray(R.array.states_values);
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equalsIgnoreCase(codigoEstado)) {
                return i;
            }
        }
        return 0;
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
     * Mark a TextView as required.
     */
    public static void markAsRequired(TextView textView) {
        textView.setText(String.format("%s (*)", textView.getText()));
    }

    /**
     * Get the mark icon by the Localizavel.
     */
    public static int getMarkByLocalizavel(Localizavel localizavel) {
        if (localizavel.isRemote() && !localizavel.isChanged()) {
            if (localizavel instanceof Loja) {
                return R.drawable.ic_mark_cablush_green;
            } else if (localizavel instanceof Evento) {
                return R.drawable.ic_mark_cablush_blue;
            } else if (localizavel instanceof Pista) {
                return R.drawable.ic_mark_cablush_orange;
            }
        }
        return R.drawable.ic_mark_cablush_grey;
    }

    /**
     * Make ViewUtils a utility class by preventing instantiation.
     */
    private ViewUtils() {
        throw new AssertionError();
    }
}
