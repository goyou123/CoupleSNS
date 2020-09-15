package com.example.couplesns.RetrofitJava;

import com.example.couplesns.DataClass.CommentData;
import com.example.couplesns.DataClass.ImgData_ex;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RequestBody.RequestPut;
import com.example.couplesns.ResponseBody.ResponseGet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
//  출처: https://kor45cw.tistory.com/5 [Developer's]
public interface RetroBaseApiService {

    /*이 인터페이스의 모든 메소드에는 Request메소드와 상대 URL을 지정하는 Http 어노테이션이 있어야 한다.
    * 동기적으로 http를 호출하기 위해서 인터페이스 구현*/


    //통신할 url 주소 (https:// 필수, )
    final String Base_URL = "http://13.125.182.117/"; //AWS 두번쨰 서버

    // final String Base_URL = "http://3.34.137.189/"; //AWS 첫번쨰 서버
    // final String Base_URL = "http://192.168.30.130/"; // 이전 서버
    // final String Base_URL = "https://jsonplaceholder.typicode.com";


    //GET방식의 통신 , https://jsonplaceholder.typicode.com/ posts/{iserID}의 주소를 호출
    //ResponseGet형식으로 된 JSON을 통신을 통해 받는다.
    // ex) id 값이 test면 https://jsonplaceholder.typicode.com/ posts/test 가 통신의 최종적인 주소가 된다.
    @GET("example.php/{userId}")
//    @GET("/posts/{userId}")
    Call<ResponseGet> getFirst(@Path("userId") String id);
//    Call<ResponseGet> getFirst(@Query("userId") String id);
    //GET방식, ResponseGet 형식으로 된 JSON을 여러개 받는다.
    //https://jsonplaceholder.typicode.com/ posts?userID = test


    // @Query Annotation은 GET방식에서만 사용가능합니다.
    @GET("example.php")
    Call<List<ResponseGet>> getSecond(@Query("userId") String id);

    @GET("example1.php")
    Call<List<UserData>> getUserData(@Query("name") String id);






    //POST방식 . 이 방식은 Get에서 사용불가
    //http://jsonplaceholder.typicode.com/posts
    //FieldMap = 필드형식을 통해 넘겨주는 값이 여러개일때 사용한다.
    @FormUrlEncoded // Field 형식을 사용할떄는 Form 이 인코딩 되어야 한다.
    @POST("/posts")
    Call<ResponseGet> postFirst(@FieldMap HashMap<String, Object> parameters);

    @PUT("/posts/1")
    Call<ResponseGet> putFirst(@Body RequestPut parameters);

    @FormUrlEncoded
    @PATCH("/posts/1")
    Call<ResponseGet> patchFirst(@Field("title") String title);

    @DELETE("/posts/1")
    Call<ResponseBody> deleteFirst();




    /*내가 추가한 레트로핏*/
    // 회원가입한 데이터 전송하는 메소드
    @FormUrlEncoded
    @POST("signup.php") //내 서버주소에 + 되는 주소  , 이 페이지로 요청
    Call<Void> plususer( //파라미터 형식의 데이터 , 원래 <Userdata>
            @FieldMap HashMap<String, Object> parameters);


    //로그인 시 내가 입력한 값을 보내는 요청 메소드
//    @FormUrlEncoded
//    @POST("login.php")
//    /*Call <받는 데이터 클래스>*/
//    Call<List<Result_login>> retroLogin( //로그인때 입력하는 한 유저의 값만 보내면 되므로 list X
//                                   @FieldMap HashMap<String, Object> logininfo);

    //로그인 시 내가 입력한 값을 보내고 true값을 가져와서 로그인을 판별함
    @FormUrlEncoded
    @POST("login.php")
    /*Call <response받는 데이터 클래스>*/
    Call<Result_login> retroLogin( //로그인때 입력하는 한 유저의 값만 보내면 되므로 list X
                                         @FieldMap HashMap<String, Object> logininfo);

    //로그인 성공여부 검사??
    @GET("login.php")
    Call<List<UserData>> logincheck(@Query("userId") String id);


    //Signup2에서 코드값 2개를 php로 보내고 DB에서 나와 상대방커플키값을 추가하고 커플 테이블 생성
    @FormUrlEncoded
    @POST("connect.php")
        /*Call <response받는 데이터 클래스>*/
    Call<Result_login> connect( //로그인때 입력하는 한 유저의 값만 보내면 되므로 list X
                                   @FieldMap HashMap<String, Object> couplecode);


    //Signup2에서 refresh버튼 눌렀을때 커플키값을 비교해서 값 추출
    @FormUrlEncoded
    @POST("refresh.php")
    /*Call <response받는 데이터 클래스>*/
    Call<Result_login> refresh(@FieldMap HashMap<String, Object> refresh);


    //그냥 이름으로 할지 고민
//    @GET("getUserData_main.php")
//    Call<List<UserData>> getUserData_main(@Query("getemail") String email);
    @GET("getUserData_main.php")
    Call<UserData> getUserData_main(@Query("getemail") String email);


    //main_사귄날 선택한 값 저장하기
    @GET("addcoupledate.php")
    Call<Result_login> addcoupledate(@Query("dday") String dday,@Query("couplekey") String couplekey);
//
    //main_사귄날 선택한 값 불러오기
    @GET("getcoupledate.php")
    Call<Result_login> getcoupledate(@Query("couplekey") String couplekey);



    //이미지 업로드 , 이미지 경로 , 이메일값 => 프로필 액티비티
    @Multipart
    @POST("upload.php")
    Call<Result_login> uploadprofile (@Part MultipartBody.Part File ,@Part("email") RequestBody email,@Part("couplekey") RequestBody couplekey);


    // 나와 상대방 프로필 이미지 가져오기
    @GET("getprofiles.php")
    Call<ThreeStringData> getprofiles (@Query("email") String email, @Query("couplekey") String couplekey);


    //내 유저 데이터 가져오기 - 프로필 수정 액티비티
    @GET("getuserprofiles.php")
    Call<UserData> getuserprofiles(@Query("email") String email);



    //프로필 수정 정보 저장하기
    @FormUrlEncoded
    @POST("editprofile.php")
    /*Call <response받는 데이터 클래스>*/
    Call<Result_login> editprofile(@FieldMap HashMap<String, Object> edituserdata);



    //스토리 내용 및 사진들 업로드
    @Multipart
    @POST("storyupload.php")
    Call<List<Result_login>> storyupload (@Part ArrayList<MultipartBody.Part> filelist,
//            @Part MultipartBody.Part File,
                                    @Part("writer") RequestBody writer,
                                      @Part("couplekey") RequestBody couplekey,
                                      @Part("myimg") RequestBody myimg,
                                      @Part("otherimg") RequestBody otherimg,
                                    @Part("story") RequestBody story,
                                    @Part("date") RequestBody date,
                                    @Part("form") RequestBody form,
                                    @Part("count") RequestBody count
                                    );



    //스토리 내용 수정 및 사진들 업로드
    @Multipart
    @POST("edit_storyupload.php")
    Call<Result_login> editstoryupload (@Part ArrayList<MultipartBody.Part> filelist,
//            @Part MultipartBody.Part File,
                                          @Part("idx") RequestBody idx,
                                          @Part("story") RequestBody story,
                                          @Part("date") RequestBody date
    );




    //writeStory_couplename가져오기(은찬 ♥ 민선)
    @GET("getcouplename.php")
    Call<Result_login> getcouplename(@Query("couplekey") String couplekey);


    /*메인화면 탭메뉴 -- 탭에 따라 다른 리사이클러뷰*/
    //메인화면 전체보기 리사이클러뷰1
    @FormUrlEncoded
    @POST("mainStory_All.php")
    Call<List<StoryData>> mainStory_All(@Field("form") String form,@Field("couplekey") String couplekey);


    //메인화면 일반 글만 모아 보기 리사이클러뷰2
    @FormUrlEncoded
    @POST("mainStory_normal.php")
    Call<List<StoryData>> mainStory_normal(@Field("form") String form,@Field("couplekey") String couplekey);

    //메인화면 익명 글만 모아 보기 리사이클러뷰3
    @FormUrlEncoded
    @POST("mainStory_secret.php")
    Call<List<StoryData>> mainStory_secret(@Field("form") String form,@Field("couplekey") String couplekey);


    /*------------------------------------------*/


    //글 삭제 - 메인리사이클러뷰1,전체보기
    @FormUrlEncoded
    @POST("mainStory_remove.php")
    Call<Void> mainStory_remove(@Field("idx") int idx);


    //이름과 프로필 가져오기 - 댓글액티비티
    @GET("getNameProfile.php")
    Call<UserData> getnameprofile(@Query("email") String email);



    //댓글 저장하기 - 댓글 액티비티
    @FormUrlEncoded
    @POST("commentUpload.php")
    /*Call <response받는 데이터 클래스>*/
    Call<Result_login> commentsupload(@FieldMap HashMap<String, Object> comments);



    //댓글 보기 리사이클러뷰
    @FormUrlEncoded
    @POST("getComment.php")
    Call<List<CommentData>> getcomment(@Field("storyidx") int storyidx);


    //게시글 좋아요 누르기 (+1)
    @FormUrlEncoded
    @POST("storyAddheart.php")
    Call<ThreeStringData> story_addheart(@Field("storyidx") int storyidx, @Field("couplekey") String couplekey);


    //게시글 좋아요 취소 (-1)
    @FormUrlEncoded
    @POST("storyRemoveheart.php")
    Call<ThreeStringData> story_removeheart(@Field("storyidx") int storyidx, @Field("couplekey") String couplekey);


    //댓글 삭제 - 댓글 액티비티
    @FormUrlEncoded
    @POST("commentRemove.php")
    Call<Void> commentremove(@Field("idx") int idx, @Field("storyidx") int storyidx);


    //댓글 수정 - 댓글 액티비티
    @FormUrlEncoded
    @POST("commentEdit.php")
    Call<Result_login> commentedit(@Field("idx") int idx, @Field("memo") String memo,@Field("date") String date);


    //익명 게시글 업로드
    @FormUrlEncoded
    @POST("secretStoryUpload.php")
        /*Call <response받는 데이터 클래스>*/
    Call<Result_login> secretstoryupload( //로그인때 입력하는 한 유저의 값만 보내면 되므로 list X
                                @FieldMap HashMap<String, Object> secretstory);



    //익명댓글 저장하기 - 익명댓글 액티비티
    @FormUrlEncoded
    @POST("secretCommentUpload.php")
    /*Call <response받는 데이터 클래스>*/
    Call<Result_login> secretcommentsupload(@FieldMap HashMap<String, Object> secretcomments);


}
