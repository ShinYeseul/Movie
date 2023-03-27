package com.example.madrascnema;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends  RecyclerView.ViewHolder {
        TextView title,pudata,userrating;
        ImageView imageView;
        View layout;
        Bitmap bitmap;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조.
            title = itemView.findViewById(R.id.TitleText);
            pudata = itemView.findViewById(R.id.PuDataText);
            userrating = itemView.findViewById(R.id.userRatingText);
            imageView = itemView.findViewById(R.id.imageView);
            layout = itemView;
        }

        public void setItem(Movie item) {
            //영화 제목
            String title1 = item.title;
            String re_title = title1.replaceAll("<b>","");
            String movie_title = re_title.replaceAll("</b>","");

            title.setText("제목 : " + movie_title);
            pudata.setText("출시일 : " + item.pubDate);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(item.image != null){
                            URL url = new URL(item.image);
                            InputStream instream = url.openStream();
                            bitmap = BitmapFactory.decodeStream(instream);
                            handler.post(new Runnable() {//외부쓰레드에서 메인 UI에 접근하기 위해 Handler를 사용
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(bitmap);
                                }
                            });
                        }else{
                            imageView.setImageResource(R.drawable.ic_launcher_foreground);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();

            userrating.setText("평점 : " + item.userRating);

        }
    }

    ArrayList<Movie> items = new ArrayList<Movie>();
    Handler handler = new Handler();

    // onCreateViewHolder(): 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(itemView);
    }

    // onBindViewHolder(): position에 해당하는 데이터를 뷰홀더의 아이템에 표시
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 실제로 Movie_item 레이아웃을 변경하는 메소드
        Movie item = items.get(position);
        holder.setItem(item);

        // 레이아웃 클릭 후 링크 연결
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.link));
                context.startActivity(intent);
            }
        });
    }

    // getItemCount(): 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Movie item){
        items.add(item);
    }

    public void setItems(ArrayList<Movie> item){
        this.items = item;
    }

    public void clearItems() {
        this.items.clear();
    }


}
