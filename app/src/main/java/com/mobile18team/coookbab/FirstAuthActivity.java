package com.mobile18team.coookbab;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class FirstAuthActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_auth);

        if(SavedSharedPrefrence.getUserName(FirstAuthActivity.this).length() == 0) {
            // call Login Activity
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            // Call Next Activity
            intent = new Intent(FirstAuthActivity.this, MainActivity.class);
            intent.putExtra("STD_NUM", SavedSharedPrefrence.getUserName(this).toString());
            startActivity(intent);
            this.finish();
        }
    }
}