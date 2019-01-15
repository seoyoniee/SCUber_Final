package com.example.scuber;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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

import com.example.scuber.login.MainLogin;
import com.example.scuber.login.Retrofit.IMyService;
import com.example.scuber.login.Retrofit.RetrofitClient;
import com.example.scuber.mypage_fragment.GiverHistory;
import com.example.scuber.mypage_fragment.SectionsPageAdapter;
import com.example.scuber.mypage_fragment.TakerHistory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MyPage extends AppCompatActivity {

    private TextView tvName;
    private TextView tvContact;
    private TextView tvPoint;
    private TextView tvNoShow;
    private ImageView ivProfile;
    private ImageButton btn_update;
    private PopupWindow popup;
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    private TextView popupN;
    private TextView popupL;
    private TextView popupNP;
    private TextView popupLP;
    private TextView totalP;

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
        setContentView(R.layout.activity_mypage);

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
        btn_update = (ImageButton) findViewById(R.id.btn_update);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPoint = (TextView) findViewById(R.id.tvPoint);
        tvNoShow = (TextView) findViewById(R.id.tvNoShow);
        tvContact = (TextView) findViewById(R.id.tvContact);
        userId = getIntent().getStringExtra("id");





        Log.e("findUserID", userId);

        //해당 아이디에 대한 정보를 가져와
        findUser(userId);



        //포인트충전 클릭시 ChargePoint 클래스로 이동
        ImageButton btn_charge = findViewById(R.id.btn_charge);
        btn_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPage.this, ChargePoint.class);
                intent.putExtra("id",userId);
                startActivity(intent);
            }
        });

        //Logout 클릭시 MainLogin 클래스로 이동
        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyPage.this, MainLogin.class));
            }
        });

        //업데이트 버튼을 누르면 갤러리가 뜨는거야 그래서 프로필 사진 바꿀수있게 할꺼야
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
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

        popupN = popupContent.findViewById(R.id.tv_noshow_howmany);
        popupL = popupContent.findViewById(R.id.tv_late_howmany);
        popupNP = popupContent.findViewById(R.id.tv_noshow_point);
        popupLP = popupContent.findViewById(R.id.tv_late_point);
        totalP = popupContent.findViewById(R.id.tv_total_point);

        //userID의 정보를 가져와야대
        findUser2(userId);

        popup.setFocusable(true);
        popup.setOutsideTouchable(true);
        popup.setAnimationStyle(R.style.PopupAnimation);

        //탭으로 History 구현하기!
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        //Setup the ViewPager with the sections adapter
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TakerHistory(), "Taker History");
        adapter.addFragment(new GiverHistory(), "Giver History");
        viewPager.setAdapter(adapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성해주는거야
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    // 선택한 이미지를 프로필사진으로 보여줘
                    ivProfile.setImageBitmap(img);

                    //이 사진을 다시 디코딩해서 db에 수정해줘야대
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                    byte[] ba = bao.toByteArray();
                    String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

                    Log.e("updateProfile", ba1);

                    // 여기서 updateProfile을 호출해야대
                   updateProfile(userId, ba1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
                        Integer noshowP = jsonObject.getInt("noShow");
                        Integer lateP = jsonObject.getInt("late");
                        Integer totalP = noshowP *2 + lateP;

                        tvName.setText(jsonObject.getString("name"));
                        tvPoint.setText(jsonObject.getString("point"));
                        tvNoShow.setText(Integer.toString(totalP));
                        tvContact.setText(jsonObject.getString("phonenum"));

                        //프로필 사진을 디코딩해서 보여주는거야
                        byte[] imageBytes = Base64.decode(jsonObject.getString("profile"), Base64.DEFAULT);
                        Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0 , imageBytes.length);
                        ivProfile.setImageBitmap(bmp);
                    }
                }));
    }

    private void findUser2(String id) {
        compositeDisposable.add(iMyService.findUser2(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.e("test33", response);
                        //response에 담긴 json을 파싱해서 보여주자!

                        JSONObject jsonObject = new JSONObject(response);

                        popupN.setText(jsonObject.getString("noShow"));
                        popupL.setText(jsonObject.getString("late"));

                        popupNP.setText(Integer.toString(jsonObject.getInt("noShow")*2));
                        popupLP.setText(Integer.toString(jsonObject.getInt("late")));
                        totalP.setText(Integer.toString(jsonObject.getInt("noShow")*2 + jsonObject.getInt("late")));



                    }
                }));

    }



    private void updateProfile(String id, String profile) {
        Log.e("updateProfile2", id);
        compositeDisposable.add(iMyService.updateProfile(id, profile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.e("resultUpdateProfile", response);
                    }
                }));

    }


}
