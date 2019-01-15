package com.example.scuber.mypage_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.scuber.R;
import com.example.scuber.Request_item;
import com.example.scuber.login.Retrofit.IMyService;
import com.example.scuber.login.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class GiverHistory extends Fragment {
    private static final String TAG = "Giver History";

    ListView listView;
    GiverHisAdapter adapter;
    List<Request_item> reqList;
    String userId;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_giver_history_fragment, container, false);


        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);


        listView = view.findViewById(R.id.requestGiver);
        reqList = new ArrayList<Request_item>();
        userId = getActivity().getIntent().getStringExtra("id");

        //리스트와 어댑터를 연결시켜줘
        adapter = new GiverHisAdapter(getActivity().getApplicationContext(), reqList, userId);
        listView.setAdapter(adapter);

        Log.e("testabc", userId);
        giverCalls(userId);

        return view;

    }

    private void giverCalls(String giver) {
        compositeDisposable.add(iMyService.giverCalls(giver)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        Log.e("giverCalls", response);
                        //이 response를 json parsing해서 listview로 보여줄거야

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            int cnt = 0;

                            String from, to, state, _id;
                            Integer time_hour, time_min;

                            while(cnt < jsonArray.length()) {
                                JSONObject object = jsonArray.getJSONObject(cnt);

                                from = object.getString("from");
                                to = object.getString("to");
                                state = object.getString("state");
                                _id = object.getString("_id");

                                time_hour = object.getInt("time_hour");
                                time_min = object.getInt("time_min");

                                Log.e("callState", state);

                                if(state.equals("SUCCESS") || state.equals("NO SHOW") || state.equals("LATE")) {
                                    Request_item item = new Request_item(from, to, time_hour, time_min, state, _id);
                                    reqList.add(item);
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