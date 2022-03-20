package com.sciencework.browser;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang3.StringUtils;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private String initUrl;
    private String currentUrl;
    private ProgressDialog progDialog;
    private PbWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView.enableSlowWholeDocumentDraw();
        setContentView(R.layout.activity_main);

        initSearchButton();
        initRefreshButton();
        initEditText();
        initProgDialog();
        initWebView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        progDialog.dismiss();
    }

    private void initSearchButton() {
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> {
            String typedUrl = editText.getText().toString();
            progDialog.show();
            if(StringUtils.isNotEmpty(typedUrl)) {
                webView.update(typedUrl);
                currentUrl = typedUrl;
            } else {
                webView.update(currentUrl);
                editText.setText(currentUrl);
            }
        });
    }

    private void initRefreshButton() {
        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(view -> {
            progDialog.show();
            webView.update(currentUrl);
            editText.setText(currentUrl);
        });
    }

    private void initEditText() {
        initUrl = this.getResources().getString(R.string.helloPage);
        editText = findViewById(R.id.urlEditText);
        editText.setText(initUrl);
        currentUrl = initUrl;
    }

    private void initProgDialog() {
        progDialog = ProgressDialog.show(this, "Loading","Please wait...", true);
        progDialog.setCancelable(false);
    }

    // Should be called after initEditText&initProgDialog
    private void initWebView() {
        webView = new PbWebView(this, progDialog, editText.getText().toString());
    }

}