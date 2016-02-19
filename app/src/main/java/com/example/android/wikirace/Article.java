package com.example.android.wikirace;

/**
 * Created by David Jung on 2016-02-18.
 *
 */
public class Article {
    private String title;
    private long id;
    private String url;

    public Article(String articleTitle, Long articleId, String articleUrl){
        title = articleTitle;
        id = articleId;
        url = articleUrl;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
