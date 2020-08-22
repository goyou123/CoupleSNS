package com.example.couplesns;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.telecom.Call;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.couplesns.DataClass.ImgData_ex;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.DataClass.UserData;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static okhttp3.MediaType.parse;

public class CoupleprofileActivity extends AppCompatActivity {
    final static String TAG = "커플프로필액티비티";
    final static int REQUEST_IMAGE_CODE = 1000;
    ImageView Imageview_Coupleprofile_Sound,Imageview_Coupleprofile_Setting,Imageview_Coupleprofile_Myprofile,
            Imageview_Coupleprofile_Anotherprofile;
    TextView Textview_Coupleprofile_Date,Textview_Coupleprofile_Myname,Textview_Coupleprofile_Anothername;
    Button Button_Coupleprofile_Follow,Button_Coupleprofile_Star,Button_Coupleprofile_Gallery,Button_Coupleprofile_Doubledate;

    ApplicationClass applicationClass;
    String coupleKey,myEmail;

    //달력관련 변수
    Calendar mCalendar;
    final int ONE_DAY = 24*60*60*1000; //Millisecond형태의 하루(24시간)
    String D_DAY;
    String pickDday;

    ProgressDialog progressDialog;
    //이미지,프로그래스



    /*데이트피커 다이얼로그*/
    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            int month1 = month+1;
            pickDday = String.format(year+"년 "+month1+"월 "+dayOfMonth+"일"); //픽하는 날짜
//            tv_Dday_Showdate.setText("우리가 사귀기 시작한 날 : " + pickDday);
            Log.d("데이트피커", "PickDdaty: "+pickDday);
            Log.d("데이트피커", "텍스트뷰에 붙는 내가 선택한 날짜: "+year+"--"+month1+"--"+dayOfMonth);

            //오늘날짜 - 선택한 날짜의 차이값 = 현재까지 사귄 날짜 (밑에서 계산)
            D_DAY = getDday(year,month,dayOfMonth); // 디 데이 출력 값
            Log.d("디데이계산", "계산값: "+D_DAY);
            Textview_Coupleprofile_Date.setText(D_DAY); // 맨위 텍스트에 붙힘

            /*먼저 이메일값을 받아온 뒤 그값으로 커플키값을 찾는다.*/
            //이메일값 먼저 가져오기
            SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
            String autoLoginKey = sharedPreferences1.getString("auto_login","no_autologin_key");
            Log.d(TAG, "onDateSet: 오토로그인키()이메일"+autoLoginKey);

            //이메일값으로 그 이메일에 해당하는 커플키 가져오기 - shared저장 signup2
            SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
            String sharedkey = sharedPreferences.getString(autoLoginKey,"no_key_login");
            Log.d(TAG, "onDateSet:이메일값에 해당하는 커플키"+sharedkey);

            ///ㅇㅋ

//            /*applicationClass에서 커플키값, 이메일값 쉐어드로 불러오기*/
//            String coupleKey1 = applicationClass.sharedcouplekey; //커플키값
//            Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: sharedcouplekey ::"+coupleKey1);




            /*선택한 Dday를 DB에 저장coupleTable - coupledate컬럼) > 필요한것 : 커플키값 */
            applicationClass.retroClient.addcoupledate(D_DAY,sharedkey, new RetroCallback() {
                @Override
                public void onError(Throwable t) {
                    Log.d(TAG, "onError: "+t.toString());
                }

                @Override
                public void onSuccess(int code, Object receivedData) {
//                    Log.d("받는거실행?", "onSuccess: "+code);
                    Result_login data = (Result_login)receivedData;
                    String serverResult = data.getServerResult();
//                    Log.d(TAG, "onSuccess: "+serverResult+"/"+code);
                    if(serverResult.equals("true")){
                        Toast.makeText(CoupleprofileActivity.this, "DDAY저장완료", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int code) {
                    Log.d(TAG, "onFailure: "+code);
                }
            });

        }
    };










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupleprofile);

        //applicationClass
        applicationClass = (ApplicationClass) getApplicationContext();

        //xml연결
//        Imageview_Coupleprofile_Sound = (ImageView) findViewById(R.id.Imageview_Coupleprofile_Sound); // 음성
//        Imageview_Coupleprofile_Setting = (ImageView) findViewById(R.id.Imageview_Coupleprofile_Setting); // 세팅
        Imageview_Coupleprofile_Myprofile = (ImageView) findViewById(R.id.Imageview_Coupleprofile_Myprofile); // 내 이미지
        Imageview_Coupleprofile_Anotherprofile = (ImageView) findViewById(R.id.Imageview_Coupleprofile_Anotherprofile); // 상대이미지
        Textview_Coupleprofile_Date = (TextView) findViewById(R.id.Textview_Coupleprofile_Date); // 사귄날
        Textview_Coupleprofile_Myname = (TextView) findViewById(R.id.Textview_Coupleprofile_Myname); // 내 이름
        Textview_Coupleprofile_Anothername = (TextView) findViewById(R.id.Textview_Coupleprofile_Anothername); //상대이름
        Button_Coupleprofile_Follow = (Button) findViewById(R.id.Button_Coupleprofile_Follow); // 팔로우 버튼
        Button_Coupleprofile_Star = (Button) findViewById(R.id.Button_Coupleprofile_Star); // 스크랩 버튼
        Button_Coupleprofile_Gallery = (Button) findViewById(R.id.Button_Coupleprofile_Gallery); // 갤러리 버튼
        Button_Coupleprofile_Doubledate = (Button) findViewById(R.id.Button_Coupleprofile_Doubledate); //받은 더블데이트 버튼


        //프로그래스바
        progressDialog = new ProgressDialog(CoupleprofileActivity.this);
        progressDialog.setMessage("이미지 업로드..");

        /*applicationClass에서 커플키값, 이메일값 쉐어드로 불러오기*/
//        coupleKey = applicationClass.sharedcouplekey; //커플키값
//        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: sharedcouplekey ::"+coupleKey);
////        myEmail = applicationClass.autoLoginKey; // 이메일값
//        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: autoLoginKey::"+myEmail);


        SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
        myEmail = sharedPreferences1.getString("auto_login","no_autologin_key");
        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: autoLoginKey::"+myEmail);

        SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
        coupleKey = sharedPreferences.getString(myEmail,"no_key_login");
        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: coupleKey::"+coupleKey);

        //내 이름과 상대이름 불러오기
        getuserinfo();


        //데이트피커
        /*사귄날 설정 - Datepicker*/
        Textview_Coupleprofile_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale.setDefault(Locale.KOREAN); // 한국어 설정
                mCalendar = new GregorianCalendar(); // 현재 날짜를 알기 위해 사용
                int year = mCalendar.get(Calendar.YEAR);
                final int month = mCalendar.get(Calendar.MONTH);
                int day = mCalendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CoupleprofileActivity.this,onDateSetListener, year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); //오늘 날짜까지만 선택 가능하도록 제한
                datePickerDialog.show();
                Log.d("디데이데이트피커", "datepicker: "+year+"/"+month+"/"+day);
            }
        });




        /*프로필 사진 변경*/
        Imageview_Coupleprofile_Myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //갤러리 열기
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //좀더공부 - 캡처
                startActivityForResult(intent, REQUEST_IMAGE_CODE);
            }
        });


        /*나와 상대방 이미지 불러오기*/
        applicationClass.retroClient.getprofiles(myEmail, coupleKey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code+receivedData);
                ThreeStringData data = (ThreeStringData)receivedData;
                String myProfile = data.getFirst();
                String otherProfile = data.getSecond();
                Log.d(TAG, "onSuccess: 내 프로필 사진"+myProfile);
                Log.d(TAG, "onSuccess: 상대 프로필 사진"+otherProfile);
                if (myProfile!=null){
                    Glide.with(getApplicationContext()).load("http://192.168.30.130/img/"+myProfile).into(Imageview_Coupleprofile_Myprofile); //글라이드 오류
                }else{
                    Glide.with(getApplicationContext()).load(applicationClass.defaultProfile).into(Imageview_Coupleprofile_Myprofile);
                }

                if (otherProfile!=null){
                    Glide.with(getApplicationContext()).load("http://192.168.30.130/img/"+otherProfile).into(Imageview_Coupleprofile_Anotherprofile);
                }else{
                    Glide.with(getApplicationContext()).load(applicationClass.defaultProfile).into(Imageview_Coupleprofile_Anotherprofile);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });


    }//OnCreate


    /*프로필 이미지 변경*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //코드가 일치할때 , +예외처리
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && data != null) {
//            try {
//
//                Uri imageuri = data.getData();
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageuri);
//                Imageview_Coupleprofile_Myprofile.setImageBitmap(bitmap);
//                progressDialog.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


//            try {
                Uri imageuri = data.getData();
//                Glide.with(this).load(imageuri).into(Imageview_Coupleprofile_Myprofile);
                String maybeimg = getPath(imageuri); // 갤러리에서 선택한 이미지 uri은 정상경로가 아닌데 그걸 바꿔줌
                Glide.with(this).load(data.getData()).into(Imageview_Coupleprofile_Myprofile);
                uploadFile(maybeimg);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } //if
    }//OnActivityResult()




    /*수요일*/
    private void uploadFile(String maybeimg){
        //경로
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"uploaded_file");
//        storageDir.mkdirs(); // 폴더생성
//        Log.d(TAG, "uploadFile: 디렉토리생성" +storageDir.mkdirs());
        //이미지 파일
//        File file = new File(storageDir,uri+".jpg");
        File file = new File(maybeimg); //파일 이름만 만든거다. // 아님
//        inputstream과정을 해줘야 한다 - 필요없음
        Log.d(TAG, "uploadFile: "+file);

                                                                    //contentType()
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file); // uri파일을 리퀘스트바디에 담을 수 있는 multipart로 파싱
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file",file.getName(),requestFile);
        Log.d(TAG, "uploadFile: 이게 뭘까  "+file.getName());
        Log.d(TAG, "uploadFile: 이게 뭘까2  "+requestFile.contentType());
        Log.d(TAG, "uploadFile: 이게 뭘까3  "+body.body());
        Log.d(TAG, "uploadFile: 이게 뭘까4  "+myEmail);
        RequestBody requestEmail = RequestBody.create(MediaType.parse("text/plain"),myEmail);
        applicationClass.retroClient.uploadprofile(body,requestEmail, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
                Toast.makeText(applicationClass, "onError: "+t.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Result_login data = (Result_login)receivedData;
                String serverResult = data.getServerResult();
                String imgresult = data.getCouplekey();
                Log.d(TAG, "onSuccess: 레트로핏 이미지 "+code+"/"+imgresult);
//                Toast.makeText(applicationClass, "onSuccess: "+code+"/"+imgresult, Toast.LENGTH_LONG).show();
                Toast.makeText(applicationClass, "프로필이 변경되었습니다. ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
                Toast.makeText(applicationClass, "onFailure: "+code, Toast.LENGTH_SHORT).show();
            }
        });



    } //uploadFle()


    /* 갤러리에서 인텐트로 받은 이미지의 주소(uri)는 한번에 안받아지므로 따로 정의해주는 매쏘드 => 절대경로
    * https://nittaku.tistory.com/24*/
    public String getPath(Uri uri){

        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    }

    /*이메일값을 서버로 보내고 그걸 이용해 그 사람의 데이터 가져오기*/
    public void getuserinfo(){
        applicationClass.retroClient.getUserData_main(myEmail, new RetroCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                UserData data = (UserData) receivedData;
                Log.d("데이터 불러오기", "onSuccess: "+data.getName());

                if(data.getOther()==null){
                    //signup2에서 바로 메인으로 넘어가서 상대방이 없는 경우
                    Textview_Coupleprofile_Myname.setText(data.getName());
                    Textview_Coupleprofile_Anothername.setText("상대방을 등록해주세요!");
                }else{
                    //커플일 경우에 나와 상대방 이름 등록
                    Textview_Coupleprofile_Myname.setText(data.getName());
                    Textview_Coupleprofile_Anothername.setText(data.getOther());
                }


            }

            @Override
            public void onFailure(int code) {

            }
        });

    }//getuserinfo()


    /* D-day 반환해주는 메소드*/
    private String getDday(int a_year,int a_month ,int a_day){

        final Calendar ddayCalendar = Calendar.getInstance(); //현재 시간을 받아옴
        ddayCalendar.set(a_year, a_month, a_day); //현재 시간을 년도, 달, 일까지 계산

        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today 의 차를 구한다.
        final long dday = ddayCalendar.getTimeInMillis() /ONE_DAY ;
        final long today = Calendar.getInstance().getTimeInMillis()/ONE_DAY ; //오늘날
//        final Date dday = ddayCalendar.getTime();
//        final Date today = Calendar.getInstance().getTime();//오늘날
        Log.d("디데이계산", "dday: "+dday);
        Log.d("디데이계산", "today: "+today);
        long result = dday - today;
        Log.d("디데이계산", "날짜뺀값 "+result);

        // 출력 시 d-day 에 맞게 표시
        final String strFormat;
        if (result > 0) {
            strFormat = "D-"+result;
        } else if (result == 0) {
            strFormat = "♥ 사랑한지 \n0일 ♥";
        } else {
            result *= -1;
            strFormat = "사랑한지\n"+ result + "일";
        }

        final String strCount = (String.format(strFormat, result));
        return strCount;
    }//getDday()


    /*리줌일때 사귄날 불러옴*/
    @Override
    protected void onResume() {
        super.onResume();

        /*applicationClass에서 커플키값, 이메일값 쉐어드로 불러오기*/
//        String coupleKey1 = applicationClass.sharedcouplekey; //커플키값
//        Log.d("애플리케이션클래스에서 가져온", "CoupleprofileActivity: sharedcouplekey ::"+coupleKey1);
        /*먼저 이메일값을 받아온 뒤 그값으로 커플키값을 찾는다.*/
        //이메일값 먼저 가져오기
        SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
        String autoLoginKey = sharedPreferences1.getString("auto_login","no_autologin_key");
        Log.d(TAG, "onDateSet: 오토로그인키()이메일"+autoLoginKey);

        //이메일값으로 그 이메일에 해당하는 커플키 가져오기 - shared저장 signup2
        SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
        String sharedkey = sharedPreferences.getString(autoLoginKey,"no_key_login");
        Log.d(TAG, "onDateSet:이메일값에 해당하는 커플키"+sharedkey);

        /*사귄날 불러오기*/
        applicationClass.retroClient.getcoupledate(sharedkey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Result_login data = (Result_login)receivedData;
//                String serverResult = data.getServerResult();
                String couplekeyResult = data.getCouplekey();
                Log.d(TAG, "onSuccess: "+couplekeyResult+"/"+code);

                //oncreate에서 사귄날 메인에 보여주기
                if(couplekeyResult==null){
                    Textview_Coupleprofile_Date.setText("사귄날을\n등록해주세요\n→");
                }else {
                    Textview_Coupleprofile_Date.setText(couplekeyResult);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });


        Log.d(TAG, "onResume: "+myEmail+"///"+coupleKey);
//        /*나와 상대방 이미지 불러오기*/
//        applicationClass.retroClient.getprofiles(myEmail, coupleKey, new RetroCallback() {
//            @Override
//            public void onError(Throwable t) {
//                Log.d(TAG, "onError: "+t.toString());
//            }
//
//            @Override
//            public void onSuccess(int code, Object receivedData) {
//                Log.d(TAG, "onSuccess: "+code+receivedData);
//                ThreeStringData data = (ThreeStringData)receivedData;
//                String myProfile = data.getFirst();
//                String otherProfile = data.getSecond();
//                Log.d(TAG, "onSuccess: 내 프로필 사진"+myProfile);
//                Log.d(TAG, "onSuccess: 상대 프로필 사진"+otherProfile);
//                if (myProfile!=null){
//                    Glide.with(getApplicationContext()).load("http://192.168.30.130/img/"+myProfile).into(Imageview_Coupleprofile_Myprofile); //글라이드 오류
//                }else{
//                    Glide.with(getApplicationContext()).load(applicationClass.defaultProfile).into(Imageview_Coupleprofile_Myprofile);
//                }
//
//                if (otherProfile!=null){
//                    Glide.with(getApplicationContext()).load("http://192.168.30.130/img/"+otherProfile).into(Imageview_Coupleprofile_Anotherprofile);
//                }else{
//                    Glide.with(getApplicationContext()).load(applicationClass.defaultProfile).into(Imageview_Coupleprofile_Anotherprofile);
//                }
//            }
//
//            @Override
//            public void onFailure(int code) {
//                Log.d(TAG, "onFailure: "+code);
//            }
//        });

    } //onResume


}//END