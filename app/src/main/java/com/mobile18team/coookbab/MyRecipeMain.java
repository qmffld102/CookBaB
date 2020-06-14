package com.mobile18team.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class MyRecipeMain extends AppCompatActivity {
    private DatabaseReference RcpListDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference rcpRef;
    private LinearLayout rcplinearlayout;
    private LinearLayout littlelayout;
    private FirebaseStorage storage;
    private EditText editText;
    private String userUrl = "";
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_main);
        button =(Button)findViewById(R.id.button);
        editText = (EditText)findViewById(R.id.editText);
        rcplinearlayout = findViewById(R.id.myrcplinearlayout);
        RcpListDatabase = FirebaseDatabase.getInstance().getReference();
        rcpRef = FirebaseDatabase.getInstance().getReference().child("recipe");
        storage=FirebaseStorage.getInstance("gs://cook-bab.appspot.com");
        final StorageReference storageRef = storage.getReference().child("recipe_photo");

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userUrl = user.getUid();

        final LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RcpListDatabase.child("user").child(userUrl).child("myrecipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rcplinearlayout.removeAllViews();
                for(final DataSnapshot recipeData : dataSnapshot.getChildren()){
                    final String rtnum = recipeData.getValue().toString();
                    final TextView tv_recipe = new TextView(getApplicationContext());
                    rcpRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            tv_recipe.setText(dataSnapshot.child(rtnum).child("title").getValue().toString());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    littlelayout = new LinearLayout(getApplicationContext());
                    littlelayout.setOrientation(LinearLayout.VERTICAL);
                    littlelayout.setGravity(Gravity.CENTER);

                    ImageView imageView =new ImageView(getApplicationContext());
                    int width = 1000;
                    int height = 1000;
                    imageView.setForegroundGravity(Gravity.TOP);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

                    imageView.setLayoutParams(params);
                    StorageReference storageReference = storageRef.child(rtnum+".jpg");
                    Glide.with(MyRecipeMain.this)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .into(imageView);
                    tv_recipe.setTextColor(Color.BLACK);
                    Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.namum);
                    tv_recipe.setTypeface(typeface);
                    tv_recipe.setTextSize(30);
                    tv_recipe.setGravity(Gravity.CENTER | Gravity.BOTTOM); //추가
                    tv_recipe.setHeight(200);
                    tv_recipe.setLayoutParams(layoutParams);
                    littlelayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                            intent.putExtra("recipe_num", Integer.parseInt(recipeData.getValue().toString()));
                            startActivity(intent);
                        }
                    });
                    TextView enter= new TextView(getApplicationContext());
                    enter.setText(" ");
                    enter.setTextSize(30);
                    View view = new View(getApplicationContext());
                    view.setBackgroundColor(Color.GRAY);
                    width = 1000;
                    height = 5;

                    params = new LinearLayout.LayoutParams(width, height);

                    view.setLayoutParams(params);
                    rcplinearlayout.addView(littlelayout);

                    littlelayout.addView(tv_recipe);
                    littlelayout.addView(imageView);
                    littlelayout.addView(enter);
                    littlelayout.addView(view);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //검색기능시작
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String search = editText.getText().toString();
                if(search.length()==0){
                    RcpListDatabase.child("user").child(userUrl).child("myrecipe").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            rcplinearlayout.removeAllViews();
                            for(final DataSnapshot recipeData : dataSnapshot.getChildren()){
                                final String rtnum = recipeData.getValue().toString();
                                final TextView tv_recipe = new TextView(getApplicationContext());
                                rcpRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        tv_recipe.setText(dataSnapshot.child(rtnum).child("title").getValue().toString());
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                littlelayout = new LinearLayout(getApplicationContext());
                                littlelayout.setOrientation(LinearLayout.VERTICAL);
                                littlelayout.setGravity(Gravity.CENTER);

                                ImageView imageView =new ImageView(getApplicationContext());
                                int width = 1000;
                                int height = 1000;
                                imageView.setForegroundGravity(Gravity.TOP);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

                                imageView.setLayoutParams(params);
                                StorageReference storageReference = storageRef.child(rtnum+".jpg");
                                Glide.with(MyRecipeMain.this)
                                        .using(new FirebaseImageLoader())
                                        .load(storageReference)
                                        .into(imageView);
                                tv_recipe.setTextColor(Color.BLACK);
                                Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.namum);
                                tv_recipe.setTypeface(typeface);
                                tv_recipe.setTextSize(30);
                                tv_recipe.setGravity(Gravity.CENTER | Gravity.BOTTOM); //추가
                                tv_recipe.setHeight(200);
                                tv_recipe.setLayoutParams(layoutParams);
                                littlelayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                                        intent.putExtra("recipe_num", Integer.parseInt(recipeData.getValue().toString()));
                                        startActivity(intent);
                                    }
                                });
                                TextView enter= new TextView(getApplicationContext());
                                enter.setText(" ");
                                enter.setTextSize(30);
                                View view = new View(getApplicationContext());
                                view.setBackgroundColor(Color.GRAY);
                                width = 1000;
                                height = 5;

                                params = new LinearLayout.LayoutParams(width, height);

                                view.setLayoutParams(params);
                                rcplinearlayout.addView(littlelayout);
                                littlelayout.addView(tv_recipe);
                                littlelayout.addView(imageView);
                                littlelayout.addView(enter);
                                littlelayout.addView(view);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else {
                    RcpListDatabase.child("user").child(userUrl).child("myrecipe").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            rcplinearlayout.removeAllViews();
                            for (final DataSnapshot recipeData : dataSnapshot.getChildren()) {
                                final String rtnum = recipeData.getValue().toString();
                                final TextView tv_recipe = new TextView(getApplicationContext());
                                rcpRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dts) {
                                        String name = dts.child(rtnum).child("title").getValue().toString();
                                        if (name.contains(search)) {
                                            final String rtnum = recipeData.getValue().toString();
                                            final TextView tv_recipe = new TextView(getApplicationContext());
                                            rcpRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    tv_recipe.setText(dataSnapshot.child(rtnum).child("title").getValue().toString());
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                            littlelayout = new LinearLayout(getApplicationContext());
                                            littlelayout.setOrientation(LinearLayout.VERTICAL);
                                            littlelayout.setGravity(Gravity.CENTER);

                                            ImageView imageView =new ImageView(getApplicationContext());
                                            int width = 1000;
                                            int height = 1000;
                                            imageView.setForegroundGravity(Gravity.TOP);

                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

                                            imageView.setLayoutParams(params);
                                            StorageReference storageReference = storageRef.child(rtnum+".jpg");
                                            Glide.with(MyRecipeMain.this)
                                                    .using(new FirebaseImageLoader())
                                                    .load(storageReference)
                                                    .into(imageView);
                                            tv_recipe.setTextColor(Color.BLACK);
                                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.namum);
                                            tv_recipe.setTypeface(typeface);
                                            tv_recipe.setTextSize(30);
                                            tv_recipe.setGravity(Gravity.CENTER|Gravity.BOTTOM); //추가
                                            tv_recipe.setHeight(200);
                                            tv_recipe.setLayoutParams(layoutParams);
                                            littlelayout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                                                    intent.putExtra("recipe_num", Integer.parseInt(recipeData.getValue().toString()));
                                                    startActivity(intent);
                                                }
                                            });
                                            TextView enter= new TextView(getApplicationContext());
                                            enter.setText(" ");
                                            enter.setTextSize(30);
                                            View view = new View(getApplicationContext());
                                            view.setBackgroundColor(Color.GRAY);
                                            width = 1000;
                                            height = 5;

                                            params = new LinearLayout.LayoutParams(width, height);

                                            view.setLayoutParams(params);
                                            rcplinearlayout.addView(littlelayout);
                                            littlelayout.addView(tv_recipe);
                                            littlelayout.addView(imageView);
                                            littlelayout.addView(enter);
                                            littlelayout.addView(view);
                                        } else {
                                            int many = Integer.parseInt(dts.child(rtnum).child("need").getValue().toString());
                                            for (int i = 1; i <= many; i++) {
                                                String ingname = dts.child(rtnum).child("ingredients").child(String.valueOf(i)).child("name").getValue().toString();
                                                if (search.equals(ingname)) {
                                                    final String rtnum = recipeData.getValue().toString();
                                                    final TextView tv_recipe = new TextView(getApplicationContext());
                                                    rcpRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            tv_recipe.setText(dataSnapshot.child(rtnum).child("title").getValue().toString());
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        }
                                                    });
                                                    littlelayout = new LinearLayout(getApplicationContext());
                                                    littlelayout.setOrientation(LinearLayout.VERTICAL);
                                                    littlelayout.setGravity(Gravity.CENTER);
                                                    rcplinearlayout.addView(littlelayout);

                                                    ImageView imageView =new ImageView(getApplicationContext());
                                                    int width = 1000;
                                                    int height = 1000;
                                                    imageView.setForegroundGravity(Gravity.TOP);

                                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

                                                    imageView.setLayoutParams(params);
                                                    StorageReference storageReference = storageRef.child(rtnum+".jpg");
                                                    Glide.with(MyRecipeMain.this)
                                                            .using(new FirebaseImageLoader())
                                                            .load(storageReference)
                                                            .into(imageView);
                                                    tv_recipe.setTextColor(Color.BLACK);
                                                    Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.namum);
                                                    tv_recipe.setTypeface(typeface);
                                                    tv_recipe.setTextSize(30);
                                                    tv_recipe.setGravity(Gravity.CENTER|Gravity.BOTTOM); //추가
                                                    tv_recipe.setHeight(200);
                                                    tv_recipe.setLayoutParams(layoutParams);
                                                    littlelayout.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                                                            intent.putExtra("recipe_num", Integer.parseInt(recipeData.getValue().toString()));
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    if(littlelayout.getParent() != null){
                                                        ((ViewGroup)littlelayout.getParent()).removeView(littlelayout);
                                                    }
                                                    TextView enter= new TextView(getApplicationContext());
                                                    enter.setText(" ");
                                                    enter.setTextSize(30);
                                                    View view = new View(getApplicationContext());
                                                    view.setBackgroundColor(Color.GRAY);
                                                    width = 1000;
                                                    height = 5;

                                                    params = new LinearLayout.LayoutParams(width, height);

                                                    view.setLayoutParams(params);
                                                    rcplinearlayout.addView(littlelayout);

                                                    littlelayout.addView(tv_recipe);
                                                    littlelayout.addView(imageView);
                                                    littlelayout.addView(enter);
                                                    littlelayout.addView(view);
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
            }
        });
        //검색기능 끝
    }
}
