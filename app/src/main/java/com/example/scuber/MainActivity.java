package com.example.scuber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.scuber.giver.MainGiver;
import com.example.scuber.taker.MainTaker;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //btn_taker를 클릭시 MainTaker 클래스로 이동
        ImageButton btn_taker = findViewById(R.id.btn_taker);
        btn_taker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainTaker.class));
            }
        });

        //btn_giver를 클릭시 MainGiver 클래스로 이동
        ImageButton btn_giver = findViewById(R.id.btn_giver);
        btn_giver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainGiver.class));
            }
        });


    }
}
