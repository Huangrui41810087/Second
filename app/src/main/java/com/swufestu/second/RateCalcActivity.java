package com.swufestu.second;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class RateCalcActivity extends AppCompatActivity {

    String TAG = "rateCalc";
    TextView title;
    EditText input;
    TextView result;
    float rate1=0F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calc);
        title = findViewById(R.id.calc_title);
        input = findViewById(R.id.calc_rmb);
        result = findViewById(R.id.calc_result);

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        result.setText(intent.getStringExtra("rate"));

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() > 0) {
                    result.setText(String.valueOf(Float.parseFloat(str) * (100 / rate1)));

                } else {
                    result.setText("");
                }
            }
        });


    }
}
