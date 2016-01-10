package com.cablush.cablushapp.view.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
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
public class LoginDialog extends DialogFragment implements LoginPresenter.LoginView {

    private static final String TAG = LoginDialog.class.getSimpleName();

    private EditText emailEdit;
    private EditText passwordEdit;

    /**
     * Interface to be implemented by this Dialog's client.
     */
    public interface LoginDialogListener {
        void onLoginDialogSuccess();
        void onLoginDialogError(String message);
        void onRegisterButtonClicked();
    }

    // Use this instance of the interface to deliver action events
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

    // Override the Fragment.onAttach() method to instantiate the LoginDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the LoginDialogListener so we can send events to the host
            mListener = new WeakReference<>((LoginDialogListener) activity);
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement LoginDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
                    LoginPresenter loginPresenter = new LoginPresenter(LoginDialog.this, getActivity());
                    loginPresenter.doLogin(email, password);
                } else {
                    Toast.makeText(getActivity(), R.string.msg_login_missing_data, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNeutralButton(R.string.btn_register, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.get().onRegisterButtonClicked();
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // nothing
            }
        });

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

    @Override
    public void onLoginSuccess() {
        mListener.get().onLoginDialogSuccess();
    }

    @Override
    public void onLoginError(String message) {
        mListener.get().onLoginDialogError(message);
    }
}
