package com.plastic.bevslch.europool2016;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.plastic.bevslch.europool2016.Helpers.PreffHelper;

public class LoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        if (PreffHelper.getInstance().getToken() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            Log.i("blah", PreffHelper.getInstance().getToken());
            startActivity(new Intent(this, HomeActivity.class));
        }
        finish();
    }
}
