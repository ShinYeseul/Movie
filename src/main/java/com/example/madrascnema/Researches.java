package com.example.madrascnema;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class Researches extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Movie> dataList = new ArrayList<>();
    ReMovieAdater adapter;
    RoomDB database;
    Movie data = new Movie();
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_researches);

        button = findViewById(R.id.button);
        recyclerView = findViewById(R.id.recylerview);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.mainDao().reset(dataList);

                dataList.clear();
                dataList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();

            }
        });

        database = RoomDB.getInstance(this);
        dataList.addAll(database.mainDao().getAll());


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ReMovieAdater(this, dataList);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        if (dataList.size()>10) {
            int size = dataList.size()-1;
            database.mainDao().delete(dataList.remove(size));

            dataList.clear();
            dataList.addAll(database.mainDao().getAll());
            adapter.notifyDataSetChanged();
        }


    }


}