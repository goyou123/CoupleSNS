package com.example.couplesns.DataClass;

import java.util.HashMap;

public class UserData {
    String name;
    String email;
    String phone;
    String myCode;
    String other;
    String profileimg;
    String couplekey;
    String sex;
//    public UserData(HashMap<String, Object> parameters) {
//        this.name = (String) parameters.get("name");
//        this.email = (String) parameters.get("email");
//        this.phone = (int) parameters.get("phone");
//    }
//


    public UserData(String name, String email, String phone, String myCode,
                    String other, String profileimg, String couplekey, String sex) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.myCode = myCode;
        this.other = other;
        this.profileimg = profileimg;
        this.couplekey = couplekey;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMyCode() {
        return myCode;
    }

    public void setMyCode(String myCode) {
        this.myCode = myCode;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public String getCouplekey() {
        return couplekey;
    }

    public void setCouplekey(String couplekey) {
        this.couplekey = couplekey;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
