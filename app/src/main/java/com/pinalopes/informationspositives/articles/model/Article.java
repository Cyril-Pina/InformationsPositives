package com.pinalopes.informationspositives.articles.model;

import com.pinalopes.informationspositives.editor.Editor;

import javax.inject.Inject;

public class Article {

    private Editor editor;

    @Inject
    public Article(Editor editor) {
        this.editor = editor;
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }
}
