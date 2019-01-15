package com.example.scuber.mypage_fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.scuber.R;
import com.example.scuber.Request_item;
import com.example.scuber.login.Retrofit.IMyService;
import com.example.scuber.login.Retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;

public class GiverHisAdapter extends BaseAdapter {

    private Context context;
    private List<Request_item> reqList;
    private String userID;

    IMyService iMyService;


    public GiverHisAdapter(Context context, List<Request_item> reqList, String userID) {
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
        View v = View.inflate(context, R.layout.activity_giver_history_content, null);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        //view에 요소들을 연결시켜줘
        TextView from = (TextView)v.findViewById(R.id.tvFrom);
        TextView to = (TextView)v.findViewById(R.id.tvDest);
        TextView time_hour = (TextView)v.findViewById(R.id.tvHour);
        TextView time_min = (TextView)v.findViewById(R.id.tvMin);
        TextView state = (TextView)v.findViewById(R.id.tvState);


        final String objectID = reqList.get(position).get_id();

        from.setText(reqList.get(position).getFrom());
        to.setText(reqList.get(position).getTo());
        state.setText(reqList.get(position).getState());
        time_hour.setText(Integer.toString(reqList.get(position).getTime_hour()));
        time_min.setText(Integer.toString(reqList.get(position).getTime_min()));


        return v;
    }


}
