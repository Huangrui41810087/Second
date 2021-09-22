package com.swufestu.second;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText input = findViewById(R.id.height);
        EditText input1 = findViewById(R.id.weight);
        Button btn = findViewById(R.id.btn_count);
        btn.setOnClickListener(this);
        //this.onclick
    }
    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: AAAAAAAAAA");
        //获取用户输入
        EditText input = findViewById(R.id.height);
        EditText input1 = findViewById(R.id.weight);
        Double height = Double.parseDouble(input.getText().toString());
        Double weight = Double.parseDouble(input1.getText().toString());
        Double BMI = weight / (height * height);
        DecimalFormat df = new DecimalFormat("#.00");
        TextView resault = (TextView)findViewById(R.id.txt);
        if (BMI < 18.5) {
            resault.setText(df.format(BMI) + ",您的体重偏轻，请您多注意营养！");
            } else if (BMI <= 24.9) {
            resault.setText(df.format(BMI) + ",您的体重正常,请您保持！");
            } else if (BMI <= 29.9) {
            resault.setText(df.format(BMI) + ",您的体重偏重，请您控制饮食！");
            } else if (BMI <= 34.9) {
            resault.setText(df.format(BMI) + ",您的体重肥胖，请您控制饮食，适当锻炼!");
            } else if (BMI <= 39.9) {
            resault.setText(df.format(BMI) + ",您的体重过于肥胖，请您控制饮食，多加锻炼!");
            } else {
            resault.setText(df.format(BMI) + ",您的体重严重肥胖，请咨询医生做相关治疗!");
            }
}
}

