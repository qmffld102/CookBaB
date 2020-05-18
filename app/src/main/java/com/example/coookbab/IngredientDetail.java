package com.example.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class IngredientDetail extends AppCompatActivity {
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase;
    private FirebaseStorage storage;
    private ImageView imageView;
    private Button savebtn;
    private Button delbtn;
    private EditText ingredientnum;
    private EditText ingredientlife;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_detail);

        storage= FirebaseStorage.getInstance("gs://cook-bab.appspot.com");
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("user").child("refrigerator").child("ingredient");

        imageView = (ImageView)findViewById(R.id.imageView);
        savebtn = (Button)findViewById(R.id.savebtn);
        delbtn = (Button)findViewById(R.id.delbtn);
        ingredientlife =(EditText)findViewById(R.id.ingredientlife);
        ingredientnum = (EditText)findViewById(R.id.ingredientnum);


        Intent intent = getIntent();
        final String filename =intent.getExtras().getString("filename");
        IngredientData temp=null;//temp에 filename에 해당하는 db가져와야됨

        StorageReference storageRef = storage.getReference().child("ingredient_photo/"+filename+".JPG");
        Glide.with(IngredientDetail.this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(imageView);

        ingredientnum.setText(temp.getNum());
        ingredientlife.setText(temp.getLife());

        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(IngredientDetail.this, RefrigeratorMain.class);

                startActivity(intent1);
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(IngredientDetail.this, RefrigeratorMain.class);

                startActivity(intent1);
            }
        });
    }
}
