package com.example.couplesns.DataClass;

public class ChatData {
    String idx;
    String user_name;
    String email;
    String message;
    String chat_time;
    String read_result;
    String user_profile;
    String msg_type;

    public ChatData() {
    }

    public ChatData(String user_name, String email, String message, String chat_time, String read_result, String user_profile, String msg_type) {
        this.user_name = user_name;
        this.email = email;
        this.message = message;
        this.chat_time = chat_time;
        this.read_result = read_result;
        this.user_profile = user_profile;
        this.msg_type = msg_type;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChat_time() {
        return chat_time;
    }

    public void setChat_time(String chat_time) {
        this.chat_time = chat_time;
    }

    public String getRead_result() {
        return read_result;
    }

    public void setRead_result(String read_result) {
        this.read_result = read_result;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }
}
