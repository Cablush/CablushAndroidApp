package com.cablush.cablushapp.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.presenter.RegisterPresenter;
import com.cablush.cablushapp.utils.ValidateUtils;

/**
 * Created by oscar on 27/03/16.
 */
public class SignUpActivity extends CablushActivity implements RegisterPresenter.RegisterView {

    private RegisterPresenter mPresenter;

    private EditText nameEdit;
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
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(getTitle());
        }

        mPresenter = new RegisterPresenter(this, this);

        initializeView();
    }

    private void initializeView() {
        // Fields
        nameEdit = (EditText)findViewById(R.id.nameEditText);
        nameEdit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        emailEdit = (EditText)findViewById(R.id.emailEditText);
        emailEdit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        passwordEdit = (EditText)findViewById(R.id.passwordEditText);
        passwordEdit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        // Button listeners
        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverSignUp();
            }
        });

        // Spinner
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
    }

    // Server SignUp
    private void serverSignUp() {
        String name = nameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if (!ValidateUtils.isNotBlank(name)) {
            Toast.makeText(this, R.string.msg_register_missing_data, Toast.LENGTH_SHORT).show();
        } else if (!ValidateUtils.isValidEmail(email, true)) {
            Toast.makeText(this, R.string.msg_invalid_email, Toast.LENGTH_SHORT).show();
        } else if (!ValidateUtils.isValidPassword(password)) {
            Toast.makeText(this, R.string.msg_invalid_password, Toast.LENGTH_SHORT).show();
        } else {
            mPresenter.register(name, email, password);
            spinner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRegisterResponse(RegisterPresenter.RegisterResponse response) {
        spinner.setVisibility(View.GONE);
        if (RegisterPresenter.RegisterResponse.SUCCESS.equals(response)) {
            Toast.makeText(this, R.string.success_register, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.error_register), Toast.LENGTH_SHORT).show();
        }
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
     * Return to the parent activity.
     */
    private void navigateBack() {
        setResult(RESULT_OK, SignInActivity.makeIntent(this)); // Always return true
        finish();
    }
}
