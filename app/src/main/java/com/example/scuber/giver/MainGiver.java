package com.example.scuber.giver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.scuber.MyPage;
import com.example.scuber.R;
import com.example.scuber.Request_item;
import com.example.scuber.login.Retrofit.IMyService;
import com.example.scuber.login.Retrofit.RetrofitClient;
import com.example.scuber.taker.MainTaker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainGiver extends Activity {

    String userId;
    ListView listView;
    GiverReqAdapter adapter;
    List<Request_item> reqList;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giver);

        userId = getIntent().getStringExtra("id");

        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);


        listView = findViewById(R.id.requests);
        reqList = new ArrayList<Request_item>();

        //리스트와 어댑터를 연결시켜줘
        adapter = new GiverReqAdapter(getApplicationContext(), reqList, userId);
        listView.setAdapter(adapter);


        getCalls();


        //btn_mypage를 클릭시 MyPage 클래스로 이동
        Button btn_mypage = findViewById(R.id.btn_mypage);
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGiver.this, MyPage.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });


        //btn_requsts를 클릭시 내가 giver이고 state가 match completed인거만 보여줘
        Button btn_requests = findViewById(R.id.btn_requests);
        btn_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGiver.this, GiverRequests.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });



    }

    private void getCalls() {
        compositeDisposable.add(iMyService.getCalls()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        Log.e("getCalls", response);
                        //이 response를 json parsing해서 listview로 보여줄거야

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int cnt = 0;

                            String from, to, state, _id, id;
                            Integer time_hour,time_min;

                            while(cnt < jsonArray.length()) {
                                JSONObject object = jsonArray.getJSONObject(cnt);

                                from = object.getString("from");
                                to = object.getString("to");
                                state = object.getString("state");

                                time_hour = object.getInt("time_hour");
                                time_min = object.getInt("time_min");

                                _id = object.getString("_id");
                                id = object.getString("id");

                                Log.e("plz", id);
                                Log.e("plz2", userId);
                                if(state.equals("match waiting")) {
                                    if(!id.equals(userId)){
                                        Request_item item = new Request_item(from, to, time_hour, time_min, state, _id);
                                        reqList.add(item);
                                    }
                                }
                                cnt++;
                            }

                            adapter.notifyDataSetChanged();

                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }
}
