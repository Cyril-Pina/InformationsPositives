package com.pinalopes.informationspositives.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SentimentsModelDao {

    @Query("SELECT * FROM sentiments_model")
    List<SentimentsModel> getSentimentsModels();

    @Insert
    void insertModel(SentimentsModel model);

    @Update
    void updateModel(SentimentsModel model);
}
