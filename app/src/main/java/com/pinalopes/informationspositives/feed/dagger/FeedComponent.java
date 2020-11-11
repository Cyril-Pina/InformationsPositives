package com.pinalopes.informationspositives.feed.dagger;

import com.pinalopes.informationspositives.feed.model.FeedFragment;

import dagger.Component;

@Component(modules = LoadingModule.class)
public interface FeedComponent {
    void inject(FeedFragment feedFragment);
}