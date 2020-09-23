package com.example.couplesns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.couplesns.Adapter.CommentAdapter;
import com.example.couplesns.Adapter.FollowerAdapter;
import com.example.couplesns.Adapter.FollowingAdapter;
import com.example.couplesns.DataClass.CommentData;
import com.example.couplesns.DataClass.FollowData;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FollowActivity extends AppCompatActivity {
    final static String TAG = "팔로우 액티비티";
    ApplicationClass applicationClass;

    //리사이클러뷰
    ArrayList<FollowData> followDataArrayList,followDataArrayList2;
    ArrayList<FollowData> setFollowerList,setFollowingList;
    RecyclerView recyclerView,recyclerView2;
    RecyclerView.LayoutManager layoutManager,layoutManager2;
    FollowingAdapter followingAdapter;
    FollowerAdapter followerAdapter;
    String myCouplekey;

    EditText Edittext_Follow_Find2,Edittext_Follow_Find1;
    TabItem tabItem1,tabItem2;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        applicationClass = (ApplicationClass) getApplicationContext();;

        myCouplekey = applicationClass.getShared_Couplekey();


        /*탭 레이아웃 관련설정*/
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // TODO : process tab selection event.
                //탭 선택시 관련 레이아웃 보이기 / 안보이기
                int pos = tab.getPosition();
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });



        /*검색 기능 - 팔로워*/
        Edittext_Follow_Find1 = (EditText) findViewById(R.id.Edittext_Follow_Find1);
        Edittext_Follow_Find2 = (EditText) findViewById(R.id.Edittext_Follow_Find2);
        tabItem1 = (TabItem) findViewById(R.id.tabItem1);
        tabItem2 = (TabItem) findViewById(R.id.tabItem2);


    }//OnCreate


    /*탭 메뉴*/
    private void changeView(int index) {
        LinearLayout View1 = (LinearLayout) findViewById(R.id.View1);
        LinearLayout View2 = (LinearLayout) findViewById(R.id.View2);

        switch (index) {
            case 0:
                View1.setVisibility(View.VISIBLE);
                View2.setVisibility(View.INVISIBLE);
                break;
            case 1:
                View1.setVisibility(View.INVISIBLE);
                View2.setVisibility(View.VISIBLE);
                break;


        }
    }//changeView()


    @Override
    protected void onResume() {
        super.onResume();
        getFollowingRecyclerView();
        getFollowerRecyclerView();
    }

    /*팔로잉 리사이클러뷰 불러오기*/
    public void getFollowingRecyclerView(){
        followDataArrayList = new ArrayList<>(); //리사이클러뷰 리스트
        setFollowingList = new ArrayList<>(); //검색에 사용될 리스트

        applicationClass.retroClient.follow_getfollowingList(myCouplekey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                List<FollowData> followData = (List<FollowData>)receivedData;
                for (int i = 0; i<followData.size(); i++){
                    followDataArrayList.add(followData.get(i));
                    Log.d(TAG, "onSuccess: 리사이클러뷰리스트"+followDataArrayList);

                    //검색을 위한 리스트
                    setFollowingList.add(followData.get(i));
                    Log.d(TAG, "onSuccess: 검색에 사용될 리스트"+setFollowingList);
                }

                //팔로잉 옆에 갯수
                tabLayout.getTabAt(1).setText("팔로잉 "+followDataArrayList.size());

                //팔로잉 리사이클러뷰 세팅
                recyclerView = findViewById(R.id.RCV_Follow_View2); // xml , 리사이클러뷰 연결
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(FollowActivity.this);
                recyclerView.setLayoutManager(layoutManager);

                followingAdapter = new FollowingAdapter(followDataArrayList,FollowActivity.this);
                followingAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(followingAdapter);


                //팔로잉 검색
                Edittext_Follow_Find2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String text = Edittext_Follow_Find2.getText().toString();
                        searchFollowing(text);
                    }
                });

            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });
    }




    /*팔로워 리사이클러뷰 불러오기*/
    public void getFollowerRecyclerView(){
        followDataArrayList2 = new ArrayList<>();

        setFollowerList = new ArrayList<>(); //검색에 사용될 리스트
        applicationClass.retroClient.follow_getfollowerList(myCouplekey, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: "+t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(TAG, "onSuccess: "+code);
                List<FollowData> followData = (List<FollowData>)receivedData;
                for (int i = 0; i<followData.size(); i++){
                    followDataArrayList2.add(followData.get(i));
                    Log.d(TAG, "onSuccess: 리사이클러뷰리스트"+followDataArrayList2);

                    setFollowerList.add(followData.get(i));
                    Log.d(TAG, "onSuccess: 검색에 사용될 리스트"+setFollowerList);
                }

                //팔로워 옆에 갯수
                tabLayout.getTabAt(0).setText("팔로워 "+followDataArrayList2.size());


                //팔로워 리사이클러뷰 세팅
                recyclerView2 = findViewById(R.id.RCV_Follow_View1); // xml , 리사이클러뷰 연결
                recyclerView2.setHasFixedSize(true);
                layoutManager2 = new LinearLayoutManager(FollowActivity.this);
                recyclerView2.setLayoutManager(layoutManager2);

                followerAdapter = new FollowerAdapter(followDataArrayList2,FollowActivity.this);
                followerAdapter.notifyDataSetChanged();
                recyclerView2.setAdapter(followerAdapter);

                //팔로워 검색
                Edittext_Follow_Find1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String text = Edittext_Follow_Find1.getText().toString();
                        searchFollower(text);
                    }
                });

            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });

    }



    /*검색을 수행하는 메소드 - 팔로워*/
    public void searchFollower(String charText){
        //문자 입력시마다 리스트를 지우고 새로 뿌려줌
        followDataArrayList2.clear();

        //문자입력 없을시 모든 데이터 보여주기
        if (charText.length() == 0) {
            followDataArrayList2.addAll(setFollowerList);
        }else{
            //문자 입력 할때
            Log.d(TAG, "검색!searchFollower: "+setFollowerList.size());

            //리스트의 모든 데이터를 검색
           for(int i=0; i<setFollowerList.size(); i++){

               //리스트의 커플 이름에 입력한 단어가 포함되어있으면 트루를 반환
               //contains() = String 문자값이 같은지 비교
                if(setFollowerList.get(i).getCouplename().contains(charText)){

                    //검색된 데이터를 리스트에 추가한다
                    followDataArrayList2.add(setFollowerList.get(i));
                    Log.d(TAG, "검색!searchFollower: "+followDataArrayList2);
                }
            }
        }


        followerAdapter.notifyDataSetChanged();

    }//searchFollower()



    /*검색을 수행하는 메소드 - 팔로잉*/
    public void searchFollowing(String charText){
        //문자 입력시마다 리스트를 지우고 새로 뿌려줌
        followDataArrayList.clear();

        //문자입력 없을시 모든 데이터 보여주기
        if (charText.length() == 0) {
            followDataArrayList.addAll(setFollowingList);
        }else{
            //문자 입력 할때
            Log.d(TAG, "검색!searchFollower: "+setFollowingList.size());

            //리스트의 모든 데이터를 검색
            for(int i=0; i<setFollowingList.size(); i++){

                //리스트의 커플 이름에 입력한 단어가 포함되어있으면 트루를 반환
                if(setFollowingList.get(i).getCouplename().contains(charText)){

                    //검색된 데이터를 리스트에 추가한다
                    followDataArrayList.add(setFollowingList.get(i));
                    Log.d(TAG, "검색!searchFollower: "+followDataArrayList);
                }
            }
        }


        followingAdapter.notifyDataSetChanged();

    }//searchFollowing()






}//END