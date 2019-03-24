package com.shoppers_era.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.shoppers_era.R;
import com.shoppers_era.Session.Session_Manager;


public class Verification extends AppCompatActivity {

    private static final String KEY_OTP = "c_otp";
    private static final String KEY_REF_EMAIL = "c_mobile";
    private static final String VERIFICATION_URL = "https://apps.itrifid.com/shoppersera/rest_server/verifyOTP/API-KEY/123456";
    private static final String RESEND_OTP_URL = "https://apps.itrifid.com/shoppersera/rest_server/resendOTP/API-KEY/123456";
    private ProgressDialog mProgressDialog;
    private String mobile;
    private Session_Manager session_manager;
    private EditText otp;
    private Button btn;
    private TextView resend_otp;
    private int seconds=30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null){
            mobile = extras.getString("mobile");
        }

        session_manager = new Session_Manager(getApplicationContext());

        otp = (EditText)findViewById(R.id.email);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP();
                showProgressDialog();
            }
        });

        resend_otp = (TextView) findViewById(R.id.otp_link);

        //Declare the timer
        final Timer t = new Timer();
        //Set the schedule function and rate
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        resend_otp.setText(String.valueOf("00")+":"+String.valueOf(seconds));
                        seconds -= 1;
                        if(seconds == 0)
                        {
                            resend_otp.setText("Didn't receive the code? Send Again");
                            t.cancel();
                        }
                    }
                });
            }

        }, 0, 1000);

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seconds==0){
                    showProgressDialog();
                    resendOTP();
                }
            }
        });

    }



    private void verifyOTP(){

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_REF_EMAIL,mobile);
        postParam.put(KEY_OTP,otp.getText().toString().trim());

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                VERIFICATION_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            hideProgressDialog();

                            if(status.equals("201")){
                                Toast.makeText(Verification.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                                Login_Actvity.getInstance().finish();
                                session_manager.setLogin(true);

                                Intent intent = new Intent(getApplicationContext(),Main_Handler_Activity.class);
                                startActivity(intent);

                                finish();

                            }else {
                                Toast.makeText(Verification.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(Verification.this,"Connection Error",Toast.LENGTH_LONG).show();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);

    }

    private void resendOTP(){

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_REF_EMAIL,mobile);

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                RESEND_OTP_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            mProgressDialog.hide();

                            if(status.equals("200")){
                                Toast.makeText(Verification.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Verification.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(Verification.this,"Connection Error",Toast.LENGTH_LONG).show();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjReq);

    }



    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

}
