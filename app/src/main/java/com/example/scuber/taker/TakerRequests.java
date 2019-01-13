package com.example.scuber.taker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scuber.R;
import com.example.scuber.login.Retrofit.IMyService;
import com.example.scuber.login.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TakerRequests extends AppCompatActivity {

    //String[] FROM = {"N1","E11","E2","N22","W8-1","W10","E9-2","E12"};
    //String[] DEST = {"E2","S18","N4","E11","E3","N10","N1","N1"};

    ListView listView;
    ReqAdapter adapter;
    List<Request_item> reqList;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taker_requests);


        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);


        listView = findViewById(R.id.request);
        reqList = new ArrayList<Request_item>();

        //리스트와 어댑터를 연결시켜줘
        adapter = new ReqAdapter(getApplicationContext(), reqList);
        listView.setAdapter(adapter);

        getCalls();

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

                            String from, to;
                            Integer time_hour, time_min;

                            while(cnt < jsonArray.length()) {
                                JSONObject object = jsonArray.getJSONObject(cnt);

                                from = object.getString("from");
                                to = object.getString("to");

                                time_hour = object.getInt("time_hour");
                                time_min = object.getInt("time_min");

                                Request_item item = new Request_item(from, to, time_hour, time_min);
                                reqList.add(item);
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
