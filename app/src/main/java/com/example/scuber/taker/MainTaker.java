package com.example.scuber.taker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.scuber.MainActivity;
import com.example.scuber.MyPage;
import com.example.scuber.R;
import com.example.scuber.login.MainLogin;
import com.example.scuber.login.Retrofit.IMyService;
import com.example.scuber.login.Retrofit.RetrofitClient;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainTaker extends Activity {

    String userId;
    int time_hour, time_min;
    EditText from, to;
    TimePicker mTimePicker;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop(){
        compositeDisposable.clear();
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taker);

        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);


       userId = getIntent().getStringExtra("id");
       from = (EditText) findViewById(R.id.btn_from);
       to = (EditText) findViewById(R.id.btn_to);
       mTimePicker = (TimePicker) findViewById(R.id.time_picker);

       Button btn_mypage = findViewById(R.id.btn_mypage);
       Button btn_request = findViewById(R.id.btn_request);
       Button btn_requests = findViewById(R.id.btn_requests);




        Log.e("myPageID", userId);




        //btn_page를 클릭시 MyPage 클래스로 이동
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainTaker.this, MyPage.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });

        //btn_request를 클릭시 registerCall을 호출해서 정보 넣어줘
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("registerCall","registerCall!!" );

             //시간을 24시제로 해줘
                mTimePicker.setIs24HourView(true);

                time_min = mTimePicker.getMinute();
                time_hour = mTimePicker.getHour();

                if(TextUtils.isEmpty(from.getText().toString())){
                    Toast.makeText(MainTaker.this,"Departure cannot be null or empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(to.getText().toString())){
                    Toast.makeText(MainTaker.this,"Destination cannot be null or empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerCall(userId, from.getText().toString(),
                        to.getText().toString(), time_hour, time_min);


            }
        });

        //btn_requests를 클릭시 가지고 있는 request들의 정보를 보여줘
        btn_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainTaker.this, TakerRequests.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });

    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("valid", false);

        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }


    private void registerCall(String id, String from, String to, Integer time_hour, Integer time_min) {
        compositeDisposable.add(iMyService.registerCall(id, from, to, time_hour, time_min)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        //registerCall을 할때 response로 해당 call의 objectID가 들어와
                        Log.e("registerCallsRes", response);
                        Intent intent = new Intent(MainTaker.this, TakerRequests.class);
                        intent.putExtra("id",userId);
                      //  intent.putExtra("call_id",response);
                        startActivity(intent);
                        Toast.makeText(MainTaker.this, "Call Registration Success", Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
