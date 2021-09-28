package com.swufestu.second;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FirstActivity extends AppCompatActivity implements Runnable {
    private static final String TAG = "FirstActivity";
    float dollar_rate = 0.35f;
    float euro_rate = 0.28f;
    float won_rate = 501;

    Handler handler;
    TextView result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        result = findViewById(R.id.result);
        //获取文件中保存的数据
        loadFromSP();
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage: 收到消息");
                if (msg.what == 6) {
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: str=" + str);
                    result.setText(str);
                }
                super.handleMessage(msg);
            }
        };
        //启动线程
        Thread thread = new Thread(this);
        thread.start();//this.run()
    }
    private void loadFromSP() {
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        dollar_rate = sharedPreferences.getFloat("dollar_rate", 0.35f);
        euro_rate = sharedPreferences.getFloat("euro_rate",  0.28f);
        won_rate = sharedPreferences.getFloat("won_rate", 501f);
        Log.i(TAG, "onCreat: dollarRate="+dollar_rate);
        Log.i(TAG, "onCreat: euroRate= "+euro_rate);
        Log.i(TAG, "onCreat: wonRate="+won_rate);
    }
    public void click(View btn) {
        Log.i(TAG, "click: ");
        EditText rmb = findViewById(R.id.RMB);
        //TextView result = findViewById(R.id.result);
        String inp = rmb.getText().toString();
        Log.i(TAG, "click: click:inp" + inp);
        if (inp.length() > 0) {
            //处理有输入的数据
            float num = Float.parseFloat(inp);
            float r = 0;
            if (btn.getId() == R.id.btn3) {
                r = num * dollar_rate;
            } else if (btn.getId() == R.id.btn2) {
                r = num * euro_rate;
            } else {
                r = num * won_rate;
            }
            Log.i(TAG, "click: r=" + r);
            //显示结果
            result.setText(String.valueOf(r));
        } else {
            Toast.makeText(this, "请输入金额后再进行转换", Toast.LENGTH_SHORT).show();
            result.setText("Hello");

        }
    }

    public void openConfig(View btn) {
        Log.i(TAG, "openConfig：");
        //打开新窗口
        config();
    }

    private void config() {
        Intent config = new Intent(this, SencondActivity.class);
        config.putExtra("dollar_rate_key", dollar_rate);
        config.putExtra("euro_rate_key", euro_rate);
        config.putExtra("won_rate_key", won_rate);


        Log.i(TAG, "openConfig: dollar_rate=" + dollar_rate);
        Log.i(TAG, "openConfig: euro_rate=" + euro_rate);
        Log.i(TAG, "openConfig: won_rate=" + won_rate);

        //startActivity(config)
        startActivityForResult(config, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == 3) {
            dollar_rate = data.getFloatExtra("dollar_key2", 0.1f);
            euro_rate = data.getFloatExtra("euro_key2", 0.1f);
            won_rate = data.getFloatExtra("won_key2", 0.1f);

            Log.i(TAG, "onActivityResult: dollar_rate=" + dollar_rate);
            Log.i(TAG, "onActivityResult: euro_rate=" + euro_rate);
            Log.i(TAG, "onActivityResult: won_rate=" + won_rate);
        } else if (requestCode == 1 && resultCode == 5) {
            Bundle bundle = data.getExtras();
            dollar_rate = bundle.getFloat("dollar_key3", 1);
            euro_rate = bundle.getFloat("euro_key3", 1);
            won_rate = bundle.getFloat("won_key3", 1);

            Log.i(TAG, "onActivityResult: dollar_rate3=" + dollar_rate);
            Log.i(TAG, "onActivityResult: euro_rate3=" + euro_rate);
            Log.i(TAG, "onActivityResult: won_rate3=" + won_rate);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
        // return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            config();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {
        Log.i(TAG, "run: run......");
        //延迟
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //获取网络连接
        URL url = null;
        try {
            Log.i(TAG, "run: 访问url");
            url = new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream in = http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG, "run: html=" + html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //发送消息
        Message msg = handler.obtainMessage(6);
        //msg.what=6
        msg.obj = "Hello from run";
        handler.sendMessage(msg);
        Log.i(TAG, "run: 消息已发送");
    }
    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize=1024;
        final char[] buffer =new char[bufferSize];
        final StringBuilder out=new StringBuilder();
        String charsetName;
        Reader in =new InputStreamReader(inputStream,charsetName="gb2312");
        while (true){
            int rsz=in.read(buffer,0,buffer.length);
            if(rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return  out.toString();
    }
}


