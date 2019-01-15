package com.example.scuber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.scuber.giver.MainGiver;
import com.example.scuber.login.MainLogin;
import com.example.scuber.taker.MainTaker;

import static android.support.constraint.Constraints.TAG;

public class MainActivity extends Activity {

//ALOOOOALAOALAAO
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //인자로 유저의 아이디를 받아와
        userId = getIntent().getStringExtra("id");
        Log.d("aaa",userId);

        //btn_taker를 클릭시 MainTaker 클래스로 이동
        ImageButton btn_taker = findViewById(R.id.btn_taker);
        btn_taker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainTaker.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });


        //btn_giver를 클릭시 MainGiver 클래스로 이동
        ImageButton btn_giver = findViewById(R.id.btn_giver);
        btn_giver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainGiver.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });


    }
}
