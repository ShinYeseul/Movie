package com.example.madrascnema;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MainDao
{
    @Insert(onConflict = REPLACE)
    void insert(Movie movie);

    @Delete
    void reset(List<Movie> movie);

    @Delete
    void delete(Movie movie);

    @Query("UPDATE movie SET title = :sText")
    void update(String sText);

    @Query("SELECT * FROM movie ORDER BY id DESC")
    List<Movie> getAll();
}
