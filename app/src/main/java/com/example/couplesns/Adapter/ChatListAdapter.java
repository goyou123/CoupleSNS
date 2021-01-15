package com.example.couplesns.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.couplesns.ApplicationClass;
import com.example.couplesns.ChatActivity;
import com.example.couplesns.DataClass.ChatData;
import com.example.couplesns.DataClass.ChatListData;
import com.example.couplesns.DataClass.Result_login;
import com.example.couplesns.DataClass.ThreeStringData;
import com.example.couplesns.R;
import com.example.couplesns.RetrofitJava.RetroCallback;
import com.example.couplesns.RetrofitJava.RetroClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>  {
    ArrayList<ChatListData> ChatListDataArrayList;
    Context context;
    ApplicationClass applicationClass;
    String TAG = "채팅리스트어댑터";
    static String serverImageRoot ="http://13.125.182.117/img/";
    RetroClient retroClient;


//    /*1. 롱클릭 추가 */
//    //커스텀 리스너 인터페이스 정의
//    public interface OnListItemLongSelectedInterface {
//        void onItemLongClick(View v, int position); //메인에서 이 메소드를 오버라이딩
//    }
//
//    //리스너 객체를 전달하는 메소드 와 전달될 객체를 저장할 변수 추가(mlistener)
//    private OnListItemLongSelectedInterface mLongListener;
//
//
//
//    /*2. 생성자에 롱클릭 추가*/
//
//    public ChatListAdapter(ArrayList<ChatListData> chatListDataArrayList, Context context,OnListItemLongSelectedInterface longListener ) {
//        this.ChatListDataArrayList = chatListDataArrayList;
//        this.context = context;
//        this.mLongListener = longListener;
//    }


    public ChatListAdapter(ArrayList<ChatListData> chatListDataArrayList, Context context) {
        this.ChatListDataArrayList = chatListDataArrayList;
        this.context = context;
    }

    static class ChatListViewHolder extends RecyclerView.ViewHolder {

        TextView ChatListData_Members,ChatListData_LastMessage,ChatListData_Count,ChatListData_LastTime;
        ConstraintLayout const1;

        /*3. 뷰홀더 매개변수 안에 롱클릭리스너추가*/
        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            ChatListData_Members = (TextView)itemView.findViewById(R.id.ChatListData_Members);
            ChatListData_LastMessage = (TextView)itemView.findViewById(R.id.ChatListData_LastMessage);
            ChatListData_Count = (TextView)itemView.findViewById(R.id.ChatListData_Count);
            ChatListData_LastTime = (TextView)itemView.findViewById(R.id.ChatListData_LastTime);
            const1 = (ConstraintLayout)itemView.findViewById(R.id.const1);


//            /*4. 뷰홀더 안에 롱클릭*/
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int pos = getAdapterPosition();
//
//                    longListener.onItemLongClick(v,pos);
//
//                    return false;
//                }
//            });
        }
    }

    @NonNull
    @Override
    public ChatListAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_chatlistdata,parent,false);
        ChatListViewHolder chatListViewHolder = new ChatListViewHolder(view);


        return chatListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListViewHolder holder, final int position) {


        String my_couplekey = ChatListDataArrayList.get(position).getMy_couplekey();
        String other_couplekey = ChatListDataArrayList.get(position).getOther_couplekey();

        applicationClass = (ApplicationClass) context.getApplicationContext();
        retroClient = applicationClass.retroInAdapter();
        Log.d(TAG, "채팅리스트에 저장된 내 커플키: "+my_couplekey);
        Log.d(TAG, "채팅리스트에 저장된 상대 커플키: "+other_couplekey);
        Log.d(TAG, "채팅리스트, 원래 내 커플키"+applicationClass.getShared_Couplekey());

        //채팅리스트에서 채팅방에 우리커플이름이 안나오오도록 처리
       if(other_couplekey.equals(applicationClass.getShared_Couplekey())){
            Log.d(TAG, "저장된 내 커플키와 저장된 상대 커플키가 같은경우 : ");
            retroClient.chat_get_othername(my_couplekey,new RetroCallback() {
                @Override
                public void onError(Throwable t) {
                    Log.d(TAG, "onError: "+t.toString());
                }

                @Override
                public void onSuccess(int code, Object receivedData) {
//                ArrayList chat_idx_List;
                    List<ThreeStringData> data = (List<ThreeStringData>)receivedData;
                    String other_name1 = data.get(0).getFirst();
                    String other_name2 = data.get(1).getFirst();
                    holder.ChatListData_Members.setText(other_name1+", "+other_name2);

                }

                @Override
                public void onFailure(int code) {
                    Log.d(TAG, "onFailure: "+code);
                }
            });

        }  else {
            Log.d(TAG, "일반적인 경우 : ");
            retroClient.chat_get_othername(other_couplekey,new RetroCallback() {
                @Override
                public void onError(Throwable t) {
                    Log.d(TAG, "onError: "+t.toString());
                }

                @Override
                public void onSuccess(int code, Object receivedData) {
//                ArrayList chat_idx_List;
                    List<ThreeStringData> data = (List<ThreeStringData>)receivedData;
                    String other_name1 = data.get(0).getFirst();
                    String other_name2 = data.get(1).getFirst();
                    holder.ChatListData_Members.setText(other_name1+", "+other_name2);

                }

                @Override
                public void onFailure(int code) {
                    Log.d(TAG, "onFailure: "+code);
                }
            });

        }

       /*룸리스트의 카운트 값이 0 이면 안보이게, new 면 보이게*/
//        holder.ChatListData_Count.setText(ChatListDataArrayList.get(position).getCount());
        String count_vaule = ChatListDataArrayList.get(position).getCount();
        Log.d(TAG, "룸의 카운트값: "+count_vaule);
        if(count_vaule.equals("0")){
            holder.ChatListData_Count.setVisibility(View.INVISIBLE);
        }else {
            //new 인경우
            holder.ChatListData_Count.setText(count_vaule);
        }


        holder.ChatListData_LastMessage.setText(ChatListDataArrayList.get(position).getLast_message());

        holder.ChatListData_LastTime.setText(ChatListDataArrayList.get(position).getLast_msg_time());
//        int int_count = ChatListDataArrayList.get(position).getCount();
//        String count = Integer.toString(int_count);
//        holder.ChatListData_Count.setText(count);


        /*채팅방으로 이동*/
        holder.const1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String my_couplekey = ChatListDataArrayList.get(position).getMy_couplekey();
                String other_couplekey = ChatListDataArrayList.get(position).getOther_couplekey();
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("intent_my_couplekey",my_couplekey);
                intent.putExtra("intent_other_couplekey",other_couplekey);
                context.startActivity(intent);
            }
        });



        /*채팅방 롱클릭 후 삭제*/


        holder.const1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
//                Toast.makeText(context, "롱클릭", Toast.LENGTH_SHORT).show();
                final List<String> ListItems = new ArrayList<>();
                ListItems.add("방 나가기");




                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pos) {
                        String selectedText = items[pos].toString();

                        if(selectedText.equals("방 나가기")){

                            /*한번 더 물어보기*/
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("정말 채팅방을 나가시겠습니까?");
                            builder.setMessage("채팅방을 나가면 이전 대화내용이 모두 삭제되고 상대방과 대화할 수 없습니다.");
                            builder.setPositiveButton("예",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String roomID = ChatListDataArrayList.get(position).getRoom_idx();
                                            /*삭제 처리 코드*/

                                            //리사이클러뷰 리스트에서 삭제
                                            ChatListDataArrayList.remove(position);
                                            notifyDataSetChanged();

                                            retroClient.chat_delete_room(roomID, new RetroCallback() {
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
                                                        Log.d(TAG, "채팅방 나가기관련 DB삭제 성공: ");
                                                    }
                                                }

                                                @Override
                                                public void onFailure(int code) {
                                                    Log.d(TAG, "onFailure: "+code);
                                                }
                                            });

                                            Toast.makeText(context,"채팅방을 나갔습니다..",Toast.LENGTH_LONG).show();

                                        }
                                    });
                            builder.setNegativeButton("아니오",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context,"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();

                                        }
                                    });
                            builder.show();







                            /*삭제 처리 코드*/
//
//                            //리사이클러뷰 리스트에서 삭제
//                            ChatListDataArrayList.remove(position);
//                            notifyDataSetChanged();
//
//                            retroClient.chat_delete_room(roomID, new RetroCallback() {
//                                @Override
//                                public void onError(Throwable t) {
//                                    Log.d(TAG, "onError: "+t.toString());
//                                }
//
//                                @Override
//                                public void onSuccess(int code, Object receivedData) {
//                                    Log.d(TAG, "onSuccess: "+code);
//                                    Result_login data = (Result_login)receivedData;
//                                    String serverResult = data.getServerResult();
//                                    if(serverResult.equals("true")){
//
//                                        Toast.makeText(context, "채팅방을 나갔습니다.", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(int code) {
//                                    Log.d(TAG, "onFailure: "+code);
//                                }
//                            });



                        }
                    }
                });
                builder.show();

                return true;
            }
        });


    }


    @Override
    public int getItemCount() {
        return (ChatListDataArrayList!=null ? ChatListDataArrayList.size():0);
    }


    public void addItem (ChatListData data){
        ChatListDataArrayList.add(data);
    }


}
