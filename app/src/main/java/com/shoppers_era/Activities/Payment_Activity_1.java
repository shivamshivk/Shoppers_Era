package com.shoppers_era.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.shoppers_era.R;
import com.shoppers_era.Session.Account_Session;
import com.shoppers_era.Session.Bank_Name_Session;
import com.shoppers_era.Session.COD_Session;
import com.shoppers_era.Session.Customer_Session;
import com.shoppers_era.Session.Holder_Name_Session;
import com.shoppers_era.Session.IFSC_Session;
import com.shoppers_era.Session.Name_Session;
import com.shoppers_era.Session.Phone_Session;
import com.shoppers_era.Session.Shipment_Session;


public class Payment_Activity_1 extends Activity {

    static Payment_Activity_1 activity;
    private CheckBox cod;
    private CheckBox op;
    private CheckBox bt;
    private TextView base_a;
    private TextView total_text;
    private TextView cod_amount;
    private TextView shiiping_text;
    private Double ship_cost=0.0;
    private Double cod_cost=0.0;
    private Double total;
    private TextView apply;
    private String product_id;
    private Double tot_amount;
    private Dialog dialog;
    private Dialog seller_dialog;
    private EditText edit_name;
    private EditText edit_phone;
    private EditText edit_margin;
    private EditText edit_notes;
    private EditText editText;
    private FrameLayout btn_ok;
    private FrameLayout btn_okk;
    private Phone_Session phone_session;
    private Name_Session name_session;
    private int id=0;
    private Button cnt;
    private TextView margin_text;
    private COD_Session cod_session;
    private Shipment_Session shipment_session;
    private Bank_Name_Session bank_name_session;
    private Holder_Name_Session holder_name_session;
    private Account_Session account_session;
    private IFSC_Session ifsc_session;
    private TextView bank_name;
    private TextView account_name;
    private TextView holder_name;
    private TextView ifsc_code;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_mode);

        cod_session = new COD_Session(getApplicationContext());
        shipment_session=new Shipment_Session(getApplicationContext());

        base_a= (TextView)  findViewById(R.id.total_price);
        shiiping_text = (TextView) findViewById(R.id.shipping_charge);
        cod_amount = (TextView) findViewById(R.id.cod_charge);
        total_text = (TextView) findViewById(R.id.total_amount);

        final Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null){
            product_id = extras.getString("product_id");
            tot_amount = Double.valueOf(extras.getString("tot_cost"));
            base_a.setText("Rs"+String.valueOf(tot_amount));
            cod_amount.setText("Rs "+cod_session.getCodCharge());
        }

        ship_cost = Double.valueOf(shipment_session.getShipmentCharge());
        shiiping_text.setText("Rs "+ship_cost);

        activity = this;

        calculateCodCost();

        name_session = new Name_Session(getApplicationContext());
        phone_session = new Phone_Session(getApplicationContext());
        account_session = new Account_Session(getApplicationContext());
        ifsc_session = new IFSC_Session(getApplicationContext());
        holder_name_session = new Holder_Name_Session(getApplicationContext());
        bank_name_session = new Bank_Name_Session(getApplicationContext());


        ifsc_code = (TextView) findViewById(R.id.ifsc);
        bank_name = (TextView) findViewById(R.id.bnk_name);
        account_name = (TextView) findViewById(R.id.acc);
        holder_name= (TextView) findViewById(R.id.bnk_holder_name);

        ifsc_code.setText("IFSC - "+ifsc_session.getIFSC());
        bank_name.setText("Bank Name - "+bank_name_session.getBankName());
        account_name.setText("Account No - "+account_session.getAccountNo());
        holder_name.setText("Holder Name - "+holder_name_session.getHolderName());


        seller_dialog  = new Dialog(Payment_Activity_1.this,R.style.BottomDialog);
        seller_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        seller_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams.copyFrom(seller_dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        seller_dialog.getWindow().setAttributes(layoutParams);
        seller_dialog.setCancelable(true);
        seller_dialog.setContentView(R.layout.seller_request_dialog);

        edit_name = (EditText) seller_dialog.findViewById(R.id.input_name);
        edit_name.setText(name_session.getName());
        edit_phone = (EditText) seller_dialog.findViewById(R.id.input_phone);
        edit_phone.setText(phone_session.getPhoneNO());


        edit_notes = (EditText) seller_dialog.findViewById(R.id.input_nnote);

        edit_margin = (EditText) seller_dialog.findViewById(R.id.input_margin);

        btn_ok = (FrameLayout) seller_dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edit_margin.getText().toString().trim().equals("") && !edit_phone.getText().toString().trim().equals("") && !edit_name.getText().toString().trim().equals("") &&  !edit_notes.getText().toString().trim().equals("")){
                    double cost = Double.parseDouble(edit_margin.getText().toString().trim());
                    total = total+cost;

                    if (id==1){
                        Double t;
                        t = total * 15/100;
                        int t1 = (int) (total - t);
                        total_text.setText("Rs"+t1);
                    }else {
                        total_text.setText("Rs"+total);
                    }

                    if (cod.isChecked()){
                        Intent intent1 = new Intent(getApplicationContext(),Address_Activity.class);
                        intent1.putExtra("base_amount",String.valueOf(tot_amount));
                        intent1.putExtra("total_cost",String.valueOf(total));
                        intent1.putExtra("cod_charge","50.0");
                        intent1.putExtra("product_id",product_id);
                        intent1.putExtra("p_mode","cod");
                        intent1.putExtra("margin",edit_margin.getText().toString().trim());
                        intent1.putExtra("c_name",edit_name.getText().toString().trim());
                        intent1.putExtra("c_phone",edit_phone.getText().toString().trim());
                        intent1.putExtra("notes",edit_notes.getText().toString().trim());
                        startActivity(intent1);
                    }else if (bt.isChecked()){
                        Intent intent1 = new Intent(getApplicationContext(),Address_Activity.class);
                        intent1.putExtra("base_amount",String.valueOf(tot_amount));
                        intent1.putExtra("total_cost",String.valueOf(total));
                        intent1.putExtra("cod_charge","0.0");
                        intent1.putExtra("product_id",product_id);
                        intent1.putExtra("p_mode","Bank Transfer");
                        intent1.putExtra("margin",edit_margin.getText().toString().trim());
                        intent1.putExtra("c_name",edit_name.getText().toString().trim());
                        intent1.putExtra("c_phone",edit_phone.getText().toString().trim());
                        intent1.putExtra("notes",edit_notes.getText().toString().trim());
                        startActivity(intent1);
                    }else if(op.isChecked()){
                        Intent intent1 = new Intent(getApplicationContext(),Address_Activity.class);
                        intent1.putExtra("base_amount",String.valueOf(tot_amount));
                        intent1.putExtra("total_cost",String.valueOf(total));
                        intent1.putExtra("cod_charge","0.0");
                        intent1.putExtra("product_id",product_id);
                        intent1.putExtra("p_mode","op");
                        intent1.putExtra("margin",edit_margin.getText().toString().trim());
                        intent1.putExtra("c_name",edit_name.getText().toString().trim());
                        intent1.putExtra("c_phone",edit_phone.getText().toString().trim());
                        intent1.putExtra("notes",edit_notes.getText().toString().trim());
                        startActivity(intent1);
                    }else {
                        Toast.makeText(Payment_Activity_1.this, "Please Select Payment Mode..", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(Payment_Activity_1.this, "Please fill it out", Toast.LENGTH_SHORT).show();
                }
            }

        });


        dialog  = new Dialog(Payment_Activity_1.this,R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomDialogAnimation;
        layoutParams2.copyFrom(dialog.getWindow().getAttributes());
        layoutParams2.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.gravity = Gravity.BOTTOM;
        dialog.getWindow().setAttributes(layoutParams2);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.coupon_dialog);

        editText = (EditText) dialog.findViewById(R.id.input_name);

        btn_okk = (FrameLayout) dialog.findViewById(R.id.btn_ok);
        btn_okk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().trim().equals(phone_session.getPhoneNO())){
                    dialog.dismiss();
                    if (id==0){
                        double tot = tot_amount;
                        tot = tot * 15/100;
                        int t1 = (int) (tot_amount - tot);
                        total = Double.valueOf(t1) + cod_cost + ship_cost;
                        total_text.setText("Rs "+total);
                        id =1;
                        apply.setText("Coupon Applied");
                    }else {
                        Toast.makeText(Payment_Activity_1.this, "Coupon Applied..", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(Payment_Activity_1.this, "Wrong Coupon Code", Toast.LENGTH_SHORT).show();
                }
            }
        });


        apply = (TextView) findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        op =(CheckBox) findViewById(R.id.op);
        op.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (op.isChecked()){
                    cod.setChecked(false);
                    calculateCost();
                }
            }
        });

        bt = (CheckBox) findViewById(R.id.bt);
        bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (bt.isChecked()){
                    cod.setChecked(false);
                    calculateCost();
                }
            }
        });

        cod = (CheckBox) findViewById(R.id.cod);
        cod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cod.isChecked()){
                    bt.setChecked(false);
                    calculateCodCost();
                }
            }
        });

        cnt = (Button) findViewById(R.id.cnt);
        cnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edit_name.getText().toString().trim().equals("") && !edit_phone.getText().toString().trim().equals("") && !edit_margin.getText().toString().trim().equals("") && !edit_notes.getText().toString().trim().equals("")){
                    if (cod.isChecked()){
                        Intent intent1 = new Intent(getApplicationContext(),Address_Activity.class);
                        intent1.putExtra("base_amount",String.valueOf(tot_amount));
                        intent1.putExtra("total_cost",String.valueOf(total));
                        intent1.putExtra("cod_charge",cod_session.getCodCharge());
                        intent1.putExtra("product_id",product_id);
                        intent1.putExtra("p_mode","cod");
                        intent1.putExtra("margin",edit_margin.getText().toString().trim());
                        intent1.putExtra("c_name",edit_name.getText().toString().trim());
                        intent1.putExtra("c_phone",edit_phone.getText().toString().trim());
                        startActivity(intent1);
                    }else if (bt.isChecked()){
                        Intent intent1 = new Intent(getApplicationContext(),Address_Activity.class);
                        intent1.putExtra("base_amount",String.valueOf(tot_amount));
                        intent1.putExtra("total_cost",String.valueOf(total));
                        intent1.putExtra("cod_charge","0.0");
                        intent1.putExtra("product_id",product_id);
                        intent1.putExtra("p_mode","Bank Transfer");
                        intent1.putExtra("margin",edit_margin.getText().toString().trim());
                        intent1.putExtra("c_name",edit_name.getText().toString().trim());
                        intent1.putExtra("c_phone",edit_phone.getText().toString().trim());
                        intent1.putExtra("notes",edit_notes.getText().toString().trim());
                        startActivity(intent1);
                    }else if(op.isChecked()){
                        Intent intent1 = new Intent(getApplicationContext(),Address_Activity.class);
                        intent1.putExtra("base_amount",String.valueOf(tot_amount));
                        intent1.putExtra("total_cost",String.valueOf(total));
                        intent1.putExtra("cod_charge","0.0");
                        intent1.putExtra("product_id",product_id);
                        intent1.putExtra("p_mode","op");
                        intent1.putExtra("margin",edit_margin.getText().toString().trim());
                        intent1.putExtra("c_name",edit_name.getText().toString().trim());
                        intent1.putExtra("c_phone",edit_phone.getText().toString().trim());
                        intent1.putExtra("notes",edit_notes.getText().toString().trim());
                        startActivity(intent1);
                    }else {
                        Toast.makeText(Payment_Activity_1.this, "Please Select Payment Mode..", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    seller_dialog.show();
                }
            }
        });

    }

    void calculateCodCost(){

        cod_cost = Double.valueOf(cod_session.getCodCharge());
        total = tot_amount + ship_cost +cod_cost;

        if (id==1){
            Double t;
            t = tot_amount * 15/100;
            int t1 = (int) (tot_amount - t);
            total = t1+ship_cost +cod_cost;
            total_text.setText("Rs "+total);
        }else {
            total_text.setText("Rs"+total);
        }
        cod_amount.setText("Rs 30");

    }

    void calculateCost(){

        cod_cost = 0.0;
        total = tot_amount + ship_cost +cod_cost;

        if (id==1){
            double tot = tot_amount;
            tot = tot * 15/100;
            int t1 = (int) (tot_amount - tot);
            total = Double.valueOf(t1) + cod_cost + ship_cost;
            total_text.setText("Rs "+total);
        }else {
            total_text.setText("Rs"+total);
        }
        cod_amount.setText("Rs0.0");

    }

}
