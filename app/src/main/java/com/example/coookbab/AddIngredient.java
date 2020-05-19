package com.example.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;

public class AddIngredient extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
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
        final String Uid="hUeiODcSXrSEe1MJ9stKlAbcpcv2";
        mDatabase = FirebaseDatabase.getInstance();
        Intent intent =getIntent();
        final String where =intent.getExtras().getString("where");

        mReference=mDatabase.getReference("user").child(Uid).child("refrigerator").child(where).child("ingredient");
        DatabaseReference spaceRef = mDatabase.getReference("how_to_sore");

        linearLayout.removeAllViews();
        final LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT /* layout_width */, LinearLayout.LayoutParams.WRAP_CONTENT /* layout_height */, 1f /* layout_weight */);

        for(int i = 1; i<59; i++){
            final int numb=i;
            spaceRef.child(String.valueOf(i)).child("ingredient").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class)!=null){
                        String value = dataSnapshot.getValue(String.class);
                        Log.e(this.getClass().getName(), value);
                        final TextView ingredient = new TextView(getApplicationContext());
                        ingredient.setText(value);
                        ingredient.setLayoutParams(layoutParams);
                        ingredient.setId(numb);
                        linearLayout.addView(ingredient);
                        ingredient.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                number= String.valueOf(ingredient.getId());
                                name =String.valueOf(ingredient.getText());
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddIngredient.this, RefrigeratorMain.class);
                mReference.push().child(name);
                mReference.child(name).child("ingredientid").setValue(number);
                mReference.child(name).child("num").setValue(editnum.getText().toString());
                mReference.child(name).child("life").setValue(editlife.getText().toString());
                startActivity(intent);
            }
        });
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddIngredient.this, RefrigeratorMain.class);
                startActivity(intent);
            }
        });
    }

}
