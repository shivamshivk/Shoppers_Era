package com.shoppers_era.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.shoppers_era.R;

public class Thanks_Activity extends AppCompatActivity {

    private TextView btn_ok;
    private TextView ref_no;
    private String order_id;
    private int order_status;
    private TextView t1;
    private TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks_);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null){
            order_id = extras.getString("order_id");
            order_status = extras.getInt("order_status");
        }

        btn_ok = (TextView) findViewById(R.id.btn);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish();
            }
        });

        t1 =(TextView) findViewById(R.id.t1);
        t2 =(TextView) findViewById(R.id.t2);


        ref_no = (TextView) findViewById(R.id.t3);
        ref_no.setText("Your Order ID is "+order_id);

        if (order_status==2){
            ref_no.setVisibility(View.GONE);
            t1.setText("Try Again !");
            t2.setText("Transaction Failed ");
        }
    }
}
