package com.shoppers_era.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.shoppers_era.Adapters.Order_Adapter;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.Model.Order;
import com.shoppers_era.R;
import com.shoppers_era.Session.Customer_Session;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class My_Orders extends AppCompatActivity {


    private RecyclerView recyclerView;
    private static final String KEY_RES = "reseller_id";
    private static final String KEY_OFFSET= "offset";
    private static final String ORDER_URL = "https://apps.itrifid.com/shoppersera/rest_server/allResellerOrder/API-KEY/123456";
    private ProgressBar p_bar;
    private List<Order> orders= new ArrayList<>();
    private Order_Adapter mAdapter;
    private int offset=0;
    private Customer_Session customer_session;
    private ProgressDialog mProgressDialog;
    private LinearLayout lin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        customer_session=new Customer_Session(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.r_view);
        p_bar = (ProgressBar) findViewById(R.id.p_bar);
        lin = (LinearLayout) findViewById(R.id.empty_layout);

        getOrders();
    }

    void getOrders(){

        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_OFFSET, String.valueOf(offset));
        postParam.put(KEY_RES,customer_session.getCustomerID());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ORDER_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            offset = response.getInt("new_offset");
                            String status = response.getString("status");
                            if (status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("userData");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String order_id = jsonObject.getString("order_id");
                                    String product_id = jsonObject.getString("product_id");
                                    String inv_total = jsonObject.getString("invoice_total");
                                    String tracking_id = jsonObject.getString("shipment_status");
                                    String reseller_margin = jsonObject.getString("reseller_margin");
                                    String img_url = jsonObject.getString("thumb_img_dest");

                                    Order order = new Order(order_id,inv_total,reseller_margin,product_id,img_url,tracking_id);
                                    orders.add(order);
                                }

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());

                                mAdapter = new Order_Adapter(recyclerView,getApplicationContext(),orders);

                                recyclerView.setAdapter(mAdapter);

                                mAdapter.setOnLoadMoreListener(new Order_Adapter.OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        orders.add(null);
                                        mAdapter.notifyItemRemoved(orders.size());
                                        loadOrders();
                                    }
                                });

                                p_bar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                            }else {
                                p_bar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.GONE);
                                lin.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                p_bar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                lin.setVisibility(View.VISIBLE);
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    void loadOrders(){

        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_OFFSET, String.valueOf(offset));
        postParam.put(KEY_RES,customer_session.getCustomerID());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ORDER_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            offset = response.getInt("new_offset");
                            String status = response.getString("status");
                            if (status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("userData");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String order_id = jsonObject.getString("order_id");
                                    String product_id = jsonObject.getString("product_id");
                                    String inv_total = jsonObject.getString("invoice_total");
                                    String tracking_id = jsonObject.getString("shipment_status");
                                    String reseller_margin = jsonObject.getString("reseller_margin");
                                    String img_url = jsonObject.getString("thumb_img_dest");

                                    Order order = new Order(order_id,inv_total,reseller_margin,product_id,img_url,tracking_id);
                                    orders.add(order);
                                }


                                mAdapter.setLoaded();
                                mAdapter.notifyDataSetChanged();
                            }else {


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(My_Orders.this, "Connection Error", Toast.LENGTH_SHORT).show();
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

}
