package com.example.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyRecipeMain extends AppCompatActivity {
    private DatabaseReference RcpListDatabase;
    private DatabaseReference rcpRef;
    private LinearLayout rcplinearlayout;
    private EditText editText;
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
        final String uid="hUeiODcSXrSEe1MJ9stKlAbcpcv2";
        final LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RcpListDatabase.child("user").child(uid).child("myrecipe").addValueEventListener(new ValueEventListener() {
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
        //검색기능시작
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String search = editText.getText().toString();
                if(search.length()==0){
                    RcpListDatabase.child("user").child(uid).child("myrecipe").addValueEventListener(new ValueEventListener() {
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
                else{
                    RcpListDatabase.child("user").child(uid).child("myrecipe").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            rcplinearlayout.removeAllViews();
                            for(final DataSnapshot recipeData : dataSnapshot.getChildren()){
                                final String rtnum = recipeData.getValue().toString();
                                final TextView tv_recipe = new TextView(getApplicationContext());
                                rcpRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dts) {
                                       String name=dts.child(rtnum).child("title").getValue().toString();
                                       if(name.contains(search)) {
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
                                       }
                                       else {
                                           int many=Integer.parseInt(dts.child(rtnum).child("need").getValue().toString());
                                           for(int i=1;i<=many;i++) {
                                               String ingname = dts.child(rtnum).child("ingredients").child("howto").child(String.valueOf(i)).child("name").getValue().toString();
                                               if(search.equals(ingname)){
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
        //검색기능 끝
    }
}
