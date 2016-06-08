package com.plastic.bevslch.europool2016.Adapters;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sjsaldanha on 2016-06-08.
 */

public class HelperAsyncTask extends AsyncTask<String, Void, String> {
    String urlSourceCode;

    @Override
    protected String doInBackground(String... params) {
        String inputLine = "";
        String urlSource = "";
        try {
            URL url = new URL(params[0].toString());
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            InputStream iS = conn.getInputStream();
            InputStreamReader iSR = new InputStreamReader(iS);
            BufferedReader bR = new BufferedReader(iSR);
            inputLine = bR.readLine();
            while(inputLine != null)
            {
                urlSource += inputLine;
                android.util.Log.d("getURLSource", inputLine);
                inputLine = bR.readLine();
            }
            bR.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlSource;
    }
    protected void onPostExecute(String urlSource) {
        urlSourceCode = urlSource;
    }

    public String getUrlSourceCode() {
        return urlSourceCode;
    }
}
