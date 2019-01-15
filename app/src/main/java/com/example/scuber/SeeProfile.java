package com.example.scuber;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.scuber.giver.MainGiver;
import com.example.scuber.login.Retrofit.IMyService;
import com.example.scuber.login.Retrofit.RetrofitClient;
import com.example.scuber.mypage_fragment.SectionsPageAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SeeProfile extends AppCompatActivity {

    private TextView tvName;
    private TextView tvContact;
    private TextView tvPoint;
    private TextView tvNoShow;
    private ImageView ivProfile;
    private ImageButton btn_update;
    private PopupWindow popup;
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    ListView lvTaker;

    List<Request_item> reqList;


    IMyService iMyService;
    String userId;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeprofile);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

//        lvTaker = findViewById(R.id.taker_history);
        // lvGiver = findViewById(R.id.giver_history);
        reqList = new ArrayList<Request_item>();

        //리스트와 어댑터를 연결시켜줘
//        adapTaker = new TReqHisAdapter(getApplicationContext(), reqList);
//        lvTaker.setAdapter(adapTaker);
        // lvGiver.setAdapter(adapGiver);

        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvName = (TextView) findViewById(R.id.tvName);
        tvNoShow = (TextView) findViewById(R.id.tvNoShow);
        tvContact = (TextView) findViewById(R.id.tvContact);
        userId = getIntent().getStringExtra("id");

        Log.e("findUserID", userId);

        //해당 아이디에 대한 정보를 가져와
        findUser(userId);

        //btn_close 클릭시 GiverReqAdapter 클래스로 이동
        Button btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeeProfile.this, MainGiver.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });

        //원하는 팝업창 레이아웃을 인플레이터로 띄우는거야
        View popupContent = getLayoutInflater().inflate(R.layout.activity_noshow, null);
        popup = new PopupWindow();

        //popup should wrap content view
        popup.setWindowLayoutMode(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setHeight(250);
        popup.setWidth(350);
        popup.setContentView(popupContent);

        popupContent.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setAnimationStyle(R.style.PopupAnimation);

    }

    @Override
    protected void onPause() {
        super.onPause();
        popup.dismiss();
    }

    public boolean onTouch(View v, MotionEvent event) {
        //Handle direct touch events passed to the PopupWindow
        return false;
    }

    public void onShowWindowClick(View v) {
        if (popup.isShowing()) {
            popup.dismiss();
        } else {
            //Show the PopupWindow anchored to the button we
            //pressed. It will be displayed below the button
            //if there's room, otherwise above.
            popup.showAsDropDown(v);
        }
    }

    private void findUser(String id) {
        compositeDisposable.add(iMyService.findUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.e("test33", response);
                        //response에 담긴 json을 파싱해서 보여주자!

                        JSONObject jsonObject = new JSONObject(response);
                        tvName.setText(jsonObject.getString("name"));
                        tvNoShow.setText(jsonObject.getString("noShow"));
                        tvContact.setText(jsonObject.getString("phonenum"));

                        //프로필 사진을 디코딩해서 보여주는거야
                        byte[] imageBytes = Base64.decode(jsonObject.getString("profile"), Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0 , imageBytes.length);
                        ivProfile.setImageBitmap(bmp);
                    }
                }));
    }
}
