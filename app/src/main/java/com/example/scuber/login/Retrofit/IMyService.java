package com.example.scuber.login.Retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IMyService {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("id") String id,
                                    @Field("password") String password,
                                    @Field("name") String name,
                                    @Field("phonenum") String phonenum);

    @POST("registerCall")
    @FormUrlEncoded
    Observable<String> registerCall(@Field("id") String id,
                                    @Field("from") String from,
                                    @Field("to") String to,
                                    @Field("time_hour") Integer time_hour,
                                    @Field("time_min") Integer time_min);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("id") String id,
                                 @Field("password") String password);

    @POST("userInfo")
    @FormUrlEncoded
    Observable<String> findUser(@Field("id") String id);


    @PUT("updateProfile")
    Observable<String> updateProfile(@Field("id") String id,
                                     @Field("profile") String profile);
    

    @GET("calls")
    Observable<String> getCalls ();

}
