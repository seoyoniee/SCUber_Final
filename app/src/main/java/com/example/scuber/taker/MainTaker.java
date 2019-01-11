package com.example.scuber.taker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.scuber.R;

public class MainTaker extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taker);

        //btn_taker를 클릭시 MainTaker 클래스로 이동
        Button btn_taker = findViewById(R.id.btn_mypage);
        btn_taker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainTaker.this, TakerPage.class));
            }
        });

    }
}
