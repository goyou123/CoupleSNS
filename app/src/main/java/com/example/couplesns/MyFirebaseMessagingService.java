package com.example.couplesns;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    static final String TAG = "서비스에프씨엠";
    String title,msg,roomidx;




    public MyFirebaseMessagingService() {
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*새로운 메세지를 받았을 때 호출되는 메소드 (포그라운드)
         * 클라우드 서버에서 보내오는 메세지는 FirebaseMessagingService에서 받고
         * onMessageReceived() 메서드가 자동으로 호출된다.
         * 여기서 받아온 메세지를 됨*/
        //푸시 메시지는 FirebaseMessagingService 클래스를 상속받아 onMessageReceived 메소드를 오버라이딩 해서 커스텀하게 처리한다.
        //onMessageReceived 메소드에서 메시지 알림 처리를 하면 앱이 포그라운드, 백그라운드 상관없이 푸시 메시지가 도착한다.




//        if(remoteMessage.getNotification() != null){
//
////            String title1 = remoteMessage.getData().get("title");
////            String body1 = remoteMessage.getData().get("body");
////            String roomidx = remoteMessage.getData().get("room_idx");
//
//            title = remoteMessage.getNotification().getTitle();
//            msg = remoteMessage.getNotification().getBody();
//            roomID =remoteMessage.getData().get("room_idx");
//
//            Log.d(TAG, "romote_message: "+title1+ " / "+body1+ " / " +roomidx);
//            Log.d(TAG, "title: "+title);
//            Log.d(TAG, "msg: "+msg);
//            Log.d(TAG, "roomID: "+roomID);
//        }


       String chat_roomid = remoteMessage.getData().get("room_idx");
//        roomidx = remoteMessage.getData().get("room_idx");
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("fcm_roomidx",chat_roomid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //노피티케이션 눌렀을때
//        PendingIntent contentIntent = PendingIntent.getActivity(this,0
//                ,intent,PendingIntent.FLAG_ONE_SHOT); //원래는 0
        PendingIntent contentIntent = PendingIntent.getActivity(this,0
                ,new Intent(intent),PendingIntent.FLAG_ONE_SHOT); //원래는 0


        /*FCM메세지를 받아서 내용은 뽑은 후 roomID를 채팅리스트 액티비티로 보낸다
        * 브로드캐스트를 활용하고, 보내는 룸ID로 채팅방을 업데이트 해준다.*/
        if(remoteMessage.getData() != null) {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "DATA: " + remoteMessage.getData());
            title = remoteMessage.getData().get("title");
            msg = remoteMessage.getData().get("body");
            roomidx = remoteMessage.getData().get("room_idx");


            Log.d(TAG, "romote_message: " + title + " / " + msg + " / " + roomidx);


            //채팅리스트 리사이클러뷰를 업데이트 해주는 메소드(브로드캐스트)
            String UPDATE = "UPDATE";
            sendmsg(UPDATE,roomidx);
        }





        //노티피케이션 설정
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        // 오레오 이상 버전 채널 추가
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.d(TAG, "onMessageReceived에서  오레오 이상 버전 코드 실행되나");
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
//
//            // Configure the notification channel.
//            notificationChannel.setDescription("Channel description");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//            notificationManager.createNotificationChannel(notificationChannel); //채널생성
//
//            builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());


//====================================================================
            String channelId = "default_channel_id";
            String channelDescription = "Default Channel";
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelId, channelDescription, importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            builder = new NotificationCompat.Builder(this, channelId);



        }else{
            Log.d(TAG, "onMessageReceived에서  오레오 해당안됨");
            builder = new NotificationCompat.Builder(getApplicationContext());



        }


        //기존
//        NotificationCompat.Builder
//                builder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher)
        builder = builder
                .setContentTitle(title) //푸시알림떴을때의 제목
                .setContentText(msg) // 내용
                .setAutoCancel(true) //자동닫기
                .setSmallIcon(R.mipmap.ic_launcher) //이미지 아이콘 필수로 들어가야함
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //알림소리
                .setVibrate(new long[]{1,1000}).setContentIntent(contentIntent)
               ; //1초동안 진동 울려라
        Log.d(TAG, "onMessageReceived에서 builder생성?");

        notificationManager.notify(0,builder.build());

//        builder.setContentIntent(contentIntent); //아까만든 팬딩인텐트







        Log.d(TAG, "onMessageReceived() 호출종료 ");



    /*청*/
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            //오레오 이후 버전에서는 알림 채널이 지정되어야한다
//            String channelName = "Channel Name";
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(channel);
//        }
//        //[4] Builder 객체의 build()를 호출하면 Notification 객체가 생성된다
//        //[5] NotificationManager의 notify() 메서드를 호출하면서 이 Notification 객체를 파라미터로 전달하면
//        //      알람이 띄워진다.
//        notificationManager.notify(0, builder.build());




    }






    @Override
    public void onNewToken(String token){
        /* 새로운 토큰을 확인했을 때 호출되는 메서드
         * - 이 앱이 Firebase서버에 등록되었을때 호출됨
         * - 파라미터의 등록정보는 이 앱의 등록id를 의미함*/
        // 새 토큰이 생성될 때마다 onNewToken() 함수가 호출됨. 토큰은 다음의 이유로 변경될 수 있음
        // 1. 앱에서 인스턴스 ID 삭제
        // 2. 새 기기에서 앱 복원
        // 3. 사용자가 앱 삭제 / 재설치
        // 4. 사용자가 앱 데이터 소거
        super.onNewToken(token);
        Log.d(TAG, "onNewToken 호출됨: "+token);

    }


    public void sendmsg(String update,String roomID) {
        Intent intent = new Intent("MoveServiceFilter");
        intent.putExtra("UPDATE",update);
        intent.putExtra("roomID",roomID);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d(TAG, "sendmsg: 채팅방리스트업데이트 메소드 실행");
    }

}
