package com.swufestu.second;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class RateCalcActivity extends AppCompatActivity {

    String TAG="rateCalc";
    float rate=0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calc);
        String title=getIntent().getStringExtra("title");
        rate=getIntent().getFloatExtra("rate",0f);

        Log.i(TAG, "onCreate: title="+title);
        Log.i(TAG, "onCreate: rate="+rate);
        ((TextView)findViewById(R.id.title2)).setText(title);


    }
}
