package com.shoppers_era.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.R;
import com.shoppers_era.Session.Customer_Session;
import com.shoppers_era.Session.Name_Session;
import com.shoppers_era.Session.Phone_Session;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class Payment_Activity extends Activity {

    private static final String PRODUCT_ID = "product_id";
    private Button req_btn;
    private static final String ORDER_URL = "https://apps.itrifid.com/shoppersera/rest_server/getUserOrder/API-KEY/123456";
    private static final String CUS_NAME = "cus_name";
    private static final String CUS_PHONE = "cus_mobile";
    private static final String CUS_ID = "cus_id";
    private Dialog bottom_dialog;
    private EditText c_name;
    private EditText c_phone;
    private ProgressDialog mProgressDialog;
    private Name_Session name_session;
    private Phone_Session phone_session;
    private Customer_Session customer_session;
    private String product_id;
    private String tot_amount;
    private TextView price;
    private FrameLayout btn_ok;
    private TextView apply;
    private int id =0;
    private Dialog dialog;
    private FrameLayout btn_okk;
    private EditText editText;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null){
            product_id = extras.getString("product_id");
            tot_amount = extras.getString("tot_cost");
        }

        phone_session = new Phone_Session(getApplicationContext());

        dialog  = new Dialog(Payment_Activity.this,R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams2.copyFrom(dialog.getWindow().getAttributes());
        layoutParams2.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(layoutParams2);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.coupon_dialog);

        editText = (EditText) dialog.findViewById(R.id.input_name);

        btn_okk = (FrameLayout) dialog.findViewById(R.id.btn_ok);
        btn_okk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().equals(phone_session.getPhoneNO())){
                    dialog.dismiss();
                    if (id==0){
                        double tot = Double.parseDouble(tot_amount);
                        tot = tot * 15/100;
                        int t1 = (int) (Double.parseDouble(tot_amount) - tot);
                        price.setText("Pay "+t1+" INR");
                        id =1;
                        apply.setText("Coupon Applied");
                    }else {
                        Toast.makeText(Payment_Activity.this, "Coupon Applied..", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(Payment_Activity.this, "Wrong Coupon Code", Toast.LENGTH_SHORT).show();
                }
            }
        });


        apply = (TextView) findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        price = (TextView) findViewById(R.id.payment_amount);
        price.setText("Pay "+tot_amount+" INR");

        name_session = new Name_Session(getApplicationContext());
        phone_session = new Phone_Session(getApplicationContext());
        customer_session = new Customer_Session(getApplicationContext());

        bottom_dialog  = new Dialog(Payment_Activity.this,R.style.BottomDialog);
        bottom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams3 = new WindowManager.LayoutParams();
        bottom_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams3.copyFrom(bottom_dialog.getWindow().getAttributes());
        layoutParams3.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams3.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.gravity = Gravity.BOTTOM;
        bottom_dialog.getWindow().setAttributes(layoutParams2);
        bottom_dialog.setCancelable(true);
        bottom_dialog.setContentView(R.layout.seller_request_dialog);

        c_name = (EditText) bottom_dialog.findViewById(R.id.input_name);
        c_name.setText(name_session.getName());
        c_phone = (EditText) bottom_dialog.findViewById(R.id.input_phone);
        c_phone.setText(phone_session.getPhoneNO());


        btn_ok =   (FrameLayout) bottom_dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!c_name.getText().toString().trim().isEmpty() && !c_phone.getText().toString().trim().isEmpty()){
                    bottom_dialog.dismiss();
                    showProgressDialog();
                    postOrder();
                }else {
                    Toast.makeText(Payment_Activity.this, "Please fill it..", Toast.LENGTH_SHORT).show();
                }
            }
        });


        req_btn = (Button) findViewById(R.id.request_order);
        req_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_dialog.show();
            }
        });

    }

    void postOrder(){

        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(CUS_NAME,c_name.getText().toString().trim());
        postParam.put(CUS_PHONE,c_phone.getText().toString().trim());
        postParam.put(CUS_ID,customer_session.getCustomerID());
        postParam.put(PRODUCT_ID,product_id);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ORDER_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            hideProgressDialog();
                            bottom_dialog.dismiss();
                            String status = response.getString("status");
                            if (status.equals("200")){
                                startActivity(new Intent(getApplicationContext(),Thanks_Activity.class));
                                finish();
                            }else {
                                Toast.makeText(Payment_Activity.this, "Connection Error , try Again", Toast.LENGTH_SHORT).show();
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

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(Payment_Activity.this);
            mProgressDialog.setMessage("Loading..");
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
