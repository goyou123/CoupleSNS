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

import com.example.couplesns.Adapter.StoryImageAdapter;
import com.example.couplesns.Adapter.ViewPagerAdapter;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.StoryImageData;
import com.example.couplesns.DataClass.Viewpage_Img;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.example.couplesns.ApplicationClass.serverImageRoot;

public class EditStoryActivity extends AppCompatActivity {
    final static String TAG = "글수정 액티비티";
    ImageView Imageview_EditStory_back,Imageview_EditStory_Gallery;
    Button Button_EditStory_add;
    EditText Edittext_EditStory_Story;

    final static int REQUEST_IMAGE_CODE = 1001;

    //리사이클러뷰
    RecyclerView recyclerView;
    //    RecyclerView.LayoutManager layoutManager;
    StoryImageAdapter storyImageAdapter;
    ArrayList<StoryImageData> storyImageDataArrayList;
    StoryImageData storyImageData;
    ArrayList<String> imageList;

    //이미지 서버 전송
    ArrayList<MultipartBody.Part> multiList;

    ApplicationClass applicationClass;
    String couplekey;

    String editIdx,editStoryCouplekey,editImages,editContent,getTime,addStory;

    String imgs[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_story);

        /*커플키 생성*/
        applicationClass = (ApplicationClass) getApplicationContext();
        couplekey=applicationClass.getShared_Couplekey();

        //xml 연결
        Imageview_EditStory_back = (ImageView)findViewById(R.id.Imageview_WriteStory_back);
        Imageview_EditStory_Gallery = (ImageView)findViewById(R.id.Imageview_Writestroy_Gallery);
        Button_EditStory_add = (Button)findViewById(R.id.Button_Writestory_add);
        Edittext_EditStory_Story = (EditText)findViewById(R.id.Edittext_Writestory_Story);


        //인텐트로 받은 게시글 내용들
        Intent intent = getIntent();
        editIdx = intent.getStringExtra("getidx");
        editStoryCouplekey = intent.getStringExtra("getCouplekey");
        editImages = intent.getStringExtra("getStoryImgs");
        editContent = intent.getStringExtra("getContent");
        Log.d(TAG, "onCreate: 이미지들 "+editImages);

        //글 내용 대입
        Edittext_EditStory_Story.setText(editContent);


        //인텐트로 받아온 이미지 리스트 리사이클러뷰에 보여주기
        imgs = editImages.split("--");
        storyImageDataArrayList = new ArrayList<>();
        for (int i=0; i<imgs.length; i++){
            Log.d("이미지들분할", "onBindViewHolder: "+serverImageRoot+imgs[i]);
            storyImageDataArrayList.add(new StoryImageData(serverImageRoot+imgs[i]));
        }
        Log.d(TAG, "이미지 리스트: "+storyImageDataArrayList);

        /*리사이클러뷰 연결 - 사진들 리사이클러뷰에 보이게*/
        recyclerView = findViewById(R.id.RCV_Writestroy_addimage); //xml연결
        recyclerView.setHasFixedSize(true);
        //가로 리사이클러뷰 설정
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        storyImageAdapter = new StoryImageAdapter(storyImageDataArrayList,this);
        storyImageAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(storyImageAdapter);






        /*작성하기 버튼 눌렀을때 DB에서 업데이트 되도록 */
        Button_EditStory_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(applicationClass, "서버에 사진 여러장 올리기 수정중..", Toast.LENGTH_SHORT).show();
                editStoryUpload();

            }
        });




        //글 작성할때 이미지 여러장 불러오기 (갤러리 열기)
        Imageview_EditStory_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*이전 이미지데이터 리스트 초기화*/
                storyImageDataArrayList.clear();
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
    public void editStoryUpload () {

        //수정한 글 내용
        addStory = Edittext_EditStory_Story.getText().toString();

        //현재시간
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        getTime = simpleDate.format(mDate);


        //서버로 보낼 수 있도록 파일 형식 변환
        RequestBody requestEditStory = RequestBody.create(MediaType.parse("text/plain"),addStory);
        RequestBody requestEditIdx = RequestBody.create(MediaType.parse("text/plain"),editIdx); //게시글 idx
        RequestBody requestEditDate = RequestBody.create(MediaType.parse("text/plain"),getTime);

        applicationClass.retroClient.editstoryupload(multiList, requestEditIdx, requestEditStory, requestEditDate, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                Result_login data = (Result_login)receivedData;
                Log.d(TAG, "onSuccess: "+data);
                //수정이 성공해서 이쪽으로 오면
                Toast.makeText(applicationClass, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
//                Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
//                startActivity(intent);
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });


    }




    }//END