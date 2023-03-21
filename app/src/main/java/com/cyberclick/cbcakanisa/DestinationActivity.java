package com.cyberclick.cbcakanisa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Locale;

public class DestinationActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar mProgressBar;
    public ValueCallback<Uri> mUploadMessage;
    public static final int FILECHOOSER_RESULTCODE = 5173;
    private static final String TAG = MainActivity.class.getSimpleName();

    private SwipeRefreshLayout refreshLayout;
    Dialog user_dialog;
    String historyUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        setTitle("CBCA Kanisa");

        if(!isConnected(DestinationActivity.this))buildDialog(DestinationActivity.this).show();
        else {

            webView = findViewById(R.id.webview);
            mProgressBar = findViewById(R.id.progressBar);
            refreshLayout = findViewById(R.id.refreshLayout);
            user_dialog = new Dialog(this);
            mProgressBar.setMax(100);

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setSupportZoom(false);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setAllowFileAccess(true);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webView.loadUrl("https://www.cbca-kanisa.org/actu/"+DataStore.getInstance(this).getLastPostId());


            webView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    DownloadManager.Request myRequest = new DownloadManager.Request(Uri.parse(url));
                    myRequest.allowScanningByMediaScanner();
                    myRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    DownloadManager myManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    myManager.enqueue(myRequest);

                    Toast.makeText(DestinationActivity.this, "Your file is downloading...", Toast.LENGTH_SHORT).show();
                }
            });


            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mProgressBar.setVisibility(View.GONE);
                    WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();

                    if (mWebBackForwardList.getCurrentIndex() > 0){
                        historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - 1).getUrl();
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    webView.loadUrl("file:///android_asset/error.html");
                }
            });
            //Adds here

            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    webView.loadUrl(webView.getUrl());
                    refreshLayout.setColorSchemeColors(Color.BLUE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(false);
                        }
                    }, 4 * 1000);
                }
            });

            if (Build.VERSION.SDK_INT < 18) {
                webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            }

            registerForContextMenu(webView);

            webView.setWebChromeClient(new WebChromeClient() {

                public void onPermissionRequest(final PermissionRequest request) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        request.grant(request.getResources());
                    }
                }

                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                }

                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                    mUploadMessage = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("*/*");
                    DestinationActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"),
                            FILECHOOSER_RESULTCODE);
                }
            });
        }
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Oops");
        if(Locale.getDefault().getLanguage() == "fr")
        {
            builder.setMessage("Veuillez activer les données mobile ou le wifi pour utiliser l'application CBCA Kanisa");
        }else{
            builder.setMessage("Please switch on Mobile Data or wifi to use CBCA Kanisa the app");
        }
        builder.setPositiveButton("Ok", (dialog, which) -> finish());

        return builder;
    }

    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}