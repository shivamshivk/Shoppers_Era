package com.shoppers_era.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import com.shoppers_era.Activities.Cat_Details;
import com.shoppers_era.Activities.Full_Image;
import com.shoppers_era.Activities.Main_Handler_Activity;
import com.shoppers_era.Adapters.Fav_Adapter;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.Model.Fav;
import com.shoppers_era.Model.Selection;
import com.shoppers_era.R;
import com.shoppers_era.Session.Customer_Session;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */

public class Fragment_Loved extends Fragment {

    private static final String KEY_ID = "c_id";
    private static final String FAV_URL = "https://apps.itrifid.com/shoppersera/rest_server/userLove/API-KEY/123456";
    private static final String LIKE_URL = "https://apps.itrifid.com/shoppersera/rest_server/userlikelist/API-KEY/123456";
    private RecyclerView recyclerView;
    private View rootView;
    private Fav_Adapter mAdapter;
    private List<Fav> favs = new ArrayList<>();
    private Customer_Session customer_session;
    private static final String PRODUCT_ID = "product_id";
    private static final String TAG = Cat_Details.class.getSimpleName();
    private ProgressBar p_bar;
    private SwipeRefreshLayout refresh;
    private Button btn_download;
    private RelativeLayout btn_share;
    private ProgressDialog mProgressDialog;
    private ArrayList<Uri> files ;
    private int total_selected=0;
    private LinearLayout lin;
    private List<Selection> images;
    private CheckBox share_all_check;
    private CheckBox ck;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    //Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private int id =0;
//    private AdView adView;
    public Fragment_Loved() {
        // Required empty public constructor
    }
    private LinearLayout empty_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView =inflater.inflate(R.layout.fragment_fragment__loved, container, false);

        lin= (LinearLayout) rootView.findViewById(R.id.lin);

        empty_layout=(LinearLayout) rootView.findViewById(R.id.empty_layout);

//        adView = (AdView) rootView.findViewById(R.id.adView);

        share_all_check = (CheckBox) rootView.findViewById(R.id.share);
        share_all_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mAdapter.selectAll();
                }else {
                    mAdapter.deselectAll();
                }
            }
        });

        btn_download = (Button) rootView.findViewById(R.id.download_btn);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<favs.size();i++){

                    if(favs.get(i).getSelect().equals(true)){

                        showProgressDialog();
                        mProgressDialog.setMessage("Downloading..");


                        Thread shivThread1 = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            downloadImages();
                                        }
                                    });

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        };

                        shivThread1.start();

                        break;
                    }else {
                        if(i==favs.size()-1){
                            Toast.makeText(getActivity(), "Select at least one image", Toast.LENGTH_SHORT).show();
                        }
                    }


                }



            }
        });

        btn_share = (RelativeLayout) rootView.findViewById(R.id.sahre_btn);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int k=0;k<images.size();k++) {

                    if (images.get(k).getSelect().equals(true)) {
                        shareImages();
                        break;

                    }else {
                        if(k==images.size()-1){
                            Toast.makeText(getActivity(), "Select at least one image", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
        });

        customer_session= new Customer_Session(getActivity());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

//        recyclerView.addOnItemTouchListener(new Diary_Adapter.RecyclerTouchListener(getActivity(), recyclerView, new Diary_Adapter.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//
//                for (int i=0;i<favs.size();i++){
//                    Selection selection = new Selection(favs.get(i).getImg_url(),favs.get(i).getSelect(),"",favs.get(i).getSelling_price(),favs.get(i).getActual_price(),favs.get(i).getCat_name(),true);
//                    images.add(selection);
//                }
//
//                Intent intent = new Intent(getActivity(),Full_Image.class);
//                Bundle data = new Bundle();
//                data.putSerializable("images", (Serializable) images);
//                intent.putExtra("ca",data);
//                intent.putExtra("pos",position);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

        p_bar = (ProgressBar) rootView.findViewById(R.id.p_bar);

        refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.simpleSwipeRefreshLayout);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                p_bar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                lin.setVisibility(View.GONE);
                empty_layout.setVisibility(View.GONE);
                getData();
                refresh.setRefreshing(false);
            }
        });

        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
    }

    void shareImages(){

        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }else {

            if (whatsappInstalledOrNot("com.whatsapp") && whatsappInstalledOrNot("com.whatsapp.w4b")){
                String title = "Send to :";
                CharSequence[] itemlist ={"Whatsapp",
                        "Whatsapp Business",
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //builder.setIcon(R.drawable.icon_app);
                builder.setTitle(title);
                builder.setItems(itemlist, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// Take Photo
                                id=0;
                                total_selected=0;
                                for (int i=0;i<favs.size();i++){
                                    if (favs.get(i).getSelect().equals(true)){
                                        total_selected+=1;
                                    }
                                }

                                files=new ArrayList<>();
                                for(int j=0;j<favs.size();j++){

                                    if(favs.get(j).getSelect().equals(true)){
                                        String img_url = favs.get(j).getImg_url();
                                        new ShareTask().execute(img_url);
                                    }

                                }
                                break;
                            case 1:// Choose Existing Photo
                                id =1;
                                total_selected=0;
                                for (int i=0;i<favs.size();i++){
                                    if (favs.get(i).getSelect().equals(true)){
                                        total_selected+=1;
                                    }
                                }

                                files=new ArrayList<>();
                                for(int j=0;j<favs.size();j++){

                                    if(favs.get(j).getSelect().equals(true)){
                                        String img_url = favs.get(j).getImg_url();
                                        new ShareTask().execute(img_url);
                                    }

                                }
                                break;

                            default:
                                break;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();

            }else {
                total_selected=0;
                for (int i=0;i<favs.size();i++){
                    if (favs.get(i).getSelect().equals(true)){
                        total_selected+=1;
                    }
                }

                files=new ArrayList<>();
                for(int j=0;j<favs.size();j++){

                    if(favs.get(j).getSelect().equals(true)){
                        String img_url = favs.get(j).getImg_url();
                        new ShareTask().execute(img_url);
                    }

                }
            }

        }

    }


    private class ShareTask extends AsyncTask<String,Integer,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... param) {
            return getBitmapFromURL(param[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            Uri uri = getImageUri(getActivity(), bitmap);
            files.add(uri);

            if (total_selected==files.size()){

                final Thread thread= new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            Thread.sleep(1000);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                                    intent.setType("image/jpeg"); /* This example is sharing jpeg images. */
                                    if(id==0){
                                        intent.setPackage("com.whatsapp");
                                    }else {
                                        intent.setPackage("com.whatsapp.w4b");
                                    }
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                                    startActivity(Intent.createChooser(intent, "Send these images"));
                                    hideProgressDialog();
                                }
                            });


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });

                thread.start();


            }
        }

    }



    public static Bitmap getBitmapFromURL(String src) {

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void downloadImages() {

        for (int i = 0; i < favs.size(); i++) {

            if(favs.get(i).getSelect().equals(true)){
                String img_url = favs.get(i).getImg_url();
                new DownloadTask().execute(img_url);
            }

            if(i==favs.size()-1){
                hideProgressDialog();
                Toast.makeText(getActivity(), "Images Downloaded Successfully", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class DownloadTask extends AsyncTask<String,Integer,Bitmap>{

        final String Dir = "Fav";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... param) {
            return getBitmapFromURL(param[0]);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            try {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/~AK " + Dir);

                if (!myDir.exists()) {
                    myDir.mkdirs();
                }

                String name = new Date().getTime() + ".jpg";
                myDir = new File(myDir, name);
                FileOutputStream out = new FileOutputStream(myDir);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                out.flush();
                out.close();
                Log.i("TAG", "scanning File " + myDir.getAbsolutePath());
                MediaScannerConnection.scanFile(getActivity(),
                        new String[]{myDir.getAbsolutePath()}, null, null);


                // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myDir)));


                Log.i("TAG", "Agter scanning" + myDir.getAbsolutePath());

            } catch (Exception e) {
                // some action
            }


            String root = Environment.getExternalStorageDirectory().toString();

            File filePath = new File(root + "/~AK " + Dir);

            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(String.valueOf(filePath)))));

            mProgressDialog.hide();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
    }


    void getData(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_ID,customer_session.getCustomerID());


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                FAV_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){

                                favs.clear();

                                JSONArray jsonArray = response.getJSONArray("picData");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                JSONArray jsonArray1 = jsonObject.getJSONArray("properties");

                                for (int i=0;i<jsonArray1.length();i++){

                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                                    JSONObject properties = jsonObject1.getJSONObject("product_data");

                                    String actual_price = properties.getString("actual_price");
                                    String selling_p = properties.getString("selling_price");
                                    String img_url = properties.getString("original_img_dest");
                                    String img_id = properties.getString("img_id");
                                    String count_all = properties.getString("like_count");
                                    String image_name = properties.getString("image_name");

                                    Fav fav = new Fav("",actual_price,selling_p,img_url,false,img_id,count_all,true,image_name);
                                    favs.add(fav);
                                }

                                images = new ArrayList<>();

                                for (int i=0;i<favs.size();i++){
                                    Selection selection = new Selection(favs.get(i).getImg_url(),favs.get(i).getSelect(),"",favs.get(i).getSelling_price(),favs.get(i).getActual_price(),favs.get(i).getImage_name(),"",true);
                                    images.add(selection);
                                }


                                mAdapter = new Fav_Adapter(getActivity(), favs, new Fav_Adapter.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {

                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }

                                    @Override
                                    public void onImgClick(View view, int position) {
                                        Intent intent = new Intent(getActivity(),Full_Image.class);
                                        Bundle data = new Bundle();
                                        data.putSerializable("images", (Serializable) images);
                                        intent.putExtra("ca",data);
                                        intent.putExtra("pos",position);
                                        intent.putExtra("cat_name",images.get(position).getCat_name());
                                        intent.putExtra("id",3);
                                        intent.putExtra("img_name",images.get(position).getImage_name());
                                        intent.putExtra("pNo",((Main_Handler_Activity) getActivity()).wp_no);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFavClick(View v, int pos, View v2) {
                                        showProgressDialog();
                                        addToFav((ImageView)v,favs.get(pos).getImg_id(),(TextView) v2);
                                    }
                                });

                                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mAdapter);

                                recyclerView.setVisibility(View.VISIBLE);
                                p_bar.setVisibility(View.GONE);
                                lin.setVisibility(View.VISIBLE);
                                empty_layout.setVisibility(View.GONE);

                            }else {
                                recyclerView.setVisibility(View.GONE);
                                p_bar.setVisibility(View.GONE);
                                lin.setVisibility(View.GONE);
                                empty_layout.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Connection Error", Toast.LENGTH_LONG).show();
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


    private void addToFav(final ImageView v, String img_id, final TextView t) {

        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_ID,customer_session.getCustomerID());
        postParam.put(PRODUCT_ID,img_id);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                LIKE_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideProgressDialog();

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){
                                v.setImageResource(R.drawable.heart_re);
                                t.setText(response.getString("all_like_counter"));
                            }else if (status.equals("208")){
                                v.setImageResource(R.drawable.heart_outline);
                                t.setText(response.getString("all_like_counter"));
                                p_bar.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                lin.setVisibility(View.GONE);
                                getData();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


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

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

}
