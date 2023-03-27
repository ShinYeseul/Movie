package com.example.madrascnema;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "movie")
public class Movie implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @ColumnInfo(name = "title")
    String title;
    String link;
    String image;
    String pubDate;
    float userRating;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
