package com.shoppers_era.Fragments;


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
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.shoppers_era.Activities.Cat_Details;
import com.shoppers_era.Activities.Full_Image;
import com.shoppers_era.Adapters.Cat_Image_Adapter;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.Model.Categories;
import com.shoppers_era.Model.Selection;
import com.shoppers_era.R;
import com.shoppers_era.Session.Customer_Session;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_cat_details extends Fragment {

    private static final String FAV_URL = "https://apps.itrifid.com/shoppersera/rest_server/userlikelist/API-KEY/123456";
    private static final String KEY_ID = "c_id";
    private static final String PRODUCT_ID = "product_id";
    private static final String TAG = Cat_Details.class.getSimpleName();
    private static final String DATA_URL = "https://apps.itrifid.com/shoppersera/rest_server/catcontent/API-KEY/123456";
    private static final String KEY_CAT_ID = "category_id";
    private static final String KEY_C_ID = "c_id";
    private ArrayList<Selection> images=new ArrayList<>();
    private TextView cat_text;
    private TextView catno;
    private TextView catdesc;
    private TextView tag_name;
    private TextView tag_name_2;
    private RecyclerView recyclerView;
    // Progress Dialog
    private ProgressDialog mProgressDialog;
    private Button btn_download;
    private RelativeLayout btn_share;
    private CheckBox share_all_check;
    private int total_selected=0;
    private ArrayList<Uri> files ;
    private RelativeLayout relativeLayout;
    private Customer_Session customer_session;
    private ArrayList<Categories> arrays;
    private ProgressBar p_bar;
    private int position;
    private Cat_Image_Adapter mAdapter;
    //Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private int id =0;
    private Boolean isLiked;
    private View rootView;


    public Fragment_cat_details() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_fragment_cat_details, container, false);

        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/arial.ttf");

        customer_session = new Customer_Session(getActivity());

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();

        if(extras!=null){
            position= extras.getInt("pos");
            Bundle data = extras.getBundle("ca");
            arrays = (ArrayList<Categories>) data.getSerializable("categories");
        }

        getDetails();

        p_bar = (ProgressBar) rootView.findViewById(R.id.p_bar);

        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.nested_scroll);

        cat_text = (TextView) rootView.findViewById(R.id.cat_name);
        cat_text.setText(arrays.get(position).getCat_name());
        cat_text.setTypeface(tf1);

        catno = (TextView) rootView.findViewById(R.id.cat_no);
        catno.setTypeface(tf1);

        catdesc = (TextView) rootView.findViewById(R.id.cat_desc);
        catdesc.setText(arrays.get(position).getCat_desc());
        catdesc.setTypeface(tf1);

        tag_name = (TextView) rootView.findViewById(R.id.newly_added);
        tag_name.setText(extras.getString("tag_name"));

        tag_name_2 = (TextView) rootView.findViewById(R.id.newly_added_1);
        tag_name_2.setText(extras.getString("tag_name_2"));

        if (extras.getString("tag_name").equals("Default") || extras.getString("tag_name").equals("")){
            tag_name.setVisibility(View.GONE);
        }

        if (extras.getString("tag_name_2").equals("Default") ||extras.getString("tag_name_2").equals("") ){
            tag_name_2.setVisibility(View.GONE);
        }

        try{
            tag_name.setBackgroundColor(Color.parseColor(extras.getString("tag_color")));
            tag_name_2.setBackgroundColor(Color.parseColor(extras.getString("tag_color_2")));
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Downloading file. Please wait...");
        mProgressDialog.setCanceledOnTouchOutside(false);


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

                for(int i=0;i<images.size();i++){

                    if(images.get(i).getSelect().equals(true)){

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
                        if(i==images.size()-1){
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
                                        try{
                                            id=0;
                                            shareImages();
                                        }catch (NullPointerException e){
                                        e.printStackTrace();
                                        }
                                        break;
                                    case 1:// Choose Existing Photo
                                        try{
                                            id=1;
                                            shareImages();
                                        }catch (NullPointerException e){
                                            e.printStackTrace();
                                        }break;

                                    default:
                                        break;
                                }
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.setCancelable(true);
                        alert.show();

                    }else {

                        try{
                            total_selected=0;
                            for (int j=0;j<images.size();j++){
                                if (images.get(j).getSelect().equals(true)){
                                    total_selected+=1;
                                }
                            }

                            files=new ArrayList<>();
                            for(int j=0;j<images.size();j++){

                                if(images.get(j).getSelect().equals(true)){
                                    String img_url = images.get(j).getImg_url();
                                    new ShareTask().execute(img_url);
                                }

                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    }

                }


            }
        });

        return rootView;
    }


    public void downloadImages() {

        for (int i = 0; i < images.size(); i++) {

            if(images.get(i).getSelect().equals(true)){
                String img_url = images.get(i).getImg_url();
                new DownloadTask().execute(img_url);
            }

            if(i==images.size()-1){
                mProgressDialog.hide();
                Toast.makeText(getActivity(), "Images Downloaded Successfully", Toast.LENGTH_SHORT).show();
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

        final String Dir = arrays.get(position).getCat_name();

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

    void shareImages(){

        total_selected=0;
        for (int i=0;i<images.size();i++){
            if (images.get(i).getSelect().equals(true)){
                total_selected+=1;
            }
        }

        files=new ArrayList<>();
        for(int j=0;j<images.size();j++){

            if(images.get(j).getSelect().equals(true)){
                String img_url = images.get(j).getImg_url();
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

    void getDetails(){

        String tag_json_obj = "json_obj_req";

        Map<String,String> post_param = new HashMap<>();
        post_param.put(KEY_CAT_ID,arrays.get(position).getCat_no());
        post_param.put(KEY_C_ID,customer_session.getCustomerID());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                DATA_URL,new JSONObject(post_param),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){
                                JSONArray jsonArray = response.getJSONArray("userData");

                                for (int i=0;i<jsonArray.length();i++){

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    JSONObject properties= jsonObject.getJSONObject("product_data");

                                    isLiked = jsonObject.getBoolean("isLiked");
                                    String img_url = properties.getString("original_img_dest");
                                    String img_id = properties.getString("img_id");
                                    String price = properties.getString("selling_price");
                                    String actual_price = properties.getString("actual_price");
                                    String cat_name = properties.getString("category_name");
                                    String count_all = properties.getString("like_count");
                                    String image_name = properties.getString("image_name");

                                    Selection selection = new Selection(img_url,false,img_id,price,actual_price,cat_name,count_all,isLiked,image_name);
                                    images.add(selection);
                                }


                                mAdapter = new Cat_Image_Adapter(getActivity(), images,  new Cat_Image_Adapter.ClickListener() {

                                    @Override
                                    public void onFavClick(View v, int pos, View v2) {
                                        addToFav((ImageView) v,images.get(pos).getImg_id(),(TextView) v2);
                                    }

                                    @Override
                                    public void onClick(View view, int position) {

                                    }

                                    @Override
                                    public void onImgClick(View view, int position) {
                                        Intent intent = new Intent(getActivity(),Full_Image.class);
                                        Bundle data = new Bundle();
                                        data.putSerializable("images", images);
                                        intent.putExtra("ca",data);
                                        intent.putExtra("pos",position);
                                        intent.putExtra("cat_name",images.get(position).getCat_name());
                                        intent.putExtra("id",1);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {

                                    }
                                });

                                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(mAdapter);

                                catno.setText(images.size()+" Designs");

                                p_bar.setVisibility(View.GONE);
                                relativeLayout.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getActivity(),e.toString(), Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void addToFav(final ImageView v, String img_id, final TextView t) {


        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(KEY_ID,customer_session.getCustomerID());
        postParam.put(PRODUCT_ID,img_id);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                FAV_URL, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){
                                v.setImageResource(R.drawable.heart_re);
                                t.setText(response.getString("all_like_counter"));
                            }else if (status.equals("208")){
                                v.setImageResource(R.drawable.heart_outline);
                                t.setText(response.getString("all_like_counter"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

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


}
