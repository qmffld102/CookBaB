package com.example.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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
    private Button plusimage;
    private LinearLayout littlelinearlayout;
    private Button srhbtn;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator_main);

        plusimage =(Button) findViewById(R.id.plusimage);
        linearLayout=(LinearLayout)findViewById(R.id.linearlayout);
        srhbtn = (Button)findViewById(R.id.srhbtn);
        search = (EditText)findViewById(R.id.search);

        mDatabase = FirebaseDatabase.getInstance();
        final String uid="hUeiODcSXrSEe1MJ9stKlAbcpcv2";
        mReference = mDatabase.getReference().child("user").child(uid).child("refrigerator").child("ingredient");
        storage=FirebaseStorage.getInstance("gs://cook-bab.appspot.com");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();
                int i=0;
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    final String filename =messageData.child("ingredientid").getValue().toString();
                    Log.e("##", filename);

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
                    //윗줄이 들어오는 사진 크긴데 저거 좀 조절해주라,,ㅎ 한 줄ㅇ 5개씩 들어가게 만들긴 했어
                    if(i%5==0){
                        littlelinearlayout = new LinearLayout(getApplicationContext());
                        littlelinearlayout.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.addView(littlelinearlayout);
                        littlelinearlayout.addView(imageView);
                    }
                    else{
                        littlelinearlayout.addView(imageView);
                    }
                    i++;
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
       search.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final String igname=search.getText().toString();
               if(igname.length()==0){
                   mReference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           linearLayout.removeAllViews();
                           int i=0;
                           for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                               final String filename =messageData.child("ingredientid").getValue().toString();

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
                               //윗줄이 들어오는 사진 크긴데 저거 좀 조절해주라,,ㅎ 한 줄ㅇ 5개씩 들어가게 만들긴 했어
                               if(i%5==0){
                                   littlelinearlayout = new LinearLayout(getApplicationContext());
                                   littlelinearlayout.setOrientation(LinearLayout.HORIZONTAL);
                                   linearLayout.addView(littlelinearlayout);
                                   littlelinearlayout.addView(imageView);
                               }
                               else{
                                   littlelinearlayout.addView(imageView);
                               }
                               i++;
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {
                       }
                   });
               }
               else{
                   mReference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           linearLayout.removeAllViews();
                           int i=0;
                           for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                               final String filename =messageData.child("ingredientid").getValue().toString();
                               Log.e("##", filename);

                               if(filename.equals(igname)) {
                                   ImageView imageView = new ImageView(getApplicationContext());
                                   //imageView.setMaxWidth(30);
                                   //imageView.setMaxHeight(30);
                                   StorageReference storageRef = storage.getReference().child("ingredient_photo/" + filename + ".JPG");
                                   Glide.with(RefrigeratorMain.this)
                                           .using(new FirebaseImageLoader())
                                           .load(storageRef)
                                           .into(imageView);
                                   imageView.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent intent = new Intent(RefrigeratorMain.this, IngredientDetail.class);
                                           intent.putExtra("filename", filename);
                                           startActivity(intent);
                                       }
                                   });
                                   imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                   //윗줄이 들어오는 사진 크긴데 저거 좀 조절해주라,,ㅎ 한 줄ㅇ 5개씩 들어가게 만들긴 했어
                                   if (i % 5 == 0) {
                                       littlelinearlayout = new LinearLayout(getApplicationContext());
                                       littlelinearlayout.setOrientation(LinearLayout.HORIZONTAL);
                                       linearLayout.addView(littlelinearlayout);
                                       littlelinearlayout.addView(imageView);
                                   } else {
                                       littlelinearlayout.addView(imageView);
                                   }
                                   i++;
                               }
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {
                       }
                   });
               }
           }
       });
    }

}
