package com.example.notificationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity2 extends AppCompatActivity {

    private String token;
    ArrayAdapter adapter;
    ListView listView;
    List<String> announcementList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        adapter = new ArrayAdapter(this,R.layout.activity_listview);
        listView = (ListView) findViewById(R.id.window_list);
        token = getIntent().getStringExtra("TOKEN");
        getTags(getIntent().getStringExtra("ID"));
        listView.setAdapter(adapter);
    }


    public void getTags(String id) {
        String url = "http://127.0.0.1:4200/'create/"+"\""+id+"\"";

        RequestQueue requestQueue = Volley.newRequestQueue(Activity2.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getAnnouncements(response);
                System.out.println(response);
            }
        }, new Response.ErrorListener() {

            public void onErrorResponse() {
                onErrorResponse();
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                // params.put("Content-Type","application/json; charset=utf-8");
                params.put("Authorization","Bearer "+ token);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void getAnnouncements(String tags) {
        String url = "http://127.0.0.1:4200/'create/"+tags;
        RequestQueue requestQueue = Volley.newRequestQueue(Activity2.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray obj = new JSONArray(response);

                    for (int i=0;i<obj.length();i++) {
                        final JSONObject jsonObject = obj.getJSONObject(i);
                      /*  String title = jsonObject.getString("title");
                        String id = jsonObject.getString("_id");
                        String description = jsonObject.getString("description");
                        String details = jsonObject.getString("details");
                        String link = jsonObject.getString("link");
                        notificationList.add(new Announcement(id,title,description,details,link));
                        System.out.println(notificationList.get(0)); */
                        announcementList.add(jsonObject.getString("_id"));
                        adapter.add("\nDate: "+jsonObject.getString("date")+"\n\n"+"Title: "+jsonObject.getString("title")+"\n\n"+"Description: "+jsonObject.getString("description")+"\n\n\n");
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                System.out.println("Clicked Position :"+position);
                                System.out.println(announcementList.get(position));
                                openActivity3(announcementList.get(position),token);
                            }
                        });
                    }
                    System.out.println(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                params.put("Authorization","Bearer "+ token);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void openActivity3(String id,String token) {
        Intent intent = new Intent(this,ShowNotification.class);
        intent.putExtra("ID", id);
        intent.putExtra("TOKEN",token);
        startActivity(intent);
    }
}
