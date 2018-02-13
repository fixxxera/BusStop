package com.example.fixxxer.busstop;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class MainActivity extends Activity {
    public CircularProgressButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.checButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    button.startAnimation();
                    new Request().execute(new URL("https://api-arrivals.sofiatraffic.bg/api/v1/arrivals/0914/"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public class Request extends AsyncTask<URL, Void, JSONArray> {


        @Override
        protected JSONArray doInBackground(URL... params) {
            BufferedReader reader;
            InputStream is;
            JSONArray lines = null;
            try {

                StringBuilder responseBuilder = new StringBuilder();
                HttpURLConnection conn = (HttpURLConnection) params[0].openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));
                for (String line; (line = reader.readLine()) != null; ) {
                    responseBuilder.append(line).append("\n");
                }
                JSONObject resp= new JSONObject(responseBuilder.toString());
                reader.close();
                is.close();
                lines = resp.getJSONArray("lines");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


            return lines;
        }


        @Override
        protected void onPostExecute(JSONArray lines) {
//            for (int i = 0;i<lines.length();i++){
//                try {
//                    switch (lines.getJSONObject(i).getString("name")) {
//                        case "64":
//
//                            break;
//                        case "64":
//
//                            break;
//                        case "64":
//
//                            break;
//                        case "64":
//
//                            break;
//                    }
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
            button.doneLoadingAnimation(Color.GREEN, BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_48dp));
            button.revertAnimation();
            button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_default));
        }
    }
}
