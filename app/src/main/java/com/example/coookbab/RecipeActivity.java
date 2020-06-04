package com.example.coookbab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class RecipeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String userUrl="";
    private TextView tv_title, tv_ingredient, tv_recipe;
    private Button btn_youtube, btn_cook, btn_myrecipe;
    private DatabaseReference rDatabase;
    private String str;
    private ImageView cook_image;
    int i=0;
    private int my_recipe_tf=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();

        final int recipe_num = intent.getExtras().getInt("recipe_num");
        Log.e(this.getClass().getName(), String.valueOf(recipe_num));

        cook_image = findViewById(R.id.cook_image);
        tv_title = findViewById(R.id.tv_title);
        tv_ingredient=findViewById(R.id.tv_ingredient);
        tv_recipe = findViewById(R.id.tv_recipe);
        btn_youtube=findViewById(R.id.btn_youtube);
        btn_cook = findViewById(R.id.btn_cook);
        btn_myrecipe = findViewById(R.id.btn_myrecipe);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userUrl = user.getUid();
        rDatabase = FirebaseDatabase.getInstance().getReference();

        rDatabase.child("recipe").child(String.valueOf(recipe_num)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseStorage storage = FirebaseStorage.getInstance("gs://cook-bab.appspot.com/");
                StorageReference storageRef = storage.getReference();
                StorageReference recipeRef = storageRef.child("recipe_photo").child(String.valueOf(recipe_num)+".jpg");
                Glide.with(getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(recipeRef)
                        .into(cook_image);
                str = dataSnapshot.child("title").getValue().toString();
                tv_title.setText(str);
                str=dataSnapshot.child("ingredients").child("text").getValue().toString();
                tv_ingredient.setText(str);
                str=dataSnapshot.child("howto").getValue().toString();
                tv_recipe.setText("\n"+str);
                str=dataSnapshot.child("link").getValue().toString();
                final Uri uri =Uri.parse(str);
                final String i=dataSnapshot.child("need").getValue().toString();
                tv_ingredient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), RecipeIngredient.class);
                        intent.putExtra("recipe_num", String.valueOf(recipe_num));
                        intent.putExtra("i", i);
                        intent.putExtra("photo_num", Integer.parseInt(str));
                        startActivity(intent);
                    }
                });
                btn_youtube.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
                str=dataSnapshot.child("photo_num").child("total").getValue().toString();
                btn_cook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), CookActivity.class);
                        intent.putExtra("recipe_num", recipe_num);
                        intent.putExtra("photo_num", Integer.parseInt(str));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        rDatabase.child("user").child(userUrl).child("myrecipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot myData : dataSnapshot.getChildren()){
                    String myNum = myData.getValue().toString();
                    if(Integer.parseInt(myNum) == recipe_num){
                        my_recipe_tf=1;
                        btn_myrecipe.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.my_recipe_on));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_myrecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(my_recipe_tf==0){
                    btn_myrecipe.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.my_recipe_on));
                    my_recipe_tf=1;
                    rDatabase.child("user").child(userUrl).child("myrecipe").push().setValue(String.valueOf(recipe_num))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
                else if(my_recipe_tf==1){
                    btn_myrecipe.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.my_recipe_off));
                    my_recipe_tf=0;
                    rDatabase.child("user").child(userUrl).child("myrecipe").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(final DataSnapshot myData : dataSnapshot.getChildren()){
                                String myNum = myData.getValue().toString();
                                if(Integer.parseInt(myNum) == recipe_num){
                                    myData.getRef().removeValue();
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
