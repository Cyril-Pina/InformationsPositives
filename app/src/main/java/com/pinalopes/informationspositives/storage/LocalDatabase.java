package com.pinalopes.informationspositives.storage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RecentSearch.class, SentimentsModel.class}, version = 7)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract RecentSearchesDao recentSearchesDao();
    public abstract SentimentsModelDao sentimentsModelDao();
}
