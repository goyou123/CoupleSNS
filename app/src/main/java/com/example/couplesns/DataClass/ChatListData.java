package com.example.couplesns.DataClass;

public class ChatListData {
    String idx;
    String other_couple_name;
    String last_message;
    int count;
    String my_couplekey;
    String other_couplekey;

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
