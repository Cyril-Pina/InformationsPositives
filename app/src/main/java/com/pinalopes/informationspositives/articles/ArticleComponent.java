package com.pinalopes.informationspositives.articles;

import com.pinalopes.informationspositives.editor.EditorModule;
import com.pinalopes.informationspositives.home.model.MainActivity;

import dagger.Component;

@Component (modules = EditorModule.class)
public interface ArticleComponent {
    void inject(MainActivity mainActivity);
}
