package com.example.scuber.giver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scuber.R;
import com.example.scuber.Request_item;
import com.example.scuber.SeeProfile;
import com.example.scuber.login.Retrofit.IMyService;
import com.example.scuber.login.Retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class GiverReqAdapter_accept extends BaseAdapter {

    private Context context;
    private List<Request_item> reqList;
    private String userID;

    IMyService iMyService;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public GiverReqAdapter_accept(Context context, List<Request_item> reqList, String userID) {
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

    //아이템별 아이디를 반환하는 메소드야
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.activity_giver_accept_content, null);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);


        //view에 요소들을 연결시켜줘
        TextView from = (TextView) v.findViewById(R.id.tvFrom);
        TextView to = (TextView) v.findViewById(R.id.tvDest);
        TextView time_hour = (TextView) v.findViewById(R.id.tvHour);
        TextView time_min = (TextView) v.findViewById(R.id.tvMin);
        //TextView id = (TextView)v.findViewById(R.id.tvTaker);


        final String objectID = reqList.get(position).get_id();

        from.setText(reqList.get(position).getFrom());
        to.setText(reqList.get(position).getTo());
        //id.setText(reqList.get(position).getId());
        time_hour.setText(Integer.toString(reqList.get(position).getTime_hour()));
        time_min.setText(Integer.toString(reqList.get(position).getTime_min()));

        //btn_profile 클릭시 해당 request를 요청한 user의 id를 가져와서 그 id의 프로필을 띄울거야
        Button btn_profile = v.findViewById(R.id.btn_profile);
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeeProfile.class);

                intent.putExtra("id", userID);     //지금 id는 아니야!
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return v;
    }

        private void returnGiverID (String _id) {
            compositeDisposable.add(iMyService.returnGiverID(_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {

                       //return response;
                    }

                }));
    }


}


