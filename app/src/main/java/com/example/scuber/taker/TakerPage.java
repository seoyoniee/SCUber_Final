package com.example.scuber.taker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.scuber.ChargePoint;
import com.example.scuber.R;

import java.util.ArrayList;

public class TakerPage extends Activity {

    private TextView tvName;
    private TextView tvPoint;
    private TextView tvNoShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takerpage);

        final ArrayAdapter<Object> listAdapter = new ArrayAdapter<>(this, R.layout.activity_takerpage);
        final ListView taker_history = findViewById(R.id.taker_history);

        tvName = (TextView)findViewById(R.id.tvName);
        tvPoint = (TextView)findViewById(R.id.tvPoint);
        tvNoShow = (TextView)findViewById(R.id.tvNoShow);

        //해당 아이디에 대한 정보를 가져와서 json 파싱해서 보여줘


        taker_history.setAdapter(listAdapter);
        taker_history.setChoiceMode(ListView.CHOICE_MODE_SINGLE);





        //포인트충전 클릭시 ChargePoint 클래스로 이동
        ImageButton btn_charge= findViewById(R.id.btn_charge);
        btn_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TakerPage.this, ChargePoint.class));
            }
        });
    }

}
