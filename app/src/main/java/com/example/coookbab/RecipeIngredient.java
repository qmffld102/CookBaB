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
        final String recipe_num=intent.getExtras().getString("recipe_num");
        String recipe_need=intent.getExtras().getString("i");
        final String str = intent.getExtras().getString("photo_num");
        final int rcpneed= Integer.parseInt(recipe_need);
        String uid="hUeiODcSXrSEe1MJ9stKlAbcpcv2";

        final String[] []Array = new String[3][100];
        final int cnt=0;

        startcook = (Button)findViewById(R.id.startcook);
        linearLayout=(LinearLayout)findViewById(R.id.linearlayout);
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("recipe").child(recipe_num).child("ingredients");
        myrefrigerator = mDatabase.getReference().child("user").child(uid).child("refrigerator");
        store = mDatabase.getReference().child("how_to_sore");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                for(int i=1;i<=rcpneed;i++){
                    final String ingredientid = dataSnapshot.child(String.valueOf(i)).child("ingredientid").getValue().toString();
                    Log.e("## ", ingredientid);
                    final String need = dataSnapshot.child(String.valueOf(i)).child("need").getValue().toString();
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
                    myrefrigerator.child("ingredient").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dts) {
                            String ingname = name.getText().toString();
                            int suc=0;
                            for (DataSnapshot messageData : dts.getChildren()){
                                Log.e("#", messageData.child("name").getValue().toString());//승희야 이거 도와줘 이거 하나씩 가자고 오고 싶은데 다 가져오네
                                //if(ingname.equals(messageData.child("name").getValue().toString())){ suc=1; }
                            }
                            if(suc == 0) { have.setText("0"); }
                            else {
                                String ihave = dts.child(ingredientid).child("num").getValue().toString();
                                have.setText(ihave);
                                Array[0][cnt]=ihave;
                                Array[1][cnt]=ingredientid;
                                Array[2][cnt]=need;
                            }
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
        startcook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myrefrigerator.child("ingredient").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(int i=0;i<=cnt;i++){
                            int A= Integer.parseInt(Array[0][i]);
                            int b=Integer.parseInt(Array[2][i]);
                            A-=b;
                            String w = String.valueOf(A);
                            myrefrigerator.child("ingredient").child(Array[1][i]).child("num").setValue(w);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                Intent intent = new Intent(getApplicationContext(), CookActivity.class);
                intent.putExtra("recipe_num", recipe_num);
                intent.putExtra("photo_num", Integer.parseInt(str));
                startActivity(intent);
            }
        });
    }
}
