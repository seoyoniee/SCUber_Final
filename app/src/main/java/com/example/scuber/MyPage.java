package com.example.scuber;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scuber.login.Retrofit.IMyService;
import com.example.scuber.login.Retrofit.RetrofitClient;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MyPage extends Activity {

    private TextView tvName;
    private TextView tvPoint;
    private TextView tvNoShow;
    private ImageView ivProfile;
    private ImageButton btn_update;



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

        final ArrayAdapter<Object> listAdapter = new ArrayAdapter<>(this, R.layout.activity_mypage);
        final ListView taker_history = findViewById(R.id.taker_history);

        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        btn_update = (ImageButton) findViewById(R.id.btn_update);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPoint = (TextView) findViewById(R.id.tvPoint);
        tvNoShow = (TextView) findViewById(R.id.tvNoShow);
        userId = getIntent().getStringExtra("id");


        //해당 아이디에 대한 정보를 가져와
        findUser(userId);



        taker_history.setAdapter(listAdapter);
        taker_history.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        //포인트충전 클릭시 ChargePoint 클래스로 이동
        ImageButton btn_charge = findViewById(R.id.btn_charge);
        btn_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyPage.this, ChargePoint.class));
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
                   // updateProfile(userId, ba1);
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
                        tvName.setText(jsonObject.getString("name"));
                        tvPoint.setText(jsonObject.getString("point"));
                        tvNoShow.setText(jsonObject.getString("noShow"));

                        //프로필 사진을 디코딩해서 보여주는거야
                       byte[] imageBytes = Base64.decode(jsonObject.getString("profile"), Base64.DEFAULT);
                       Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0 , imageBytes.length);
                       ivProfile.setImageBitmap(bmp);
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
