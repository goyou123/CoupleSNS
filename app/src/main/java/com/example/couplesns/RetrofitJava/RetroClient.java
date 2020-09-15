package com.example.couplesns.RetrofitJava;

import com.example.couplesns.DataClass.CommentData;
import com.example.couplesns.DataClass.ImgData_ex;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RequestBody.RequestPut;
import com.example.couplesns.ResponseBody.ResponseGet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingDeque;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private RetroBaseApiService apiService;
    public static String baseUrl = RetroBaseApiService.Base_URL;
    private static Context mContext;
    private static Retrofit retrofit;
//    private HashMap<String, Object> parameters;
//    private RetroCallback callback;

    private static class SingletonHolder {
        private static RetroClient INSTANCE = new RetroClient(mContext);
    }


    //레트로핏 인스턴스 생성 - 후에 사용하려는 액티비티에서 APi생성과 함께 인스턴스를 선언해 레트로핏 객체를 만들어야 함
    public static RetroClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }

        return SingletonHolder.INSTANCE;
    }


    public RetroClient(Context context) {
        //레트로핏 객체 생성
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                //데이터 파싱 설정 - 객체와 JSON의 변환을 자동으로 함
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(new NullOnEmptyConverterFactory())
                //서버 url설정
                .baseUrl(baseUrl)
                //객체정보 반환
                .build();

    }
    public class NullOnEmptyConverterFactory extends Converter.Factory {
        /*파싱시 null값 처리해주는 converter - 추가공부 필요*/
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
    }

    /*Retrofit 인스턴스로 인터페이스 객체 구현*/
    public RetroClient createBaseApi() {
        apiService = retrofit.create(RetroBaseApiService.class);
        return this;
    }
    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    //레트로핏 객체를 생성한다.
    public  <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }


    public void getFirst(String id, final RetroCallback callback) {
        apiService.getFirst(id).enqueue(new Callback<ResponseGet>() {
            @Override
            public void onResponse(Call<ResponseGet> call, Response<ResponseGet> response) {
                if (response.isSuccessful()) {
                    //결과 코드가 200범위일때 참값을 반환 - (정상통신 = 200 / 에러 = 500)
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseGet> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getSecond(String id, final RetroCallback callback) {
        apiService.getSecond(id).enqueue(new Callback<List<ResponseGet>>() {
            @Override
            public void onResponse(Call<List<ResponseGet>> call, Response<List<ResponseGet>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ResponseGet>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getUserData(String id, final RetroCallback callback) {
        apiService.getUserData(id).enqueue(new Callback<List<UserData>>() {
            @Override
            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserData>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }





    //POST방식으로 통신하는 방식
    public void postFirst(HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postFirst(parameters).enqueue(new Callback<ResponseGet>() {
            @Override
            public void onResponse(Call<ResponseGet> call, Response<ResponseGet> response) {
                Log.d("테스트", "onResponse:  is Success ");
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseGet> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


    public void putFirst(HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.putFirst(new RequestPut(parameters)).enqueue(new Callback<ResponseGet>() {
            @Override
            public void onResponse(Call<ResponseGet> call, Response<ResponseGet> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseGet> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void patchFirst(String title, final RetroCallback callback) {
        apiService.patchFirst(title).enqueue(new Callback<ResponseGet>() {
            @Override
            public void onResponse(Call<ResponseGet> call, Response<ResponseGet> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseGet> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void deleteFirst(final RetroCallback callback) {
        apiService.deleteFirst().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

/*----------------여기서부터 내가 추가한 메소드------------------------*/

    /*SignupActivity1 : 회원가입한 데이터 서버로 전송시켜 저장*/

    public void plususer(HashMap<String, Object> parameters, final RetroCallback callback){
        apiService.plususer(parameters).enqueue(new Callback<Void>() { //인터페이스로부터 함수 호출
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("새로운서버 회원가입", "onResponse: 실행 ");
                if(response.isSuccessful()){
                    //response가 성공한 상태 결과 코드가 200범위일때 참값을 반환 - (정상통신 = 200 ~300 / 에러 = 500)
                    callback.onSuccess(response.code(),response.body());
                    Log.d("새로운서버 회원가입", "onResponse:  is Success "+response.code());
                }else {
                    //서버와 통신에 성공하였지만, 서버 내부 동작 중에서 잘못된 점이 있을때
                    Log.d("새로운서버 회원가입", "onResponse:  is Fail ");
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t);
                Log.d("새로운서버 회원가입", "onFailure: "+t.toString());
                // Request가 실패한 상태 (통신자체, 서버의 구현 이외의 에러 발생)
                // 통신불가, 서버와 연결 실패 등
            }
        });
    }
//    public void plususer(HashMap<String, Object> parameters, final RetroCallback callback){
//        apiService.plususer(parameters).enqueue(new Callback<UserData>() { //인터페이스로부터 함수 호출
//            @Override
//            public void onResponse(Call<UserData> call, Response<UserData> response) {
//                Log.d("테스트", "onResponse: 실행 ");
//                if(response.isSuccessful()){
//                    //response가 성공한 상태 결과 코드가 200범위일때 참값을 반환 - (정상통신 = 200 ~300 / 에러 = 500)
//                    callback.onSuccess(response.code(),response.body());
//                    Log.d("테스트", "onResponse:  is Success "+response.code());
//                }else {
//                    //서버와 통신에 성공하였지만, 서버 내부 동작 중에서 잘못된 점이 있을때
//                    Log.d("테스트", "onResponse:  is Fail ");
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserData> call, Throwable t) {
//                callback.onError(t);
//                Log.d("테스트", "onFailure: "+t.toString());
//                // Request가 실패한 상태 (통신자체, 서버의 구현 이외의 에러 발생)
//                // 통신불가, 서버와 연결 실패 등
//            }
//        });
//    }


    /*LoginActivity : 로그인시 성공여부 체크*/
    public void retroLogin(HashMap<String, Object> logininfo, final RetroCallback callback){
        Log.d("레트로 로그인", "첫줄은실행됨 ");
        apiService.retroLogin(logininfo).enqueue(new Callback<Result_login>() {
            @Override
            public void onResponse(Call<Result_login> call, Response<Result_login> response) {
                Log.d("레트로 로그인", "로그인(retroLogin):  is Success ");

                if (response.isSuccessful()){
                    Log.d("레트로 로그인", "로그인(retroLogin):  is Success "+response.body());
                    callback.onSuccess(response.code(),response.body());
                    Log.d("레트로 로그인", "로그인(retroLogin):  is Success ");

                }else {
                    Log.d("레트로 로그인", "로그인(retroLogin):  is Fail ");
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Result_login> call, Throwable t) {

            }
        });


    }

/*<List>와 그냥의 차이*/
//    //    로그인시 데이터 보내기 <List<Result_login>>
//    public void retroLogin(HashMap<String, Object> logininfo, final RetroCallback callback){
//        Log.d("레트로 로그인", "첫줄은실행됨 ");
//        apiService.retroLogin(logininfo).enqueue(new Callback<List<Result_login>>() {
//            @Override
//            public void onResponse(Call<List<Result_login>> call, Response<List<Result_login>>response) {
//                Log.d("레트로 로그인", "로그인(retroLogin):  is Success ");
//
//                if (response.isSuccessful()){
//                    Log.d("레트로 로그인", "로그인(retroLogin):  is Success "+response.body());
//                    callback.onSuccess(response.code(),response.body());
//                    Log.d("레트로 로그인", "로그인(retroLogin):  is Success ");
//
//                }else {
//                    Log.d("레트로 로그인", "로그인(retroLogin):  is Fail ");
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Result_login>> call, Throwable t) {
//
//            }
//        });
//
//
//    }
    /*Signup2Activity : 키값 2개 서버로 보냄*/
    public void connect(HashMap<String, Object> couplecode, final RetroCallback callback){
        apiService.connect(couplecode).enqueue(new Callback<Result_login>() { //인터페이스로부터 함수 호출
            @Override
            public void onResponse(Call<Result_login> call, Response<Result_login> response) {
                Log.d("테스트", "onResponse: 실행 ");
                if(response.isSuccessful()){
                    //response가 성공한 상태 결과 코드가 200범위일때 참값을 반환 - (정상통신 = 200 ~300 / 에러 = 500)
                    callback.onSuccess(response.code(),response.body());
                    Log.d("테스트", "onResponse:  is Success "+response.code());
                }else {
                    //서버와 통신에 성공하였지만, 서버 내부 동작 중에서 잘못된 점이 있을때
                    Log.d("테스트", "onResponse:  is Fail ");
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Result_login> call, Throwable t) {
                callback.onError(t);
                Log.d("테스트", "onFailure: "+t.toString());
                // Request가 실패한 상태 (통신자체, 서버의 구현 이외의 에러 발생)
                // 통신불가, 서버와 연결 실패 등
            }
        });
    }

    /*Signup2Activity : refresh 눌렀을때*/
    public void refresh(HashMap<String, Object> refresh, final RetroCallback callback){
        apiService.refresh(refresh).enqueue(new Callback<Result_login>() { //인터페이스로부터 함수 호출
            @Override
            public void onResponse(Call<Result_login> call, Response<Result_login> response) {
                Log.d("테스트", "onResponse: 실행 ");
                if(response.isSuccessful()){
                    //response가 성공한 상태 결과 코드가 200범위일때 참값을 반환 - (정상통신 = 200 ~300 / 에러 = 500)
                    callback.onSuccess(response.code(),response.body());
                    Log.d("테스트", "onResponse:  is Success "+response.code());
                }else {
                    //서버와 통신에 성공하였지만, 서버 내부 동작 중에서 잘못된 점이 있을때
                    Log.d("테스트", "onResponse:  is Fail ");
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Result_login> call, Throwable t) {
                callback.onError(t);
                Log.d("테스트", "onFailure: "+t.toString());
                // Request가 실패한 상태 (통신자체, 서버의 구현 이외의 에러 발생)
                // 통신불가, 서버와 연결 실패 등
            }
        });
    }


    /*getUserData_main*/
//    public void getUserData_main(String email, final RetroCallback callback){
//        apiService.getUserData_main(email).enqueue(new Callback<List<UserData>>() {
//            @Override
//            public void onResponse(Call<List<UserData>> call, Response<List<UserData>>response) {
//                if (response.isSuccessful()){
//                    Log.d("레트로 유저데이터", "getUserData_main:  is Success "+response.body());
//                    callback.onSuccess(response.code(),response.body());
//                    Log.d("레트로 유저데이터", "getUserData_main:  is Success ");
//
//                }else {
//                    Log.d("레트로 유저데이터", "getUserData_main:  is Fail ");
//                    callback.onFailure(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<UserData>> call, Throwable t) {
//
//            }
//        });
        public void getUserData_main(String email, final RetroCallback callback){
            apiService.getUserData_main(email).enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData>response) {
                    if (response.isSuccessful()){
                        Log.d("레트로 유저데이터", "getUserData_main:  is Success "+response.body());
                        callback.onSuccess(response.code(),response.body());
                        Log.d("레트로 유저데이터", "getUserData_main:  is Success ");

                    }else {
                        Log.d("레트로 유저데이터", "getUserData_main:  is Fail ");
                        callback.onFailure(response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {

                }
            });


    }

    //사귄날 저장
    public void addcoupledate(String dday ,String couplekey, final RetroCallback callback){
        apiService.addcoupledate(dday,couplekey).enqueue(new Callback<Result_login>() {
            @Override
            public void onResponse(Call<Result_login> call, Response<Result_login>response) {
                if (response.isSuccessful()){
                    Log.d("레트로 디데이저장", "getUserData_main:  is Success "+response.body());
                    callback.onSuccess(response.code(),response.body());
                    Log.d("레트로 디데이저장", "getUserData_main:  is Success "+response.code());

                }else {
                    Log.d("레트로 디데이저장", "getUserData_main:  is Fail ");
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Result_login> call, Throwable t) {
                Log.d("레트로 디데이저장", "onFailure: "+t.toString());
            }
        });


    }


    /*사귄날 불러오기*/
    public void getcoupledate(String couplekey, final RetroCallback callback){
        apiService.getcoupledate(couplekey).enqueue(new Callback<Result_login>() {
            @Override
            public void onResponse(Call<Result_login> call, Response<Result_login>response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Result_login> call, Throwable t) {
                Log.d("레트로 디데이저장", "onFailure: "+t.toString());
            }
        });


    }




    /*수요일 이미지 첫시도-  -https://philip1994.tistory.com/15 */
    public void uploadprofile(MultipartBody.Part File, RequestBody email, RequestBody couplekey, final RetroCallback callback){
        apiService.uploadprofile(File,email,couplekey).enqueue(new Callback<Result_login>() {
            @Override
            public void onResponse(Call<Result_login> call, Response<Result_login> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Result_login> call, Throwable t) {
                //여기로 옴
                Log.d("레트로 사진업로드", "onFailure: "+t.toString());
            }
        });
    }


    /*나와 상대방 프로필 이미지 불러오기*/
    public void getprofiles(String email, String couplekey ,final RetroCallback callback){
        apiService.getprofiles(email,couplekey).enqueue(new Callback<ThreeStringData>() {
            @Override
            public void onResponse(Call<ThreeStringData> call, Response<ThreeStringData>response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ThreeStringData> call, Throwable t) {
                Log.d("레트로 getprofiles", "onFailure: "+t.toString());
            }
        });


    }



    /*사진 여러장 저장하기 ->WitestoryActivity*/
    public void storyupload(ArrayList<MultipartBody.Part> filelist,
                            RequestBody writer,RequestBody couplekey, RequestBody myimg, RequestBody otherimg, RequestBody story, RequestBody date, RequestBody form, RequestBody count,
                            final RetroCallback callback){
        apiService.storyupload(filelist,writer,couplekey,myimg,otherimg,story,date,form,count).enqueue(new Callback<List<Result_login>>() {
            @Override
            public void onResponse(Call<List<Result_login>> call, Response<List<Result_login>> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Result_login>> call, Throwable t) {
                Log.d("레트로 storyupload", "onFailure = 통신실패: "+t.toString());
            }
        });
    }


    /*게시글 수정 , 사진 여러장 -> EditStroyActivvitiy*/
    /*사진 여러장 저장하기 ->WitestoryActivity*/
    public void editstoryupload(ArrayList<MultipartBody.Part> filelist,
                             RequestBody idx, RequestBody story, RequestBody date,
                            final RetroCallback callback){
        apiService.editstoryupload(filelist,idx,story,date).enqueue(new Callback<Result_login>() {
            @Override
            public void onResponse(Call<Result_login> call, Response<Result_login> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Result_login> call, Throwable t) {
                Log.d("레트로 storyupload", "onFailure = 통신실패: "+t.toString());
            }
        });
    }







    /*유저데이터 다 불러오기 =>프로필수정액티비티*/
    public void getuserprofiles(String email,final RetroCallback callback){
        apiService.getuserprofiles(email).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.d("레트로 getuserprofiles", "onFailure = 통신실패: "+t.toString());
            }
        });{

        }
    }


    /*변경할 유저 데이터 저장 => 프로필수정 액티비티*/
    public void editprofile(HashMap edituserdata,final RetroCallback callback){
        apiService.editprofile(edituserdata).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("레트로 editprofile", "onFailure = 통신실패: "+t.toString());
            }
        });
    }


    /*커플 네임 가져오기 (은찬 ♥ 민선) 같은거*/
    public void getcouplename(String couplekey,final RetroCallback callback){
        apiService.getcouplename(couplekey).enqueue(new Callback<Result_login>() {
            @Override
            public void onResponse(Call<Result_login> call, Response<Result_login> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Result_login> call, Throwable t) {
                Log.d("레트로 getcouplename", "onFailure = 통신실패: "+t.toString());
            }
        });
    }




    /*메인화면 리사이클러뷰 1 - 전체 보기 */
    public void mainStory_All(String form, String couplekey,final RetroCallback callback){
            apiService.mainStory_All(form,couplekey).enqueue(new Callback<List<StoryData>>() {
                @Override
                public void onResponse(Call<List<StoryData>> call, Response<List<StoryData>> response) {
                    if (response.isSuccessful()){
                        callback.onSuccess(response.code(),response.body());

                    }else {
                        callback.onFailure(response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<StoryData>> call, Throwable t) {

                }
            });
    }



    /*메인화면 리사이클러뷰 1 - idx값을 사용해 DB에서 삭제하기*/
    public void mainStory_remove(int idx, final RetroCallback callback){
        apiService.mainStory_remove(idx).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("레트로 mainStory_remove / ", "onFailure: "+t.toString());
            }
        });

    }



    /*댓글액티비티 - 로그인한 사람의 이름과 유저 정보 가져오기
    * 애플리케이션클래스에서 가져와서 쭉 쓰면 어떨까? */
    public void getnameprofile(String email,final RetroCallback callback){
        apiService.getnameprofile(email).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.d("레트로 getNameProfile / ", "onFailure: "+t.toString());
            }
        });
    }





    /*댓글 저장하기*/
    public void commentsupload(HashMap comments,final RetroCallback callback){
        apiService.commentsupload(comments).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("레트로 commentsupload", "onFailure = 통신실패: "+t.toString());
            }
        });
    }




    /*댓글 리사이클러뷰 불러오기 */
    public void getcomment(int storyidx,final RetroCallback callback){
        apiService.getcomment(storyidx).enqueue(new Callback<List<CommentData>>() {
            @Override
            public void onResponse(Call<List<CommentData>> call, Response<List<CommentData>> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CommentData>> call, Throwable t) {

            }
        });
    }



    /*게시글 좋아요 +1*/
    public void story_addheart(int storyidx,String couplekey,final RetroCallback callback){
        apiService.story_addheart(storyidx,couplekey).enqueue(new Callback<ThreeStringData>() {
            @Override
            public void onResponse(Call<ThreeStringData> call, Response<ThreeStringData> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ThreeStringData> call, Throwable t) {
                Log.d("story_addheart", "onFailure: "+t.toString());
            }
        });
    }

    /*게시글 좋아요 취소-1*/
    public void story_removeheart(int storyidx,String couplekey,final RetroCallback callback){
        apiService.story_removeheart(storyidx,couplekey).enqueue(new Callback<ThreeStringData>() {
            @Override
            public void onResponse(Call<ThreeStringData> call, Response<ThreeStringData> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ThreeStringData> call, Throwable t) {
                Log.d("story_addheart", "onFailure: "+t.toString());
            }
        });
    }



    /*댓글 삭제하기*/
    public void commentremove(int idx, int storyidx, final RetroCallback callback){
        apiService.commentremove(idx,storyidx).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("레트로 commentremove / ", "onFailure: "+t.toString());
            }
        });

    }


    /*댓글 수정하기*/
    public void commentedit(int idx, String memo, String date, final RetroCallback callback){
        apiService.commentedit(idx,memo,date).enqueue(new Callback<Result_login>() {
            @Override
            public void onResponse(Call<Result_login> call, Response<Result_login> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.code(),response.body());
                }else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<Result_login> call, Throwable t) {
                Log.d("레트로commentedit / ", "onFailure: "+t.toString());
            }
        });
    }








    /*안쓰는거같음*/
    public void logincheck(String id, final RetroCallback callback) {
        apiService.logincheck(id).enqueue(new Callback<List<UserData>>() {
            @Override
            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    Log.d("클래스", "로그인(retroLogin):  is Fail ");
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserData>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }



}
