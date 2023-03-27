package com.example.madrascnema;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    EditText searchEt;
    Button searchBt,researchbt;
    RecyclerView recyclerView;
    MovieAdapter adapter;
    List<Movie> dataList = new ArrayList<>();
    RoomDB database;

    NestedScrollView nestedScrollView;
    ProgressBar progressBar;
    private Intent intent;
    private String title;

    private String client_id = "rTmZsKWThFIi5PFICHZn";
    private String client_pw = "mElbbMRtkL";
    int start=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEt = findViewById(R.id.searchEt);
        searchBt = findViewById(R.id.searchBt);
        researchbt=findViewById(R.id.researchbt);
        recyclerView = findViewById(R.id.recylerview);
        progressBar = findViewById(R.id.progressBar);
        nestedScrollView = findViewById(R.id.scroll_view);

        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getAll();

        progressBar.setVisibility(View.GONE);

        adapter = new MovieAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResultSearch(start);
                save();
                adapter.clearItems();
            }
        });

        researchbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Researches.class);
                startActivity(intent);
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener()
        {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY)
            {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
                {
                    start++;
                    progressBar.setVisibility(View.VISIBLE);
                    getResultSearch(start);
                }
            }
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        intent = getIntent();
        title = intent.getStringExtra("title");
        searchEt.setText(title);

        getResultSearch(start);
        adapter.clearItems();

    }

    void save(){
        String sText = searchEt.getText().toString();
        if (!sText.equals("")) {
            Movie data = new Movie();
            data.setTitle(sText);
            database.mainDao().insert(data);

            Log.d("save text: ",sText);
        }
    }

    void getResultSearch(int start) {
        ApiInterface apiInterface = ApiClient.getInstance().create(ApiInterface.class);
        Call<String> call = apiInterface.getSearchResult(client_id, client_pw, "movie.json",searchEt.getText().toString(),start);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body();
                    Log.e(TAG, "Success : " + result);
                    processResponse(result);
                }
                else {
                    Log.e(TAG, "error : " + response.body());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t){
                Log.e(TAG, "에러 : " + t.getMessage());
            }
        });
    }

    private void processResponse(String response){
        Gson gson = new Gson();
        MovieList movieList = gson.fromJson(response, MovieList.class);
        for(int i = 0; i < movieList.items.size(); i++){
            Movie movie = movieList.items.get(i);
            adapter.addItem(movie);
        }
        adapter.notifyDataSetChanged();
    }

}



