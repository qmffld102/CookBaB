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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        linearLayout =(LinearLayout)findViewById(R.id.linearlayout);
        editlife =(EditText)findViewById(R.id.editlife);
        editnum =(EditText)findViewById(R.id.editnum);
        savebtn =(Button)findViewById(R.id.savebtn);
        canclebtn =(Button)findViewById(R.id.canclebtn);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("how_to_sore");

        /*
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();
                for(DataSnapshot messageData : dataSnapshot.getChildren()){
                    HowToSore temp = messageData.getValue(HowToSore.class);

                    final String ingredientname = String.valueOf(temp.getIngredientname());//db안에 how_to_sore안의 모든 데이터 읽어오라고 만들었는데 왜 안될까?
                    Log.e("@@@", String.valueOf(temp));

                    TextView ingredient = new TextView(getApplicationContext());
                    ingredient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ingredient.setText(ingredientname);
                    linearLayout.addView(ingredient);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         */
        linearLayout.removeAllViews();
        final LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT /* layout_width */, LinearLayout.LayoutParams.WRAP_CONTENT /* layout_height */, 1f /* layout_weight */);




        for(int i=1; i<20; i++){
            mReference.child(String.valueOf(i)).child("ingredient").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue(String.class)!=null){
                        String value = dataSnapshot.getValue(String.class);
                        Log.e(this.getClass().getName(), value.toString());
                        TextView ingredient = new TextView(getApplicationContext());
                        ingredient.setText(value.toString());
                        ingredient.setLayoutParams(layoutParams);
                        linearLayout.addView(ingredient);
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
                String num=editnum.getText().toString();
                String life=editlife.getText().toString();
                mReference.child("user").child("refrigerator").child("ingredient").push().setValue(num);
                mReference.child("user").child("refrigerator").child("ingredient").push().setValue(life);
                //선택한 재료의 종류 알아와야됨
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
