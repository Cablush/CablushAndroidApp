package com.cablush.cablushapp.view.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by oscar on 09/02/16.
 */
public class TimePickerFragmentDialog extends DialogFragment {

    private static final String TAG = TimePickerFragmentDialog.class.getSimpleName();

    private Calendar calendar;

    private TimePickerDialog.OnTimeSetListener listener;

    /**
     *
     * @param fragmentManager
     * @param calendar
     * @param listener
     */
    public static void showDialog(@NonNull FragmentManager fragmentManager,
                                  @NonNull Calendar calendar,
                                  @NonNull TimePickerDialog.OnTimeSetListener listener) {
        TimePickerFragmentDialog dialog = new TimePickerFragmentDialog();
        dialog.calendar = calendar; // TODO send this via bundle
        dialog.listener = listener; // TODO send this via bundle
        dialog.show(fragmentManager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        if (calendar.getTime().getTime() == 0) {
            calendar.setTime(new Date());
        }
        // Get the default values for the picker from the initial Calendar
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a new instance of TimePickerFragmentDialog and return it
        return new TimePickerDialog(getActivity(), listener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

}
