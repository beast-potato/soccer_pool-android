package com.plastic.bevslch.europool2016;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
    private ProgressDialog dialog;

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
                dialog = ProgressDialog.show(LoginActivity.this, "", "Logging you in. Please wait...", true);
                LoginEndpointApiRequest loginEndpointApiRequest = new LoginEndpointApiRequest(Constants.BASE_URL, LoginActivity.this);
                loginEndpointApiRequest.setContentType(Constants.contentTypeJson);
                loginEndpointApiRequest.setEmail(mEmailView.getText().toString());
                loginEndpointApiRequest.setPassword(mPasswordView.getText().toString());
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
                            dialog.dismiss();
                            if (data != null && data.success) {
                                PreffHelper.getInstance().setEmail(mEmailView.getText().toString());
                                PreffHelper.getInstance().setToken(data.token);
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, data.errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            dialog.dismiss();
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
        });

        findViewById(R.id.skip_sign_in_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        });
    }

    private void configView() {
        if (PreffHelper.getInstance().getEmail() != null) {
            mEmailView.setText(PreffHelper.getInstance().getEmail());
        }
    }

}

