package com.example.coookbab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;


public class MainActivity extends TabActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //첫번째 탭
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;

        Intent intent = new Intent().setClass(this, RecipeMain.class);
        spec = tabHost.newTabSpec("recipe").setIndicator("레시피").setContent(intent);
        tabHost.addTab(spec);

        //두번째 탭
        intent = new Intent().setClass(this, RefrigeratorMain.class);
        spec = tabHost.newTabSpec("refri").setIndicator("냉장고").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);

        //세번째 탭
        intent = new Intent().setClass(this, MyRecipeMain.class);
        spec = tabHost.newTabSpec("my").setIndicator("내 레시피").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
}
