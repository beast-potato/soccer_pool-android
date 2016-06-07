package com.plastic.bevslch.europool2016;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.beastpotato.potato.api.net.ApiRequest;
import com.plastic.bevslch.europool2016.Helpers.PreffHelper;
import com.plastic.bevslch.europool2016.endpoints.LoginEndpointApiRequest;
import com.plastic.bevslch.europool2016.endpoints.loginendpointresponse.LoginEndpointApiResponse;
import com.plastic.bevslch.europool2016.views.LoadingOverlayView;

import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private static String TAG = "LoginActivity";

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button mEmailSignInButton;
    private LoadingOverlayView mLoginOverlay;
    private Boolean memailConfirmed;
    private Boolean mdialogClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        setListeners();
        configView();


    }

    private void initView() {
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mLoginOverlay = (LoadingOverlayView) findViewById(R.id.login_overlay);
        memailConfirmed = false;
        mdialogClick = false;
    }

    private void setListeners() {
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                makeLoginCall(false);
            }
        });
    }

    private void configView() {
        if (PreffHelper.getInstance().getEmail() != null) {
            mEmailView.setText(PreffHelper.getInstance().getEmail());
        }
    }


    private void makeLoginCall(boolean signup) {
        if (!mEmailView.getText().toString().contains("@plasticmobile.com")) {
            mEmailView.setError("Enter a Plastic Mobile Email addess");
        } else {
            mLoginOverlay.setVisibility(View.VISIBLE);
            final LoginEndpointApiRequest loginEndpointApiRequest = new LoginEndpointApiRequest(Constants.BASE_URL, LoginActivity.this);
            loginEndpointApiRequest.setContentType(Constants.contentTypeJson);
            loginEndpointApiRequest.setEmail(mEmailView.getText().toString());
            loginEndpointApiRequest.setPassword(mPasswordView.getText().toString());
            loginEndpointApiRequest.setSignup(signup?String.valueOf(signup):null);
            List<LoginEndpointApiRequest.Fields> invalidFieldsList = loginEndpointApiRequest.validateFields();
            if (invalidFieldsList.size() > 0) {
                for (LoginEndpointApiRequest.Fields field : invalidFieldsList) {
                    switch (field) {
                        case email:
                            Toast.makeText(LoginActivity.this, "Enter valid email.", Toast.LENGTH_LONG).show();
                            break;
                        case password:
                            Toast.makeText(LoginActivity.this, "Enter valid email.", Toast.LENGTH_LONG).show();
                            break;
                        case contentType:
                            break;
                    }
                }
            } else {
                Log.i(TAG, "will send request to url: " + loginEndpointApiRequest.getFullUrl());
                loginEndpointApiRequest.send(new ApiRequest.RequestCompletion<LoginEndpointApiResponse>() {
                    @Override
                    public void onResponse(LoginEndpointApiResponse data) {
                        mLoginOverlay.setVisibility(View.GONE);
                        if (data != null) {
                            if (data.success) {
                                PreffHelper.getInstance().setEmail(mEmailView.getText().toString());
                                PreffHelper.getInstance().setToken(data.token);
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }
                            if (!data.success) {
                                Log.d(TAG, "onResponse: YOU HAVE REACHED");
                                if (data.errorCode == 1) {
                                    mPasswordView.setError("Password is incorrect, please enter the correct password");
                                }
                                if (data.errorCode == 2) {
                                    new AlertDialog.Builder(LoginActivity.this)
                                            .setTitle("Create User")
                                            .setMessage("Do you wish to create a new account for the EURO 2016 Pool?")
                                            .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    makeLoginCall(true);
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            })
                                            .show();
                                }
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Error:"+data.errorMessage, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        mLoginOverlay.setVisibility(View.GONE);
                        StringBuilder builder = new StringBuilder("Error:");
                        if (error.networkResponse != null)
                            builder.append(error.networkResponse.statusCode);
                        if (error.getMessage() != null)
                            builder.append(" - " + error.getMessage());
                        Toast.makeText(LoginActivity.this, builder.toString(), Toast.LENGTH_LONG).show();
                        if (error.networkResponse != null)
                            Log.e(TAG, "Error:" + new String(error.networkResponse.data));
                    }
                });
            }
        }
    }
}

