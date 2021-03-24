package com.pinalopes.informationspositives.feed.model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pinalopes.informationspositives.databinding.ArticlesFragmentBinding;
import com.pinalopes.informationspositives.feed.viewmodel.ArticleRowViewModel;

import java.util.List;

import static com.pinalopes.informationspositives.Constants.DIRECTION_SCROLL_VERTICALLY;
import static com.pinalopes.informationspositives.Constants.TOP_SCROLL_POSITION;

public class ArticlesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ArticlesFragmentBinding binding;
    private OnRefreshEventListener listener;

    public interface OnRefreshEventListener {
        void onRefresh();
        void onFeedListReachesBottomListener();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ArticlesFragmentBinding.inflate(inflater, container, false);

        View rootView = binding.getRoot();

        initNewsSwipeRefreshLayout();
        setOnFeedListReachesBottomListener();
        return rootView;
    }

    @Override
    public void onRefresh() {
        listener.onRefresh();
    }

    private void initNewsSwipeRefreshLayout() {
        binding.newsSwipeRefreshLayout.setOnRefreshListener(this);
    }

    public void initFeedRecyclerViewAdapter(List<ArticleRowViewModel> feedArticleDataList, int nbElementsAdded, LinearLayoutManager layoutManager) {
        binding.feedRecyclerView.setLayoutManager(layoutManager);
        if (binding.feedRecyclerView.getAdapter() != null
                && feedArticleDataListIsAlreadyFilled(feedArticleDataList, nbElementsAdded)) {
            int startIndex = feedArticleDataList.size() - nbElementsAdded;
            notifyItemsAddedInFeed(startIndex, nbElementsAdded);
        } else {
            FeedRecyclerAdapter adapter = new FeedRecyclerAdapter(feedArticleDataList);
            binding.feedRecyclerView.setAdapter(adapter);
            binding.feedRecyclerView.getAdapter().notifyDataSetChanged();
        }
        binding.newsSwipeRefreshLayout.setRefreshing(false);
    }

    public void stopSwipeRefreshing() {
        binding.newsSwipeRefreshLayout.setRefreshing(false);
    }

    private boolean feedArticleDataListIsAlreadyFilled(List<ArticleRowViewModel> feedArticleDataList, int nbElementsAdded) {
        return feedArticleDataList.size() > nbElementsAdded;
    }

    private void notifyItemsAddedInFeed(int startIndex, int size) {
        if (binding.feedRecyclerView.getAdapter() != null) {
            binding.feedRecyclerView.getAdapter().notifyItemRangeInserted(startIndex, size);
        }
    }

    private void setOnFeedListReachesBottomListener() {
        binding.feedRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(DIRECTION_SCROLL_VERTICALLY) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    listener.onFeedListReachesBottomListener();
                }
            }
        });
    }

    public void scrollUpToTopOfList() {
        binding.feedRecyclerView.smoothScrollToPosition(TOP_SCROLL_POSITION);
    }

    public void setOnRefreshEventListener(OnRefreshEventListener listener) {
        this.listener = listener;
    }
}
