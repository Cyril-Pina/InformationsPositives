package com.pinalopes.informationspositives.search.model;

import com.pinalopes.informationspositives.storage.RecentSearch;

public interface OnRecentSearchClickListener {
    void setOnRecentSearchClick(RecentSearch recentSearchSelected);
    void setOnDeleteRecentSearchClick(RecentSearch recentSearchToDelete, int position);
}
