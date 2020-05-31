package com.example.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecipeMain extends AppCompatActivity {
    private DatabaseReference RcpListDatabase;
    private FirebaseAuth mAuth;
    private LinearLayout rcplinearlayout;
    private DatabaseReference rcpRef;
    private EditText editText;
    private Button button;
    private String userUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main);

        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        rcplinearlayout = findViewById(R.id.rcplinearlayout);
        RcpListDatabase = FirebaseDatabase.getInstance().getReference();
        rcpRef = FirebaseDatabase.getInstance().getReference().child("recipe");

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
                    String rt = recipeData.child("title").getValue().toString();
                    TextView tv_recipe = new TextView(getApplicationContext());

                    tv_recipe.setTextSize(30);
                    tv_recipe.setHeight(200);
                    tv_recipe.setText(rt.toString());
                    tv_recipe.setLayoutParams(layoutParams);
                    tv_recipe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                            intent.putExtra("recipe_num", Integer.parseInt(recipeData.getKey()));
                            startActivity(intent);
                        }
                    });
                    rcplinearlayout.addView(tv_recipe);
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
                if(search.length()==0){
                    RcpListDatabase.child("recipe").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            rcplinearlayout.removeAllViews();
                            for(final DataSnapshot recipeData : dataSnapshot.getChildren()){
                                final String rtnum = recipeData.getKey();
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
                                tv_recipe.setTextSize(30);
                                tv_recipe.setHeight(200);
                                tv_recipe.setLayoutParams(layoutParams);
                                tv_recipe.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                                        intent.putExtra("recipe_num", Integer.parseInt(recipeData.getKey()));
                                        startActivity(intent);
                                    }
                                });
                                rcplinearlayout.addView(tv_recipe);
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
                                final String rtnum = recipeData.getKey().toString();
                                final TextView tv_recipe = new TextView(getApplicationContext());
                                rcpRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dts) {
                                        String name = dts.child(rtnum).child("title").getValue().toString();
                                        if (name.contains(search)) {
                                            tv_recipe.setText(name);
                                            tv_recipe.setTextSize(30);
                                            tv_recipe.setHeight(200);
                                            tv_recipe.setLayoutParams(layoutParams);
                                            tv_recipe.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                                                    intent.putExtra("recipe_num", Integer.parseInt(recipeData.getKey()));
                                                    startActivity(intent);
                                                }
                                            });
                                            rcplinearlayout.addView(tv_recipe);
                                        } else {
                                            int many = Integer.parseInt(dts.child(rtnum).child("need").getValue().toString());
                                            for (int i = 1; i <= many; i++) {
                                                String ingname = dts.child(rtnum).child("ingredients").child(String.valueOf(i)).child("name").getValue().toString();
                                                Log.e(this.getClass().getName(), rtnum+"/"+ingname);
                                                if (search.equals(ingname)) {
                                                    tv_recipe.setText(name);
                                                    tv_recipe.setTextSize(30);
                                                    tv_recipe.setHeight(200);
                                                    tv_recipe.setLayoutParams(layoutParams);
                                                    tv_recipe.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                                                            intent.putExtra("recipe_num", Integer.parseInt(recipeData.getKey()));
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    rcplinearlayout.addView(tv_recipe);
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
    }
}
