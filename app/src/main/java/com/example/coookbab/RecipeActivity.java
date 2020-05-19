package com.example.coookbab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RecipeActivity extends AppCompatActivity {

    private TextView tv_title, tv_ingredient, tv_recipe;
    private Button btn_youtube, btn_cook;
    private DatabaseReference rDatabase;
    private String str;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();

        final int recipe_num = intent.getExtras().getInt("recipe_num");
        Log.e(this.getClass().getName(), String.valueOf(recipe_num));

        tv_title = findViewById(R.id.tv_title);
        tv_ingredient=findViewById(R.id.tv_ingredient);
        tv_recipe = findViewById(R.id.tv_recipe);
        btn_youtube=findViewById(R.id.btn_youtube);
        btn_cook = findViewById(R.id.btn_cook);

        rDatabase = FirebaseDatabase.getInstance().getReference();

        rDatabase.child("recipe").child(String.valueOf(recipe_num)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                str = dataSnapshot.child("title").getValue().toString();
                tv_title.setText(str);
                str=dataSnapshot.child("ingredients").child("text").getValue().toString();
                tv_ingredient.setText(str);
                str=dataSnapshot.child("howto").getValue().toString();
                tv_recipe.setText("\n"+str);
                str=dataSnapshot.child("link").getValue().toString();
                final String i=dataSnapshot.child("need").getValue().toString();
                tv_ingredient.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), RecipeIngredient.class);
                        intent.putExtra("recipe_num", String.valueOf(recipe_num));
                        intent.putExtra("i", i);
                        startActivity(intent);
                    }
                });
                btn_youtube.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(str.toString()));
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
    }
}
