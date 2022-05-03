package com.sciencework.browser;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.sciencework.browser.manager.PbQueueManager;
import com.sciencework.browser.manager.PbWebViewManager;

import org.apache.commons.lang3.StringUtils;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private String currentUrl;
    private PbWebViewManager pbWebViewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("THREAD", "MainActivity.onCreate: " + Thread.currentThread());
        WebView.enableSlowWholeDocumentDraw();
        setContentView(R.layout.activity_main);

        PbQueueManager.getInstance().queue().clear();
        pbWebViewManager = new PbWebViewManager(this);
        initSearchButton();
        initRefreshButton();
        initEditText();
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
    public void onDestroy() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        super.onDestroy();
    }

    private void initSearchButton() {
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> {
            String typedUrl = editText.getText().toString();
            if(StringUtils.isNotEmpty(typedUrl)) {
                pbWebViewManager.update(typedUrl);
                currentUrl = typedUrl;
            } else {
                pbWebViewManager.update(currentUrl);
                editText.setText(currentUrl);
            }
        });
    }

    private void initRefreshButton() {
        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(view -> {
            pbWebViewManager.update(currentUrl);
            editText.setText(currentUrl);
        });
    }

    private void initEditText() {
        String initUrl = this.getResources().getString(R.string.helloPage);
        editText = findViewById(R.id.urlEditText);
        editText.setText(initUrl);
        currentUrl = initUrl;
    }

}