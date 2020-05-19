package com.example.coookbab;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecipeIngredient extends AppCompatActivity {
    private LinearLayout linearLayout;
    private DatabaseReference mReference;
    private DatabaseReference myrefrigerator;
    private DatabaseReference store;
    private FirebaseDatabase mDatabase;
    private Button startcook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredient);
        final Intent intent=getIntent();
        String recipe_num=intent.getExtras().getString("recipe_num");
        String recipe_need=intent.getExtras().getString("i");
        final int rcpneed= Integer.parseInt(recipe_need);
        String uid="hUeiODcSXrSEe1MJ9stKlAbcpcv2";

        startcook = (Button)findViewById(R.id.startcook);
        linearLayout=(LinearLayout)findViewById(R.id.linearlayout);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("recipe").child(recipe_num).child("ingredients").child("howto");
        myrefrigerator = mDatabase.getReference().child("user").child(uid).child("refrigerator");
        store = mDatabase.getReference().child("how_to_sore");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for(int i=1;i<=rcpneed;i++){
                    final String ingredientid = dataSnapshot.child(String.valueOf(i)).child("ingredientid").getValue().toString();
                    String need = dataSnapshot.child(String.valueOf(i)).child("need").getValue().toString();

                    final Button marketbtn = new Button(getApplicationContext());
                    TextView textView = new TextView(getApplicationContext());
                    final TextView have = new TextView(getApplicationContext());
                    final TextView name = new TextView(getApplicationContext());

                    store.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dts) {
                            String ingname = dts.child(ingredientid).child("ingredient").getValue().toString();
                            name.setText(ingname);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError dtE) {
                        }
                    });

                    myrefrigerator.addValueEventListener(new ValueEventListener() {//냉장고에 재료 없을 때 해결하기
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dts) {
                            String ihave = dts.child("ingredient").child(ingredientid).child("num").getValue().toString();
                            Log.e("###", ihave);
                            if(ihave== "null") { have.setText("0"); }//내 냉장고에 이 재료가 없다면 ihave는 뭘로 나오지??->돌아가지 않는다.
                            else{have.setText(ihave); }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError dtE) {
                        }
                    });

                    LinearLayout littlelinearlayout=new LinearLayout(getApplicationContext());
                    linearLayout.addView(littlelinearlayout);

                    textView.setText(need);
                    littlelinearlayout.setOrientation(LinearLayout.HORIZONTAL);
                    littlelinearlayout.addView(name);
                    littlelinearlayout.addView(textView);
                    littlelinearlayout.addView(have);
                    littlelinearlayout.addView(marketbtn);
                    marketbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //마켓으로 연결
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
