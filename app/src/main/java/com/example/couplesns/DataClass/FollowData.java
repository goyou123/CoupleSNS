package com.example.couplesns.DataClass;

public class FollowData {
    //follow table
    String idx;
    String our_couple;
    String target_couple;
    String date;

    //couples table
    String couplekeys;
    String couplename;
    String member1;
    String member2;

    public FollowData(String idx, String our_couple, String target_couple, String date, String couplekeys, String couplename, String member1, String member2) {
        this.idx = idx;
        this.our_couple = our_couple;
        this.target_couple = target_couple;
        this.date = date;
        this.couplekeys = couplekeys;
        this.couplename = couplename;
        this.member1 = member1;
        this.member2 = member2;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getOur_couple() {
        return our_couple;
    }

    public void setOur_couple(String our_couple) {
        this.our_couple = our_couple;
    }

    public String getTarget_couple() {
        return target_couple;
    }

    public void setTarget_couple(String target_couple) {
        this.target_couple = target_couple;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCouplekeys() {
        return couplekeys;
    }

    public void setCouplekeys(String couplekeys) {
        this.couplekeys = couplekeys;
    }

    public String getCouplename() {
        return couplename;
    }

    public void setCouplename(String couplename) {
        this.couplename = couplename;
    }

    public String getMember1() {
        return member1;
    }

    public void setMember1(String member1) {
        this.member1 = member1;
    }

    public String getMember2() {
        return member2;
    }

    public void setMember2(String member2) {
        this.member2 = member2;
    }


}
