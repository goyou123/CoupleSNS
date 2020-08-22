package com.example.couplesns.DataClass;

public class Result_login {
    String serverResult;
    String couplekey;

    public Result_login(String serverResult, String couplekey) {
        this.serverResult = serverResult;
        this.couplekey = couplekey;
    }

    public String getServerResult() {
        return serverResult;
    }

    public void setServerResult(String serverResult) {
        this.serverResult = serverResult;
    }

    public String getCouplekey() {
        return couplekey;
    }

    public void setCouplekey(String couplekey) {
        this.couplekey = couplekey;
    }
}
