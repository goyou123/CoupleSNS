package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    final static String TAG = "환경설정 액티비티";
    ImageView Imageview_Setting_back;
    TextView TextView_Setting_Logout,TextView_Setting_Breack,TextView_Setting_EditDate;
    ApplicationClass applicationClass;
    String coupleKey,myEmail;

    //달력관련 변수
    Calendar mCalendar;
    final int ONE_DAY = 24*60*60*1000; //Millisecond형태의 하루(24시간)
    String D_DAY;
    String pickDday;



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
//            Textview_Coupleprofile_Date.setText(D_DAY); // 맨위 텍스트에 붙힘

            /*먼저 이메일값을 받아온 뒤 그값으로 커플키값을 찾는다.*/
            //이메일값 먼저 가져오기
            SharedPreferences sharedPreferences1 = getSharedPreferences("autologin",MODE_PRIVATE);
            String autoLoginKey = sharedPreferences1.getString("auto_login","no_autologin_key");
            Log.d(TAG, "onDateSet: 오토로그인키()이메일"+autoLoginKey);

            //이메일값으로 그 이메일에 해당하는 커플키 가져오기 - shared저장 signup2
            SharedPreferences sharedPreferences = getSharedPreferences("CoupleKey",MODE_PRIVATE);
            String sharedkey = sharedPreferences.getString(autoLoginKey,"no_key_login");
            Log.d(TAG, "onDateSet:이메일값에 해당하는 커플키"+sharedkey);





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
                        Toast.makeText(SettingActivity.this, "선택하신 사귄날은 "+pickDday+" 입니다!", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_setting);

        //applicationClass
        applicationClass = (ApplicationClass) getApplicationContext();

        Imageview_Setting_back = (ImageView) findViewById(R.id.Imageview_Setting_back);
        TextView_Setting_Logout = (TextView) findViewById(R.id.TextView_Setting_Logout);
        TextView_Setting_Breack = (TextView) findViewById(R.id.TextView_Setting_Breack);
        TextView_Setting_EditDate = (TextView) findViewById(R.id.TextView_Setting_EditDate);

        //이메일,커플키값 가져오기
        myEmail = applicationClass.getShared_Email();
        coupleKey = applicationClass.getShared_Couplekey();
        Log.d(TAG, "onCreate: "+myEmail+" / "+coupleKey);


        //로그아웃
        TextView_Setting_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //자동로그인을 데이터 삭제
                SharedPreferences sharedPreferences = getSharedPreferences("autologin",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("auto_login");
                editor.commit();

                //로그인페이지로 이동
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        //사귄 날짜 변경하기 위해 데이트피커 다이얼로그 나오게
        TextView_Setting_EditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Locale.setDefault(Locale.KOREAN); // 한국어 설정
                mCalendar = new GregorianCalendar(); // 현재 날짜를 알기 위해 사용
                int year = mCalendar.get(Calendar.YEAR);
                final int month = mCalendar.get(Calendar.MONTH);
                int day = mCalendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SettingActivity.this,onDateSetListener, year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); //오늘 날짜까지만 선택 가능하도록 제한
                datePickerDialog.show();
                Log.d("디데이데이트피커", "datepicker: "+year+"/"+month+"/"+day);



            }
        });


        //뒤로가기
        Imageview_Setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }//OnCreate

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
}//END