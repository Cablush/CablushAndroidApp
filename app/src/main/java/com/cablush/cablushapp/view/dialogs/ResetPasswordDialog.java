package com.cablush.cablushapp.view.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.presenter.ResetPasswordPresenter;
import com.cablush.cablushapp.utils.ValidateUtils;
import com.cablush.cablushapp.utils.ViewUtils;

/**
 * Created by oscar on 27/03/16.
 */
public class ResetPasswordDialog extends DialogFragment
        implements ResetPasswordPresenter.ResetPasswordView {

    private static final String TAG = ResetPasswordDialog.class.getSimpleName();

    private EditText emailEdit;

    private ProgressBar spinner;

    private ResetPasswordPresenter mPresenter;

    /**
     * Show the Change Password Dialog.
     *
     * @param fragmentManager
     */
    public static void showDialog(@NonNull FragmentManager fragmentManager) {
        ResetPasswordDialog dialog = new ResetPasswordDialog();
        dialog.show(fragmentManager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(initializeView());

        mPresenter = new ResetPasswordPresenter(this);

        // Set the dialog title
        builder.setCustomTitle(ViewUtils.getCustomTitleView(getActivity().getLayoutInflater(),
                getString(R.string.title_reset_password),
                R.drawable.ic_mark_cablush_orange));

        // Add action buttons
        builder.setPositiveButton(R.string.btn_send, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String email = emailEdit.getText().toString();
                if (ValidateUtils.isValidEmail(email, true)) {
                    mPresenter.resetPassword(email);
                    spinner.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), R.string.msg_invalid_email, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        // Create the ResetPasswordDialog object and return it
        return builder.create();
    }

    private View initializeView() {
        // Get the layout inflater and inflate the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reset_password, null);

        emailEdit = (EditText)view.findViewById(R.id.emailEditText);
        emailEdit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        spinner = (ProgressBar)getActivity().findViewById(R.id.progressBar);

        return view;
    }

    @Override
    public void onResponse(ResetPasswordPresenter.Response response) {
        spinner.setVisibility(View.GONE);
        if (response == ResetPasswordPresenter.Response.SUCCESS) {
            Toast.makeText(getActivity(), R.string.msg_reset_password_sent, Toast.LENGTH_SHORT).show();
        }
    }
}
