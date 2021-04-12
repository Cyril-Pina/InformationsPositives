package com.pinalopes.informationspositives.storage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LikesModelDao {

    @Query("SELECT * FROM like_model WHERE title = :currentTitle")
    List<LikeModel> getLike(String currentTitle);

    @Insert
    void insertLike(LikeModel model);

    @Delete
    void deleteLike(LikeModel model);
}
