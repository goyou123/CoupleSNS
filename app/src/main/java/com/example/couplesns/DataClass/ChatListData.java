package com.example.couplesns.DataClass;

public class ChatListData {
    // 방에 속하는 유저들의 이메일들 (idx) X4 (화면표시)
    String my_couplekey; //인덱스
    String other_couplekey; //인덱스
    String room_idx; // 방 인덱스
    String last_message; // 마지막 메세지 (화면표시)
    String last_msg_time; // 마지막 메세지 시간 (화면표시, 정렬)
    int count; // 안읽은 메세지 개수 (화면표시)

//    1 -> 서버로 hi 를 보내
//            서버에서는 이 hi 를 1,2,3,4 한테 보내야 되잖아
//    서버는 4명의 데이터를 가지고 있고 룸 ID 보유
//    룸 아이디가 A 이고 1,2,3,4 가 있을때 얘네한테만 데이터를 보낸다.
//            B 방에있는 1,2, 5,6




    String idx;
    String other_couple_name;


    public ChatListData() {
    }

    public ChatListData(String idx, String other_couple_name, String last_message, int count, String my_couplekey, String other_couplekey) {
        this.idx = idx;
        this.other_couple_name = other_couple_name;
        this.last_message = last_message;
        this.count = count;
        this.my_couplekey = my_couplekey;
        this.other_couplekey = other_couplekey;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getOther_couple_name() {
        return other_couple_name;
    }

    public void setOther_couple_name(String other_couple_name) {
        this.other_couple_name = other_couple_name;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMy_couplekey() {
        return my_couplekey;
    }

    public void setMy_couplekey(String my_couplekey) {
        this.my_couplekey = my_couplekey;
    }

    public String getOther_couplekey() {
        return other_couplekey;
    }

    public void setOther_couplekey(String other_couplekey) {
        this.other_couplekey = other_couplekey;
    }
}
