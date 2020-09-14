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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationClass extends Application {
    /*사용을 위해서 메니페스트 application name:.Application.class 등록*/
    static String serverImageRoot;
    static String serverStoryImageRoot;
    String defaultProfile ;
    Retrofit retrofit;
    RetroBaseApiService apiService;
    String autoLoginKey;
    String sharedcouplekey;
    RetroClient retroClient;


    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;




    @Override
    public void onCreate() {
        super.onCreate();
        defaultProfile = "http://13.125.182.117/img/default_profile.png";
        serverImageRoot ="http://13.125.182.117/img/";
        serverStoryImageRoot = "http://13.125.182.117/img/storyimages";
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

    //레트로핏 빌드를 어댑터에서 사용하기 위해 리턴함 - StoryAdapter
    public RetroClient retroInAdapter(){
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
        return retroClient;
    }

    public String formatTimeString(long regTime) {
        long curTime = System.currentTimeMillis();
        Log.d("커맨트어댑터 어프리케이션클래스", "formatTimeString: "+curTime);
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if (diffTime < SEC) {
            msg = "방금 전";
        } else if ((diffTime /= SEC) < MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= MIN) < HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= HOUR) < DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= DAY) < MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }

    //댓글같은거 특정시간 불러와서 몇분전, 몇시간전 계산

//    /** 몇분전, 방금 전, */
//    private static class TIME_MAXIMUM{
//        public static final int SEC = 60;
//        public static final int MIN = 60;
//        public static final int HOUR = 24;
//        public static final int DAY = 30;
//        public static final int MONTH = 12;
//    }
//    public static String formatTimeString(long regTime) {
//        long curTime = System.currentTimeMillis();
//        long diffTime = (curTime - regTime) / 1000;
//        String msg = null;
//        if (diffTime < TIME_MAXIMUM.SEC) {
//            msg = "방금 전";
//        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
//            msg = diffTime + "분 전";
//        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
//            msg = (diffTime) + "시간 전";
//        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
//            msg = (diffTime) + "일 전";
//        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
//            msg = (diffTime) + "달 전";
//        } else {
//            msg = (diffTime) + "년 전";
//        }
//        return msg;
//    }

    /*비밀번호 암호화*/
    public String encryptSHA256(String str){

        String sha = "";

        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }

            sha = sb.toString();

        }catch(NoSuchAlgorithmException e){
            //e.printStackTrace();
            System.out.println("Encrypt Error - NoSuchAlgorithmException");
            sha = null;
        }

        return sha;
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
