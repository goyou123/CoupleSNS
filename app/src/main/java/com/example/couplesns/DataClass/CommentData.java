package com.example.couplesns.DataClass;

public class CommentData {
    int idx;
    int storyidx;
    int replys;
    String couplekey;
    String writeremail;
    String writer;
    String writerimg;
    String memo;
    String commentdate;

    public CommentData(int idx, int storyidx, int replys, String couplekey, String writeremail,
                       String writer, String writerimg, String memo, String commentdate) {
        this.idx = idx;
        this.storyidx = storyidx;
        this.replys = replys;
        this.couplekey = couplekey;
        this.writeremail = writeremail;
        this.writer = writer;
        this.writerimg = writerimg;
        this.memo = memo;
        this.commentdate = commentdate;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getStoryidx() {
        return storyidx;
    }

    public void setStoryidx(int storyidx) {
        this.storyidx = storyidx;
    }

    public int getReplys() {
        return replys;
    }

    public void setReplys(int replys) {
        this.replys = replys;
    }

    public String getCouplekey() {
        return couplekey;
    }

    public void setCouplekey(String couplekey) {
        this.couplekey = couplekey;
    }

    public String getWriteremail() {
        return writeremail;
    }

    public void setWriteremail(String writeremail) {
        this.writeremail = writeremail;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getWriterimg() {
        return writerimg;
    }

    public void setWriterimg(String writerimg) {
        this.writerimg = writerimg;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCommentdate() {
        return commentdate;
    }

    public void setCommentdate(String commentdate) {
        this.commentdate = commentdate;
    }
}
