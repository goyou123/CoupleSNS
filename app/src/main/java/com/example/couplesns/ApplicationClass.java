package com.example.couplesns;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.couplesns.RetrofitJava.RetroBaseApiService;
import com.example.couplesns.RetrofitJava.RetroClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationClass extends Application {
    /*사용을 위해서 메니페스트 application name:.Application.class 등록*/
    String defaultProfile ;
    Retrofit retrofit;
    RetroBaseApiService apiService;
    String autoLoginKey;
    String sharedcouplekey;
    RetroClient retroClient;
    @Override
    public void onCreate() {
        super.onCreate();
        defaultProfile = "http://3.34.137.189/img/default_profile.png";
        Gson gson = new GsonBuilder()
        .setLenient()
        .create();
        retrofit = new Retrofit
                .Builder()
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(RetroBaseApiService.Base_URL)
                .build();
        apiService = retrofit.create(RetroBaseApiService.class);
        retroClient = RetroClient.getInstance(this).createBaseApi();;;


        SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
        autoLoginKey = sharedPreferences1.getString("auto_login","no_autologin_key");

        //이메일값으로 그 이메일에 해당하는 커플키 가져오기 - shared저장 signup2
        SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
        sharedcouplekey = sharedPreferences.getString(autoLoginKey,"no_key_login");
    } //OnCreate

    //로그인한 사람 이메일 가져오는 메소드
    public String getShared_Email(){
        SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
        autoLoginKey = sharedPreferences1.getString("auto_login","no_autologin_key");
        return autoLoginKey;
    }

    //로그인한 사람의 커플키를 가져오는 메소드
    public String getShared_Couplekey(){
        SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
        sharedcouplekey = sharedPreferences.getString(autoLoginKey,"no_key_login");
        return sharedcouplekey;
    }






    public class NullOnEmptyConverterFactory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<ResponseBody, Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) return null;
                    return delegate.convert(body);
                }
            };
        }
    }//NULL 어쩌고



}//END
