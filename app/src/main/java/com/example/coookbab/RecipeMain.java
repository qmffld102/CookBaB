package com.example.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
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

public class RecipeMain extends AppCompatActivity {
    private DatabaseReference RcpListDatabase;
    private FirebaseAuth mAuth;
    private LinearLayout rcplinearlayout;
    private FirebaseStorage storage;
    private DatabaseReference rcpRef;
    private EditText editText;
    private Button button;
    private String userUrl="";
    private LinearLayout littlelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main);

        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        rcplinearlayout = findViewById(R.id.rcplinearlayout);
        RcpListDatabase = FirebaseDatabase.getInstance().getReference();
        rcpRef = FirebaseDatabase.getInstance().getReference().child("recipe");
        storage=FirebaseStorage.getInstance("gs://cook-bab.appspot.com");
        final StorageReference storageRef = storage.getReference().child("recipe_photo");

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userUrl = user.getUid();

        rcplinearlayout.removeAllViews();
        final LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RcpListDatabase.child("recipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot recipeData : dataSnapshot.getChildren()){
                    littlelayout = new LinearLayout(getApplicationContext());
                    littlelayout.setOrientation(LinearLayout.VERTICAL);
                    littlelayout.setGravity(Gravity.CENTER);
                    rcplinearlayout.addView(littlelayout);
                    final String rcpnum=recipeData.getKey();

                    ImageView imageView =new ImageView(getApplicationContext());
                    int width = 1000;
                    int height = 1000;
                    imageView.setForegroundGravity(Gravity.TOP);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

                    imageView.setLayoutParams(params);
                    StorageReference storageReference = storageRef.child(rcpnum+".jpg");
                    Glide.with(RecipeMain.this)
                            .using(new FirebaseImageLoader())
                            .load(storageReference)
                            .into(imageView);
                    String rt = recipeData.child("title").getValue().toString();
                    TextView tv_recipe = new TextView(getApplicationContext());
                    tv_recipe.setTextColor(Color.BLACK);
                    Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.namum);
                    tv_recipe.setTypeface(typeface);
                    tv_recipe.setTextSize(30);
                    tv_recipe.setGravity(Gravity.CENTER|Gravity.BOTTOM); //추가
                    tv_recipe.setHeight(200);
                    tv_recipe.setText(rt);
                    tv_recipe.setLayoutParams(layoutParams);
                    littlelayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                            intent.putExtra("recipe_num", Integer.parseInt(rcpnum));
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
        //여기 검색기능
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String search = editText.getText().toString();
                if(search.equals("0")){
                    rcplinearlayout.removeAllViews();
                    RcpListDatabase.child("recipe").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(final DataSnapshot recipeData : dataSnapshot.getChildren()){
                                littlelayout = new LinearLayout(getApplicationContext());
                                littlelayout.setOrientation(LinearLayout.VERTICAL);
                                littlelayout.setGravity(Gravity.CENTER);
                                rcplinearlayout.addView(littlelayout);
                                final String rcpnum=recipeData.getKey();

                                ImageView imageView =new ImageView(getApplicationContext());
                                int width = 1000;
                                int height = 1000;
                                imageView.setForegroundGravity(Gravity.TOP);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

                                imageView.setLayoutParams(params);
                                StorageReference storageReference = storageRef.child(rcpnum+".jpg");
                                Glide.with(RecipeMain.this)
                                        .using(new FirebaseImageLoader())
                                        .load(storageReference)
                                        .into(imageView);
                                String rt = recipeData.child("title").getValue().toString();
                                TextView tv_recipe = new TextView(getApplicationContext());
                                tv_recipe.setTextColor(Color.BLACK);
                                Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.namum);
                                tv_recipe.setTypeface(typeface);
                                tv_recipe.setTextSize(30);
                                tv_recipe.setHeight(200);
                                tv_recipe.setGravity(Gravity.CENTER|Gravity.BOTTOM); //추가
                                tv_recipe.setText(rt);
                                tv_recipe.setLayoutParams(layoutParams);
                                littlelayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                                        intent.putExtra("recipe_num", Integer.parseInt(rcpnum));
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
                    RcpListDatabase.child("recipe").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            rcplinearlayout.removeAllViews();
                            for (final DataSnapshot recipeData : dataSnapshot.getChildren()) {
                                final String rtnum = recipeData.child("rcpnum").getValue().toString();
                                Log.e("#", rtnum);
                                final TextView tv_recipe = new TextView(getApplicationContext());
                                String name = recipeData.child("title").getValue().toString();
                                if (name.contains(search)) {
                                    littlelayout = new LinearLayout(getApplicationContext());
                                    littlelayout.setOrientation(LinearLayout.VERTICAL);
                                    littlelayout.setGravity(Gravity.CENTER);
                                    rcplinearlayout.addView(littlelayout);
                                    final String rcpnum=recipeData.getKey();

                                    ImageView imageView =new ImageView(getApplicationContext());
                                    int width = 1000;
                                    int height = 1000;
                                    imageView.setForegroundGravity(Gravity.TOP);

                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

                                    imageView.setLayoutParams(params);
                                    StorageReference storageReference = storageRef.child(rcpnum+".jpg");
                                    Glide.with(RecipeMain.this)
                                            .using(new FirebaseImageLoader())
                                            .load(storageReference)
                                            .into(imageView);
                                    String rt = recipeData.child("title").getValue().toString();
                                    tv_recipe.setTextColor(Color.BLACK);
                                    Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.namum);
                                    tv_recipe.setTypeface(typeface);
                                    tv_recipe.setTextSize(30);
                                    tv_recipe.setGravity(Gravity.CENTER|Gravity.BOTTOM); //추가
                                    tv_recipe.setHeight(200);
                                    tv_recipe.setText(rt);
                                    tv_recipe.setLayoutParams(layoutParams);
                                    littlelayout.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                                            intent.putExtra("recipe_num", Integer.parseInt(rcpnum));
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
                                    littlelayout.addView(tv_recipe);
                                    littlelayout.addView(imageView);
                                    littlelayout.addView(enter);
                                    littlelayout.addView(view);

                                } else {
                                    int many = Integer.parseInt(recipeData.child("need").getValue().toString());
                                    for (int i = 1; i <= many; i++) {
                                        String ingname = recipeData.child("ingredients").child(String.valueOf(i)).child("name").getValue().toString();
                                        if (search.equals(ingname)) {
                                            littlelayout = new LinearLayout(getApplicationContext());
                                            littlelayout.setOrientation(LinearLayout.VERTICAL);
                                            littlelayout.setGravity(Gravity.CENTER);
                                            rcplinearlayout.addView(littlelayout);
                                            final String rcpnum=recipeData.getKey();

                                            ImageView imageView =new ImageView(getApplicationContext());
                                            int width = 1000;
                                            int height = 1000;
                                            imageView.setForegroundGravity(Gravity.TOP);

                                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

                                            imageView.setLayoutParams(params);
                                            StorageReference storageReference = storageRef.child(rcpnum+".jpg");
                                            Glide.with(RecipeMain.this)
                                                    .using(new FirebaseImageLoader())
                                                    .load(storageReference)
                                                    .into(imageView);
                                            String rt = recipeData.child("title").getValue().toString();
                                            Log.e("##", "글씨체적용");
                                            tv_recipe.setTextColor(Color.BLACK);
                                            Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.namum);
                                            tv_recipe.setTypeface(typeface);
                                            tv_recipe.setTextSize(30);
                                            tv_recipe.setHeight(200);
                                            tv_recipe.setGravity(Gravity.CENTER|Gravity.BOTTOM); //추가
                                            tv_recipe.setText(rt);
                                            tv_recipe.setLayoutParams(layoutParams);
                                            littlelayout.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                                                    intent.putExtra("recipe_num", Integer.parseInt(rcpnum));
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
                                            littlelayout.addView(tv_recipe);
                                            littlelayout.addView(imageView);
                                            littlelayout.addView(enter);
                                            littlelayout.addView(view);
                                            break;
                                        }
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
        });
    }
}
