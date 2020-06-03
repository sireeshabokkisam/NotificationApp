package com.example.notificationapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "NotificationChannel";
    private EditText username;
    private EditText password;
    private TextView view;
    private Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificationChannel";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        System.out.println("Token :"+FirebaseInstanceId.getInstance().getToken());

        username = (EditText) findViewById((R.id.username));
        password = (EditText) findViewById(R.id.password);
        view =(TextView) findViewById(R.id.view);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    // CALL GetText method to make post method call
                    GetText();
                    // getLogin();
                }
                catch(Exception ex)
                {
                    view.setText(" url exeption! " );
                }

            }
        });
    }

    private void GetText()  throws UnsupportedEncodingException {

        final String uname = username.getText().toString();
        final String pass = password.getText().toString();

        if (uname.equalsIgnoreCase("")) {

            username.setError("Enter Username");
        }
        if (pass.equalsIgnoreCase("")) {
            password.setError("Enter Password");
        } else {

            String url = "http://10.0.2.2:3000/login";

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // view.setText("Login Success");
                    System.out.println(response);
                    String[] name = response.split("\"");
                    System.out.println(name[17]);
                    openActivity(name[7],name[3],name[17]);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    view.setText("Invalid Username Or Password");
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", uname);
                    params.put("password", pass);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    // params.put("Content-Type","application/json; charset=utf-8");
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }

    }

    private void openActivity(String  uname,String id, String token) {
        Intent intent = new Intent(this,Activity2.class);
        intent.putExtra("NAME",uname);
        intent.putExtra("ID",id);
        intent.putExtra("TOKEN",token);
        startActivity(intent);
    }

    private void sendRegistrationToServer(final String refreshedToken) {

        String url = "http://10.0.2.2:3000/devicetoken";

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // view.setText("Login Success");
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error in sending token");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("token", refreshedToken);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                // params.put("Content-Type","application/json; charset=utf-8");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


}
