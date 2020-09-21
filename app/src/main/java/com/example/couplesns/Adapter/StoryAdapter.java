package com.example.couplesns.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.couplesns.ApplicationClass;
import com.example.couplesns.CommentActiviy;
import com.example.couplesns.CoupleprofileActivity;
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.DataClass.Viewpage_Img;
import com.example.couplesns.EditSecretStoryActivity;
import com.example.couplesns.EditStoryActivity;
import com.example.couplesns.Listener.OnStoryItemClickListener;
import com.example.couplesns.MainActivity;
import com.example.couplesns.OtherCoupleProfileActivity;
import com.example.couplesns.R;
import com.example.couplesns.RealMainActivity;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;
import com.example.couplesns.SecretCommentActivity;
import com.example.couplesns.SettingActivity;
import com.example.couplesns.WriteStoryActivitiy;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    public ArrayList<StoryData> StoryDataList;
    Context context;
//    OnStoryItemClickListener listener;
    static String serverImageRoot ="http://13.125.182.117/img/";
    ApplicationClass applicationClass1;
    RetroClient retroClient;

    String imgs[];
    String getidx;
    String getCouplekey;


    public StoryAdapter(ArrayList<StoryData> storyDataList,Context context) {
        this.StoryDataList = storyDataList;
        this.context = context;
    }


    static class StoryViewHolder extends RecyclerView.ViewHolder {
        /*뷰홀더에서는 그냥 xml연결만*/
        //일반
        ImageView Storydata_Manprofile,Storydata_Girlprofile,Storydata_Hambuger,Storydata_Heartimg,Storydata_Heartimg2,Storydata_Commentimg;
        ViewPager2 Storydata_MainViewpager;
        TextView Storydata_CoupleName,Storydata_CountHeart,Storydata_CountComment,Storydata_LastCommentsName,Storydata_LastComments,Storydata_AllComments,Storydata_Maincontent,Storydata_Date;


        //익명
        ImageView Secretdata_Profile,Secretdata_CommentImg,Secretdata_Hambuger;
        TextView Secretdata_Content,Secretdata_Date,Secretdata_CommentCount;
        ConstraintLayout Const2;

        public StoryViewHolder(@NonNull final View itemView) {
        /*뷰홀더에서 뭐 할 생각하지 말고 온바인드에서 처리하기. holder.storydata/..*/
            super(itemView);

            //일반 모드
            Storydata_Manprofile = (ImageView)itemView.findViewById(R.id.Storydata_Manprofile); //남자사진
            Storydata_Girlprofile = (ImageView)itemView.findViewById(R.id.Storydata_Girlprofile); //여자사진
            Storydata_Hambuger = (ImageView)itemView.findViewById(R.id.Storydata_Hambuger); //햄버거 버튼
            Storydata_Heartimg = (ImageView)itemView.findViewById(R.id.Storydata_Heartimg); //하트 이미지 색칠 X
            Storydata_Heartimg2 = (ImageView)itemView.findViewById(R.id.Storydata_Heartimg2); //하트 이미지 색칠 O
            Storydata_Commentimg = (ImageView)itemView.findViewById(R.id.Storydata_Commentimg); //말풍선 이미지
            Storydata_MainViewpager = (ViewPager2)itemView.findViewById(R.id.Storydata_MainViewpager); // 메인 사진 뷰페이저
            Storydata_Maincontent = (TextView)itemView.findViewById(R.id.Storydata_Maincontent); // 메인 글 내용
            Storydata_CoupleName = (TextView)itemView.findViewById(R.id.Storydata_CoupleName); // 커플의 이름
            Storydata_Date = (TextView)itemView.findViewById(R.id.Storydata_Date); //날짜
            Storydata_CountHeart = (TextView)itemView.findViewById(R.id.Storydata_CountHeart); // 좋아요 수
            Storydata_CountComment = (TextView)itemView.findViewById(R.id.Storydata_CountComment); // 댓글 수
            Storydata_LastCommentsName = (TextView)itemView.findViewById(R.id.Storydata_LastCommentsName); // 마지막 댓글단 사람 이름
            Storydata_LastComments = (TextView)itemView.findViewById(R.id.Storydata_LastComments); // 마지막 댓글
            Storydata_AllComments = (TextView)itemView.findViewById(R.id.Storydata_AllComments); // 댓글 모두 보기


            //익명 게시글
            Const2 = (ConstraintLayout)itemView.findViewById(R.id.Const2);
            Secretdata_Profile = (ImageView)itemView.findViewById(R.id.Secretdata_Profile); //기본프로필사진
            Secretdata_CommentImg = (ImageView)itemView.findViewById(R.id.Secretdata_CommentImg); //댓글 아이콘
            Secretdata_Hambuger = (ImageView)itemView.findViewById(R.id.Secretdata_Hambuger); //햄버거 아이콘
            Secretdata_Content = (TextView)itemView.findViewById(R.id.Secretdata_Content); // 내용
            Secretdata_Date = (TextView)itemView.findViewById(R.id.Secretdata_Date); // 날짜
            Secretdata_CommentCount = (TextView)itemView.findViewById(R.id.Secretdata_CommentCount); //댓글수
        }





    }// StoryViewHolder

    @Override
    public int getItemViewType(int position) {
        String getform = StoryDataList.get(position).getForm();
        int i = 0;
        Log.d("스토리 리사이클러뷰 뷰타입 결정", "getItemViewType: " + getform);

        if (getform.equals("normal")) {
            i = 1;

        } else if (getform.equals("secret")) {
            i = 2;

        }
        Log.d("스토리 리사이클러뷰 뷰타입 결정", "getItemViewType: "+i);
        return i;
    }



    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
//        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_stroydata,parent,false);
        if(viewType==2){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_secretstorydata,parent,false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_stroydata,parent,false);
        }
        StoryViewHolder storyViewHolder = new StoryViewHolder(view);

        return storyViewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull final StoryViewHolder holder, final int position) {
//        applicationClass1 = (ApplicationClass) context.getApplicationContext();
//        retroClient = applicationClass1.retroInAdapter();


        String formType =StoryDataList.get(position).getForm();
        if(formType.equals("secret")){
            applicationClass1 = (ApplicationClass) context.getApplicationContext();
            retroClient = applicationClass1.retroInAdapter();
            /*익명 게시글 일 경우*/
            holder.Secretdata_Content.setText(StoryDataList.get(position).getContent());
            holder.Secretdata_Date.setText(StoryDataList.get(position).getDate());
            holder.Secretdata_CommentCount.setText(String.valueOf(StoryDataList.get(position).getComments()));

            //게시글 클릭해서 상세 페이지로 이동하는 것 보다는 쓰기 쉽게 통일성을 주자 >> 댓글액티비티로 이동ㅌ
            holder.Secretdata_CommentImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "게시글클릭", Toast.LENGTH_SHORT).show();
                    String secretIdx = StoryDataList.get(position).getIdx();
                    String secretContent = StoryDataList.get(position).getContent();
                    String secretDate = StoryDataList.get(position).getDate();

                    Intent intent = new Intent(context, SecretCommentActivity.class);
                    intent.putExtra("secretIdx",secretIdx);
                    intent.putExtra("secretContent",secretContent);
                    intent.putExtra("secretDate",secretDate);
                    context.startActivity(intent);

                }
            });


            //익명 게시글 햄버거 버튼 클릭시 수정, 삭제
            String ddd = StoryDataList.get(position).getWriteremail();
            String getWriteremail = StoryDataList.get(position).getWriteremail();
            String loginEmail = applicationClass1.getShared_Email();
            Log.d("ㅇㅇㅇㅇ", "onBindViewHolder서버: "+getWriteremail);
            Log.d("ㅇㅇㅇㅇ", "onBindViewHolder쉐어드: "+loginEmail);
            if(getWriteremail.equals(loginEmail)) {
                holder.Secretdata_Hambuger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {


                        //수정, 삭제 다이얼로그 호출
                        final List<String> ListItems = new ArrayList<>();
                        ListItems.add("수정");
                        ListItems.add("삭제");


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int pos) {
                                String selectedText = items[pos].toString();

                                //idx값을 가져오는건 최대한 실행시킬 코드 가까이서 해야 포지션값이 안꼬임
                                String getidx3 = StoryDataList.get(position).getIdx();
                                int idx =Integer.parseInt(getidx3);

                                if (selectedText.equals("삭제")) {
                                    Toast.makeText(context, selectedText, Toast.LENGTH_SHORT).show();
                                    Log.d("바인드속", "onClick: "+StoryDataList);
                                    StoryDataList.remove(position);
                                    notifyDataSetChanged();


                                    //TODO: 디비에서도 삭제
//                                retroClient = applicationClass1.retroInAdapter();
                                    retroClient.mainStory_remove(idx, new RetroCallback() {
                                        @Override
                                        public void onError(Throwable t) {
                                            Log.d("어댑터-바인드 속", "onError: "+t.toString());
                                        }

                                        @Override
                                        public void onSuccess(int code, Object receivedData) {
                                            Log.d("어댑터-바인드 속", "onSuccess: "+code+receivedData);
                                        }

                                        @Override
                                        public void onFailure(int code) {
                                            Log.d("어댑터-바인드 속", "onFailure: "+code);
                                        }
                                    });



                                } else if (selectedText.equals("수정")) {
                                    //TODO: 수정 하는 액티비티로 이동하기
                                    String getidx2 = StoryDataList.get(position).getIdx(); //게시글 인덱스
//                                    getCouplekey = StoryDataList.get(position).getCouplekey(); //작성커플
//                                    String getStoryImgs = StoryDataList.get(position).getImages(); // 이미지들
                                    String getWriterEmail = StoryDataList.get(position).getWriteremail();
                                    String getContent = StoryDataList.get(position).getContent();
                                    //날짜는 새로 작성할때 ㅇㅋ

                                    //수정 액티비티에서 보여줄 게시글 관련 데이터들을 전달한다.
                                    Intent intent = new Intent(context, EditSecretStoryActivity.class);
                                    intent.putExtra("getidx",getidx2); //댓글 위치를 위해
//                                    intent.putExtra("getCouplekey",getCouplekey); //작성자의 커플키 필요있나?
                                    intent.putExtra("getWriterEmail",getWriterEmail); //작성자의 이메일 - 본인만 수정하기 위해서
                                    intent.putExtra("getContent",getContent);


                                    Log.d("말풍선 클릭", "onClick: "+intent);
                                    context.startActivity(intent);


                                }
                            }
                        });
                        builder.show();


                    }
                });
            } else {
                //내가 쓴 글 아닐때  이미지뷰 비우기
//            holder.Storydata_Hambuger.setBackgroundColor(0);
                holder.Secretdata_Hambuger.setVisibility(View.GONE);
            }




        }else{

            /*데이터 관련된 행동은 여기에서하기
            * 일반 게시글일 때*/
            //applicationClass - 수정 삭제 시 서버와 php연동하기 위해
            applicationClass1 = (ApplicationClass) context.getApplicationContext();
            retroClient = applicationClass1.retroInAdapter();

            //idx 가져와서 수정 삭제 시 DB의 INDEX값으로 해당 게시글을 찾기 위해 가져옴
            getidx = StoryDataList.get(position).getIdx();


            /*뷰페이저*/
            //이미지리스트 가져와 split으로 자르기 (뷰페이저)
            String getImg = StoryDataList.get(position).getImages();
            Log.d("이미지들", "onBindViewHolder: "+getImg);
            if(getImg!=null){
                imgs = getImg.split("--");
            }
//            imgs = getImg.split("--");

            //split으로 자른 이미지 리스트를 뷰페이저의 리스트에 대입한다,
            ArrayList<Viewpage_Img> imgArrayList = new ArrayList<>();
            if(imgs!=null){
                for (int i=0; i<imgs.length; i++){
                    Log.d("이미지들분할", "onBindViewHolder: "+imgs[i]);

                    //이미지와 이미지 총 길이와 이미지의 순번을 적용해서 보여준다.
                    imgArrayList.add(new Viewpage_Img(imgs[i],imgs.length,i+1));
                }
                holder.Storydata_MainViewpager.setAdapter(new ViewPagerAdapter(imgArrayList));

            }
//            for (int i=0; i<imgs.length; i++){
//                Log.d("이미지들분할", "onBindViewHolder: "+imgs[i]);
//
//                //이미지와 이미지 총 길이와 이미지의 순번을 적용해서 보여준다.
//                imgArrayList.add(new Viewpage_Img(imgs[i],imgs.length,i+1));
//            }

            //뷰페이저에서 보여줄 이미지 리스트가 담긴 어댑터를 뷰페이저와 연결
//            holder.Storydata_MainViewpager.setAdapter(new ViewPagerAdapter(imgArrayList));


            /*메인 게시글 리사이클러뷰 - 아이템뷰에 들어갈 내용들*/

            Glide.with(holder.itemView).load(serverImageRoot + StoryDataList.get(position).getMyimg()).fitCenter()
                    .into(holder.Storydata_Manprofile); // 남자 이미지
            Glide.with(holder.itemView).load(serverImageRoot + StoryDataList.get(position).getOtherimg()).fitCenter()
                    .into(holder.Storydata_Girlprofile); // 여자 이미지
            holder.Storydata_CoupleName.setText(StoryDataList.get(position).getWriter()); // 커플 이름
            holder.Storydata_Maincontent.setText(StoryDataList.get(position).getContent()); // 메인 글 내용
            holder.Storydata_CountHeart.setText(String.valueOf(StoryDataList.get(position).getHeart())); // 좋아요 수
            holder.Storydata_CountComment.setText(String.valueOf(StoryDataList.get(position).getComments())); // 댓글 수
            holder.Storydata_Date.setText(StoryDataList.get(position).getDate()); //날짜
            holder.Storydata_LastComments.setText(StoryDataList.get(position).getLastcomment()); // 마지막 댓글
            holder.Storydata_LastCommentsName.setText(StoryDataList.get(position).getWriterOfLastcomment()); // 마지막 댓글 작성자







            /*처름 리사이클러뷰 생성시 좋아요 유무를 판단해서 하트 색깔을 유지*/
            String storyheart_who = StoryDataList.get(position).getWho(); // 좋아요 테이블의 좋아요 누른 사람 (커플키)
            String heart_couplekey = applicationClass1.getShared_Couplekey();
            int getidx4 = StoryDataList.get(position).getStory_idx();

            Log.d("좋아요,조인", "onBindViewHolder: 커플키 "+storyheart_who); //NULL
            Log.d("좋아요,조인", "onBindViewHolder: IDX "+getidx4);

//        int idx =Integer.parseInt(getidx4);
            int storyheart_story_idx = StoryDataList.get(position).getStory_idx(); // 좋아요눌러진 게시글
            if(storyheart_who!=null){
                //좋아요 누른게 있고 그게 내 아이디랑 일치할때 빨간하트
                if(storyheart_who.equals(heart_couplekey) && getidx4==storyheart_story_idx){
                    holder.Storydata_Heartimg.setVisibility(View.INVISIBLE);
                    holder.Storydata_Heartimg2.setVisibility(View.VISIBLE);
                }else{
                    //그 외의 경우 빈하트
                    holder.Storydata_Heartimg.setVisibility(View.VISIBLE);
                    holder.Storydata_Heartimg2.setVisibility(View.INVISIBLE);
                }

            }else{
                //좋아요 안눌러서 stoygheart가 null일때 빈 하트
                holder.Storydata_Heartimg.setVisibility(View.VISIBLE);
                holder.Storydata_Heartimg2.setVisibility(View.INVISIBLE);
            }





            /*댓글 갯수에 따라 변화 - 댓글 모두 보기*/
            int countComments = StoryDataList.get(position).getComments();

            //댓글의 갯수가 1개 이상일때 "댓글 n 개 모두 보기" 가 보이도록 한다
            if(countComments<1){
                holder.Storydata_AllComments.setVisibility(View.GONE);
            }else {

                //댓글이 1개 이상 존재하여 클릭 가능할때 말풍선 눌렀을때와 똑같이 게시귿 데이터를 가지고 댓글 액티비티로 이동
                String stCount = String.valueOf(countComments);
                holder.Storydata_AllComments.setText("댓글 "+stCount+"개 모두 보기");

                //"댓글 n개 모두 보기" 클릭 시 댓글 액티비티로 이동한다
                holder.Storydata_AllComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String getidx2 = StoryDataList.get(position).getIdx();
                        getCouplekey = StoryDataList.get(position).getCouplekey();
                        String sharedEmail = applicationClass1.getShared_Email();
                        String getMyimg = StoryDataList.get(position).getMyimg();
                        String getOtherimg = StoryDataList.get(position).getOtherimg();
                        String getCouplename = StoryDataList.get(position).getWriter();
                        String getContent = StoryDataList.get(position).getContent();
                        String getDate = StoryDataList.get(position).getDate();

                        Intent intent = new Intent(context, CommentActiviy.class);
                        intent.putExtra("getidx",getidx2); //댓글 위치를 위해
                        intent.putExtra("getcCouplekey",getCouplekey); //작성자의 커플키
                        intent.putExtra("sharedEmail",sharedEmail); //작성자의 이메일
                        intent.putExtra("getMyimg",getMyimg);
                        intent.putExtra("getOtherimg",getOtherimg);
                        intent.putExtra("getCouplename",getCouplename);
                        intent.putExtra("getContent",getContent);
                        intent.putExtra("getDate",getDate);

                        Log.d("말풍선 클릭", "onClick: "+intent);
                        context.startActivity(intent);
                    }
                });


            }





            /*말풍선 이미지 클릭 시 댓글 액티비티로 이동*/
            holder.Storydata_Commentimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String getidx2 = StoryDataList.get(position).getIdx();
                    getCouplekey = StoryDataList.get(position).getCouplekey();
                    String sharedEmail = applicationClass1.getShared_Email();
                    String getMyimg = StoryDataList.get(position).getMyimg();
                    String getOtherimg = StoryDataList.get(position).getOtherimg();
                    String getCouplename = StoryDataList.get(position).getWriter();
                    String getContent = StoryDataList.get(position).getContent();
                    String getDate = StoryDataList.get(position).getDate();


                    //댓글 액티비티에서 보여줄 게시글 관련 데이터들을 전달한다.
                    Intent intent = new Intent(context, CommentActiviy.class);
                    intent.putExtra("getidx",getidx2); //댓글 위치를 위해
                    intent.putExtra("getcCouplekey",getCouplekey); //작성자의 커플키
                    intent.putExtra("sharedEmail",sharedEmail); //작성자의 이메일
                    intent.putExtra("getMyimg",getMyimg);
                    intent.putExtra("getOtherimg",getOtherimg);
                    intent.putExtra("getCouplename",getCouplename);
                    intent.putExtra("getContent",getContent);
                    intent.putExtra("getDate",getDate);

                    Log.d("말풍선 클릭", "onClick: "+intent);
                    context.startActivity(intent);
                }
            });




            /*색칠 안된 하트 클릭시 좋아요+1*/
            holder.Storydata_Heartimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //해당 글의 idx와 작성자를 서버에 보내 DB 에 저장한다.
                    String getidx2 = StoryDataList.get(position).getIdx();
                    int idx = Integer.parseInt(getidx2);
                    String couplekey = applicationClass1.getShared_Couplekey();

                    //좋아요 테이블에 데이터를 추가한다.
                    retroClient.story_addheart(idx,couplekey, new RetroCallback() {
                        @Override
                        public void onError(Throwable t) {
                            Log.d("스토리 어댑터 바인드속, 좋아요", "onError: "+t.toString());
                        }

                        @Override
                        public void onSuccess(int code, Object receivedData) {
                            Log.d("스토리 어댑터 바인드속, 좋아요", "onSuccess: "+code);

                            //서버에서 데이터를 추가하는 쿼리가 성공적으로 이루어졌으면 빈 하트를 빨간 하트로 바꾼다.
                            ThreeStringData data = (ThreeStringData)receivedData;
                            String result = data.getFirst();
                            if(result.equals("true")){
                                Toast.makeText(context, "좋아요!", Toast.LENGTH_SHORT).show();
                                holder.Storydata_Heartimg.setVisibility(View.INVISIBLE);
                                holder.Storydata_Heartimg2.setVisibility(View.VISIBLE);

                                //일단 인텐트플래그를 통해 좋아요 숫자가 바로 반영되도록 한다. >> 수정필요
                                Intent intent = new Intent(context,RealMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                context.startActivity(intent);
                            }

                        }

                        @Override
                        public void onFailure(int code) {
                            Log.d("스토리 어댑터 바인드속, 좋아요", "onFailure: "+code);
                        }
                    });


                }
            });



            /*색칠 된 하트 클릭시 좋아요 취소*/
            holder.Storydata_Heartimg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //서버에 게시글의 idx와 로그인한 유저의 커플키를 보낸다.
                    String getidx2 = StoryDataList.get(position).getIdx();
                    int idx = Integer.parseInt(getidx2);
                    String couplekey = applicationClass1.getShared_Couplekey();

                    //DB에서 좋아요를 삭제한다.
                    retroClient.story_removeheart(idx,couplekey, new RetroCallback() {
                        @Override
                        public void onError(Throwable t) {
                            Log.d("스토리 어댑터 바인드속, 좋아요 취소", "onError: "+t.toString());
                        }

                        @Override
                        public void onSuccess(int code, Object receivedData) {

                            //좋아요 삭제 쿼리가 실행됬으면 빨간 하트를 다시 빈 하트로 바꾼다.
                            Log.d("스토리 어댑터 바인드속, 좋아요 취소", "onSuccess: "+code);
                            ThreeStringData data = (ThreeStringData)receivedData;
                            String result = data.getFirst();
                            if(result.equals("true")){
                                Toast.makeText(context, "좋아요 취소", Toast.LENGTH_SHORT).show();
                                holder.Storydata_Heartimg2.setVisibility(View.INVISIBLE);
                                holder.Storydata_Heartimg.setVisibility(View.VISIBLE);

                                Intent intent = new Intent(context,RealMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                context.startActivity(intent);
                            }

                        }

                        @Override
                        public void onFailure(int code) {
                            Log.d("스토리 어댑터 바인드속, 좋아요 취소", "onFailure: "+code);
                        }
                    });


                }
            });





            /*햄버거 버튼 클릭 시 게시글 수정과 삭제
             * 내가 쓴 게시글만 햄버거 버튼 보이기 - 후에 안의 다이얼로그를 변경해도 괜찮음*/

            String getcCouplekey = StoryDataList.get(position).getCouplekey();
            String loginCouple = applicationClass1.getShared_Couplekey();
            if(getcCouplekey.equals(loginCouple)) {
                holder.Storydata_Hambuger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {


                        //수정, 삭제 다이얼로그 호출
                        final List<String> ListItems = new ArrayList<>();
                        ListItems.add("수정");
                        ListItems.add("삭제");


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int pos) {
                                String selectedText = items[pos].toString();

                                //idx값을 가져오는건 최대한 실행시킬 코드 가까이서 해야 포지션값이 안꼬임
                                String getidx3 = StoryDataList.get(position).getIdx();
                                int idx =Integer.parseInt(getidx3);

                                if (selectedText.equals("삭제")) {
                                    Toast.makeText(context, selectedText, Toast.LENGTH_SHORT).show();
                                    Log.d("바인드속", "onClick: "+StoryDataList);
                                    StoryDataList.remove(position);
                                    notifyDataSetChanged();


                                    //TODO: 디비에서도 삭제
//                                retroClient = applicationClass1.retroInAdapter();
                                    retroClient.mainStory_remove(idx, new RetroCallback() {
                                        @Override
                                        public void onError(Throwable t) {
                                            Log.d("어댑터-바인드 속", "onError: "+t.toString());
                                        }

                                        @Override
                                        public void onSuccess(int code, Object receivedData) {
                                            Log.d("어댑터-바인드 속", "onSuccess: "+code+receivedData);
                                        }

                                        @Override
                                        public void onFailure(int code) {
                                            Log.d("어댑터-바인드 속", "onFailure: "+code);
                                        }
                                    });



                                } else if (selectedText.equals("수정")) {
                                    //TODO: 수정 하는 액티비티로 이동하기
                                    String getidx2 = StoryDataList.get(position).getIdx(); //게시글 인덱스
                                    getCouplekey = StoryDataList.get(position).getCouplekey(); //작성커플
                                    String getStoryImgs = StoryDataList.get(position).getImages(); // 이미지들
                                    String getContent = StoryDataList.get(position).getContent();
                                    //날짜는 새로 작성할때 ㅇㅋ

                                    //수정 액티비티에서 보여줄 게시글 관련 데이터들을 전달한다.
                                    Intent intent = new Intent(context, EditStoryActivity.class);
                                    intent.putExtra("getidx",getidx2); //댓글 위치를 위해
                                    intent.putExtra("getCouplekey",getCouplekey); //작성자의 커플키 필요있나?
                                    intent.putExtra("getStoryImgs",getStoryImgs); //작성자의 커플키 필요있나?
                                    intent.putExtra("getContent",getContent);


                                    Log.d("말풍선 클릭", "onClick: "+intent);
                                    context.startActivity(intent);


                                }
                            }
                        });
                        builder.show();


                    }
                });
            } else {
                //내가 쓴 글 아닐때  이미지뷰 비우기
//            holder.Storydata_Hambuger.setBackgroundColor(0);
                holder.Storydata_Hambuger.setVisibility(View.GONE);
            }




            /*커플 이름 클릭 시 프로필 액티비티로 이동
             * 1. 우리 프로필
             * 2. 상대 커플 프로필*/
            holder.Storydata_CoupleName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String loginCouplekey = applicationClass1.getShared_Couplekey(); //현재 로그인한 사람의 커플키
                    String writerCouplekey = StoryDataList.get(position).getCouplekey(); // 스토리 게시한 커플키

                    if(loginCouplekey.equals(writerCouplekey)){
                        //내 프로필로 이동
                        Intent intent = new Intent(context,CoupleprofileActivity.class);
                        context.startActivity(intent);
                    }else {
                        //상대 커플 프로필로 이동
                        String writerCoupleKey = StoryDataList.get(position).getCouplekey();
                        String writerCoupleName = StoryDataList.get(position).getWriter();
                        String writerProfile1 = StoryDataList.get(position).getMyimg();
                        String writerProfile2 = StoryDataList.get(position).getOtherimg();

                        Intent intent = new Intent(context, OtherCoupleProfileActivity.class);
                        intent.putExtra("writerCoupleKey",writerCoupleKey);
                        intent.putExtra("writerCoupleName",writerCoupleName);
                        intent.putExtra("writerProfile1",writerProfile1);
                        intent.putExtra("writerProfile2",writerProfile2);
                        context.startActivity(intent);
                    }

                }
            });







        } // else

    }//onbind


    @Override
    public int getItemCount() {
        return (StoryDataList!=null ? StoryDataList.size():0);
    }


//
//    @Override
//    public void onItemClick(StoryViewHolder holder, View view, int position) {
//        //OnItemClick은 뷰홀더 클래스 안에서 뷰가 클릭됬을때 호출되는 메서드
//        // 그런데 밖에서 이벤트처리를 하는 것이 일반적이므로 listener 변수를 하나 선언하고
//        if (listener != null) {
//            listener.onItemClick(holder, view, position);
//        }
//    }
//    public void setOnItemClickListener(OnStoryItemClickListener listener){
//        //외부에서 리스너를 설정할 수 있도록 하는 메서드
//        this.listener = listener;
//    }


}
