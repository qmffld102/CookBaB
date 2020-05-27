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


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecipeMain extends AppCompatActivity {
    private DatabaseReference RcpListDatabase;
    private LinearLayout rcplinearlayout;
    private DatabaseReference rcpRef;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main);

        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        rcplinearlayout = findViewById(R.id.rcplinearlayout);
        RcpListDatabase = FirebaseDatabase.getInstance().getReference();
        rcpRef = FirebaseDatabase.getInstance().getReference().child("recipe");
        final String uid="hUeiODcSXrSEe1MJ9stKlAbcpcv2";

        rcplinearlayout.removeAllViews();
        final LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RcpListDatabase.child("recipe").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot recipeData : dataSnapshot.getChildren()){
                    String rt = recipeData.child("title").getValue().toString();
                    Log.e(this.getClass().getName(), rt);
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
                                String rt = recipeData.child("title").getValue().toString();
                                Log.e(this.getClass().getName(), rt);
                                TextView tv_recipe = new TextView(getApplicationContext());
                                tv_recipe.setTextSize(30);
                                tv_recipe.setHeight(200);
                                tv_recipe.setText(rt);
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
                    RcpListDatabase.child("recipe").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            rcplinearlayout.removeAllViews();
                            for (final DataSnapshot recipeData : dataSnapshot.getChildren()) {
                                String name = recipeData.child("title").getValue().toString();
                                String rtnum = recipeData.getValue().toString();
                                if (name.contains(search)) {
                                    TextView tv_recipe = new TextView(getApplicationContext());
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
                                    int many = Integer.parseInt(recipeData.child("need").getValue().toString());
                                    for (int i = 1; i <= many; i++) {
                                        String ingname = recipeData.child("ingredients").child("howto").child(String.valueOf(i)).child("name").getValue().toString();
                                        if (search.equals(ingname)) {
                                            TextView tv_recipe = new TextView(getApplicationContext());
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
