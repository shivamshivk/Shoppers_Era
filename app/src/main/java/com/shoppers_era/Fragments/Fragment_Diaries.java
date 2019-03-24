package com.shoppers_era.Fragments;


import com.shoppers_era.Activities.Diary_Full;
import com.shoppers_era.Activities.Main_Handler_Activity;
import com.shoppers_era.Adapters.Diary_Adapter;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.Model.Diary;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.shoppers_era.R;

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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Diaries extends Fragment {

    private static final String DIARY_URL = "https://apps.itrifid.com/shoppersera/rest_server/getDiariesContents/API-KEY/123456";

    public Fragment_Diaries() {
        // Required empty public constructor
    }

    private SwipeRefreshLayout refresh;
    private RecyclerView listView;
    private View rootView;
    private List<Diary> diaries=new ArrayList<>();
    private ProgressBar pBar;
    private Diary_Adapter mAdapter;
    private TextView cat_name;
    private TextView desc;
    private RelativeLayout btn_share;
    private Button btn_download;
    private CheckBox share;
    private int pos;
    private ProgressDialog mProgressDialog;
    private ArrayList<Uri> files ;
    private int total_selected=0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private RelativeLayout relativeLayout;
    private int id =0;
    private AdView adView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_diaries, container, false);

        pBar = (ProgressBar) rootView.findViewById(R.id.p_bar);

        relativeLayout = (RelativeLayout)rootView.findViewById(R.id.rel);

        cat_name = (TextView) rootView.findViewById(R.id.cat_name);
        desc = (TextView) rootView.findViewById(R.id.cat_desc);

        adView = (AdView) rootView.findViewById(R.id.adView);

        share = (CheckBox) rootView.findViewById(R.id.share);
        share.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mAdapter.selectAll();
                }else {
                    mAdapter.deselectAll();
                }
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        listView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        listView.setNestedScrollingEnabled(false);

        btn_download = (Button) rootView.findViewById(R.id.download_btn);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<diaries.size();i++){
                    if(diaries.get(i).getSelect().equals(true)){

                        showProgressDialog();

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
                        if(i==diaries.size()-1){
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


                for(int k=0;k<diaries.size();k++) {

                    if (diaries.get(k).getSelect().equals(true)) {
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
                                            try
                                            {
                                                id=0;
                                                total_selected=0;
                                                for (int i=0;i<diaries.size();i++){
                                                    if (diaries.get(i).getSelect().equals(true)){
                                                        total_selected+=1;
                                                    }
                                                }

                                                files=new ArrayList<>();
                                                for(int j=0;j<diaries.size();j++){

                                                    if(diaries.get(j).getSelect().equals(true)){
                                                        String img_url = diaries.get(j).getImg_url();
                                                        new ShareTask().execute(img_url);
                                                    }

                                                }

                                            }catch (NullPointerException e){
                                                e.printStackTrace();
                                            }
                                            break;
                                        case 1:// Choose Existing Photo
                                            try {
                                                id=1;
                                                total_selected=0;
                                                for (int i=0;i<diaries.size();i++){
                                                    if (diaries.get(i).getSelect().equals(true)){
                                                        total_selected+=1;
                                                    }
                                                }

                                                files=new ArrayList<>();
                                                for(int j=0;j<diaries.size();j++){

                                                    if(diaries.get(j).getSelect().equals(true)){
                                                        String img_url = diaries.get(j).getImg_url();
                                                        new ShareTask().execute(img_url);
                                                    }

                                                }
                                            }catch (NullPointerException e){
                                                e.printStackTrace();
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
                            for (int i=0;i<diaries.size();i++){
                                if (diaries.get(i).getSelect().equals(true)){
                                    total_selected+=1;
                                }
                            }

                            files=new ArrayList<>();
                            for(int j=0;j<diaries.size();j++){

                                if(diaries.get(j).getSelect().equals(true)){
                                    String img_url = diaries.get(j).getImg_url();
                                    new ShareTask().execute(img_url);
                                }

                            }
                        }

                        break;

                    }else {
                        if(k==diaries.size()-1){
                            Toast.makeText(getActivity(), "Select at least one image", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }

        });

        refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.simpleSwipeRefreshLayout);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                getDiaries1();
                refresh.setRefreshing(false);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDiaries1();
    }

//    void getDiaries(){
//
//            String tag_json_obj = "json_obj_req";
//
//            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                    DATA_URL, null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                diaries.clear();
//
//                                String status = response.getString("status");
//
//                                if (status.equals("200")){
//
//                                    JSONArray array = response.getJSONArray("userData");
//
//                                    JSONObject json = array.getJSONObject(0);
//
//                                    JSONArray prop = json.getJSONArray("properties");
//
//                                    for (int i=0;i<prop.length();i++){
//
//                                        JSONObject json1 = prop.getJSONObject(i);
//
//                                        String img_url = json1.getString("original_img_url");
//                                        String text = json1.getString("comment");
//
//                                        diaries.add(new Diary(img_url,text));
//
//                                    }
//
//
//                                     mAdapter= new Diary_Adapter(getActivity(),diaries);
//
//                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//                                    listView.setLayoutManager(mLayoutManager);
//                                    listView.setItemAnimator(new DefaultItemAnimator());
//                                    listView.setAdapter(mAdapter);
//
//                                    listView.setVisibility(View.VISIBLE);
//                                    pBar.setVisibility(View.GONE);
//
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                }
//            });
//
//// Adding request to request queue
//            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//
//
//    }


    void getDiaries1(){

        String tag_json_obj = "json_obj_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                DIARY_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            diaries.clear();

                            String status = response.getString("status");

                            if (status.equals("200")){

                                JSONArray array = response.getJSONArray("userData");

                                for (int i=0;i<array.length();i++){
                                    JSONObject jsonObject = array.getJSONObject(i);

                                    String img_url = jsonObject.getString("original_img_dest");
                                    String cat_name = jsonObject.getString("category_name");
                                    String desc = jsonObject.getString("description");
                                    String content1 = jsonObject.getString("content_1");
                                    String content2 = jsonObject.getString("content_2");

                                    diaries.add(new Diary(img_url,cat_name,desc,content1,content2,false));
                                }

                                cat_name.setText("");
                                desc.setText(diaries.get(0).getDesc());

                                mAdapter= new Diary_Adapter(getActivity(), diaries, new Diary_Adapter.ClickListener() {
                                    @Override
                                    public void onFavClick(View v, int pos, View v2) {

                                    }

                                    @Override
                                    public void onClick(View view, int position) {

                                    }

                                    @Override
                                    public void onImgClick(View view, int position) {
                                        Intent intent = new Intent(getActivity(),Diary_Full.class);
                                        intent.putExtra("pos",position);
                                        Bundle data = new Bundle();
                                        data.putSerializable("diaries", (Serializable) diaries);
                                        intent.putExtra("ca",data);
                                        intent.putExtra("cat_name",diaries.get(position).getCat_name());
                                        intent.putExtra("id",0);
                                        intent.putExtra("pNo",((Main_Handler_Activity) getActivity()).wp_no);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }
                                });

                                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                                listView.setLayoutManager(staggeredGridLayoutManager);
                                listView.setItemAnimator(new DefaultItemAnimator());
                                listView.setAdapter(mAdapter);

                                listView.setVisibility(View.VISIBLE);
                                pBar.setVisibility(View.GONE);
                                relativeLayout.setVisibility(View.VISIBLE);


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


    public void downloadImages() {

        for (int i = 0; i < diaries.size(); i++) {

            if(diaries.get(i).getSelect().equals(true)){
                pos = i;
                String img_url = diaries.get(i).getImg_url();
                new DownloadTask().execute(img_url);
            }

            if(i==diaries.size()-1){
                Toast.makeText(getActivity(), "Images Downloaded Successfully", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }

        }
    }


    private void scanFile(String path) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA,""+path);
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
        getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Log.i("TAG", "Agter scanning" +path);
    }


    private class DownloadTask extends AsyncTask<String,Integer,Bitmap> {

        final String Dir = diaries.get(pos).getCat_name();

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

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
    }

    void shareImages(){

        total_selected=0;
        for (int i=0;i<diaries.size();i++){
            if (diaries.get(i).getSelect().equals(true)){
                total_selected+=1;
            }
        }

        files=new ArrayList<>();
        for(int j=0;j<diaries.size();j++){

            if(diaries.get(j).getSelect().equals(true)){
                String img_url = diaries.get(j).getImg_url();
                new ShareTask().execute(img_url);
            }

        }

    }

    private  class ShareTask extends AsyncTask<String,Integer,Bitmap>{

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
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            return BitmapFactory.decodeStream(input);
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
