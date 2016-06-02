package com.plastic.bevslch.europool2016;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    public void registerClick(View v)
    {
        EditText emailAddress = (EditText) findViewById(R.id.register_email_address);
        EditText password = (EditText) findViewById(R.id.register_password);
        
        String emailAddressText = emailAddress.getText().toString();
        String passwordText = password.getText().toString();
        // TODO: 2016-06-02 NEED TO FINISH THE VALIDATION 
    }
}
