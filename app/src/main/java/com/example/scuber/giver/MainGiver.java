package com.example.scuber.giver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.scuber.MyPage;
import com.example.scuber.R;
import com.example.scuber.taker.MainTaker;

public class MainGiver extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giver);

        //btn_mypage를 클릭시 MyPage 클래스로 이동
        Button btn_mypage = findViewById(R.id.btn_mypage);
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGiver.this, MyPage.class);
//                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });


    }
}
