package com.shoppers_era.Fragments;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.shoppers_era.Activities.Main_Handler_Activity;
import com.shoppers_era.R;

public class Fragment_Query extends Fragment {

    private View rootview;
    private Button btn;
    String wp_no="";
    String wp_text="";
    String wp_b_no="";
    String wp_b_text="";

    public Fragment_Query() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootview =inflater.inflate(R.layout.fragment_fragment__query, container, false);


        btn=(Button) rootview.findViewById(R.id.open_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whatsappInstalledOrNot("com.whatsapp.w4b") && whatsappInstalledOrNot("com.whatsapp")){
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
                            case 0:
                                // Take Photo
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
            }
        });



        return rootview;
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


    private void openWhatsApp() {

        wp_no = ((Main_Handler_Activity) getActivity()).wp_no;
        wp_text = ((Main_Handler_Activity) getActivity()).wp_text;
        wp_b_no = ((Main_Handler_Activity) getActivity()).wp_b_no;
        wp_b_text = ((Main_Handler_Activity) getActivity()).wp_b_text;

        String smsNumber = "918777099159";
        if (wp_no.equals("")){
            //do nothing
        }else {
            smsNumber = wp_no;
        }

        String message = "Hi,\n" +":";
        if (wp_text.equals("")){
            //do nithung
        }else {
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
                Toast.makeText(getActivity(), "WhatsApp not Installed",
                        Toast.LENGTH_SHORT).show();
                startActivity(goToMarket);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    private void openWhatsAppBusiness() {

        wp_no = ((Main_Handler_Activity) getActivity()).wp_no;
        wp_text = ((Main_Handler_Activity) getActivity()).wp_text;
        wp_b_no = ((Main_Handler_Activity) getActivity()).wp_b_no;
        wp_b_text = ((Main_Handler_Activity) getActivity()).wp_b_text;

        String smsNumber = "918777099159";
        if (wp_b_no.equals("")){
            //do nothing
        }else {
            smsNumber = wp_b_no;
        }

        String message = "Hi";
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

}
