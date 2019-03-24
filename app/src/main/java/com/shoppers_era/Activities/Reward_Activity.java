package com.shoppers_era.Activities;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.shoppers_era.Adapters.Reward_Adapter;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.Model.Reward;
import com.shoppers_era.R;
import com.shoppers_era.Session.Customer_Session;

public class Reward_Activity extends AppCompatActivity {

    private static final String KEY_ID = "c_id";
    private static final String REWARD_URL = "https://apps.itrifid.com/shoppersera/rest_server/userCoupon/API-KEY/123456";
    private ProgressBar progress_bar;
    private RecyclerView recyclerView;
    private Customer_Session customer_session;
    private LinearLayout linearLayout;
    private List<Reward> rewards= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progress_bar = (ProgressBar) findViewById(R.id.p_bar);

        linearLayout = (LinearLayout) findViewById(R.id.empty_layout);

        customer_session = new Customer_Session(getApplicationContext());

        getReward();
    }

    void getReward(){

        String tag_json_obj = "json_obj_req";

        Map<String,String> post_param = new HashMap<>();
        post_param.put(KEY_ID,customer_session.getCustomerID());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                REWARD_URL,new JSONObject(post_param),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");
                            if (status.equals("200")){

                                JSONArray array = response.getJSONArray("couponData");

                                JSONObject jsonObject = array.getJSONObject(0);

                                JSONArray jsonArray = jsonObject.getJSONArray("properties");

                                for(int i=0;i<jsonArray.length();i++){

                                    JSONObject properties = jsonArray.getJSONObject(i);

                                    String coupon_id = properties.getString("coupon_id");
                                    String valid_from = properties.getString("coupon_status");
                                    String valid_to = properties.getString("valid_to");
                                    String coupon_amount = properties.getString("coupon_amount");

                                    rewards.add(new Reward(coupon_id,valid_from,valid_to,coupon_amount));

                                }

                                Reward_Adapter reward_adapter = new Reward_Adapter(getApplicationContext(),rewards);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(reward_adapter);

                                progress_bar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.GONE);
                            }else {
                                progress_bar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(Reward_Activity.this,"Connection error", Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

