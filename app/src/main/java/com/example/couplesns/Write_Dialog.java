package com.example.couplesns;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Write_Dialog {
    private Context context;
    Dialog dialog;
    public Write_Dialog(Context context) {
        this.context = context;
    }

    public void callFunction(){

        dialog = new Dialog(context); //다이얼로그 클래스 생성
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //액티비티 타이틀바 제거
        dialog.setContentView(R.layout.write_dialog); // 레이아웃과 연결
        dialog.show(); // 커스텀 다이얼로그 노출

        LinearLayout line1 = (LinearLayout)dialog.findViewById(R.id.line1);
        LinearLayout line2 = (LinearLayout)dialog.findViewById(R.id.line2);
        ImageView Imageview_dialog_out = (ImageView)dialog.findViewById(R.id.Imageview_dialog_out);

        //일반글쓰기
        line1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,WriteStoryActivitiy.class);
                context.startActivity(intent);
            }
        });

        //익명글쓰기
        line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,WriteSecretStoryActivity.class);
                context.startActivity(intent);
            }
        });

        Imageview_dialog_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

}
