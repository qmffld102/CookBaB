package com.example.coookbab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends TabActivity {
    private AlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent al_intent = new Intent(this, Alarm.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, al_intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        // 지정한 시간에 매일 알림
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pIntent);

        //첫번째 탭
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;

        ImageView tabwidget01 = new ImageView(this);
        tabwidget01.setImageResource(R.drawable.tab_01);
        ImageView tabwidget02 = new ImageView(this);
        tabwidget02.setImageResource(R.drawable.tab_02);
        ImageView tabwidget03 = new ImageView(this);
        tabwidget03.setImageResource(R.drawable.tab_03);

        Intent intent = new Intent().setClass(this, RecipeMain.class);
        spec = tabHost.newTabSpec("recipe").setIndicator(tabwidget01).setContent(intent);
        tabHost.addTab(spec);

        //두번째 탭
        intent = new Intent().setClass(this, RefrigeratorMain.class);
        spec = tabHost.newTabSpec("refri").setIndicator(tabwidget02).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);

        //세번째 탭
        intent = new Intent().setClass(this, MyRecipeMain.class);
        spec = tabHost.newTabSpec("my").setIndicator(tabwidget03).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;

        tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = (screenHeight * 15) / 200;
        tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = (screenHeight * 15) / 200;
        tabHost.getTabWidget().getChildAt(2).getLayoutParams().height = (screenHeight * 15) / 200;

    }
}
