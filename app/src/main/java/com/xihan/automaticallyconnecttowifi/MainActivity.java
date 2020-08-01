package com.xihan.automaticallyconnecttowifi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xihan.automaticallyconnecttowifi.utils.WifiAutoConnectManager;

public class MainActivity extends AppCompatActivity  {

    Button btnConnect,btnOpenwifi;

    WifiManager wifiManager;
    WifiAutoConnectManager wac;
    TextView textView1;
    EditText editPwd;
    EditText editSSID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnOpenwifi = (Button) findViewById(R.id.btnOpenwifi);
        textView1 = (TextView) findViewById(R.id.txtMessage);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wac = new WifiAutoConnectManager(wifiManager);
        editPwd=(EditText) findViewById(R.id.editPwd);
        editSSID=(EditText) findViewById(R.id.editSSID);

        wac.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 操作界面
                //连接日志
                textView1.setText(textView1.getText()+"\n"+msg.obj+"");
                super.handleMessage(msg);
            }
        };
        ////跳转到WIFI设置按钮事件，可能会被wifi相关的app劫持
        btnOpenwifi.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(i);
            }
        });
        //进行WIFI连接按钮事件
        btnConnect.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    wac.connect(editSSID.getText().toString(), editPwd.getText().toString(),
                            editPwd.getText().toString().equals("")? WifiAutoConnectManager.WifiCipherType.WIFICIPHER_NOPASS: WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                } catch (Exception e) {
                    textView1.setText(e.getMessage());
                }

            }
        });
    }

}