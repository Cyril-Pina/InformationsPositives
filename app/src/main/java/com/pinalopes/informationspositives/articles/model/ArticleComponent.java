package com.pinalopes.informationspositives.articles.model;

import com.pinalopes.informationspositives.editor.EditorModule;
import com.pinalopes.informationspositives.MainActivity;

import dagger.Component;

@Component (modules = EditorModule.class)
public interface ArticleComponent {
    void inject(MainActivity mainActivity);
}
