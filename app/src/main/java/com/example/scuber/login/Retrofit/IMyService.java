package com.example.scuber.login.Retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.QueryName;

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

    @POST("userInfo")
    @FormUrlEncoded
    Observable<String> findUser2(@Field("id") String id);


    @POST("updateProfile")
    @FormUrlEncoded
    Observable<String> updateProfile(@Field("id") String id,
                                     @Field("profile") String profile);

    @POST("updateCallState")
    @FormUrlEncoded
    Observable<String> updateCallState(@Field("_id") String _id,
                                       @Field("state") String state,
                                       @Field("giver") String giver);

    @POST("updateCallState2")
    @FormUrlEncoded
    Observable<String> updateCallState2(@Field("_id") String _id,
                                       @Field("state") String state);


    @POST("pointChangePlus")
    @FormUrlEncoded
    Observable<String> pointChangePlus(@Field("id") String id,
                                       @Field("point") Integer point);

    @POST("pointChangeMinus")
    @FormUrlEncoded
    Observable<String> pointChangeMinus(@Field("id") String id,
                                       @Field("point") Integer point);

    @POST("noShowPlus")
    @FormUrlEncoded
    Observable<String> noShowPlus(@Field("id") String id);

    @POST("latePlus")
    @FormUrlEncoded
    Observable<String> latePlus(@Field("id") String id);

    @POST("returnID")
    @FormUrlEncoded
    Observable<String> returnID(@Field("_id") String _id);

    @POST("returnID")
    @FormUrlEncoded
    Observable<String> returnID2(@Field("_id") String _id);

    @POST("returnGiverID")
    @FormUrlEncoded
    Observable<String> returnGiverID1(@Field("_id") String _id);

    @POST("returnGiverID")
    @FormUrlEncoded
    Observable<String> returnGiverID2(@Field("_id") String _id);

    @POST("returnGiverID")
    @FormUrlEncoded
    Observable<String> returnGiverID3(@Field("_id") String _id);

    @GET("calls")
    Observable<String> getCalls ();

    @POST("takerCalls")
    @FormUrlEncoded
    Observable<String> takerCalls (@Field ("id") String id);

    @POST("giverCalls")
    @FormUrlEncoded
    Observable<String> giverCalls (@Field("giver") String giver);

}
