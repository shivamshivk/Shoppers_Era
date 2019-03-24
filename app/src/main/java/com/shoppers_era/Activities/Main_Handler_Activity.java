package com.shoppers_era.Activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.analytics.Tracker;
import com.shoppers_era.Applications.AppController;
import com.shoppers_era.Fragments.Fragment_Home;
import com.shoppers_era.Fragments.Fragment_Loved;
import com.shoppers_era.Fragments.Fragment_Query;
import com.shoppers_era.Fragments.Fragment_Refresh;
import com.shoppers_era.Notifications.Config;
import com.shoppers_era.Notifications.NotificationUtils;
import com.shoppers_era.R;
import com.shoppers_era.Session.Contact_Session;
import com.shoppers_era.Session.Customer_Session;
import com.shoppers_era.ViewPager.CustomViewPager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class Main_Handler_Activity extends AppCompatActivity implements Fragment_Home.onDataLoadListener,Fragment_Refresh.onDataLoadListener {


    private BottomNavigationView bottom_navigation;
    public static CustomViewPager viewPager;
    MenuItem prevMenuItem;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private FloatingActionButton floatingActionButton;
//    private ListView listView;
    private DrawerLayout drawerLayout;
    private ProgressBar p_bar;
    private static final String PROFILE_URL = "https://apps.itrifid.com/shoppersera/rest_server/userProfile/API-KEY/123456";
    private static final String KEY_C_ID  = "c_id";
    private TextView user_name;
    private TextView user_phone;
    private ProgressBar progressBar;
    private LinearLayout lin;
    private LinearLayout lin_about;
    private LinearLayout lin_help;
    private LinearLayout lin_contact;
    private LinearLayout bank_details;
    private LinearLayout my_orders;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final String TAG = Main_Handler_Activity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressDialog mProgressDialog;
    int id =0;
    //Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    int id_=0;
    private ProgressBar fragment_bar;
    private LinearLayout lin_cat;
    private LinearLayout lin_frag;
    private NestedScrollView nestedScrollView;
    private NestedScrollView nestedScrollView1;
    public String wp_no="";
    public String wp_text="";
    public String wp_b_no="";
    public String wp_b_text="";
    private Customer_Session customer_session;
    private ImageView img_search;
    private Tracker tracker;
    private Contact_Session contact_session;
    private RelativeLayout catalogue_layout;
    private NestedScrollView nestedScrollView2;




    //    private PublisherInterstitialAd mPublisherInterstitialAd;
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        verifyStoragePermissions(this);

        customer_session = new Customer_Session(getApplicationContext());
        contact_session=new Contact_Session(getApplicationContext());


        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        user_name = (TextView) drawerLayout.findViewById(R.id._name);
        user_phone = (TextView) drawerLayout.findViewById(R.id.phone);

        lin = (LinearLayout) drawerLayout.findViewById(R.id.lin);
        progressBar = (ProgressBar) drawerLayout.findViewById(R.id.p_bar);

        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/arial.ttf");

        user_name.setTypeface(tf2);
        user_phone.setTypeface(tf2);

        lin_about = (LinearLayout) drawerLayout.findViewById(R.id.about);
        lin_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Help_Activity.class);
                intent.putExtra("id",1);
                startActivity(intent);
            }
        });

        lin_help = (LinearLayout) drawerLayout.findViewById(R.id.help);
        lin_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Help_Activity.class);
                intent.putExtra("id",2);
                startActivity(intent);
            }
        });

        lin_contact = (LinearLayout) drawerLayout.findViewById(R.id.contact);
        lin_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Help_Activity.class);
                intent.putExtra("id",3);
                startActivity(intent);
            }
        });

        my_orders = (LinearLayout) drawerLayout.findViewById(R.id.my_orders);
        my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),My_Orders.class));
            }
        });

        bank_details = (LinearLayout) drawerLayout.findViewById(R.id.bank_details);
        bank_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Bank_Details.class));
            }
        });

        getProfileDetails();

        FirebaseMessaging.getInstance().subscribeToTopic("global");
        displayFirebaseRegId();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }
        };

        setSupportActionBar(toolbar);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.write_);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Write_Activity.class));
            }
        });

//        listView = (ListView) findViewById(R.id.listView);
////        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            }
//        });


        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.disableScroll(true);

        getSupportActionBar().setElevation(0);

        bottom_navigation = (BottomNavigationView)findViewById(R.id.navigation);
        removeShiftMode(bottom_navigation);

        bottom_navigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.navigation_home:
                                if (id_==0){
                                    //do nothing for now
                                }else {
                                    lin_cat.setVisibility(View.GONE);
                                    lin_frag.setVisibility(View.VISIBLE);
                                    fragment_bar.setVisibility(View.GONE);
                                    nestedScrollView.setVisibility(View.GONE);
                                    nestedScrollView1.setVisibility(View.VISIBLE);
                                    id_ =0;
                                }
                                viewPager.setCurrentItem(0);
                                floatingActionButton.setVisibility(View.GONE);
                                break;

                            case R.id.navigation_trending:
                                viewPager.setCurrentItem(1);
                                floatingActionButton.setVisibility(View.GONE);
                                break;

                            case R.id.navigation_loved:
                                viewPager.setCurrentItem(2);
                                floatingActionButton.setVisibility(View.GONE);
                                break;


                            case R.id.navigation_query:
                                floatingActionButton.setVisibility(View.GONE);
                                viewPager.setCurrentItem(3);

                                if (whatsappInstalledOrNot("com.whatsapp.w4b") && whatsappInstalledOrNot("com.whatsapp")){
                                    String title = "Choose :";
                                    CharSequence[] itemlist ={"Whatsapp",
                                            "Whatsapp Business",
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Main_Handler_Activity.this);
                                    //builder.setIcon(R.drawable.icon_app);
                                    builder.setTitle(title);
                                    builder.setItems(itemlist, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case 0:// Take Photo
                                                    openWhatsApp();
                                                    break;
                                                case 1:// Choose Existing Photo
                                                    openWhatsAppBusiness();
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
                                    openWhatsApp();
                                }
                                break;
                        }
                        return false;
                    }
                });

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottom_navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehaviour());

        //        CoordinatorLayout.LayoutParams layoutParams1 = (CoordinatorLayout.LayoutParams) toolbar.getLayoutParams();
//        layoutParams1.setBehavior(new ToolbarViewBehaviour());



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottom_navigation.getMenu().getItem(0).setChecked(false);
                }

                bottom_navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottom_navigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(tf1);

        img_search = (ImageView) toolbar.findViewById(R.id.search);
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Search_Activity.class));
            }
        });

        //Add fragments
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Refresh());
        adapter.addFragment(new Fragment_Home());
        adapter.addFragment(new Fragment_Loved());
        adapter.addFragment(new Fragment_Query());

        viewPager.setOffscreenPageLimit(4);

        //Setting adapter
        viewPager.setAdapter(adapter);

        setUpNavDrawer();

//
//        startGame();
    }



    private void getProfileDetails() {

        String tag_json_obj = "json_obj_req";

        Map<String,String> post_param = new HashMap<>();

        post_param.put(KEY_C_ID,customer_session.getCustomerID());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                PROFILE_URL,new JSONObject(post_param),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if (status.equals("200")){

                                progressBar.setVisibility(View.GONE);
                                lin.setVisibility(View.VISIBLE);

                                JSONObject js = response.getJSONObject("userData");
                                String name = js.getString("full_name");
                                String phone = js.getString("mobile");

                                user_name.setText(name);
                                user_phone.setText("+91 "+phone);

                            }else {
                                Toast.makeText(Main_Handler_Activity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Main_Handler_Activity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);



    }


//
//    void startGame(){
//        mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());
//    }


    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

//        mRewardedVideoAd.resume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mRewardedVideoAd.pause(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }



    @Override
    public void onSliderLoaded(String w_no, String wtext, String wb_no, String wb_text) {
        wp_no = w_no;
        wp_text = wtext;
        wp_b_no = wb_no;
        wp_b_text = wb_text;
    }

    @Override
    public void requiredDtata(int id, LinearLayout lin1, LinearLayout lin2, ProgressBar p_bar, NestedScrollView sv1, NestedScrollView sv2, NestedScrollView sv3, RelativeLayout catloguye_layout) {
        id_ = id;
        lin_cat = lin1;
        lin_frag = lin2;
        fragment_bar = p_bar;
        nestedScrollView = sv1;
        nestedScrollView1 = sv2;
        nestedScrollView2 = sv3;
        catalogue_layout = catloguye_layout;
    }


    @Override
    public void requiredDtata(int id, LinearLayout lin1, LinearLayout lin2, ProgressBar p_bar, NestedScrollView sv1, NestedScrollView sv2) {
        id_ = id;
        lin_cat = lin1;
        lin_frag = lin2;
        fragment_bar = p_bar;
        nestedScrollView = sv1;
        nestedScrollView1 = sv2;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }


//    private void addContact(String name, String phone) {
//        ContentValues values = new ContentValues();
//        values.put(android.provider.Contacts.People.NUMBER, phone);
//        values.put(android.provider.Contacts.People.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
//        values.put(android.provider.Contacts.People.LABEL, name);
//        values.put(android.provider.Contacts.People.NAME, name);
//        Uri dataUri = getContentResolver().insert(android.provider.Contacts.People.CONTENT_URI, values);
//        Uri updateUri = Uri.withAppendedPath(dataUri, android.provider.Contacts.People.Phones.CONTENT_DIRECTORY);
//        values.clear();
//        values.put(android.provider.Contacts.People.Phones.TYPE, android.provider.Contacts.People.TYPE_MOBILE);
//        values.put(android.provider.Contacts.People.NUMBER, phone);
//        updateUri = getContentResolver().insert(updateUri, values);
//    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId))
            //Toast.makeText(getApplicationContext(), "Push Notification Subscribed " +regId, Toast.LENGTH_LONG).show();
            //txtRegId.setText("Firebase Reg Id: " + regId);
            Log.e(TAG, "Push Notification Subscribed: " + regId);
        else
            Toast.makeText(getApplicationContext(), "No Id ", Toast.LENGTH_LONG).show();
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


    private void openWhatsApp() {


        String smsNumber = "918777099159";

        if (wp_no.equals("")){
            //do nothing
        }else {
            smsNumber = wp_no;
        }


        String message = "Hi,\n" ;
        if (!wp_text.equals("")){
            message = wp_text;
        }

        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            try{

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
                whatsappIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                startActivity(whatsappIntent);


            }catch (ActivityNotFoundException e){
                e.printStackTrace();
            }

        } else {
            try {
                Uri uri = Uri.parse("market://details?id=com.whatsapp");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                Toast.makeText(getApplicationContext(), "WhatsApp not Installed",
                        Toast.LENGTH_SHORT).show();
                startActivity(goToMarket);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    private void openWhatsAppBusiness() {


        String smsNumber = "918777099159";

        if (wp_b_no.equals("")){
            //do nothing
        }else {
            smsNumber = wp_b_no;
        }

        String message = "Hi,\n";
        if (!wp_b_text.equals("")){
            message = wp_b_text;
        }

        try{
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp.w4b");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
            whatsappIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
            startActivity(whatsappIntent);
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
        }
    }


    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }

        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }

    @Override
    public void onBackPressed() {

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
                return;
            }

        if (viewPager.getCurrentItem()!=0){
            if (viewPager.getCurrentItem()==1){
                if (id_==0){
                    viewPager.setCurrentItem(0);
                }else if(id_==1){
                    lin_cat.setVisibility(GONE);
                    lin_frag.setVisibility(View.GONE);
                    catalogue_layout.setVisibility(VISIBLE);
                    fragment_bar.setVisibility(GONE);
                    nestedScrollView.setVisibility(GONE);
                    nestedScrollView1.setVisibility(GONE);
                    nestedScrollView2.setVisibility(VISIBLE);
                    id_ =2;
                }else if (id_==2) {
                    lin_cat.setVisibility(GONE);
                    lin_frag.setVisibility(View.VISIBLE);
                    catalogue_layout.setVisibility(GONE);
                    fragment_bar.setVisibility(GONE);
                    nestedScrollView.setVisibility(GONE);
                    nestedScrollView1.setVisibility(VISIBLE);
                    nestedScrollView2.setVisibility(GONE);
                    id_ = 0;
                }
            }else {
                viewPager.setCurrentItem(0);
            }
        }else {
            if (id_==0){
                finish();
            }else {
                lin_cat.setVisibility(View.GONE);
                lin_frag.setVisibility(View.VISIBLE);
                fragment_bar.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.GONE);
                nestedScrollView1.setVisibility(View.VISIBLE);
                id_ =0;
            }
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
              finish();
              return true;

             default:
                 return super.onOptionsItemSelected(item);
        }

    }


    void setUpNavDrawer(){
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }


    @Override
    public void onDestroy() {
//        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }


    private void addContact() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {

            if (!contact_session.getContactStatus()) {
                addContact("Bukarte", "7903053441");
                contact_session.setContactStatus(true);
            }
        }
    }

    private void addContact(String name, String phone) {
        ContentValues values = new ContentValues();
        values.put(android.provider.Contacts.People.NUMBER, phone);
        values.put(android.provider.Contacts.People.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
        values.put(android.provider.Contacts.People.LABEL, name);
        values.put(android.provider.Contacts.People.NAME, name);
        Uri dataUri = getContentResolver().insert(android.provider.Contacts.People.CONTENT_URI, values);
        Uri updateUri = Uri.withAppendedPath(dataUri, android.provider.Contacts.People.Phones.CONTENT_DIRECTORY);
        values.clear();
        values.put(android.provider.Contacts.People.Phones.TYPE, android.provider.Contacts.People.TYPE_MOBILE);
        values.put(android.provider.Contacts.People.NUMBER, phone);
        updateUri = getContentResolver().insert(updateUri, values);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }


    public static  CustomViewPager getV(){
        return viewPager;
    }

}