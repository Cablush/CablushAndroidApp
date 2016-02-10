package com.cablush.cablushapp.view.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.presenter.RegisterPresenter;
import com.cablush.cablushapp.utils.ViewUtils;

import java.lang.ref.WeakReference;

/**
 * Created by oscar on 26/12/15.
 */
public class RegisterDialog extends DialogFragment {

    private static final String TAG = RegisterDialog.class.getSimpleName();

    private EditText nameEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private CheckBox shopkeeperCheck;

    private WeakReference<RegisterPresenter.RegisterView> mView;

    /**
     * Show the Login Dialog.
     *
     * @param fragmentManager
     */
    public static void showDialog(FragmentManager fragmentManager) {
        RegisterDialog dialog = new RegisterDialog();
        dialog.show(fragmentManager, TAG);
    }

    // Override the Fragment.onAttach() method to instantiate the LoginDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach()");
        try {
            mView = new WeakReference<>((RegisterPresenter.RegisterView) activity);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement RegisterPresenter.RegisterView");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(initializeView());

        // Set the dialog title
        builder.setCustomTitle(ViewUtils.getCustomTitleView(getActivity().getLayoutInflater(),
                getString(R.string.title_register),
                R.drawable.ic_account_circle_cablush_orange));

        // Add action buttons
        builder.setPositiveButton(R.string.btn_register, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                Boolean shopkeeper = shopkeeperCheck.isChecked();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    RegisterPresenter registerPresenter = new RegisterPresenter(mView.get(), getActivity());
                    registerPresenter.doRegister(name, email, password, shopkeeper);
                } else {
                    Toast.makeText(getActivity(), R.string.msg_register_missing_data, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private View initializeView() {
        // Get the layout inflater and inflate the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_register, null);

        nameEdit = (EditText)view.findViewById(R.id.nameEditText);
        nameEdit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        emailEdit = (EditText)view.findViewById(R.id.emailEditText);
        emailEdit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        passwordEdit = (EditText)view.findViewById(R.id.passwordEditText);
        passwordEdit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        shopkeeperCheck = (CheckBox) view.findViewById(R.id.shopkeeperCheckBox);

        return view;
    }
}
