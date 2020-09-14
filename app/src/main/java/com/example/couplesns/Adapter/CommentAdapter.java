package com.example.couplesns.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.couplesns.ApplicationClass;
import com.example.couplesns.CommentActiviy;
import com.example.couplesns.DataClass.CommentData;
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.EditCommentActivity;
import com.example.couplesns.EditStoryActivity;
import com.example.couplesns.R;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    ArrayList<CommentData> CommentDataList;
    Context context;
    ApplicationClass applicationClass;
    RetroClient retroClient;
    static String serverImageRoot ="http://13.125.182.117/img/";

    public CommentAdapter(ArrayList<CommentData> CommentDataList,Context context) {
        this.CommentDataList = CommentDataList;
        this.context = context;
    }


    static class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView Commentdata_profileimg,Commentdata_Hambuger;
        TextView Commentdata_Name,Commentdata_Memo,Commentdata_Date,Commentdata_Reply;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            Commentdata_profileimg =(ImageView) itemView.findViewById(R.id.Commentdata_profileimg);
            Commentdata_Name =(TextView) itemView.findViewById(R.id.Commentdata_Name);
            Commentdata_Memo =(TextView) itemView.findViewById(R.id.Commentdata_Memo);
            Commentdata_Date =(TextView) itemView.findViewById(R.id.Commentdata_Date);
            Commentdata_Reply =(TextView) itemView.findViewById(R.id.Commentdata_Reply);
            Commentdata_Hambuger = (ImageView) itemView.findViewById(R.id.Commentdata_Hambuger);

        }
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //XML과 뷰홀더 연결
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_commentdata,parent,false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(view);
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, final int position) {
        //애플리케이션 클래스 연결
        applicationClass = (ApplicationClass) context.getApplicationContext();
        retroClient = applicationClass.retroInAdapter();
        //댓글 리사이클러뷰 데이터 불러와서 보여주기
        Glide.with(holder.itemView).load(serverImageRoot + CommentDataList.get(position).getWriterimg()).fitCenter()
                .into(holder.Commentdata_profileimg);
        holder.Commentdata_Name.setText(CommentDataList.get(position).getWriter());
        holder.Commentdata_Memo.setText(CommentDataList.get(position).getMemo());



        /*date값을 1분전..방금전... 으로 변환시키기*/
        //스트링으로 저장된 날짜 값 long으로 만들기
        String commentDate = CommentDataList.get(position).getCommentdate();
        long getdate = new Long(commentDate);
        Log.d("커맨트어댑터 온바인드", "onBindViewHolder:롱타입 데이트 "+getdate);

        //long타입 다시 String으로 변환 (방금전..1분전 )
        String setTime = applicationClass.formatTimeString(getdate);
        Log.d("커맨트어댑터 온바인드", "onBindViewHolder:스트링타입 데이트 "+setTime);
        holder.Commentdata_Date.setText(setTime);



        //햄버거 버튼 클릭시 댓글 수정 삭제
        String getcCouplekey = CommentDataList.get(position).getCouplekey();
        String loginCouple = applicationClass.getShared_Couplekey();
        if(getcCouplekey.equals(loginCouple)) {
            //내가 쓴 댓글일때
            holder.Commentdata_Hambuger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //수정, 삭제 다이얼로그 호출
                    final List<String> ListItems = new ArrayList<>();
                    ListItems.add("수정");
                    ListItems.add("삭제");

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int pos) {
                            String selectedText = items[pos].toString();
                            int getidx = CommentDataList.get(position).getIdx();
                            int storyidx = CommentDataList.get(position).getStoryidx();
                            if(selectedText.equals("삭제")){
                                Toast.makeText(context, selectedText, Toast.LENGTH_SHORT).show();

                                //보여주는 댓글 리스트에서 삭제
                                CommentDataList.remove(position);
                                notifyDataSetChanged();

                                //DB에서도 댓글 삭제 - 댓글 수도 신경쓰기
                                retroClient.commentremove(getidx,storyidx, new RetroCallback() {
                                    @Override
                                    public void onError(Throwable t) {
                                        Log.d("댓글어댑터 - 댓글삭제", "onError: "+t.toString());
                                    }

                                    @Override
                                    public void onSuccess(int code, Object receivedData) {
                                        Log.d("댓글어댑터 - 댓글삭제", "onSuccess: "+code);

                                    }

                                    @Override
                                    public void onFailure(int code) {
                                        Log.d("댓글어댑터 - 댓글삭제", "onFailure: "+code);
                                    }
                                });


                            }else if(selectedText.equals("수정")){
                                Toast.makeText(context, selectedText, Toast.LENGTH_SHORT).show();

                                int getidx2 = CommentDataList.get(position).getIdx(); //댓글 인덱스
                                String stidx = String.valueOf(getidx2);
                                String getmemo = CommentDataList.get(position).getMemo();
                                String writer = CommentDataList.get(position).getWriter();
                                String writerimg = CommentDataList.get(position).getWriterimg();

                                //수정 액티비티에서 보여줄 게시글 관련 데이터들을 전달한다.
                                Intent intent = new Intent(context, EditCommentActivity.class);
                                intent.putExtra("getidx",stidx); //댓글 위치를 위해
                                intent.putExtra("writer",writer); //작성자 커플키
                                intent.putExtra("writerimg",writerimg);
                                intent.putExtra("getmemo",getmemo);

                                Log.d("말풍선 클릭", "onClick: "+intent);
                                context.startActivity(intent);

                            }


                        }
                    });
                    builder.show();
                }
            });


        }else {
            //내가 쓴 댓글 아닐때 햄버거 안보이게
            holder.Commentdata_Hambuger.setVisibility(View.GONE);
        }




    }

    @Override
    public int getItemCount() {
        return (CommentDataList!=null ? CommentDataList.size():0);
    }


}
