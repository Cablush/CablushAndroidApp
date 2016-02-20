package com.cablush.cablushapp.view.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by jonathan on 10/02/16.
 */
public class DatePickerFragmentDialog extends DialogFragment {

    private static final String TAG = DatePickerFragmentDialog.class.getSimpleName();

    private Calendar calendar;

    private DatePickerDialog.OnDateSetListener listener;

    /**
     *
     * @param fragmentManager
     * @param calendar
     * @param listener
     */
    public static void showDialog(@NonNull FragmentManager fragmentManager,
                                  @NonNull Calendar calendar,
                                  @NonNull DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragmentDialog dialog = new DatePickerFragmentDialog();
        dialog.calendar = calendar; // TODO send this via bundle
        dialog.listener = listener; // TODO send this via bundle
        dialog.show(fragmentManager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        // Get the default values for the picker from the initial Calendar
        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day   = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerFragmentDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }

}