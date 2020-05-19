package com.example.coookbab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.coookbab.mRecyclerView.ListItem;
import com.example.coookbab.mRecyclerView.MyAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecipeMain extends AppCompatActivity {
    private DatabaseReference RcpListDatabase;

    private RecyclerView rv_recipelist;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main);

        RcpListDatabase = FirebaseDatabase.getInstance().getReference();

        rv_recipelist = (RecyclerView) findViewById(R.id.rv_recipelist);
        rv_recipelist.setHasFixedSize(true);
        rv_recipelist.setLayoutManager(new LinearLayoutManager(this));

        listItems=new ArrayList<>();


        for(int j=1; j<20; j++){
            readdata(j);
        }

        listItems.add(new ListItem("test", "test"));
        adapter = new MyAdapter(listItems, this);
        rv_recipelist.setAdapter(adapter);
    }

    private void readdata(final int jj) {

        RcpListDatabase.child("recipe").child(String.valueOf(jj)).child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class)!=null){

                    String value = dataSnapshot.getValue(String.class);

                    Log.e(this.getClass().getName(), String.valueOf(jj)+" "+value.toString());

                    listItems.add(new ListItem("test", "test"));
                    listItems.add(new ListItem(value.toString(), " "));
                    listItems.add(new ListItem("test2", "test2"));
                }else{
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
