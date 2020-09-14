package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.couplesns.Adapter.StoryImageAdapter;
import com.example.couplesns.Adapter.ViewPagerAdapter;
import com.example.couplesns.DataClass.StoryImageData;
import com.example.couplesns.DataClass.Viewpage_Img;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MultipartBody;

public class EditStoryActivity extends AppCompatActivity {
    final static String TAG = "글수정 액티비티";
    ImageView Imageview_EditStory_back,Imageview_EditStory_Gallery;
    Button Button_EditStory_add;
    EditText Edittext_EditStory_Story;

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

    String editIdx,editStoryCouplekey,editImages,editContent,getTime;

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
        /*뷰페이저*/
//        //이미지리스트 가져와 split으로 자르기 (뷰페이저)
//        String getImg = StoryDataList.get(position).getImages();
//        Log.d("이미지들", "onBindViewHolder: "+getImg);
//        imgs = getImg.split("--");
//
//        //split으로 자른 이미지 리스트를 뷰페이저의 리스트에 대입한다,
//        ArrayList<Viewpage_Img> imgArrayList = new ArrayList<>();
//        for (int i=0; i<imgs.length; i++){
//            Log.d("이미지들분할", "onBindViewHolder: "+imgs[i]);
//            imgArrayList.add(new Viewpage_Img(imgs[i]));
//        }
//
//        //뷰페이저에서 보여줄 이미지 리스트가 담긴 어댑터를 뷰페이저와 연결
//        holder.Storydata_MainViewpager.setAdapter(new ViewPagerAdapter(imgArrayList));
        
        Edittext_EditStory_Story.setText(editContent);
        

        //현재시간
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        getTime = simpleDate.format(mDate);



        /*리사이클러뷰 연결*/
        recyclerView = findViewById(R.id.RCV_Writestroy_addimage); //xml연결
        //가로 리사이클러뷰 설정
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        storyImageDataArrayList = new ArrayList<>();
        storyImageAdapter = new StoryImageAdapter(storyImageDataArrayList,this);
        recyclerView.setAdapter(storyImageAdapter);




    }//OnCreate
}//END