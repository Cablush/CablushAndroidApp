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
import android.widget.EditText;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.presenter.LoginPresenter;
import com.cablush.cablushapp.utils.ViewUtils;

import java.lang.ref.WeakReference;

/**
 * Created by oscar on 13/12/15.
 */
public class LoginDialog extends DialogFragment {

    private static final String TAG = LoginDialog.class.getSimpleName();

    private EditText emailEdit;
    private EditText passwordEdit;

    /**
     * Interface to be implemented by this Dialog's client.
     */
    public interface LoginDialogListener {
        void onRegisterButtonClicked();
    }

    // Use this instance of the interface to deliver action events
    private WeakReference<LoginPresenter.LoginView> mView;
    private WeakReference<LoginDialogListener> mListener;

    /**
     * Show the Login Dialog.
     *
     * @param fragmentManager
     */
    public static void showDialog(FragmentManager fragmentManager) {
        LoginDialog dialog = new LoginDialog();
        dialog.show(fragmentManager, TAG);
    }

    // Override the Fragment.onAttach() method to instantiate the LoginPresenter.LoginView
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach()");
        try {
            mView = new WeakReference<>((LoginPresenter.LoginView) activity);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement LoginPresenter.LoginView");
        }
        try {
            mListener = new WeakReference<>((LoginDialogListener) activity);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement LoginDialog.LoginDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(initializeView());

        // Set the dialog title
        builder.setCustomTitle(ViewUtils.getCustomTitleView(getActivity().getLayoutInflater(),
                        getString(R.string.title_login),
                        R.drawable.ic_account_circle_cablush_orange));

        // Add action buttons
        builder.setPositiveButton(R.string.btn_login, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String email = emailEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    LoginPresenter loginPresenter = new LoginPresenter(mView.get(), getActivity());
                    loginPresenter.doLogin(email, password);
                } else {
                    Toast.makeText(getActivity(), R.string.msg_login_missing_data, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNeutralButton(R.string.btn_register, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginDialogListener listener = mListener.get();
                if (listener != null) {
                    listener.onRegisterButtonClicked();
                }
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // nothing
            }
        });
        builder.setCancelable(false);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private View initializeView() {
        // Get the layout inflater and inflate the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_login, null);

        emailEdit = (EditText)view.findViewById(R.id.emailEditText);
        emailEdit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        passwordEdit = (EditText)view.findViewById(R.id.passwordEditText);
        passwordEdit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        return view;
    }
}
