package com.example.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.sql.Ref;

public class AddIngredient extends AppCompatActivity {

    private DatabaseReference addDatabase;
    private FirebaseAuth mAuth;
    private String userUrl="";
    private LinearLayout linearLayout;
    private EditText editnum;
    private EditText editlife;
    private Button savebtn;
    private Button canclebtn;

    private String number;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        linearLayout =(LinearLayout)findViewById(R.id.linearlayout);
        editlife =(EditText)findViewById(R.id.editlife);
        editnum =(EditText)findViewById(R.id.editnum);
        savebtn =(Button)findViewById(R.id.savebtn);
        canclebtn =(Button)findViewById(R.id.canclebtn);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        userUrl = user.getUid();

        addDatabase = FirebaseDatabase.getInstance().getReference();

        linearLayout.removeAllViews();
        final LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT /* layout_width */, LinearLayout.LayoutParams.WRAP_CONTENT /* layout_height */, 1f /* layout_weight */);

        addDatabase.child("how_to_sore").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot foodData : dataSnapshot.getChildren()){
                    String food = foodData.child("ingredient").getValue().toString();
                    Log.e(this.getClass().getName(), food);
                    final CheckBox tv_food = new CheckBox(getApplicationContext());
                    tv_food.setTextSize(25);
                    tv_food.setText(food.toString());
                    tv_food.setLayoutParams(layoutParams);
                    tv_food.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            name = tv_food.getText().toString();
                            number= String.valueOf(foodData.getKey());
                        }
                    });
                    linearLayout.addView(tv_food);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addDatabase=FirebaseDatabase.getInstance().getReference("user").child(userUrl.toString()).child("refrigerator").child("ingredient");

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                addDatabase.push().child(number);
                addDatabase.child(number).child("name").setValue(name);
                addDatabase.child(number).child("ingredientid").setValue(number);
                addDatabase.child(number).child("num").setValue(editnum.getText().toString());
                addDatabase.child(number).child("life").setValue(editlife.getText().toString());
                startActivity(intent);
            }
        });

        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
