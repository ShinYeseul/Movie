package com.example.madrascnema;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madrascnema.Movie;
import com.example.madrascnema.R;
import com.example.madrascnema.RoomDB;

import java.util.List;

public class ReMovieAdater extends RecyclerView.Adapter<ReMovieAdater.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View layout;
        public ViewHolder(@NonNull View view)
        {
            super(view);
            textView = view.findViewById(R.id.textView);
            layout = itemView;
        }
    }
    private List<Movie> dataList;
    private Activity context;
    private RoomDB database;

    public ReMovieAdater(Activity context, List<Movie> dataList)
    {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        final Movie data = dataList.get(position);
        database = RoomDB.getInstance(context);
        holder.textView.setText(data.getTitle());

        // 레이아웃 클릭 후 링크 연결
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("title",data.getTitle());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

}