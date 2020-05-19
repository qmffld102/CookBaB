package com.example.coookbab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CookActivity extends AppCompatActivity {

    int i=1;
    int recipe_num;
    ImageView iv_cook;
    int photo_num;
    FirebaseStorage storage;
    StorageReference storageReference, cookstorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);

        iv_cook=findViewById(R.id.iv_cook);

        storage = FirebaseStorage.getInstance("gs://cook-bab.appspot.com/");
        storageReference = storage.getReference();

        Toast.makeText(CookActivity.this, "화면을 가로로 놓고 터치해주세요!", Toast.LENGTH_LONG).show();
        Intent intent = getIntent();

        recipe_num = intent.getExtras().getInt("recipe_num");
        photo_num = intent.getExtras().getInt("photo_num");
        Log.e(this.getClass().getName(), String.valueOf(photo_num));


    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        if(action==MotionEvent.ACTION_DOWN&& i<=photo_num){
            String photo = String.valueOf(recipe_num)+"-"+String.valueOf(i)+".png";
            Log.e(this.getClass().getName(), photo);
            cookstorage = storageReference.child(String.valueOf(recipe_num)).child(photo);
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(cookstorage)
                    .into(iv_cook);
            i++;
        }
        return super.onTouchEvent(event);
    }
}
