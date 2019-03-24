package com.shoppers_era.Activities;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import com.shoppers_era.Adapters.Search_Adapter;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.Model.Fav;
import com.shoppers_era.Model.Selection;
import com.shoppers_era.R;
import com.shoppers_era.Session.Customer_Session;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class Search_Activity extends AppCompatActivity {


    private static final String SEARCH_URL = "https://apps.itrifid.com/shoppersera/rest_server/searchForProducts/API-KEY/123456";
    private static final String KEY_TERM = "search_term";
    private static final String KEY_ID = "c_id";
    private AutoCompleteTextView autoCompleteTextView;
    private ImageView search_icon;
    private ImageView left_icon;
    private List<Fav> lists = new ArrayList<>();
    private RecyclerView recyclerView;
    private Customer_Session customer_session;
    private Search_Adapter mAdapter;
    private ProgressBar p_bar;
    private LinearLayout empty_layout;
    private LinearLayout linearLayout;
    private Button btn_download;
    private ArrayList<Uri> files ;
    private int total_selected=0;
    private ProgressDialog mProgressDialog;
    private List<Selection> images;
    int id =0;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    //Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String pNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_);


        Intent intent=getIntent();
        Bundle extras = intent.getExtras();
        if (extras!=null){
            pNo = extras.getString("pNo");
        }

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.search_commerce);
        search_icon = (ImageView) findViewById(R.id.search_ico);
        left_icon = (ImageView) findViewById(R.id.back_button);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //        recyclerView.addOnItemTouchListener(new Diary_Adapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new Diary_Adapter.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//
//                for (int i=0;i<lists.size();i++){
//                    Selection selection = new Selection(lists.get(i).getImg_url(),lists.get(i).getSelect(),"",lists.get(i).getSelling_price(),lists.get(i).getActual_price(),lists.get(i).getCat_name(),true);
//                    images.add(selection);
//                }
//
//                Intent intent = new Intent(getApplicationContext(),Full_Image.class);
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



        btn_download = (Button) findViewById(R.id.download_btn);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    for(int i=0;i<lists.size();i++){

                        if(lists.get(i).getSelect().equals(true)){

                            Thread shivThread1 = new Thread() {
                                @Override
                                public void run() {

                                    try {
                                        Thread.sleep(500);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                showProgressDialog();
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
                            if(i==lists.size()-1){
                                Toast.makeText(Search_Activity.this, "Select at least one image", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }

                }catch (NullPointerException e){
                    e.printStackTrace();
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
        });

//        btn_share = (RelativeLayout) findViewById(R.id.sahre_btn);
//        btn_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Check if we have write permission
//                int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//                if (permission != PackageManager.PERMISSION_GRANTED) {
//                    // We don't have permission so prompt the user
//                    ActivityCompat.requestPermissions(
//                            Search_Activity.this,
//                            PERMISSIONS_STORAGE,
//                            REQUEST_EXTERNAL_STORAGE
//                    );
//                }else {
//
//                    if (whatsappInstalledOrNot("com.whatsapp") && whatsappInstalledOrNot("com.whatsapp.w4b")){
//                        String title = "Send to :";
//                        CharSequence[] itemlist ={"Whatsapp",
//                                "Whatsapp Business",
//                        };
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(Search_Activity.this);
//                        //builder.setIcon(R.drawable.icon_app);
//                        builder.setTitle(title);
//                        builder.setItems(itemlist, new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                switch (which) {
//                                    case 0:// Take Photo
//                                        try {
//                                            id=0;
//                                            total_selected=0;
//                                            for (int i=0;i<lists.size();i++){
//                                                if (lists.get(i).getSelect().equals(true)){
//                                                    total_selected+=1;
//                                                }
//                                            }
//
//                                            files=new ArrayList<>();
//                                            for(int j=0;j<lists.size();j++){
//
//                                                if(lists.get(j).getSelect().equals(true)){
//                                                    String img_url = lists.get(j).getImg_url();
//                                                    new ShareTask().execute(img_url);
//                                                }
//
//                                            }
//
//                                        }catch (NullPointerException e){
//                                        e.printStackTrace();
//                                        }catch (IndexOutOfBoundsException e){
//                                            e.printStackTrace();
//                                        }
//                                        break;
//                                    case 1:// Choose Existing Photo
//                                        try {
//                                            id=1;
//                                            total_selected=0;
//                                            for (int i=0;i<lists.size();i++){
//                                                if (lists.get(i).getSelect().equals(true)){
//                                                    total_selected+=1;
//                                                }
//                                            }
//
//                                            files=new ArrayList<>();
//                                            for(int j=0;j<lists.size();j++){
//
//                                                if(lists.get(j).getSelect().equals(true)){
//                                                    String img_url = lists.get(j).getImg_url();
//                                                    new ShareTask().execute(img_url);
//                                                }
//
//                                            }
//
//                                        }catch (NullPointerException e){
//                                            e.printStackTrace();
//                                        }catch (IndexOutOfBoundsException e){
//                                            e.printStackTrace();
//                                        }
//
//                                        break;
//
//                                    default:
//                                        break;
//                                }
//                            }
//                        });
//                        AlertDialog alert = builder.create();
//                        alert.setCancelable(true);
//                        alert.show();
//
//                    }else {
//                        try{
//                            total_selected=0;
//                            for (int i=0;i<lists.size();i++){
//                                if (lists.get(i).getSelect().equals(true)){
//                                    total_selected+=1;
//                                }
//                            }
//
//                            files=new ArrayList<>();
//                            for(int j=0;j<lists.size();j++){
//
//                                if(lists.get(j).getSelect().equals(true)){
//                                    String img_url = lists.get(j).getImg_url();
//                                    new ShareTask().execute(img_url);
//                                }
//
//                            }
//
//                        }catch (NullPointerException e){
//                            e.printStackTrace();
//                        }catch (IndexOutOfBoundsException e){
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//            }
//        });


        linearLayout = (LinearLayout) findViewById(R.id.lin);

        p_bar = (ProgressBar) findViewById(R.id.p_bar);

        empty_layout = (LinearLayout) findViewById(R.id.empty_layout);

        customer_session = new Customer_Session(getApplicationContext());

        left_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()) {
                    search_icon.setVisibility(View.VISIBLE);
                }
            }
        });

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(lists.isEmpty()){
                    hideSoftKeyboard();
                    p_bar.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    empty_layout.setVisibility(View.GONE);
                    search_Kurtis();
                }else if (!lists.isEmpty()){
                    lists.clear();
                    mAdapter.notifyDataSetChanged();
                    hideSoftKeyboard();
                    p_bar.setVisibility(View.VISIBLE);
                    empty_layout.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    search_Kurtis();
                }
                return false;
            }
        });

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lists.isEmpty()){

                    hideSoftKeyboard();
                    p_bar.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    empty_layout.setVisibility(View.GONE);
                    search_Kurtis();
                }else if (!lists.isEmpty()){
                    lists.clear();
                    mAdapter.notifyDataSetChanged();
                    hideSoftKeyboard();
                    p_bar.setVisibility(View.VISIBLE);
                    empty_layout.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    search_Kurtis();
                }
            }
        });
    }



    private class DownloadTask extends AsyncTask<String,Integer,Bitmap> {

        final String Dir = "Search";

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
                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{myDir.getAbsolutePath()}, null, null);


                // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(myDir)));


                Log.i("TAG", "Agter scanning" + myDir.getAbsolutePath());

            } catch (Exception e) {
                // some action
            }


            String root = Environment.getExternalStorageDirectory().toString();

            File filePath = new File(root + "/~AK " + Dir);

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(String.valueOf(filePath)))));


        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }
    }

    public void downloadImages() {

        for (int i = 0; i < lists.size(); i++) {

            if(lists.get(i).getSelect().equals(true)){
                String img_url = lists.get(i).getImg_url();
                new DownloadTask().execute(img_url);
            }

            if(i==lists.size()-1){
                hideProgressDialog();
                Toast.makeText(this, "Images Downloaded Successfully", Toast.LENGTH_SHORT).show();
            }

        }
    }




    void shareImages(){

        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    Search_Activity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }else {

            if (whatsappInstalledOrNot("com.whatsapp") && whatsappInstalledOrNot("com.whatsapp.w4b")){
                String title = "Send to :";
                CharSequence[] itemlist ={"Whatsapp",
                        "Whatsapp Business",
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                //builder.setIcon(R.drawable.icon_app);
                builder.setTitle(title);
                builder.setItems(itemlist, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:// Take Photo
                                id=0;
                                total_selected=0;
                                for (int i=0;i<lists.size();i++){
                                    if (lists.get(i).getSelect().equals(true)){
                                        total_selected+=1;
                                    }
                                }

                                files=new ArrayList<>();
                                for(int j=0;j<lists.size();j++){

                                    if(lists.get(j).getSelect().equals(true)){
                                        String img_url = lists.get(j).getImg_url();
                                        new ShareTask().execute(img_url);
                                    }

                                }
                                break;
                            case 1:// Choose Existing Photo
                                id =1;
                                total_selected=0;
                                for (int i=0;i<lists.size();i++){
                                    if (lists.get(i).getSelect().equals(true)){
                                        total_selected+=1;
                                    }
                                }

                                files=new ArrayList<>();
                                for(int j=0;j<lists.size();j++){

                                    if(lists.get(j).getSelect().equals(true)){
                                        String img_url = lists.get(j).getImg_url();
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
                for (int i=0;i<lists.size();i++){
                    if (lists.get(i).getSelect().equals(true)){
                        total_selected+=1;
                    }
                }

                files=new ArrayList<>();
                for(int j=0;j<lists.size();j++){

                    if(lists.get(j).getSelect().equals(true)){
                        String img_url = lists.get(j).getImg_url();
                        new ShareTask().execute(img_url);
                    }

                }
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

            Uri uri = getImageUri(getApplicationContext(), bitmap);
            files.add(uri);

            if (total_selected==files.size()){

                final Thread thread= new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            Thread.sleep(1000);

                            runOnUiThread(new Runnable() {
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
                                    mProgressDialog.hide();
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



    void search_Kurtis(){

        lists.clear();

        p_bar.setVisibility(View.VISIBLE);

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_TERM,autoCompleteTextView.getText().toString().trim());
        postParam.put(KEY_ID,customer_session.getCustomerID());


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                SEARCH_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("picData");

                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    JSONObject properties = jsonObject.getJSONObject("product_data");

                                    String cat_name = properties.getString("image_name");
                                    String img_url = properties.getString("original_img_dest");
                                    String price = properties.getString("selling_price");
                                    String actual_price = properties.getString("actual_price");
                                    String img_id = properties.getString("img_id");

                                    Fav fav = new Fav(cat_name,actual_price,price,img_url,false,img_id);
                                    lists.add(fav);

                                }

                                mAdapter = new Search_Adapter(getApplicationContext(), lists, new Search_Adapter.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {

                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }

                                    @Override
                                    public void onImgClick(View view, int position) {

                                        images = new ArrayList<>();

                                        for (int i=0;i<lists.size();i++){
                                            Selection selection = new Selection(lists.get(i).getImg_url(),lists.get(i).getSelect(),"",lists.get(i).getSelling_price(),lists.get(i).getActual_price(),lists.get(i).getCat_name(),"",false);
                                            images.add(selection);
                                        }

                                        Intent intent = new Intent(getApplicationContext(),Full_Image.class);
                                        Bundle data = new Bundle();
                                        data.putSerializable("images", (Serializable) images);
                                        intent.putExtra("ca",data);
                                        intent.putExtra("pos",position);
                                        intent.putExtra("cat_name",images.get(position).getCat_name());
                                        intent.putExtra("id",images.get(position).getImg_id());
                                        intent.putExtra("pNo",pNo);
                                        startActivity(intent);
                                    }
                                });

                                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mAdapter);

                                recyclerView.setVisibility(View.VISIBLE);
                                p_bar.setVisibility(View.GONE);
                                empty_layout.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                                btn_download.setVisibility(View.VISIBLE);

                            }else {
                                recyclerView.setVisibility(View.GONE);
                                p_bar.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.GONE);
                                empty_layout.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),"Connection Error", Toast.LENGTH_LONG).show();
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


    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
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
