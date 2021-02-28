package com.pinalopes.informationspositives.storage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecentSearchesDao {
    @Query("SELECT * FROM recent_search")
    List<RecentSearch> getRecentSearches();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLastSearch(RecentSearch recentSearch);

    @Delete
    void deleteRecentSearch(RecentSearch recentSearch);

    @Query("DELETE FROM recent_search")
    void deleteAll();
}