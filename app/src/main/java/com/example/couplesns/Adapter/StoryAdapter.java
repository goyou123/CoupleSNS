package com.example.couplesns.Adapter;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.couplesns.ApplicationClass;
import com.example.couplesns.DataClass.StoryData;
import com.example.couplesns.Listener.OnStoryItemClickListener;
import com.example.couplesns.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    public ArrayList<StoryData> StoryDataList;
    Context context;
    OnStoryItemClickListener listener;
    static String serverImageRoot ="http://13.125.182.117/img/";
    public StoryAdapter(ArrayList<StoryData> storyDataList,Context context) {
        this.StoryDataList = storyDataList;
        this.context = context;
    }



    static class StoryViewHolder extends RecyclerView.ViewHolder {

        ImageView Storydata_Manprofile,Storydata_Girlprofile,Storydata_Hambuger;
        ViewPager Storydata_MainViewpager;
        TextView Storydata_CoupleName,Storydata_CountHeart,Storydata_CountComment,Storydata_CommentsName,Stortdata_LastComments,Stortdata_AllComments,Storydata_Maincontent;

        public StoryViewHolder(@NonNull View itemView) {

            // 1. 뷰홀더 객체의 생성자가 호출될 때 리스너 객체가 파라미터로 전달되로록 추가,
            // 뷰가 클릭되었을 때 리스너객체의 OnItemclick 이벤트 호출
            super(itemView);
            Storydata_Manprofile = (ImageView)itemView.findViewById(R.id.Storydata_Manprofile); //남자사진
            Storydata_Girlprofile = (ImageView)itemView.findViewById(R.id.Storydata_Girlprofile); //여자사진
            Storydata_Hambuger = (ImageView)itemView.findViewById(R.id.Storydata_Hambuger); //햄버거 버튼
            Storydata_MainViewpager = (ViewPager)itemView.findViewById(R.id.Storydata_MainViewpager); // 메인 사진 뷰페이저
            Storydata_Maincontent = (TextView)itemView.findViewById(R.id.Storydata_Maincontent); // 메인 글 내용
            Storydata_CoupleName = (TextView)itemView.findViewById(R.id.Storydata_CoupleName); // 커플의 이름
            Storydata_CountHeart = (TextView)itemView.findViewById(R.id.Storydata_CountHeart); // 좋아요 수
            Storydata_CountComment = (TextView)itemView.findViewById(R.id.Storydata_CountComment); // 댓글 수
            Storydata_CommentsName = (TextView)itemView.findViewById(R.id.Storydata_CommentsName); // 마지막 댓글단 사람 이름
            Stortdata_LastComments = (TextView)itemView.findViewById(R.id.Stortdata_LastComments); // 마지막 댓글
            Stortdata_AllComments = (TextView)itemView.findViewById(R.id.Stortdata_AllComments); // 댓글 모두 보기


            //햄버거 버튼 클릭 시 컨텍스트 메뉴로 수정,삭제 버튼 나오게
            Storydata_Hambuger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    Log.d("리사이클러뷰속", "햄버거: 클릭");
                    view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                            // 3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록해줍니다. ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분하게 됩니다.
                            MenuItem Edit = contextMenu.add(Menu.NONE, 1001, 1, "편집");
                            MenuItem Delete = contextMenu.add(Menu.NONE, 1002, 2, "삭제");
                            Edit.setOnMenuItemClickListener(onEditMenu);
                            Delete.setOnMenuItemClickListener(onEditMenu);

                        }
                        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener(){

                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                menuItem.getItemId();
                                if(menuItem.getItemId()==1001){
                                    Log.d("리사이클러뷰 아이템", "onMenuItemClick: 수정");
                                }else if(menuItem.getItemId()==1002){
                                    Log.d("리사이클러뷰 아이템", "onMenuItemClick: 삭제");
                                   
                                }
                                return true;
                            }//onMenuItemClick

                        };
                    });
                }
            });

//            //2. 뷰홀더에 클릭리스너 달기, 파라미터에 리스너 추가
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition(); // 이 뷰홀더에 표시할 아이템이 어댑터에서 몇번째 정보인지 알려줌
//                    if(listener !=null){
//                        listener.onItemClick(StoryViewHolder.this,v, pos);
//                    }
//                }
//            });

        }// StoryViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener



    }


    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_stroydata,parent,false);
        StoryViewHolder storyViewHolder = new StoryViewHolder(view);
        return storyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {

        Glide.with(holder.itemView).load(serverImageRoot+StoryDataList.get(position).getMyimg()).fitCenter()
                .into(holder.Storydata_Manprofile); // 남자 이미지
        Glide.with(holder.itemView).load(serverImageRoot+StoryDataList.get(position).getOtherimg()).fitCenter()
                .into(holder.Storydata_Girlprofile); // 여자 이미지
        holder.Storydata_CoupleName.setText(StoryDataList.get(position).getWriter()); // 커플 이름
        holder.Storydata_Maincontent.setText(StoryDataList.get(position).getContent()); // 메인 글 내용
        holder.Storydata_CountHeart.setText(String.valueOf(StoryDataList.get(position).getHeart())); // 좋아요 수
        holder.Storydata_CountComment.setText(String.valueOf(StoryDataList.get(position).getComments())); // 댓글 수
//        holder.Stortdata_LastComments.setText(StoryDataList.get(position).getLastcomment()); // 마지막 댓글
    }

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
