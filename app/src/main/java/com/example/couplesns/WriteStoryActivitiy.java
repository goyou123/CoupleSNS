package com.example.couplesns;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.couplesns.Adapter.StoryImageAdapter;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.StoryImageData;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class WriteStoryActivitiy extends AppCompatActivity {
    final static String FORM = "normal";
    final static int REQUEST_IMAGE_CODE = 100;
    final static String TAG = "글작성 액티비티";
    ImageView Imageview_WriteStory_back,Imageview_Writestroy_Gallery;
    Button Button_Writestory_add;
    EditText Edittext_Writestory_Story;

    ApplicationClass applicationClass;
    String couplekey;
    String coupleName;

    String sharedCouplename,sharedMyimg,sharedOtherimg;

    //리사이클러뷰
    RecyclerView recyclerView;
//    RecyclerView.LayoutManager layoutManager;
    StoryImageAdapter storyImageAdapter;
    ArrayList<StoryImageData> storyImageDataArrayList;
    StoryImageData storyImageData;
    ArrayList<String> imageList;

    //이미지 서버 전송
    ArrayList<MultipartBody.Part> multiList;
    String addStory;
    int count;



    //상대와 나의 프로필
    String myProfile,otherProfile;

    //리퀘스트바디
   RequestBody requestWriter,requestMyimg,requestOtherimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_story_activitiy);

        /*커플키 생성*/
        applicationClass = (ApplicationClass) getApplicationContext();
        couplekey=applicationClass.getShared_Couplekey();

        Imageview_WriteStory_back = (ImageView)findViewById(R.id.Imageview_WriteStory_back);
        Imageview_Writestroy_Gallery = (ImageView)findViewById(R.id.Imageview_Writestroy_Gallery);
        Button_Writestory_add = (Button)findViewById(R.id.Button_Writestory_add);
        Edittext_Writestory_Story = (EditText)findViewById(R.id.Edittext_Writestory_Story);

        /*리사이클러뷰 연결*/
        recyclerView = findViewById(R.id.RCV_Writestroy_addimage); //xml연결
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
        //가로 리사이클러뷰 설정
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        storyImageDataArrayList = new ArrayList<>();
        storyImageAdapter = new StoryImageAdapter(storyImageDataArrayList,this);
        recyclerView.setAdapter(storyImageAdapter);


        //작성완료 버튼 클릭시
        Button_Writestory_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStory = Edittext_Writestory_Story.getText().toString();
                Toast.makeText(applicationClass, "서버에 사진 여러장 올리기 수정중..", Toast.LENGTH_SHORT).show();
                storyUpload();

            }
        });


        //글 작성할때 이미지 여러장 불러오기 (갤러리 열기)
        Imageview_Writestroy_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true); //여러장 선택
                startActivityForResult(intent,REQUEST_IMAGE_CODE);
            }
        });
    }//OnCreate

    /*갤러리 사진 선택한거 불러와 리사이클러뷰로 보여주기*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && data != null) {
            if(data.getClipData() ==null){
                Toast.makeText(this, "다중선택이 안되는 기기입니다.", Toast.LENGTH_SHORT).show();
            }else{
                ClipData clipData = data.getClipData();
                Log.d(TAG, "onActivityResult: clipData "+clipData);
                if(clipData.getItemCount()>9){
                    Toast.makeText(this, "사진은 9장까지 선택 가능합니다.", Toast.LENGTH_SHORT).show();

                }else if(clipData.getItemCount() > 0 && clipData.getItemCount() < 9){
                    /*실제 여러장 추가 되는 곳*/
                    imageList = new ArrayList<>(); //사진 url 리스트
                    multiList = new ArrayList<>(); //서버 전송을 위한 Multipart형식의 리스트

                    for(int i=0; i<clipData.getItemCount(); i++){
                        //리사이클러뷰에 추가
                        Log.d(TAG, "여러장일때 (9장가능): "+getPath(clipData.getItemAt(i).getUri()));
                        String addimg = getPath(clipData.getItemAt(i).getUri());

                        //내가 갤러리에서 선택한 사진들을 리사이클러뷰로 보여줄 리스트
                        storyImageData = new StoryImageData();
                        storyImageData.setStoryimage(addimg);
                        storyImageDataArrayList.add(storyImageData);
                        storyImageAdapter.notifyDataSetChanged();

                        //uri값을 저장해 멀티파트로 바꿀 리스트 [기본 uri 스트링값 리스트]
                        imageList.add(addimg);

                        //PHP단에서 count함수를 사용하기 때문에 굳이 필요 없음
                        count = imageList.size();

                        /*선택한 이미지들의 갯수만큼 반복문을 돌면서 멀티리스트(서버로 보낼 리스트)에 멀티파트로 변환한 이미지를 리스트에 담는다.*/
                            String StringJ = String.valueOf(i);
                            File file = new File(addimg);
                            // uri파일을 리퀘스트바디에 담을 수 있는 multipart로 파싱
                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
//                          RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);
//                          MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file"+StringJ,file.getName(),requestFile);

                            /*name속성의 값 끝에 [] 를 붙혀 배열로 전송한다.*/
                            MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file[]",file.getName(),requestFile);
                            multiList.add(body);
                            Log.d(TAG, "선택한 사진들 이름: "+file.getName());

                    }

                    //로그로 각 리스트들의 값 확인
                    Log.d(TAG, "onActivityResult: +multiList : "+multiList);
                    Log.d(TAG, "onActivityResult: storyImageDataArrayList"+storyImageDataArrayList);
                    Log.d(TAG, "onActivityResult: imageList"+imageList);
                }//else if
            } //if2
        } //if1
    }//onActivityResult()

    /*이미지 절대 경로*/
    public String getPath(Uri uri){

        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);

    }


    /*작성완료 버튼 클릭시 작성 글 + 이미지들 업로드*/
    public void storyUpload (){
        //작성 글 내용
        addStory = Edittext_Writestory_Story.getText().toString();

        /*커플이름 가져와 shared에 저장하기 >> onSuccess에 있는 값을 전역으로 빼는 방법 고민*/
        applicationClass.retroClient.getcouplename(couplekey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Result_login data = (Result_login)receivedData;
                coupleName = data.getServerResult();
                Log.d(TAG, "onSuccess: 커플네임은: "+coupleName);

                SharedPreferences sharedPreferences = getSharedPreferences("STORY",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("couplename",coupleName);
                editor.apply();
            }

            @Override
            public void onFailure(int code) {

            }
        });




        /*나와 상대방 프로필 img 가져오기*/
        String myEmail=applicationClass.getShared_Email();
        String coupleKey=applicationClass.getShared_Couplekey();
        Log.d(TAG, "storyUpload: 이메일과 커플키 : "+myEmail+"///"+coupleKey);
        applicationClass.retroClient.getprofiles(myEmail, coupleKey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code+receivedData);
                ThreeStringData data = (ThreeStringData)receivedData;
                myProfile = data.getFirst();
                otherProfile = data.getSecond();
                Log.d(TAG, "onSuccess: 내 프로필 사진"+myProfile);
                Log.d(TAG, "onSuccess: 상대 프로필 사진"+otherProfile);

                SharedPreferences sharedPreferences = getSharedPreferences("STORY",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("myimg",myProfile);
                editor.putString("otherimg",otherProfile);
                editor.commit();

            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });



        //현재시간
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String getTime = simpleDate.format(mDate);

        //커플키
        applicationClass.getShared_Email();
        String writer_couplekey = applicationClass.getShared_Couplekey();

        //커플이름,나와 상대 이미지 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("STORY",MODE_PRIVATE);
        sharedCouplename = sharedPreferences.getString("couplename","NONAME");
        sharedMyimg = sharedPreferences.getString("myimg","default_profile.png");
        sharedOtherimg = sharedPreferences.getString("otherimg","default_profile.png");

        Log.d(TAG, "storyUpload: 쉐어드에서 불러온 커플네임: "+sharedCouplename);
        Log.d(TAG, "onSuccess: 서버로 저장할 내 프로필 사진:::"+sharedMyimg);
        Log.d(TAG, "onSuccess: 서버로 저장할 상대 프로필 사진1:::"+sharedOtherimg);

        //작성자 이메일 (후에 추가되었음 - 익명 게시글 수정 삭제를 위해)
        String myemail = applicationClass.getShared_Email();
        Log.d(TAG, "storyUpload:로그인, 작성자이메일 "+myemail);

        // 서버로 보낼 데이터들 - Requestbody형식으로 맞춰줌 => 레트로핏 인터페이스 매개변수와 형식 일치해야 서버로 전송 가능
        RequestBody requestWriter = RequestBody.create(MediaType.parse("text/plain"),sharedCouplename);
        RequestBody requestWriteremail = RequestBody.create(MediaType.parse("text/plain"),myemail);
        RequestBody requestCouplekey = RequestBody.create(MediaType.parse("text/plain"),writer_couplekey);
        RequestBody requestMyimg = RequestBody.create(MediaType.parse("text/plain"),sharedMyimg);
        RequestBody requestOtherimg = RequestBody.create(MediaType.parse("text/plain"),sharedOtherimg);
        RequestBody requestStory = RequestBody.create(MediaType.parse("text/plain"),addStory);
        RequestBody requestDate = RequestBody.create(MediaType.parse("text/plain"),getTime);
        RequestBody requestForm = RequestBody.create(MediaType.parse("text/plain"),FORM);
        RequestBody requestCount = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(count));

        Log.d(TAG, "storyUpload: +작성자이메일 : "+ requestWriteremail);
        Log.d(TAG, "storyUpload: +작성커플키 : "+ writer_couplekey);
        Log.d(TAG, "storyUpload: +쓴내용 : "+ addStory);
        Log.d(TAG, "storyUpload: +현재시간 : "+getTime);
        Log.d(TAG, "storyUpload: +형식 : "+ FORM);
        Log.d(TAG, "storyUpload: +멀티리스트 : "+ multiList);
        Log.d(TAG, "storyUpload: +사진갯수 : "+ requestCount+"/인트"+count);


        //서버로 사진들과 함께 저장할 데이터 저장
        applicationClass.retroClient.storyupload(multiList,requestWriter,requestWriteremail,requestCouplekey,requestMyimg,requestOtherimg,requestStory,
                requestDate, requestForm, requestCount, new RetroCallback() {
                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "onError: "+t.toString());
                    }

                    @Override
                    public void onSuccess(int code, Object receivedData) {
                        Log.d(TAG, "onSuccess: storyupload 이미지여러장 :"+code+"/"+receivedData);
                        List<Result_login> data = (List<Result_login>)receivedData;
                        String datas = data.toString();
//                        String serverResult = data.getServerResult();
//                        String ccc = data.getCouplekey();
                        Log.d(TAG, "onSuccess: +멀티리스트 : "+ multiList);
                        Log.d(TAG, "onSuccess: 사진들 서버에 업로드 결과 0: "+datas);
                        Log.d(TAG, "onSuccess: 사진들 서버에 업로드 결과 1 : "+data.get(0).toString());
//                        Log.d(TAG, "onSuccess: 사진들 서버에 업로드 결과 1 : "+serverResult);
//                        Log.d(TAG, "onSuccess: 사진들 서버에 업로드 결과 2 : "+ccc);



                        //저장 끝나고 메인으로
                        Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int code) {
                        Log.d(TAG, "onFailure: "+code);
                    }
                });


    }




}//END