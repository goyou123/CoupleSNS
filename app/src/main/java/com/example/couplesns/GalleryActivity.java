package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.couplesns.Adapter.CommentAdapter;
import com.example.couplesns.Adapter.FollowerAdapter;
import com.example.couplesns.Adapter.FollowingAdapter;
import com.example.couplesns.Adapter.GalleryAdapter;
import com.example.couplesns.DataClass.CommentData;
import com.example.couplesns.DataClass.FollowData;
import com.example.couplesns.DataClass.GalleryData;
import com.example.couplesns.RetrofitJava.RetroCallback;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    String TAG = "갤러리어댑터";
    ImageView Imageview_Gallery_back;
    ApplicationClass applicationClass;

    //리사이클러뷰
    ArrayList<GalleryData> galleryDataArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    GalleryAdapter galleryAdapter;

    String couplekey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //applicationClass
        applicationClass = (ApplicationClass) getApplicationContext();
        //xml
        Imageview_Gallery_back =(ImageView) findViewById(R.id.Imageview_Gallery_back); //뒤로가기
        //로그인한 유저의 커플키
        couplekey = applicationClass.getShared_Couplekey();



    }//OnCreate

    @Override
    protected void onResume() {
        super.onResume();
        getGalleryRecyclerView();
    }

    /*갤러리 형식으로 보여줌*/
    public void getGalleryRecyclerView(){
        galleryDataArrayList = new ArrayList<>();

        applicationClass.retroClient.gallery_getImageList(couplekey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                List<GalleryData> galleryData = (List<GalleryData>)receivedData;
                for (int i = 0; i<galleryData.size(); i++){
                    galleryDataArrayList.add(galleryData.get(i));
                    Log.d(TAG, "onCreate: 리사이클러뷰리스트"+galleryDataArrayList);

                    recyclerView = findViewById(R.id.RCV_Gallery); // xml , 리사이클러뷰 연결
                    recyclerView.setHasFixedSize(true);
                    //격자형 리사이클러뷰
                    GridLayoutManager layoutManager1 = new GridLayoutManager(GalleryActivity.this,3);
//                    layoutManager = new LinearLayoutManager(GalleryActivity.this);
                    recyclerView.setLayoutManager(layoutManager1);

                    galleryAdapter = new GalleryAdapter(galleryDataArrayList,GalleryActivity.this);
                    galleryAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(galleryAdapter);
                }

            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });



    }//getGalleryRecyclerView()


}//END