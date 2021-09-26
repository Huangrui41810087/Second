package com.swufestu.second;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SencondActivity extends AppCompatActivity {

    private static final String TAG = "SencondActivity";
    EditText dollarText,euroText,wonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sencond);

        Intent intent=getIntent();
        float dollar2=intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2=intent.getFloatExtra("euro_rate_key",0.0f);
        float won2=intent.getFloatExtra("won_rate_key",0.0f);
        Log.i(TAG, "onCreate: dollar2="+dollar2);
        Log.i(TAG, "onCreate: euro2="+euro2);
        Log.i(TAG, "onCreate: won2="+won2);

        dollarText=findViewById(R.id.new_dollar);
        euroText=findViewById(R.id.new_euro);
        wonText=findViewById(R.id.new_won);

        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));
    }

    public void save(View btn){
        Log.i(TAG, "save: ");
        //获取新输入的值
        float newDollar=Float.parseFloat(dollarText.getText().toString());
        float newEuro=Float.parseFloat(euroText.getText().toString());
        float newWon=Float.parseFloat(wonText.getText().toString());

        Intent first = getIntent();
       // first.putExtra("dollar_key2",newDollar);
        //first.putExtra("euro_key2",newEuro);
        //first.putExtra("won_key2",newWon);

        Bundle bdl = new Bundle();
        bdl.putFloat("dollar_key3",newDollar);
        bdl.putFloat("euro_key3",newEuro);
        bdl.putFloat("won_key3",newWon);
        first.putExtras(bdl);
        setResult(5,first);

        //关闭当前窗口
        finish();
    }
}
