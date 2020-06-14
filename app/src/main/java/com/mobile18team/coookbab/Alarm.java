package com.mobile18team.coookbab;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Alarm extends BroadcastReceiver {
    private FirebaseDatabase aDatabase;
    private DatabaseReference aReference;
    private FirebaseAuth aAuth;
    private String userUrl="";
    private String channelId = "channel";
    private String channelName = "channelName";
    private String date;
    private String [] delete_list = new String[500];
    private String [] tomorrow_list = new String[500];
    int d=0;
    int t=0;
    PowerManager pm;
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e("##", "알람!");
        Date currentTime = (Date) Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        date = yearFormat.format(currentTime)+monthFormat.format(currentTime)+dateFormat.format(currentTime);

        pm=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
        final boolean screen = pm.isScreenOn();

        aDatabase = FirebaseDatabase.getInstance();

        aAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = aAuth.getCurrentUser();
        userUrl = user.getUid();

        aReference = aDatabase.getReference().child("user").child(userUrl).child("refrigerator").child("ingredient");

        aReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot alimData : dataSnapshot.getChildren()){
                    if(alimData.child("life").getValue(String.class)!=null){
                        String targetdate = alimData.child("life").getValue().toString();
                        if(Integer.parseInt(targetdate)<Integer.parseInt(date)){//오늘 이전이 유통기한
                            delete_list[d]=alimData.child("name").getValue().toString();
                            d++;
                            aReference.child(alimData.getKey()).removeValue();//지우기
                        }
                        if(Integer.parseInt(targetdate)==Integer.parseInt(date)+1){//내일이 유통기한
                            tomorrow_list[t]=alimData.child("name").getValue().toString();
                            t++;
                        }
                    }
                }
                Log.e("##", "show"+d+"/"+t);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); //오레오 대응
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
                    notificationManager.createNotificationChannel(notificationChannel);
                }
                String delete="";
                for(int i=0; i<d; i++){
                    if(i<d-1){
                        delete += delete_list[i]+", ";
                    }
                    if(i==d-1 && !screen) {
                        Log.e("##", "delete에 들어왔습니다.");
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), channelId);
                        Intent notificationIntent = new Intent(context.getApplicationContext(), MainActivity.class); // 알림 클릭 시 이동할 액티비티 지정
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // FLAG_UPDATE_CURRENT : 이미 생성된 PendingIntent가 존재하면 해당 Intent의 Extra Data만 변경한다.
                        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentTitle("[쿸밥]재료의 유통기한이 지났습니다") //제목
                                .setContentText(delete+delete_list[i]+"의 유통기한이 지나 자동으로 삭제합니다!") //내용
                                .setDefaults(Notification.DEFAULT_ALL) //알림 설정(사운드, 진동)
                                .setAutoCancel(true) //터치 시 자동으로 삭제할 지 여부
                                .setPriority(NotificationCompat.PRIORITY_HIGH) // 알림의 중요도 .setSmallIcon(R.drawable.ic_sun)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentIntent(pendingIntent);
                        notificationManager.notify(0, builder.build());
                    }

                }
                String tomorrow ="";

                for(int i=0; i<t; i++){
                    if(i<t-1){
                        Log.e("##", tomorrow_list[i]);
                        tomorrow += tomorrow_list[i]+", ";
                        Log.e("##", "tomorrow:" +tomorrow);
                    }
                    if(i==t-1 && !screen){
                        Log.e("##", tomorrow);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), channelId);
                        Intent notificationIntent = new Intent(context.getApplicationContext(), MainActivity.class); // 알림 클릭 시 이동할 액티비티 지정
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // FLAG_UPDATE_CURRENT : 이미 생성된 PendingIntent가 존재하면 해당 Intent의 Extra Data만 변경한다.
                        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentTitle("[쿸밥]재료의 유통기한이 다되어 갑니다") //제목
                                .setContentText(tomorrow+tomorrow_list[i]+"의 유통기한이 하루 남았습니다!") //내용
                                .setDefaults(Notification.DEFAULT_ALL) //알림 설정(사운드, 진동)
                                .setAutoCancel(true) //터치 시 자동으로 삭제할 지 여부
                                .setPriority(NotificationCompat.PRIORITY_HIGH) // 알림의 중요도 .setSmallIcon(R.drawable.ic_sun)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentIntent(pendingIntent);
                        notificationManager.notify(1, builder.build());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
