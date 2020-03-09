package com.slangt.itumail;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    String isim;
    String sifre;

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runTask();

    }

    @Override
    public void onBackPressed() {
        WebView webview = findViewById(R.id.web_view);
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void runTask() {
        if (isNetworkAvailable()) {

            final WebView webview = findViewById(R.id.web_view);

            String url = "https://webmail.itu.edu.tr/login.php";
            webview.loadUrl(url);
            webview.getSettings().setDomStorageEnabled(true);
            WebSettings websettings = webview.getSettings();
            websettings.setJavaScriptEnabled(true);
            webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
            isim = "";
            sifre = "";

            final String js = "javascript:document.getElementById('horde_user').value='" + isim + "';" + "javascript:document.getElementById('horde_pass').value='" + sifre + "';" + "javascript:document.getElementsByName('login_button')[0].click()";
            webview.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        view.evaluateJavascript(js, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                            }
                        });

                    }
                }
            });

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("İnternete bağlı değilsiniz");
            builder.setMessage("İnternete bağlı olmanız gerekmektedir. Yeniden deneyin");

            builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

            builder.setPositiveButton("Yeniden Dene", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    runTask();

                }
            });
            AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
            dialog.show();
        }

    }

}