package com.shoppers_era.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.R;
import com.shoppers_era.Session.Customer_Session;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Profile_Activity extends AppCompatActivity {

    private static final String PROFILE_URL = "https://apps.itrifid.com/shoppersera/rest_server/userProfile/API-KEY/123456";
    private static final String KEY_C_ID  = "c_id";
    private TextView user_name;
    private TextView user_phone;
    private ProgressBar progressBar;
    private LinearLayout lin;
    private Customer_Session customer_session;
    private Button btn;
    private Button help_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        customer_session = new Customer_Session(getApplicationContext());
        user_name = (TextView) findViewById(R.id._name);
        user_phone = (TextView) findViewById(R.id.phone);

        lin = (LinearLayout) findViewById(R.id.lin);
        progressBar = (ProgressBar) findViewById(R.id.p_bar);

        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");
        user_name.setTypeface(tf1);
        user_phone.setTypeface(tf1);

        btn = (Button) findViewById(R.id.reward_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Reward_Activity.class));
            }
        });

        help_btn = (Button) findViewById(R.id.help_btn);
        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Help_Activity.class));
            }
        });

        getDetails();

    }


    void getDetails(){

        String tag_json_obj = "json_obj_req";

        Map<String,String> post_param = new HashMap<>();

        post_param.put(KEY_C_ID,customer_session.getCustomerID());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                PROFILE_URL,new JSONObject(post_param),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){

                                progressBar.setVisibility(View.GONE);
                                lin.setVisibility(View.VISIBLE);

                                JSONObject js = response.getJSONObject("userData");
                                String name = js.getString("full_name");
                                String phone = js.getString("mobile");

                                user_name.setText(name);
                                user_phone.setText("+91 "+phone);

                            }else {
                                Toast.makeText(Profile_Activity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_in);
    }
}
