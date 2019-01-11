package com.example.scuber.login.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IMyService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("id") String id,
                                    @Field("password") String password,
                                    @Field("name") String name,
                                    @Field("phonenum") String phonenum);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("id") String id,
                                 @Field("password") String password);
}
