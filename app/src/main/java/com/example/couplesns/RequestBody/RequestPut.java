package com.example.couplesns.RequestBody;

import java.util.HashMap;
import java.util.Objects;

public class RequestPut {
    public final int id;
    public final int userId;
    public final String title;
    public final String body;
    public RequestPut(HashMap<String, Object> parameters) {
        this.id = (int) parameters.get("id");
        this.userId = (int) parameters.get("userId");
        this.title = (String) parameters.get("title");
        this.body = (String) parameters.get("body");
    }
//[ userId : 100,
//    id : 101,
//    title : this is title,
//    body : this is body ]
    //형식으로 변환해준다.




}
