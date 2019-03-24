package com.shoppers_era.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.shoppers_era.Adapters.Notification_Adapter;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.Model.Notification;
import com.shoppers_era.R;


public class Notifications_Activity extends Activity {

    private static final String NOTIFICATION_URL = "https://apps.itrifid.com/shoppersera/rest_server/getAllNotification/API-KEY/123456";
    private LinearLayout empty_layout;
    private ProgressBar p_bar;
    private RecyclerView recyclerView;
    private Notification_Adapter mAdapter;
    private List<Notification> notifications= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_);

        empty_layout = (LinearLayout) findViewById(R.id.empty_layout);
        p_bar = (ProgressBar) findViewById(R.id.p_bar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.addOnItemTouchListener(new Notification_Adapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new Notification_Adapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(notifications.get(position).getBase().equals("cat_details")){
                    Intent intent = new Intent(getApplicationContext(),Cat_Details.class);
                    intent.putExtra("cat_id",notifications.get(position).getCat());
                    startActivity(intent);
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        getNotifications();
    }

    void getNotifications(){


        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                NOTIFICATION_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("userData");

                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String img_url = jsonObject.getString("image");
                                    String title = jsonObject.getString("title");
                                    String desc = jsonObject.getString("message");
                                    String date = jsonObject.getString("date");
                                    String base = jsonObject.getString("base");
                                    String cat = jsonObject.getString("cat");
                                    String time = jsonObject.getString("time");

                                    notifications.add(new Notification(img_url,title,desc,date,time,base,cat));
                                }

                                mAdapter = new Notification_Adapter(getApplicationContext(),notifications);

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mAdapter);

                                p_bar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                            }else {
                                empty_layout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                p_bar.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
