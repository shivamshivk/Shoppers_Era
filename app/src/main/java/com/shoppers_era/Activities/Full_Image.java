package com.shoppers_era.Activities;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import customfonts.Zoom.TouchImageView;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.Model.Selection;
import com.shoppers_era.R;
import com.shoppers_era.Session.Customer_Session;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shoppers_era.Session.Name_Session;
import com.shoppers_era.Session.Phone_Session;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Full_Image extends AppCompatActivity {

    private static final String FAV_URL = "https://apps.itrifid.com/shoppersera/rest_server/userlikelist/API-KEY/123456";
    private static final String KEY_ID = "c_id";
    private static final String PRODUCT_ID = "product_id";
    private static final String ORDER_URL = "https://apps.itrifid.com/shoppersera/rest_server/getUserOrder/API-KEY/123456";
    private static final String CUS_NAME = "cus_name";
    private static final String CUS_PHONE = "cus_mobile";
    private static final String CUS_ID = "cus_id";
    private ViewPager viewPager;
    private List<Selection> images;
    private RelativeLayout btn_share;
    private Button btn_download;
    private int selectedPosition = 0;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount;
    private TouchImageView full_image;
    private ImageView heart;
    private TextView cat_name;
    private TextView original_price;
    private TextView selling_price;
    private TextView product_id_text;
    private Customer_Session customer_session;
    private TextView count_all;
    private String cat_name_s;
    private int id =0;
    private String img_name;
    private RelativeLayout loader_layout;
    private TextView loading_text;
    private String p_no;
    private Phone_Session phone_session;
    private Name_Session name_session;
    private Dialog bottom_dialog;
    private EditText c_name;
    private EditText c_phone;
    private FrameLayout req_btn;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_slider);


        Intent intent=getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null){
            Bundle data = extras.getBundle("ca");
            images= (List<Selection>) data.getSerializable("images");
            selectedPosition = extras.getInt("pos");
            id = extras.getInt("id");
            cat_name_s = extras.getString("cat_name");
            p_no=extras.getString("pNo");
        }

        phone_session = new Phone_Session(getApplicationContext());
        name_session=new Name_Session(getApplicationContext());
        customer_session = new Customer_Session(getApplicationContext());
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        bottom_dialog  = new Dialog(Full_Image.this,R.style.BottomDialog);
        bottom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
        bottom_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams2.copyFrom(bottom_dialog.getWindow().getAttributes());
        layoutParams2.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.gravity = Gravity.BOTTOM;
        bottom_dialog.getWindow().setAttributes(layoutParams2);
        bottom_dialog.setCancelable(true);
        bottom_dialog.setContentView(R.layout.seller_request_dialog);

        c_name = (EditText) bottom_dialog.findViewById(R.id.input_name);
        c_name.setText(name_session.getName());
        c_phone = (EditText) bottom_dialog.findViewById(R.id.input_phone);
        c_phone.setText(phone_session.getPhoneNO());

        req_btn = (FrameLayout) bottom_dialog.findViewById(R.id.btn_ok);
        req_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!c_name.getText().toString().trim().isEmpty() && !c_phone.getText().toString().trim().isEmpty()){
                    postOrder();
                    showProgressDialog();
                }else {
                    Toast.makeText(Full_Image.this, "Please fill it..", Toast.LENGTH_SHORT).show();
                }
            }
        });



        lblCount = (TextView) findViewById(R.id.lbl_count);

        btn_share = (RelativeLayout) findViewById(R.id.sahre_btn);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    shareImage();
                }catch (NullPointerException e){
                   e.printStackTrace();
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
        });

        myViewPagerAdapter = new MyViewPagerAdapter();

        viewPager.setAdapter(myViewPagerAdapter);

        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

    }

    //  adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.full_screen_img, container, false);

            loader_layout = (RelativeLayout) view.findViewById(R.id.loader_layout);
            loading_text = (TextView) view.findViewById(R.id.loading_text);

            count_all = (TextView) view.findViewById(R.id.count_all);
            full_image = (TouchImageView) view.findViewById(R.id.image_preview);
            full_image.setMaxZoom(4f);

            heart = (ImageView) view.findViewById(R.id.fav_add);

            cat_name = (TextView) view.findViewById(R.id.cat_name_text);
            original_price = (TextView) view.findViewById(R.id.price_original);
            selling_price = (TextView) view.findViewById(R.id.price);

            product_id_text = (TextView) view.findViewById(R.id.product_id_text);

            try {

                Picasso.with(getApplicationContext()).load(images.get(position).getImg_url()).noFade().into(full_image, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {

                    }
                });

            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Wait till the image get loaded", Toast.LENGTH_LONG).show();
            }

            if (images.get(position).getLiked()){
                heart.setImageResource(R.drawable.heart_re);
            }else{
                heart.setImageResource(R.drawable.heart_outline);
            }

            count_all.setText(images.get(position).getCount_all());
            if (id==1){
                cat_name.setText(images.get(position).getImage_name());
            }else if (id==2){
                cat_name.setText(images.get(position).getCat_name());
            }else if (id==3){
                cat_name.setText(images.get(position).getCat_name());
                heart.setVisibility(View.GONE);
                count_all.setVisibility(View.GONE);
            }else {
                cat_name.setText(cat_name_s);
            }




            original_price.setText("₹"+images.get(position).getActual_price());

            original_price.setPaintFlags(original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if(images.get(position).getActual_price().equals("0.00")){
                original_price.setVisibility(View.GONE);
            }


            selling_price.setText("₹"+images.get(position).getPrice());

            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToFav((ImageView) v,images.get(position).getImg_id(),position);
                }
            });

            product_id_text.setText("Product ID - "+images.get(position).getImg_id());

            full_image.setTag(position);

            full_image.setId(position);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == (obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private void setCurrentItem(final int pos) {
        viewPager.setCurrentItem(pos, false);
        displayMetaInfo(selectedPosition);

    }

    //  page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @SuppressLint("SetTextI18n")
    private void displayMetaInfo(final int position) {
        lblCount.setText((position + 1) + " of " + images.size());


        btn_download = (Button) findViewById(R.id.download_btn);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String smsNumber = "917909029245";
//                if (p_no.equals("")){
//                    //do nothing
//                }else {
//                    smsNumber = p_no;
//                }
//
//                String message = "Hi,I want to buy this Kurtis ";
//                boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
//                if (isWhatsappInstalled) {
//
//                    try{
//
//                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
//                        whatsappIntent.setType("text/plain");
//                        whatsappIntent.setPackage("com.whatsapp");
//                        whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
//                        whatsappIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
//                        startActivity(whatsappIntent);
//
//                    }catch (ActivityNotFoundException e){
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    try {
//                        Uri uri = Uri.parse("market://details?id=com.whatsapp");
//                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                        Toast.makeText(getApplicationContext(), "WhatsApp not Installed",
//                                Toast.LENGTH_SHORT).show();
//                        startActivity(goToMarket);
//                    }catch (NullPointerException e){
//                        e.printStackTrace();
//                    }
//                }

                Intent intent = new Intent(getApplicationContext(),Payment_Activity_1.class);
                intent.putExtra("tot_cost",images.get(selectedPosition).getPrice());
                intent.putExtra("product_id",images.get(selectedPosition).getImg_id());
                startActivity(intent);
                finish();
            }
        });
    }



    private void addToFav(final ImageView v,String img_id,int pos) {


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
                                count_all.setText(response.getString("all_like_counter"));
                            }else if (status.equals("208")){
                                v.setImageResource(R.drawable.heart_outline);
                                count_all.setText(response.getString("all_like_counter"));
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

    void shareImage() {


        //Uri bmpUri = getLocalBitmapUri(imageView);
        ImageView Imgv = (ImageView)viewPager.findViewWithTag(viewPager.getCurrentItem());

        Drawable drawable = Imgv.getDrawable();
        Bitmap bmp = null;

        try {
            if (drawable instanceof BitmapDrawable)
                bmp = ((BitmapDrawable) drawable).getBitmap();
            else {

                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                bmp = bitmap;


            }
        }catch (NullPointerException e){
            Toast.makeText(getApplicationContext(),"Wait till the image get loaded",Toast.LENGTH_SHORT).show();
        }


        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);

            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Wait till the image get loaded", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            shareIntent.setPackage("com.whatsapp");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"I want to buy this");
            try {
                shareIntent.putExtra("jid", p_no+ "@s.whatsapp.net"); //phone number without "+" prefix
                startActivity(shareIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Please make sure whatsapp is installed on your device", Toast.LENGTH_LONG).show();
            }
        } else {
            // ...sharing failed, handle error
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



    public void downloadImage(String imgloc, final String Dir) {

        Picasso.with(getApplicationContext())
                .load(imgloc)
                .into(new Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
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


                                  Log.i("TAG", "Agter scanning" + myDir.getAbsolutePath());

                              } catch (Exception e) {
                                  // some action
                              }


                              String root = Environment.getExternalStorageDirectory().toString();

                              File filePath = new File(root + "/~AK " + Dir);

                              sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(String.valueOf(filePath)))));

                              Toast.makeText(Full_Image.this, "Image Downloaded successfully", Toast.LENGTH_SHORT).show();
                          }

                          @Override
                          public void onBitmapFailed(Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                );

    }

    void postOrder(){

        String tag_json_obj = "json_obj_req";

        Map<String, String> postParam= new HashMap<>();
        postParam.put(CUS_NAME,c_name.getText().toString().trim());
        postParam.put(CUS_PHONE,c_phone.getText().toString().trim());
        postParam.put(CUS_ID,customer_session.getCustomerID());
        postParam.put(PRODUCT_ID,images.get(selectedPosition).getImg_id());

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
                                Toast.makeText(Full_Image.this, "Product Requested , Seller will back to you soon..", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(Full_Image.this, "Connection Error", Toast.LENGTH_SHORT).show();
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
            mProgressDialog = new ProgressDialog(Full_Image.this);
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

