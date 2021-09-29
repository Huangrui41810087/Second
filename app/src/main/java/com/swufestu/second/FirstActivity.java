package com.swufestu.second;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FirstActivity extends AppCompatActivity implements Runnable {

    private static final String TAG = "FirstActivity";
    private final static long ONE_DAY_MSECOND = 24 * 60 * 60 * 1000;//设置一天更新一次
    private float dollarRate = 0.35f;
    private float euroRate = 0.28f;
    private float wonRate = 501;
    TextView result;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Intent i = new Intent();
        //创建一个闹钟管理器
        AlarmManager aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        // 设置闹钟唤醒休眠中的应用，设置更新时间为一天，从当前开始
        aManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ONE_DAY_MSECOND, pi);


        //Intent intent = getIntent();
        //dollarRate = intent.getFloatExtra("NewDollarRate", 0.21f);
        //euroRate = intent.getFloatExtra("NewEuroRate", 0.28f);
        //wonRate = intent.getFloatExtra("NewWonRate", 501.00f);
        //show = findViewById(R.id.show);

        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.i(TAG, "handleMessage: 收到消息");
                if (msg.what == 6) {
                    String str = (String) msg.obj;
                    Log.i(TAG, "handleMessage: ");
                    //收到msg后，在控件中显示
                    result = findViewById(R.id.result);
                    result.setText(str);
                }
                super.handleMessage(msg);
            }
        };
        Thread thread = new Thread(this);
        thread.start();
    }

    public void click(View btn) {
        result = findViewById(R.id.result);
        Log.i(TAG, "click:");
        EditText inputText = findViewById(R.id.RMB);
        String inp = inputText.getText().toString();
        if (inp.length() > 0) {
            Log.i(TAG, "click:inp=" + inp);
            float num = Float.parseFloat(inp);
            float r;
            if (btn.getId() == R.id.btn3) {
                r = num * dollarRate;
            } else if (btn.getId() == R.id.btn2) {
                r = num * euroRate;
            } else {
                r = num * wonRate;
            }
            Log.i(TAG, "r=" + r);
            result.setText(String.valueOf(r));
        } else {
            result.setText("Hello");
            Toast.makeText(this, "请输入金额后再计算", Toast.LENGTH_SHORT).show();

        }
    }

    public void openConfig(View btn) {
        Log.i(TAG, "openConfig：");
        //打开新窗口
        config();
    }

    private void config() {
        Log.i(TAG, "config: AAA");
        Intent config = new Intent(this, SencondActivity.class);
        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);


        Log.i(TAG, "openConfig: dollar_rate=" + dollarRate);
        Log.i(TAG, "openConfig: euro_rate=" + euroRate);
        Log.i(TAG, "openConfig: won_rate=" + wonRate);

        //startActivity(config)
        startActivityForResult(config, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100 && resultCode == 3) {
            dollarRate = data.getFloatExtra("NewDollarRate", 0.1f);
            euroRate = data.getFloatExtra("NewEuroRate", 0.1f);
            wonRate = data.getFloatExtra("NewWonRate", 0.1f);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Log.i(TAG, "ON operaion");
            Intent intent = new Intent(this, SencondActivity.class);
            intent.putExtra("dollar_rate_key", dollarRate);
            intent.putExtra("euro_rate_key", euroRate);
            intent.putExtra("won_rate_key", wonRate);
            Log.i(TAG, "dollarrate:" + dollarRate);
            Log.i(TAG, "eurorate:" + euroRate);
            Log.i(TAG, "wonrate:" + wonRate);
            //startActivity(intent);去新的窗口
            startActivityForResult(intent, 100);
        }

        if (item.getItemId() == R.id.loading) {
            SharedPreferences sp =
                    getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            dollarRate = sp.getFloat("dollar_rate", 0.0f);
            euroRate = sp.getFloat("euro_rate", 0.0f);
            wonRate = sp.getFloat("won_rate", 0.0f);
            Log.i(TAG, "onOptionsItemSelected: dollarRate=" + dollarRate);
            Log.i(TAG, "onOptionsItemSelected: euroRate=" + euroRate);
            Log.i(TAG, "onOptionsItemSelected: wonRate=" + wonRate);
            Toast.makeText(this, "加载汇率成功", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "run: ...");
        //获得网站内容
//        URL url = null;
//        try {
//            url = new URL("https://www.usd-cny.com/bankofchina.htm");
//            //连接不上的时候，在manifest文件的application中添加android:usesCleartextTraffic="true"
//            //在manifest文件中添加<uses-permission android:name="android.permission.INTERNET"></uses-permission>
//
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            InputStream in = http.getInputStream();
//            String html = inputStream2String(in);
//            Log.i(TAG, "run: html=" + html);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            Document doc = null;
            doc = Jsoup.connect("https://usd-cny.com/").get();
            Log.i(TAG, "run: title" + doc.title());
            Elements tables = doc.getElementsByTag("table");
            Element firstTable = tables.first();

            Elements trs = firstTable.getElementsByTag("tr");
            trs.remove(0);
            for (Element tr : trs) {
                //从行中获取td元素
                Elements tds = tr.getElementsByTag("td");
                Element td1 = tds.get(0);
                Element td2 = tds.get(4);
                // Log.i(TAG, "run: "+td1.text()+"——>"+td2.text());

                if ("美元".equals(td1.text())) {
                    Log.i(TAG, "run: " + td1.text() + "——>" + td2.text());
                    String val = td2.text();
                    dollarRate=100f/Float.parseFloat(val);
                    Log.i(TAG, "run: 美元汇率"+dollarRate);
                }
                if ("欧元".equals(td1.text())) {
                    Log.i(TAG, "run: " + td1.text() + "——>" + td2.text());
                    String val = td2.text();
                    euroRate=100f/Float.parseFloat(val);
                    Log.i(TAG, "run: 欧元汇率"+euroRate);
                }
                if ("韩币".equals(td1.text())) {
                    Log.i(TAG, "run: " + td1.text() + "——>" + td2.text());
                    String val = td2.text();
                    wonRate=100f/Float.parseFloat(val);
                    Log.i(TAG, "run: 韩元汇率"+wonRate);
                }
                Log.i(TAG, "run: tds size" + tds.size());//每一行五个.除了第一行。因此可以通过这个去除第一行
            }
           for(Element item:firstTable.getElementsByClass("bz")){
               Log.i(TAG, "run: item"+item.text());
           }
            Log.i(TAG, "run: firstTable"+firstTable);// 找table标签
          Elements tds = firstTable.getElementsByTag("td");//提取了td标签
          for (int i = 0; i < tds.size(); i += 5) {//5列
               Element td1 = tds.get(i);
               Element td2 = tds.get(i + 1);
              Log.i(TAG, "run: td1=" + td1.text() + "\t td2=" + td2.text());
                //td1=港币	 td2=82.87
            }
          Elements ths = firstTable.getElementsByTag("th");
          for(Element th:ths){
               Log.i(TAG, "run: th="+th);
            }
            //输出th2
            Element th2 = ths.get(1);
           Log.i(TAG, "run: th2="+th2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage();
        msg.what = 6;
        msg.obj = "Hello from run";
        handler.sendMessage(msg);
        Log.i(TAG, "run: 消息已经发送");
    }


    private String inputStream2String(InputStream inputStream)
            throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");

        while (true) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}