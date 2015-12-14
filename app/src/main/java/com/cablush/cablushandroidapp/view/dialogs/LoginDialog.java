package com.cablush.cablushandroidapp.view.dialogs;

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

import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.UsuariosMediator;

/**
 * Created by oscar on 13/12/15.
 */
public class LoginDialog extends DialogFragment {

    private static final String TAG = LoginDialog.class.getSimpleName();

    private EditText edtUsuario;
    private EditText edtSenha;

    /* The activity that creates an instance of this dialog fragment
     * must implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     */
    public interface LoginDialogListener {
        void onLoginDialogSuccess();
        void onLoginDialogCancel();
    }

    // Use this instance of the interface to deliver action events
    LoginDialogListener mListener;

    /**
     * Show the Login Dialog.
     *
     * @param fragmentManager
     */
    public static void showLoginDialog(FragmentManager fragmentManager) {
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
            mListener = (LoginDialogListener) activity;
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
        builder.setTitle(getActivity().getString(R.string.title_login));

        // Add action buttons
        builder.setPositiveButton(R.string.txt_login, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String user = edtUsuario.getText().toString();
                String pass = edtSenha.getText().toString();

                if (!user.isEmpty() && !pass.isEmpty()) {
                    UsuariosMediator usuariosMediator = new UsuariosMediator(getActivity());
                    if (usuariosMediator.doLogin(user,pass)) {
                        mListener.onLoginDialogSuccess();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.msg_login_ou_senha_missing, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.txt_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onLoginDialogCancel();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private View initializeView() {
        // Get the layout inflater and inflate the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_login, null);

        edtUsuario  = (EditText)view.findViewById(R.id.edtUsuario);
        edtUsuario.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        edtSenha    = (EditText)view.findViewById(R.id.edtSenha);
        edtSenha.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        return view;
    }
}
