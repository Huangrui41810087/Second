package com.swufestu.second;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RateTask2 implements Runnable{

    private static final String TAG="RateTask";
    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
    @Override
    public void run() {
        //获取网络数据
        List<HashMap<String,String>> retlist = new ArrayList<HashMap<String,String>>();
        try {
            Thread.sleep(1000);
            Document doc=Jsoup.connect(" https://usd-cny.com/").get();
            Log.i(TAG, "run:title="+doc.title());
            Element firstTable = doc.getElementsByTag("table").first();

            Elements trs = firstTable.getElementsByTag("tr");
            trs.remove(0);
            //获取表中数据
            for(Element tr : trs){
            Elements tds = tr.getElementsByTag("td");

                String td1=tds.get(0).text();
                String td2=tds.get(4).text();
                Log.i(TAG, "run: td1"+td1+"--->"+td2);

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("ItemTitle",td1);
                map.put("ItemDetail",td2);
                retlist.add(map);

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        //发送消息给主线程
        Message msg=handler.obtainMessage(9,retlist);
        handler.sendMessage(msg);
        Log.i(TAG, "run: 消息已发送");
    }
}
