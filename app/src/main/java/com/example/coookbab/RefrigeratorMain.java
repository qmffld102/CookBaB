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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private LinearLayout linearLayout;
    private Button plusimage;
    private LinearLayout littlelinearlayout;
    private LinearLayout inglayout;
    private Button srhbtn;
    private EditText search;
    private String userUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator_main);

        plusimage =(Button) findViewById(R.id.plusimage);
        linearLayout=(LinearLayout)findViewById(R.id.linearlayout);
        srhbtn = (Button)findViewById(R.id.srhbtn);
        search = (EditText)findViewById(R.id.search);

        mDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userUrl = user.getUid();

        mReference = mDatabase.getReference().child("user").child(userUrl).child("refrigerator").child("ingredient");
        storage=FirebaseStorage.getInstance("gs://cook-bab.appspot.com");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();
                int i=0;
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    final String filename =messageData.child("ingredientid").getValue().toString();
                    final String ingname = messageData.child("name").getValue().toString();
                    Log.e("##", ingname);

                    ImageView imageView = new ImageView(getApplicationContext());
                    StorageReference storageRef = storage.getReference().child("ingredient_photo/"+filename+".png");
                    inglayout = new LinearLayout(getApplicationContext());
                    inglayout.setOrientation(LinearLayout.VERTICAL);
                    TextView ingn = new TextView(getApplicationContext());
                    ingn.setText(ingname);
                    Glide.with(RefrigeratorMain.this)
                            .using(new FirebaseImageLoader())
                            .load(storageRef)
                            .override(200, 200)
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
                    if(i%5==0){
                        littlelinearlayout = new LinearLayout(getApplicationContext());
                        littlelinearlayout.setOrientation(LinearLayout.HORIZONTAL);
                        linearLayout.addView(littlelinearlayout);
                        littlelinearlayout.addView(inglayout);
                        inglayout.addView(imageView);
                        inglayout.addView(ingn);
                    }
                    else{
                        littlelinearlayout.addView(inglayout);
                        inglayout.addView(imageView);
                        inglayout.addView(ingn);
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
       srhbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(RefrigeratorMain.this,"모든 재료를 보려면 0을 입력해 주세요",Toast.LENGTH_LONG).show();
               final String igname=search.getText().toString();
               if(igname.equals("0")){
                   mReference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           linearLayout.removeAllViews();
                           int i=0;
                           for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                               final String filename =messageData.child("ingredientid").getValue().toString();
                               final String ingname = messageData.child("name").getValue().toString();
                               inglayout = new LinearLayout(getApplicationContext());
                               inglayout.setOrientation(LinearLayout.VERTICAL);
                               TextView ingn = new TextView(getApplicationContext());
                               ingn.setText(ingname);

                               ImageView imageView = new ImageView(getApplicationContext());
                               StorageReference storageRef = storage.getReference().child("ingredient_photo/"+filename+".png");
                               Glide.with(RefrigeratorMain.this)
                                       .using(new FirebaseImageLoader())
                                       .load(storageRef)
                                       .override(200, 200)
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
                               if(i%5==0){
                                   littlelinearlayout = new LinearLayout(getApplicationContext());
                                   littlelinearlayout.setOrientation(LinearLayout.HORIZONTAL);
                                   linearLayout.addView(littlelinearlayout);
                                   littlelinearlayout.addView(inglayout);
                                   inglayout.addView(imageView);
                                   inglayout.addView(ingn);
                               }
                               else{
                                   littlelinearlayout.addView(inglayout);
                                   inglayout.addView(imageView);
                                   inglayout.addView(ingn);
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
                               final String ingname = messageData.child("name").getValue().toString();
                               inglayout = new LinearLayout(getApplicationContext());
                               inglayout.setOrientation(LinearLayout.VERTICAL);
                               TextView ingn = new TextView(getApplicationContext());
                               ingn.setText(ingname);


                               if(filename.equals(igname)) {
                                   ImageView imageView = new ImageView(getApplicationContext());
                                   StorageReference storageRef = storage.getReference().child("ingredient_photo/" + filename + ".png");
                                   Glide.with(RefrigeratorMain.this)
                                           .using(new FirebaseImageLoader())
                                           .load(storageRef)
                                           .override(200, 200)
                                           .into(imageView);
                                   imageView.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent intent = new Intent(RefrigeratorMain.this, IngredientDetail.class);
                                           intent.putExtra("filename", filename);
                                           startActivity(intent);
                                       }
                                   });
                                   imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));                                   if (i % 5 == 0) {
                                       littlelinearlayout = new LinearLayout(getApplicationContext());
                                       littlelinearlayout.setOrientation(LinearLayout.HORIZONTAL);
                                       linearLayout.addView(littlelinearlayout);
                                       littlelinearlayout.addView(inglayout);
                                       inglayout.addView(imageView);
                                       inglayout.addView(ingn);
                                   } else {
                                       littlelinearlayout.addView(inglayout);
                                       inglayout.addView(imageView);
                                       inglayout.addView(ingn);
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
