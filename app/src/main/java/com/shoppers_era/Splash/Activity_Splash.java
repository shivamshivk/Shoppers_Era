package com.shoppers_era.Splash;

import android.content.Intent;
import com.shoppers_era.Activities.Login_Actvity;
import com.shoppers_era.Activities.Main_Handler_Activity;
import com.shoppers_era.R;
import com.shoppers_era.Session.Session_Manager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Activity_Splash extends AppCompatActivity {

    private Session_Manager session_manager;
    private String base="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        if (extras!=null){
            base = extras.getString("base");
        }


        session_manager = new Session_Manager(getApplicationContext());

        Thread shivThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (session_manager.isLoggedIn()){

                    try{
                        if (base.equals("cat_details")){
                            Intent intent = new Intent(getApplicationContext(), Main_Handler_Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("id",1);
                            intent.putExtra("cat",extras.getString("cat"));
                            startActivity(intent);
                            overridePendingTransition (0, 0);
                            finish();
                        }else {
                            Intent intent = new Intent(getApplicationContext(), Main_Handler_Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("id",0);
                            intent.putExtra("cat","");
                            startActivity(intent);
                            overridePendingTransition (0, 0);
                            finish();
                        }

                    }catch(NullPointerException e){
                        Intent intent = new Intent(getApplicationContext(), Main_Handler_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("id",0);
                        intent.putExtra("cat","");
                        startActivity(intent);
                        overridePendingTransition (0, 0);
                        finish();
                    }

                }else {
                    Intent intent = new Intent(getApplicationContext(), Login_Actvity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition (0, 0);
                    finish();
                }


            }
        });

        shivThread.start();
    }
}
