package com.shoppers_era.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.shoppers_era.R;


public class Help_Activity extends AppCompatActivity {


    private static final String help_url = "https://apps.itrifid.com/shoppersera/help.php";
    private static final String contact_url = "https://apps.itrifid.com/shoppersera/contact.php";
    private static final String about_url = "https://apps.itrifid.com/shoppersera/about.php";
    private WebView webView;
    private ProgressBar p_bar;
    private int id =0;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null){
            id = extras.getInt("id");
        }

        p_bar = (ProgressBar) findViewById(R.id.p_bar);

        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        if (id==1){
            webView.loadUrl(about_url);
        }else if (id==2){
            webView.loadUrl(help_url);
        }else {
            webView.loadUrl(contact_url);
        }

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);
                p_bar.setVisibility(View.GONE);
            }
        });

    }
}
