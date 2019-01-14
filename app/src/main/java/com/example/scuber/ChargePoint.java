package com.example.scuber;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.scuber.login.MainLogin;
import com.example.scuber.login.Retrofit.IMyService;
import com.example.scuber.login.Retrofit.RetrofitClient;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ChargePoint extends Activity {

    EditText etPoint;
    String tmp, userId;
    Integer chargePoint;
    Button btn_charge, btn_mypage;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        btn_charge = findViewById(R.id.btn_charge);
        btn_mypage = findViewById(R.id.btn_mypage);

        etPoint = findViewById(R.id.etPoint);
        userId = getIntent().getStringExtra("id");

        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        //etPoint의 값을 읽어와서 해당 id 값을 db에서 검색해서 바꿔줘야대대


        //btn_charge 누르면 충전완료야
        btn_charge.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                tmp = etPoint.getText().toString();
                chargePoint =  Integer.parseInt(tmp);
                pointChangePlus(userId, chargePoint);
            }
        });

        //btn_mypage 누르면 다시 내 정보로!
        btn_mypage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(ChargePoint.this, MyPage.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });


    }

    private void pointChangePlus(String id, Integer point) {
        compositeDisposable.add(iMyService.pointChangePlus(id, point)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.e("testCharge", response);

                    }
                }));

    }

}