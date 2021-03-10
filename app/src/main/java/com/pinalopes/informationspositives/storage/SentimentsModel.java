package com.pinalopes.informationspositives.storage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "sentiments_model", indices = {@Index(value = {"model"},
        unique = true)})
public class SentimentsModel {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "model")
    public String model;
    @ColumnInfo(name = "last_modified_time")
    public String lastModifiedTime;
}