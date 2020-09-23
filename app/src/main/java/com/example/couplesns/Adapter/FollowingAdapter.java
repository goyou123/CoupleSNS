package com.example.couplesns.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.couplesns.DataClass.CommentData;
import com.example.couplesns.DataClass.FollowData;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.OtherCoupleProfileActivity;
import com.example.couplesns.R;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder> {
    ArrayList<FollowData> FollowDataList;
    Context context;
    ApplicationClass applicationClass;
    RetroClient retroClient;
    static String serverImageRoot ="http://13.125.182.117/img/";
    static String defaultProfile = "http://13.125.182.117/img/default_profile.png";
    String TAG = "팔로우어댑터";

    public FollowingAdapter(ArrayList<com.example.couplesns.DataClass.FollowData> followData, Context context) {
        FollowDataList = followData;
        this.context = context;
    }


    static class FollowingViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout follow_const1;
        ImageView Followingdata_profileimg,Followingdata_profileimg2;
        TextView Followingdata_Couplename;
        Button Followingdata_RemoveBtn;

        public FollowingViewHolder(@NonNull View itemView) {
            super(itemView);
            follow_const1 = (ConstraintLayout) itemView.findViewById(R.id.follow_const1);
            Followingdata_profileimg = (ImageView) itemView.findViewById(R.id.Followingdata_profileimg);
            Followingdata_profileimg2 = (ImageView) itemView.findViewById(R.id.Followingdata_profileimg2);
            Followingdata_Couplename = (TextView) itemView.findViewById(R.id.Followingdata_Couplename);
            Followingdata_RemoveBtn = (Button) itemView.findViewById(R.id.Followingdata_RemoveBtn);




        }
    }//FollowingViewHolder class



    @NonNull
    @Override
    public FollowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_followingdata,parent,false);
        FollowingViewHolder followingViewHolder = new FollowingViewHolder(view);
        return followingViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FollowingViewHolder holder, final int position) {

        applicationClass = (ApplicationClass) context.getApplicationContext();
        retroClient = applicationClass.retroInAdapter();

        holder.Followingdata_Couplename.setText(FollowDataList.get(position).getCouplename());

        String member1 = FollowDataList.get(position).getMember1();
        String member2 = FollowDataList.get(position).getMember2();
        String otherCouplekey = FollowDataList.get(position).getCouplekeys();
        Log.d("팔로잉어댑터 - 바인드", "onBindViewHolder: "+member1+"/"+member2);

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
                    Glide.with(holder.itemView).load(serverImageRoot+myProfile).into(holder.Followingdata_profileimg
                    ); //글라이드 오류
                }else{
                    Glide.with(holder.itemView).load(defaultProfile).into(holder.Followingdata_profileimg);
                }

                if (otherProfile!=null){
                    Glide.with(holder.itemView).load("http://13.125.182.117/img/"+otherProfile).into(holder.Followingdata_profileimg2);
//                    Glide.with(getApplicationContext()).load("http://3.34.137.189/img/"+otherProfile).into(Imageview_Coupleprofile_Anotherprofile);
                }else{
                    Glide.with(holder.itemView).load(defaultProfile).into(holder.Followingdata_profileimg2);
                }
            }

            @Override
            public void onFailure(int code) {
                Log.d(TAG, "onFailure: "+code);
            }
        });




        /*배경 클릭시 상대커플액티비티로 이동*/
        holder.follow_const1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String writerCouplekey = FollowDataList.get(position).getCouplekeys();//해당 커플키
                String writerCoupleName = FollowDataList.get(position).getCouplename();//해당 커플키

                Intent intent = new Intent(context, OtherCoupleProfileActivity.class);
                intent.putExtra("writerCoupleKey",writerCouplekey);
                intent.putExtra("writerCoupleName",writerCoupleName);
                //사진 2장을 어떻게 처리할 것인지.
                // 1. 커플키를 넘기고 그 커플키- 멤버 1,2 를 사용해 이미지를 불러오는 방식?
                // 2. 3단 조인으로 (그러면 데이터 클래스 구조 바뀌어야함)
                // 3. 여기서 구한 이미지 2개 를 어떻게든 넘겨보기
//                intent.putExtra("writerProfile1",myProfile);
//                intent.putExtra("writerProfile2",writerProfile2);
                context.startActivity(intent);
            }
        });





        /*팔로잉 해제하기 (팔로우 취소하기)*/
        holder.Followingdata_RemoveBtn.setOnClickListener(new View.OnClickListener() {
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

                //팔로우 테이블에서 팔로우 취소하기
                retroClient.follow_remove(addFollow, new RetroCallback() {
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

                            builder.setMessage("팔로우를 취소 하시겠습니까?");
                            builder.setPositiveButton("예",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context,"팔로우를 취소 하였습니다.",Toast.LENGTH_LONG).show();
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






}//END
