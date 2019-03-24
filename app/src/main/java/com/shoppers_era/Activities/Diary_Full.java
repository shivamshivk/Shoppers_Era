package com.shoppers_era.Activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import customfonts.Zoom.TouchImageView;
import com.shoppers_era.Model.Diary;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;


public class Diary_Full extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Diary> images;
    private RelativeLayout btn_share;
    private Button btn_download;
    private int selectedPosition = 0;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView lblCount;
    private TouchImageView full_image;
    private TextView cat_name;
    private TextView original_price;
    private Customer_Session customer_session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_slider);


        Intent intent=getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null){
            Bundle data = extras.getBundle("ca");
            images= (List<Diary>) data.getSerializable("diaries");
            selectedPosition = extras.getInt("pos");
        }

        customer_session = new Customer_Session(getApplicationContext());
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        lblCount = (TextView) findViewById(R.id.lbl_count);

        btn_download = (Button) findViewById(R.id.download_btn);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImage(images.get(selectedPosition).getImg_url(),"Diaries");
            }
        });

        btn_share = (RelativeLayout) findViewById(R.id.sahre_btn);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
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
            View view = layoutInflater.inflate(R.layout.diary_full_screen_img, container, false);

            selectedPosition = position;

            full_image = (TouchImageView) view.findViewById(R.id.image_preview);
            full_image.setMaxZoom(4f);

            cat_name = (TextView) view.findViewById(R.id.cat_name_text);

            try {

                Glide.with(getApplicationContext()).load(images.get(position).getImg_url())
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(full_image);


            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Wait till the image get loaded", Toast.LENGTH_LONG).show();
            }

            original_price = (TextView) view.findViewById(R.id.price);

            cat_name.setText(images.get(position).getContent1());
            original_price.setText(images.get(position).getContent2());

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

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
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
    private void displayMetaInfo(int position) {
        lblCount.setText((position + 1) + " of " + images.size());
    }

//
//    private void addToFav(final ImageView v,String img_id,int pos) {
//
//
//        String tag_json_obj = "json_obj_req";
//
//        Map<String, String> postParam= new HashMap<>();
//        postParam.put(KEY_ID,customer_session.getCustomerID());
//        postParam.put(PRODUCT_ID,img_id);
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                FAV_URL, new JSONObject(postParam),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//
//                        try {
//                            String status = response.getString("status");
//
//                            if (status.equals("200")){
//                                v.setImageResource(R.drawable.heart_re);
//                                count_all.setText(response.getString("all_like_counter"));
//                            }else if (status.equals("208")){
//                                v.setImageResource(R.drawable.heart_outline);
//                                count_all.setText(response.getString("all_like_counter"));
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//// Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//    }

    void shareImage() {
        //Uri bmpUri = getLocalBitmapUri(imageView);
        Drawable drawable = full_image.getDrawable();
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
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Wait till the image get loaded", Toast.LENGTH_SHORT).show();
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

            try {
                // Launch sharing dialog for image
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Please make sure whatsapp is installed on your device", Toast.LENGTH_LONG).show();
            }
        } else {
            // ...sharing failed, handle error
        }
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

                              Toast.makeText(Diary_Full.this, "Image Downloaded successfully", Toast.LENGTH_SHORT).show();
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

}

