package com.shoppers_era.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.R;
import com.shoppers_era.Session.Customer_Session;


public class Status_Activity extends AppCompatActivity {

    private TextView coupon_id;
    private TextView valid_from;
    private TextView valid_till;
    private TextView amount;
    private String coupon_s;
    private String valid_s;
    private String valid_t_s;
    private String amount_s;
    private static final String REWARD_URL = "https://apps.itrifid.com/shoppersera/rest_server/CouponCodeForAd/API-KEY/123456";
    private static final String KEY_ID = "c_id";
    private Customer_Session customer_session;
    private RelativeLayout status_layout;
    private ProgressBar p_bar;
    private TextView btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_);

        p_bar = (ProgressBar) findViewById(R.id.p_bar);
        status_layout = (RelativeLayout) findViewById(R.id.status_layout);

        coupon_id = (TextView) findViewById(R.id.order_id);
        valid_from = (TextView) findViewById(R.id.order_date);
        valid_till = (TextView) findViewById(R.id.payment_mode);
        amount = (TextView) findViewById(R.id.payable_amount);

        btn = (TextView) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        coupon_id.setText(coupon_s);
        valid_from.setText(valid_s);
        valid_till.setText(valid_t_s);
        amount.setText(amount_s);

        customer_session = new Customer_Session(getApplicationContext());

        rewardUser();
    }

    void rewardUser(){

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
                                JSONArray array = response.getJSONArray("data");
                                JSONObject jsonObject = array.getJSONObject(0);

                                JSONObject properties = jsonObject.getJSONObject("properties");
                                coupon_s = properties.getString("coupon_id");
                                valid_s = properties.getString("valid_from");
                                valid_t_s = properties.getString("valid_to");
                                amount_s = properties.getString("coupon_amount");

                                coupon_id.setText(coupon_s);
                                valid_from.setText(valid_s);
                                valid_till.setText(valid_t_s);
                                amount.setText("₹"+amount_s);


                                status_layout.setVisibility(View.VISIBLE);
                                p_bar.setVisibility(View.GONE);

                            }else {
                             }

                        } catch (JSONException e) {
                            Toast.makeText(Status_Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
