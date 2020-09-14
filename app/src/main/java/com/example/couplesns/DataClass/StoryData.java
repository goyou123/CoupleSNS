package com.example.couplesns.DataClass;

public class StoryData {
    String who; //좋아요 테이블
    int story_idx; //좋아요 테이블

    //게시글 테이블
    String idx;
    String writer;
    String couplekey;
    String myimg;
    String otherimg;
    String content;
    String date;
    String images;
    String form;
    int heart;
    int comments;
    int script;
    String lastcomment;
    String writerOfLastcomment;

    public StoryData(String who, int story_idx, String idx, String writer, String couplekey, String myimg, String otherimg,
                     String content, String date, String images, String form, int heart, int comments, int script, String lastcomment, String writerOfLastcomment) {
        this.who = who;
        this.story_idx = story_idx;
        this.idx = idx;
        this.writer = writer;
        this.couplekey = couplekey;
        this.myimg = myimg;
        this.otherimg = otherimg;
        this.content = content;
        this.date = date;
        this.images = images;
        this.form = form;
        this.heart = heart;
        this.comments = comments;
        this.script = script;
        this.lastcomment = lastcomment;
        this.writerOfLastcomment = writerOfLastcomment;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public int getStory_idx() {
        return story_idx;
    }

    public void setStory_idx(int story_idx) {
        this.story_idx = story_idx;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCouplekey() {
        return couplekey;
    }

    public void setCouplekey(String couplekey) {
        this.couplekey = couplekey;
    }

    public String getMyimg() {
        return myimg;
    }

    public void setMyimg(String myimg) {
        this.myimg = myimg;
    }

    public String getOtherimg() {
        return otherimg;
    }

    public void setOtherimg(String otherimg) {
        this.otherimg = otherimg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getScript() {
        return script;
    }

    public void setScript(int script) {
        this.script = script;
    }

    public String getLastcomment() {
        return lastcomment;
    }

    public void setLastcomment(String lastcomment) {
        this.lastcomment = lastcomment;
    }

    public String getWriterOfLastcomment() {
        return writerOfLastcomment;
    }

    public void setWriterOfLastcomment(String writerOfLastcomment) {
        this.writerOfLastcomment = writerOfLastcomment;
    }
}
