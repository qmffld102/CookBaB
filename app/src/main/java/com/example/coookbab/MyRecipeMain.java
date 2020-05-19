package com.example.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_main);
        rcplinearlayout = findViewById(R.id.myrcplinearlayout);
        RcpListDatabase = FirebaseDatabase.getInstance().getReference();
        rcpRef = FirebaseDatabase.getInstance().getReference().child("recipe");
        String uid="hUeiODcSXrSEe1MJ9stKlAbcpcv2";
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
    }
}
