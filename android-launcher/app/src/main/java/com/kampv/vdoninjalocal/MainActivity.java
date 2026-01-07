package com.kampv.vdoninjalocal;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.WindowManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.webkit.JavascriptInterface;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.BatteryManager;
import java.util.Set;
import org.json.JSONObject;
import org.json.JSONArray;

public class MainActivity extends Activity {

    private WebView myWebView;
    private static final String LOCAL_SERVER_IP = "10.10.10.48"; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        myWebView = new WebView(this);
        setContentView(myWebView);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDatabaseEnabled(true);
        
        myWebView.addJavascriptInterface(new NexusBridge(this), "NexusBridge");
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        String url = "http://" + LOCAL_SERVER_IP + ":8080/";
        myWebView.loadUrl(url);
    }

    public class NexusBridge {
        Context mContext;
        BluetoothAdapter btAdapter;

        NexusBridge(Context c) {
            mContext = c;
            btAdapter = BluetoothAdapter.getDefaultAdapter();
        }

        @JavascriptInterface
        public void openSettings() {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            mContext.startActivity(intent);
        }

        @JavascriptInterface
        public String getBluetoothDevices() {
            if (btAdapter == null) return "[]";
            Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
            JSONArray deviceList = new JSONArray();
            try {
                for (BluetoothDevice device : pairedDevices) {
                    JSONObject d = new JSONObject();
                    d.put("name", device.getName());
                    d.put("address", device.getAddress());
                    deviceList.put(d);
                }
            } catch (Exception e) {}
            return deviceList.toString();
        }

        @JavascriptInterface
        public String getTelemetry() {
            JSONObject telemetry = new JSONObject();
            try {
                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = mContext.registerReceiver(null, ifilter);
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                float batteryPct = level * 100 / (float)scale;
                
                telemetry.put("battery", batteryPct);
                telemetry.put("uptime", android.os.SystemClock.elapsedRealtime());
                telemetry.put("model", android.os.Build.MODEL);
            } catch (Exception e) {}
            return telemetry.toString();
        }

        @JavascriptInterface
        public void connectBluetooth(String address) {
            // Android 9 A2DP auto-connect is handled via system intent usually, 
            // but we can trigger the Bluetooth Settings for the user to pick.
            Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            mContext.startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        myWebView.resumeTimers();
    }

    @Override
    public void onBackPressed() {
        // Kiosk mode lock
    }
}