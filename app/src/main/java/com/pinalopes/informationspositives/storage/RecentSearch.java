package com.pinalopes.informationspositives.storage;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recent_search", indices = {@Index(value = {"article_searched"},
        unique = true)})
public class RecentSearch {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "article_searched")
    public String articleSearched;
}
