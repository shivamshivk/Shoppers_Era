package com.shoppers_era.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.R;
import com.shoppers_era.Session.Customer_Session;
import com.shoppers_era.Session.Name_Session;
import com.shoppers_era.Session.Phone_Session;
import com.shoppers_era.Session.Session_Manager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login_Actvity extends AppCompatActivity {

    private static final String LOGIN_URL = "https://apps.itrifid.com/shoppersera/rest_server/userRegistration/API-KEY/123456";
    private static final String KEY_NAME = "c_name";
    private static final String KEY_PHONE = "c_mobile";
    private static final String KEY_EMAIL = "c_email";
    private EditText password;
    private EditText email_id;
    private Button btn;
    private ProgressDialog mProgressDialog;
    private EditText email;
    private Customer_Session customer_session;
    private Session_Manager session_manager;
    static Login_Actvity activity;
    private Phone_Session phone_session;
    private Name_Session name_session;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__actvity);

        password = (EditText) findViewById(R.id.shiv);

        activity = this;

        phone_session=new Phone_Session(getApplicationContext());
        name_session=new Name_Session(getApplicationContext());

        email = (EditText) findViewById(R.id.email);
        email_id = (EditText) findViewById(R.id.email_id);
        customer_session=new Customer_Session(getApplicationContext());
        session_manager = new Session_Manager(getApplicationContext());

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email_ = email.getText().toString().trim();
                final String password_ = password.getText().toString().trim();


                if (email_.isEmpty() ) {
                    email.setError("Your name please");
                }else if (password_.isEmpty()){
                    password.setError("Mobile number mandatory");
                    Toast.makeText(Login_Actvity.this, "Mobile number mandatory", Toast.LENGTH_SHORT).show();
                }else{
                    showProgressDialog();
                    login();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_in);
    }

    void login(){

        String tag_json_obj = "json_obj_req";

        Map<String,String> post_param = new HashMap<>();
        post_param.put(KEY_NAME,email.getText().toString().trim());
        post_param.put(KEY_PHONE,password.getText().toString().trim());
        post_param.put(KEY_EMAIL,email_id.getText().toString().trim());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                LOGIN_URL,new JSONObject(post_param),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");
                            if (status.equals("200")){

                                hideProgressDialog();
                                JSONArray jsonArray = response.getJSONArray("data");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                String properties = jsonObject.getString("user_id");
                                String account_status = jsonObject.getString("account_status");
                                customer_session.setCustomerID(properties);
                                phone_session.setPhonNo(password.getText().toString().trim());
                                name_session.setName(email.getText().toString().trim());

                                if (account_status.equals("1")){
                                    session_manager.setLogin(true);
                                    Intent intent = new Intent(getApplicationContext(), Main_Handler_Activity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    overridePendingTransition (0, 0);
                                    finish();
                                }else {
                                    session_manager.setLogin(true);
                                    Intent intent = new Intent(getApplicationContext(), Main_Handler_Activity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    overridePendingTransition (0, 0);
                                    finish();
//                                    Intent intent = new Intent(getApplicationContext(), Verification.class);
//                                    intent.putExtra("mobile",password.getText().toString().trim());
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//                                    overridePendingTransition (0, 0);
                                }

                            }else {
                                hideProgressDialog();
                                Toast.makeText(Login_Actvity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            hideProgressDialog();
                            Toast.makeText(Login_Actvity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

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

    public static Login_Actvity getInstance(){
        return activity;
    }

}
