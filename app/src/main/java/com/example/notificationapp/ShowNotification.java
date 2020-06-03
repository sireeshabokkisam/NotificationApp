package com.example.notificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.net.Uri.*;
import static com.example.notificationapp.R.layout.activity_show_notification;

public class ShowNotification extends AppCompatActivity {
    TextView notification;
    private ImageView imageView;
    Button logout;
    private CharBuffer Picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification);
        notification = (TextView) findViewById(R.id.notification);
        imageView = this.<ImageView>findViewById(R.id.imageView);
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
        showNotification(getIntent().getStringExtra("ID"));
    }

    private void openActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void showNotification(String id) {
        String url = "http://127.0.0.1:4200/'notifications/"+getIntent().getStringExtra("ID");

        RequestQueue requestQueue = Volley.newRequestQueue(ShowNotification.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.has("imageURL")) {
                        notification.setText("Date :" + object.getString("date") + "\n\n" + "Title :  " + object.getString("title") + "\n\n" + "Description :  " + object.getString("description") + "\n\nDetails :  " + object.getString("details") + "\n\nLink :  " + object.getString("link")+"\n\nImage:");
                        String url = object.getString("imageURL").replace("localhost","localhost");
                    }else{
                        notification.setText("Date :" + object.getString("date") + "\n\n" + "Title :  " + object.getString("title") + "\n\n" + "Description :  " + object.getString("description") + "\n\nDetails :  " + object.getString("details") + "\n\nLink :  " + object.getString("link"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                // params.put("Content-Type","application/json; charset=utf-8");
                params.put("Authorization","Bearer "+getIntent().getStringExtra("TOKEN"));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}
