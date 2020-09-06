package com.example.couplesns;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppCompatActivity {

    final static int REQUEST_IMAGE_CODE = 1000;
    final static String TAG = "프로필 수정 액티비티";

    ImageView Imageview_Edit_back,Imageview_Editprofile_Img,Imageview_Edit_Ok;
    TextView Textview_Editprofile_ChangeImg;
    EditText Edittext_EditProfile_Name,Edittext_EditProfile_Phone,Edittext_EditProfile_Email,Edittext_EditProfile_Sex;
    ApplicationClass applicationClass;

    String myEmail,coupleKey;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //applicationClass
        applicationClass = (ApplicationClass) getApplicationContext();

        //xml 연결
        Imageview_Edit_back =(ImageView) findViewById(R.id.Imageview_Edit_back); // 뒤로가기
        Imageview_Editprofile_Img =(ImageView) findViewById(R.id.Imageview_Editprofile_Img); // 이미지 보여주기
        Imageview_Edit_Ok =(ImageView) findViewById(R.id.Imageview_Edit_Ok); // 변경완료 버튼
        Textview_Editprofile_ChangeImg =(TextView) findViewById(R.id.Textview_Editprofile_ChangeImg);//갤러리로 이동 버튼
        Edittext_EditProfile_Name =(EditText) findViewById(R.id.Edittext_EditProfile_Name); // 이름
        Edittext_EditProfile_Phone =(EditText) findViewById(R.id.Edittext_EditProfile_Phone); // 폰번호
        Edittext_EditProfile_Email =(EditText) findViewById(R.id.Edittext_EditProfile_Email); // 이메일
        Edittext_EditProfile_Sex =(EditText) findViewById(R.id.Edittext_EditProfile_Sex); //성별 - 이건 따로 바꿔야댐

        /*쉐어드로 내 이메일, 커플키값 불러오기*/
        SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
        myEmail = sharedPreferences1.getString("auto_login","no_autologin_key");
        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: autoLoginKey::"+myEmail);

        SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
        coupleKey = sharedPreferences.getString(myEmail,"no_key_login");
        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: coupleKey::"+coupleKey);

        /*프로필 사진 받아오기 - 위치주의! 쉐어드 키값들 밑에 위치*/
        getmyprofileImage();



        /*프로필 정보 수정하기*/
        Imageview_Edit_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(applicationClass, "변경 클릭", Toast.LENGTH_SHORT).show();
                editprofileok();
            }
        });


        /*프로필 사진 변경하기 - 갤러리 오픈*/
        Textview_Editprofile_ChangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //좀더공부 - 캡처
                startActivityForResult(intent, REQUEST_IMAGE_CODE);
            }
        });

    }//OnCreate


    /*프로필 이미지 변경*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //코드가 일치할때 , +예외처리
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && data != null) {

            Uri imageuri = data.getData();
//                Glide.with(this).load(imageuri).into(Imageview_Coupleprofile_Myprofile);
            String maybeimg = getPath(imageuri); // 갤러리에서 선택한 이미지 uri은 정상경로가 아닌데 그걸 바꿔줌
            Glide.with(this).load(data.getData()).into(Imageview_Editprofile_Img);
            uploadFile(maybeimg);

        } //if
    }//OnActivityResult()

    @Override
    protected void onResume() {
        super.onResume();

        /*내 프로필 정보 받아오기*/
        getmyprofiles();
    }

    /*이미지 서버에 업로드 - 프로필 사진 한장*/
    private void uploadFile(String maybeimg) {

        //이미지 파일
        File file = new File(maybeimg); //파일 이름만 만든거다. // 아님
        Log.d(TAG, "uploadFile: " + file);

                                                                    //contentType()
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file); // uri파일을 리퀘스트바디에 담을 수 있는 multipart로 파싱
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);
        RequestBody requestEmail = RequestBody.create(MediaType.parse("text/plain"), myEmail);
        RequestBody requestCouplekey = RequestBody.create(MediaType.parse("text/plain"), coupleKey);

        Log.d(TAG, "uploadFile: 이게 뭘까  " + file.getName());
        Log.d(TAG, "uploadFile: 이게 뭘까2  " + requestFile.contentType());
        Log.d(TAG, "uploadFile: 이게 뭘까3  " + body.body());
        Log.d(TAG, "uploadFile: 이게 뭘까4  " + myEmail);

        //서버로 사진과 이메일 전송
        applicationClass.retroClient.uploadprofile(body, requestEmail,requestCouplekey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: " + t.toString());
                Toast.makeText(applicationClass, "onError: " + t.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Result_login data = (Result_login) receivedData;
                String serverResult = data.getServerResult();
                String imgresult = data.getCouplekey();
                Log.d(TAG, "onSuccess: 레트로핏 이미지 " + code + "/" + imgresult);
                Log.d(TAG, "onSuccess: 사진 결과" + serverResult);
//                Toast.makeText(applicationClass, "onSuccess: "+code+"/"+imgresult, Toast.LENGTH_LONG).show();
                Toast.makeText(applicationClass, "프로필이 변경되었습니다. ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: " + code);
                Toast.makeText(applicationClass, "onFailure: " + code, Toast.LENGTH_SHORT).show();
            }
        });
    }//uploadFile()






    /* 갤러리에서 인텐트로 받은 이미지의 주소(uri)는 한번에 안받아지므로 따로 정의해주는 매쏘드 => 절대경로
     * https://nittaku.tistory.com/24*/
    public String getPath(Uri uri){

        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    } //getPath()



    public void getmyprofileImage(){
        /*나와 상대방 이미지 불러오기*/
        applicationClass.retroClient.getprofiles(myEmail, coupleKey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                //커플프로필 코드 재활용, 상대이미지는 필요없음
                Log.d(TAG, "onSuccess: "+code+receivedData);
                ThreeStringData data = (ThreeStringData)receivedData;
                String myProfile = data.getFirst();
                String otherProfile = data.getSecond();
                Log.d(TAG, "onSuccess: 내 프로필 사진"+myProfile);
                Log.d(TAG, "onSuccess: 상대 프로필 사진"+otherProfile);
                if (myProfile!=null){
                    Glide.with(getApplicationContext()).load(applicationClass.serverImageRoot+myProfile).into(Imageview_Editprofile_Img); //글라이드와 생명주기 고려
                }else{
                    Glide.with(getApplicationContext()).load(applicationClass.defaultProfile).into(Imageview_Editprofile_Img);
                }

            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });

    }



    /*내 프로필 정보 가져오기*/
    public void getmyprofiles(){
        applicationClass.retroClient.getuserprofiles(myEmail, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "getmyprofiles onError: " + t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "getmyprofiles onSuccess: " + code);
                //서버에서 데이터 받아오기
                UserData data = (UserData)receivedData;
                String responseName = data.getName();
                String responsePhone= data.getPhone();
                String responseEmail = data.getEmail();
                String responseSex = data.getSex();

                //화면에 적용하기
                Edittext_EditProfile_Name.setText(responseName);
                Edittext_EditProfile_Phone.setText(responsePhone);
                Edittext_EditProfile_Email.setText(responseEmail);
                if(responseSex.equals("W")){
                    Edittext_EditProfile_Sex.setText("여자");
                }else {
                    Edittext_EditProfile_Sex.setText("남자");
                }



            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "getmyprofiles onFailure: " + code);
            }
        });
    }// getmyprofiles()



    /*프로필 정보 수정하기*/
    public void editprofileok(){
        String editName =Edittext_EditProfile_Name.getText().toString();
        String editEmail =Edittext_EditProfile_Email.getText().toString();
        String editPhone =Edittext_EditProfile_Phone.getText().toString();
//        String editSex =Edittext_EditProfile_Name.getText().toString();
        HashMap<String, Object> editdata = new HashMap<>();
        editdata.put("email",myEmail);//이메일은 고유값처럼 shared로 쓰기 때문에 변경X , php에서 구분용으로사용
        editdata.put("editName",editName);
        editdata.put("editPhone",editPhone);
        editdata.put("couplekey",coupleKey);
        Log.d(TAG, "editprofileok: 서버로 보내는 커플키값(프로필변경)"+coupleKey);
        applicationClass.retroClient.editprofile(editdata, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d("레트로핏", "editprofileok onError: ."+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d("레트로핏", "editprofileok onSuccess: ."+code);
                Result_login data = (Result_login)receivedData;
                String serverResult = data.getServerResult();
                if(serverResult.equals("true")){
                    Toast.makeText(applicationClass, "프로필 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(applicationClass, "수정오류", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            @Override
            public void onFailure(int code) {
                Log.d("레트로핏", "editprofileok onFailure: ."+code);
            }
        });
    }//editprofileok()


}//END