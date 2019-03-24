package com.shoppers_era.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shoppers_era.R;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.Session.Customer_Session;


public class Fragment_View extends Fragment {


    private View rootview;
    private Button view;
    private ImageView img;
    private static final String REWARD_URL = "https://apps.itrifid.com/shoppersera/rest_server/CouponCodeForAd/API-KEY/123456";
    private static  final String IMAGE_URL= "https://apps.itrifid.com/shoppersera/rest_server/getvieandwininfo/API-KEY/123456";
    private ProgressBar progressBar;
    private LinearLayout empty_layout;
//    private AdView adView;
//    private AdView adView1;
//    private RewardedVideoAd ad;
    private Customer_Session customer_session;
    private ProgressDialog mProgressDialog;

    public Fragment_View() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootview =inflater.inflate(R.layout.fragment_fragment__view, container, false);

        progressBar = (ProgressBar) rootview.findViewById(R.id.p_bar);
        empty_layout = (LinearLayout) rootview.findViewById(R.id.empty_layout);

        img = (ImageView) rootview.findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showAd();
            }
        });

//        adView = (AdView) rootview.findViewById(R.id.adView);
//        adView1 = (AdView) rootview.findViewById(R.id.adView1);


        customer_session = new Customer_Session(getActivity());

//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//
//        AdRequest adRequest1 = new AdRequest.Builder().build();
//        adView1.loadAd(adRequest1);
//
        // Use an activity context to get the rewarded video instance.
//        ad = MobileAds.getRewardedVideoAdInstance(getActivity());
//        ad.setRewardedVideoAdListener(new RewardedVideoAdListener() {
//            @Override
//            public void onRewardedVideoAdLoaded() {
//
//            }
//
//            @Override
//            public void onRewardedVideoAdOpened() {
//
//            }
//
//            @Override
//            public void onRewardedVideoStarted() {
//
//            }
//
//            @Override
//            public void onRewardedVideoAdClosed() {
//                startGame();
//            }
//
//            @Override
//            public void onRewarded(RewardItem rewardItem) {
//                rewardUser();
//            }
//
//            @Override
//            public void onRewardedVideoAdLeftApplication() {
//
//            }
//
//            @Override
//            public void onRewardedVideoAdFailedToLoad(int i) {
//
//                startGame();
//            }
//        });

        view = (Button) rootview.findViewById(R.id.open_btn);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        });

        progressBar = (ProgressBar) rootview.findViewById(R.id.p_bar);

        getImg();

       return rootview;
    }

    void rewardUser(){

        String tag_json_obj = "json_obj_req";

        HashMap<String,String> postParam = new HashMap<>();
        postParam.put("c_id",customer_session.getCustomerID());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                REWARD_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideProgressDialog();

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){
                                Toast.makeText(getActivity(), "Congratulations! You are rewarded", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    void getImg(){

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                IMAGE_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("userData");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String img_url = jsonObject.getString("img_url");
                                Picasso.with(getActivity()).load(img_url).noFade().placeholder(R.drawable.phimg).into(img);

                                if (jsonObject.getString("add_status").equals("1")){
//                                    startGame();
                                }else {
                                    //do nothing for now
                                }

                                empty_layout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);

                            }else {
                                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

//    void startGame(){
//        ad.loadAd(getString(R.string.ad_video_id), new AdRequest.Builder().build());
//    }
//
//    public void showAd(){
//        if (ad.isLoaded()){
//            ad.show();
//        }else {
//            Toast.makeText(getActivity(), "Reward is not prepared, please try again after some minutes", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
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


    @Override
    public void onPause() {
        super.onPause();
//        ad.pause(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
//        ad.resume(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ad.destroy(getActivity());
    }

}

