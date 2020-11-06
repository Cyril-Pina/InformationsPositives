package com.pinalopes.informationspositives.home.model;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pinalopes.informationspositives.R;
import com.pinalopes.informationspositives.articles.Article;
import com.pinalopes.informationspositives.articles.ArticleComponent;
import com.pinalopes.informationspositives.articles.DaggerArticleComponent;
import com.pinalopes.informationspositives.editor.EditorModule;
import com.pinalopes.informationspositives.editor.SocialNetworks;

import javax.inject.Inject;

import io.sentry.Sentry;

public class MainActivity extends AppCompatActivity {

    @Inject
    Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Sentry.captureMessage("testing SDK setup");

        ArticleComponent articleComponent = DaggerArticleComponent.builder().editorModule(new EditorModule(
               "NewMan",
               "Nicolas",
               "Nick",
               new SocialNetworks(
                       "nick@gmail.com",
                       "Nicolas Newman",
                       "n.newman",
                       "Nick Newman"
               ))).build();
        articleComponent.inject(this);
    }
}