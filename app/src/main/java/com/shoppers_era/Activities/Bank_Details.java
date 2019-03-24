package com.shoppers_era.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.R;
import com.shoppers_era.Session.Customer_Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class Bank_Details extends AppCompatActivity {


    private Customer_Session customer_session;
    private EditText account_no;
    private EditText bank_name;
    private EditText ifsc;
    private Button btn;
    private EditText name;
    private static final String GET_DETAILS_URL = "https://apps.itrifid.com/shoppersera/rest_server/allResellerOBankDetails/API-KEY/123456";
    private static final String INSERT_DETAILS_URL = "https://apps.itrifid.com/resham/rest_server/getResellerBankDetails/API-KEY/123456";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_);

    customer_session = new Customer_Session(getApplicationContext());

        account_no = (EditText) findViewById(R.id.input_no);
        bank_name = (EditText) findViewById(R.id.input_bank_name);
        ifsc = (EditText) findViewById(R.id.input_ifsc);
        name = (EditText) findViewById(R.id.input_name);


        btn =(Button ) findViewById(R.id.submit_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!account_no.getText().toString().trim().equals("") && !bank_name.getText().toString().trim().equals("") && !ifsc.getText().toString().trim().equals("") && !name.getText().toString().trim().equals("")){
                    showProgressDialog();
                    addAddress();
                }else {
                    Toast.makeText(Bank_Details.this, "Please Fill it out..", Toast.LENGTH_SHORT).show();
                }
            }
        });


        showProgressDialog();
        getDetails();

    }

    void getDetails(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put("reseller_id",customer_session.getCustomerID());


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                GET_DETAILS_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideProgressDialog();
                        try {
                            String status = response.getString("status");

                            if(status.equals("200")){
                                JSONArray jsonObject = response.getJSONArray("userData");
                                JSONObject jsonObject1 = jsonObject.getJSONObject(0);
                                bank_name.setText(jsonObject1.getString("bank_name"));
                                ifsc.setText(jsonObject1.getString("bank_ifsc_code"));
                                account_no.setText(jsonObject1.getString("bank_ac_number"));
                                account_no.setText(jsonObject1.getString("ac_holder_name"));
                                btn.setVisibility(View.GONE);
                            }else {
                                //do nothing
                                btn.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Bank_Details.this,"Connection error",Toast.LENGTH_LONG).show();
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    void addAddress(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put("res_id",customer_session.getCustomerID());
        postParam.put("bank_name",bank_name.getText().toString().trim());
        postParam.put("bank_ifsc_code",ifsc.getText().toString().trim());
        postParam.put("bank_ac_number", account_no.getText().toString().trim());
        postParam.put("ac_holder_name",name.getText().toString().trim());


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                INSERT_DETAILS_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if(status.equals("200")){
                                showProgressDialog();
                                getDetails();
                            }else {
                                hideProgressDialog();
                                Toast.makeText(Bank_Details.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Bank_Details.this,"Connection error",Toast.LENGTH_LONG).show();
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
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
