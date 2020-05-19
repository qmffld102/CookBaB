package com.example.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RefrigeratorMain extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseStorage storage;
    private LinearLayout linearLayout;
    private LinearLayout linearLayoutn;
    private ImageView plusimage;
    private ImageView imageViewn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator_main);

        plusimage =(ImageView)findViewById(R.id.imageView);
        linearLayout=(LinearLayout)findViewById(R.id.linearlayout);
        linearLayoutn=(LinearLayout)findViewById(R.id.linearlayoutn);
        imageViewn = (ImageView)findViewById(R.id.imageViewn);

        mDatabase = FirebaseDatabase.getInstance();
        String uid="hUeiODcSXrSEe1MJ9stKIAbcpcv2";
        mReference = mDatabase.getReference("user").child("uid").child("refrigerator");
        storage=FirebaseStorage.getInstance("gs://cook-bab.appspot.com");
        final StorageReference storageplus = storage.getReference().child("ingredient_photo").child("plus.jpg");

        mReference.child("nengjang").child("ingredient").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //linearLayout.removeAllViews();

                Glide.with(RefrigeratorMain.this)
                        .using(new FirebaseImageLoader())
                        .load(storageplus)
                        .into(plusimage);
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    IngredientData temp = messageData.getValue(IngredientData.class);
                    final String filename=String.valueOf(temp.getIngredientid());
                    Log.e("###",filename);

                    ImageView imageView = new ImageView(getApplicationContext());
                    StorageReference storageRef = storage.getReference().child("ingredient_photo/"+filename+".JPG");
                    Glide.with(RefrigeratorMain.this)
                            .using(new FirebaseImageLoader())
                            .load(storageRef)
                            .into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(RefrigeratorMain.this,IngredientDetail.class);
                            intent.putExtra("filename",filename);
                            startActivity(intent);
                        }
                    });
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayout.addView(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mReference.child("nengdong").child("ingredient").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //linearLayout.removeAllViews();

                Glide.with(RefrigeratorMain.this)
                        .using(new FirebaseImageLoader())
                        .load(storageplus)
                        .into(imageViewn);

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    IngredientData temp = messageData.getValue(IngredientData.class);
                    final String filename=String.valueOf(temp.getIngredientid());

                    ImageView imageView = new ImageView(getApplicationContext());
                    StorageReference storageRef = storage.getReference().child("ingredient_photo/"+filename+".JPG");
                    Glide.with(RefrigeratorMain.this)
                            .using(new FirebaseImageLoader())
                            .load(storageRef)
                            .into(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(RefrigeratorMain.this,IngredientDetail.class);
                            intent.putExtra("filename",filename);
                            startActivity(intent);
                        }
                    });
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    linearLayoutn.addView(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       plusimage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(RefrigeratorMain.this, AddIngredient.class);
               startActivity(intent);
           }
       });
    }

}
