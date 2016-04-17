package com.cablush.cablushapp.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.presenter.LoginPresenter;
import com.cablush.cablushapp.utils.ValidateUtils;
import com.cablush.cablushapp.view.dialogs.ResetPasswordDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by oscar on 06/03/16.
 */
public class SignInActivity extends CablushActivity implements LoginPresenter.LoginView,
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_SIGN_IN = 101;

    private CallbackManager facebookCallbackManager;

    private GoogleApiClient googleApiClient;

    private LoginPresenter loginPresenter;

    private EditText emailEdit;
    private EditText passwordEdit;

    private Toolbar toolbar;
    private ProgressBar spinner;

    /**
     * Make the intent of this activity.
     *
     * @param context
     * @return
     */
    public static Intent makeIntent(@NonNull Context context) {
        return new Intent(context, SignInActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(getTitle());
        }

        loginPresenter = new LoginPresenter(this, this);

        initializeFacebookSDK();

        initializeGoogleSDK();

        initializeView();
    }

    private void initializeFacebookSDK() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        facebookCallbackManager = CallbackManager.Factory.create();
    }

    private void initializeGoogleSDK() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.GOOGLE_CLIENT_ID))
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initializeView() {
        // Fields
        emailEdit = (EditText)findViewById(R.id.emailEditText);
        emailEdit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        passwordEdit = (EditText)findViewById(R.id.passwordEditText);
        passwordEdit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        // Button listeners
        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.signUpButton).setOnClickListener(this);
        findViewById(R.id.resetPassButton).setOnClickListener(this);

        SignInButton googleSignInButton = (SignInButton) findViewById(R.id.googleSignInButton);
        googleSignInButton.setOnClickListener(this);
        setGooglePlusButtonText(googleSignInButton, getString(R.string.btn_login_google));

        LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.facebookLoginButton);
        facebookLoginButton.setReadPermissions("email", "public_profile", "user_friends");
        registerFacebookCallback(facebookLoginButton);

        // Spinner
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
    }

    private void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(15);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText(buttonText);
                return;
            }
        }
    }

    private void registerFacebookCallback(LoginButton loginButton) {
        loginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Facebook login successful.");
                AccessToken token = loginResult.getAccessToken();
                loginPresenter.omniauthCallback(LoginPresenter.OmniauthProvider.FACEBOOK, token.getToken());
                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook login canceled.");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e(TAG, "Error on Facebook Login", exception);
                Toast.makeText(SignInActivity.this, R.string.error_login_facebook, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleSignInButton:
                googleSignIn();
                break;
            case R.id.loginButton:
                serverLogin();
                break;
            case R.id.signUpButton:
                startActivity(SignUpActivity.makeIntent(this));
                break;
            case R.id.resetPassButton:
                ResetPasswordDialog.showDialog(getFragmentManager());
                break;
        }
    }

    // Google SignIn
    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, REQUEST_SIGN_IN);
    }

    // Server Login
    private void serverLogin() {
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if (ValidateUtils.isNotBlank(email) && ValidateUtils.isNotBlank(password)) {
            loginPresenter.login(email, password);
            spinner.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, R.string.msg_login_missing_data, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "Google SignIn Result: " + result);
            if (result.isSuccess()) {
                // Signed in successfully.
                GoogleSignInAccount acct = result.getSignInAccount();
                loginPresenter.omniauthCallback(LoginPresenter.OmniauthProvider.GOOGLE, acct.getIdToken());
                spinner.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, R.string.error_login_google, Toast.LENGTH_SHORT).show();
            }
        } else {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onLoginResponse(LoginPresenter.LoginResponse response) {
        spinner.setVisibility(View.GONE);
        if (LoginPresenter.LoginResponse.SUCCESS.equals(response) && Usuario.LOGGED_USER != null) {
            Toast.makeText(this,
                    getString(R.string.success_login, Usuario.LOGGED_USER.getNome()),
                    Toast.LENGTH_SHORT).show();
            navigateBack();
        } else {
            Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, R.string.error_login_google, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                navigateBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Return to the main activity.
     */
    private void navigateBack() {
        setResult(RESULT_OK, MainActivity.makeIntent(this)); // Always return true
        finish();
    }
}
