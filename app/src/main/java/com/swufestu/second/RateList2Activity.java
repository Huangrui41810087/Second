package com.swufestu.second;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RateList2Activity extends AppCompatActivity implements  AdapterView.OnItemClickListener {

ListView mylist2;
    private static final String TAG = "Ratelist2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_list2);


        //加载数据及适配器
        //ArrayList<HashMap<String ,String>> listItems = new ArrayList<>();
        //for(int i=0;i<10;i++){
        //    HashMap<String ,String> map=new HashMap<String,String>();
          //  map.put("ItemTitle","Rate:"+i);
           // map.put("ItemDetail","Detail:"+i);
           // listItems.add(map);


     //   }

       // SimpleAdapter listItemAdapter=new SimpleAdapter(this,listItems,R.layout.list_item,
         //       new String[]{"ItemTitle","ItemDetail"},
           //     new int[]{R.id.itemTitle,R.id.itemDetail}
     //   );

         mylist2=findViewById(R.id.mylist2);
         mylist2.setOnItemClickListener(this);
       // mylist2.setAdapter(listItemAdapter);
        ProgressBar bar = findViewById(R.id.progressBar);

        Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==9){
                ArrayList<HashMap<String,String>> rlist = (ArrayList<HashMap<String,String>>)msg.obj;
                //List<String> list2=(List<String>) msg.obj;
               // SimpleAdapter listItemAdapter = new SimpleAdapter(RateList2Activity.this,
                //        rlist,
                  //      R.layout.list_item,
                  //      new String[]{"ItemTitle","ItemDetail"},
                   //     new int[]{R.id.itemTitle,R.id.itemDetail}
                     //   );
                MyAdapter myAdapter=new MyAdapter(RateList2Activity.this,R.layout.list_item,rlist);

                mylist2.setAdapter(myAdapter);
                //切换显示状态
                bar.setVisibility(View.GONE);
                mylist2.setVisibility(View.VISIBLE);

            }

        }
    };

        RateTask2 task2 = new RateTask2();
        task2.setHandler(handler);
        Thread t2 = new Thread(task2);
        t2.start();

    }

    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id) {
        Object itemAtPositon = mylist2.getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String, String>) itemAtPositon;
        String titleStr =map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Log.i(TAG, "onItemClick: titleStr="+titleStr);
        Log.i(TAG, "onItemClick: detailStr="+detailStr);

        TextView title = (TextView) view.findViewById(R.id.itemTitle);
        TextView detail = (TextView) view.findViewById(R.id.itemDetail);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());
        Log.i(TAG, "onItemClick: title2=" + title2);
        Log.i(TAG, "onItemClick: detail2=" + detail2);

        //打开新的页面，传入参数
        Intent rateCalc=new Intent(this,RateCalcActivity.class);
        rateCalc.putExtra("title",titleStr);
        rateCalc.putExtra("rate",Float.parseFloat(detailStr));
        startActivity(rateCalc);
    }





}
