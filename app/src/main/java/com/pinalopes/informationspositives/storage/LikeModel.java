package com.pinalopes.informationspositives.storage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "like_model", indices = {@Index(value = {"title"},
        unique = true)})
public class LikeModel {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "title")
    public String title;
}