package com.example.couplesns.RetrofitJava;

public interface RetroCallback<T>{ //통신마다 다른 output을 받기 떄문에 Generic으로 설정

    //레트로핏 통신은 백그라운드 스레드에서 동작함.
    // 실행된 결과값을 UI 스레드에 사용하기 위해서는 CAllBaCK함수가 필요함
    void onError(Throwable t);

    void onSuccess(int code, T receivedData);

    void onFailure(int code);
}
