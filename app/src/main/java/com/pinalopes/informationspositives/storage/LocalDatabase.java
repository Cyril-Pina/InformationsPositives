package com.pinalopes.informationspositives.storage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RecentSearch.class}, version = 4)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract RecentSearchesDao recentSearchesDao();
}
