package com.example.scuber.taker;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scuber.R;
import com.example.scuber.Request_item;
import com.example.scuber.login.Retrofit.IMyService;
import com.example.scuber.login.Retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class EvalAdapter extends BaseAdapter {

    private Context context;
    private List<Request_item> reqList;
    private String userID;

    IMyService iMyService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public EvalAdapter(Context context, List<Request_item> reqList, String userID) {
        this.context = context;
        this.reqList = reqList;
        this.userID = userID;
    }

    //총 개수를 알려주는 메소드
    @Override
    public int getCount() {
        return reqList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int position) {
        return reqList.get(position);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.activity_taker_evaluate_content, null);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        Button btn_success;
        Button btn_noshow;
        Button btn_late;

        //view에 요소들을 연결시켜줘
        TextView from = (TextView)v.findViewById(R.id.tvFrom);
        TextView to = (TextView)v.findViewById(R.id.tvDest);
        TextView time_hour = (TextView)v.findViewById(R.id.tvHour);
        TextView time_min = (TextView)v.findViewById(R.id.tvMin);

        final String objectID = reqList.get(position).get_id();

        from.setText(reqList.get(position).getFrom());
        to.setText(reqList.get(position).getTo());
        time_hour.setText(Integer.toString(reqList.get(position).getTime_hour()));
        time_min.setText(Integer.toString(reqList.get(position).getTime_min()));


        btn_success = (Button) v.findViewById(R.id.btn_success);
        btn_success.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String newState = "SUCCESS";
                updateCallState2(objectID, newState);

            }
        });

        btn_noshow = (Button) v.findViewById(R.id.btn_make_noshow);
        btn_noshow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String newState = "NO SHOW";
                updateCallState2(objectID, newState);

                //objectID 찾아서 giverID반환해
                returnGiverID1(objectID);


            }
        });

        btn_late = (Button) v.findViewById(R.id.btn_late);
        btn_late.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String newState = "LATE";
                updateCallState2(objectID, newState);

                //objectID 찾아서 giverID반환해
                returnGiverID2(objectID);

            }
        });

        return v;
    }

    private void updateCallState2(String _id, String state) {
        compositeDisposable.add(iMyService.updateCallState2(_id, state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        Toast.makeText(context, "match evaluated!", Toast.LENGTH_SHORT).show();
                    }
                }));


    }

    private void returnGiverID1 (String _id) {
        compositeDisposable.add(iMyService.returnGiverID1(_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        //response가 giver의 아이디야 -> giver의 id로 정보 찾아서 noShow값 +1해줘야대
                        noShowPlus(response);
                    }

                }));
    }

    private void returnGiverID2 (String _id) {
        compositeDisposable.add(iMyService.returnGiverID2(_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                        //response가 giver의 아이디야 -> giver의 id로 정보 찾아서 late값 +1해줘야대
                        latePlus(response);
                    }

                }));
    }


    private void latePlus (String id) {
        compositeDisposable.add(iMyService.latePlus(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                    }

                }));
    }

    private void noShowPlus(String id) {
        compositeDisposable.add(iMyService.noShowPlus(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.e("testnoShow", response);
                    }
                }));
        }

    }


