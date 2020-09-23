package com.example.couplesns.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.couplesns.ApplicationClass;
import com.example.couplesns.DataClass.FollowData;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.R;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder> {
    ArrayList<FollowData> FollowDataList;
    Context context;
    ApplicationClass applicationClass;
    RetroClient retroClient;
    static String serverImageRoot ="http://13.125.182.117/img/";
    static String defaultProfile = "http://13.125.182.117/img/default_profile.png";
    String TAG = "팔로워어댑터";

    public FollowerAdapter(ArrayList<FollowData> followDataList, Context context) {
        FollowDataList = followDataList;
        this.context = context;
    }



    static class FollowerViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout follower_const1;
        ImageView Followerdata_profileimg,Followerdata_profileimg2;
        TextView Followerdata_Couplename;
        Button Followerdata_RemoveBtn;

        public FollowerViewHolder(@NonNull View itemView) {
            super(itemView);
            follower_const1 = (ConstraintLayout) itemView.findViewById(R.id.follower_const1);
            Followerdata_profileimg = (ImageView) itemView.findViewById(R.id.Followerdata_profileimg);
            Followerdata_profileimg2 = (ImageView) itemView.findViewById(R.id.Followerdata_profileimg2);
            Followerdata_Couplename = (TextView) itemView.findViewById(R.id.Followerdata_Couplename);
            Followerdata_RemoveBtn = (Button) itemView.findViewById(R.id.Followerdata_RemoveBtn);

        }
    }//FollowerViewHolder class



    @NonNull
    @Override
    public FollowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_followerdata,parent,false);
        FollowerViewHolder followerViewHolder = new FollowerViewHolder(view);
        return followerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FollowerViewHolder holder, final int position) {
        applicationClass = (ApplicationClass) context.getApplicationContext();
        retroClient = applicationClass.retroInAdapter();

        holder.Followerdata_Couplename.setText(FollowDataList.get(position).getCouplename());


        String member1 = FollowDataList.get(position).getMember1();
        String member2 = FollowDataList.get(position).getMember2();
        String otherCouplekey = FollowDataList.get(position).getCouplekeys();
        Log.d("팔로워어댑터 - 바인드", "onBindViewHolder: "+member1+"/"+member2);

        /*나와 상대방 이미지 불러오기*/
        retroClient.getprofiles(member1, otherCouplekey, new RetroCallback() {
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
                    Glide.with(holder.itemView).load(serverImageRoot+myProfile).into(holder.Followerdata_profileimg
                    ); //글라이드 오류
                }else{
                    Glide.with(holder.itemView).load(defaultProfile).into(holder.Followerdata_profileimg);
                }

                if (otherProfile!=null){
                    Glide.with(holder.itemView).load("http://13.125.182.117/img/"+otherProfile).into(holder.Followerdata_profileimg2);
//                    Glide.with(getApplicationContext()).load("http://3.34.137.189/img/"+otherProfile).into(Imageview_Coupleprofile_Anotherprofile);
                }else{
                    Glide.with(holder.itemView).load(defaultProfile).into(holder.Followerdata_profileimg2);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });



        /*삭제 버튼 클릭시 내가 팔로워 삭제*/
        holder.Followerdata_RemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //현재시간
                long mNow = System.currentTimeMillis();
                Date mDate = new Date(mNow);
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String getTime = simpleDate.format(mDate);

                String myCouplekey = applicationClass.getShared_Couplekey();
                String otherCouplekey = FollowDataList.get(position).getCouplekeys();

                HashMap<String, Object> addFollow = new HashMap<>();
                addFollow.put("our_couple",myCouplekey);
                addFollow.put("target_couple",otherCouplekey);
                addFollow.put("date",getTime);

                retroClient.follower_remove(addFollow, new RetroCallback() {
                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "onError: "+t.toString());
                    }

                    @Override
                    public void onSuccess(int code, Object receivedData) {
                        Log.d(TAG, "onSuccess: "+code);
                        Result_login data = (Result_login)receivedData;
                        String serverResult = data.getServerResult();
                        if(serverResult.equals("true")){

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);

                            builder.setMessage("팔로워를 삭제 하시겠습니까?");
                            builder.setPositiveButton("예",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context,"팔로워를 삭제 하였습니다",Toast.LENGTH_LONG).show();
                                            FollowDataList.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    });
                            builder.setNegativeButton("아니오",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context,"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();

                                        }
                                    });
                            builder.show();


                        }
                    }

                    @Override
                    public void onFailure(int code) {
                        Log.d(TAG, "onFailure: "+code);
                    }
                });



            }
        });




    }

    @Override
    public int getItemCount() {
        return (FollowDataList!=null ? FollowDataList.size():0);
    }


}
