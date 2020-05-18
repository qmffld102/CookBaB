package com.example.coookbab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.coookbab.mRecyclerView.ListItem;
import com.example.coookbab.mRecyclerView.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecipeMain extends AppCompatActivity {
    private RecyclerView rv_recipelist;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main);

        rv_recipelist = findViewById(R.id.rv_recipelist);
        rv_recipelist.setHasFixedSize(true);
        rv_recipelist.setLayoutManager(new LinearLayoutManager(this));

        listItems=new ArrayList<>();

        listItems.add(new ListItem("사과", "사과나무"));
        listItems.add(new ListItem("배", "배나무"));

        adapter = new MyAdapter(listItems, this);
        rv_recipelist.setAdapter(adapter);
    }
}
