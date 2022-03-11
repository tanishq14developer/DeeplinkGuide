package com.tanishq.legaldeepilinking;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.TextView;




public class MainActivity extends AppCompatActivity {

    public static final String PRODUCTS_DEEP_LINK = "/";

    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textview);



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}