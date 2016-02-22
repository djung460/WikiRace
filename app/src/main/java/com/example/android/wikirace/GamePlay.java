package com.example.android.wikirace;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GamePlay extends AppCompatActivity implements ArticleAsyncTask.AsyncResponse {
    private WebView mWikiWebView;
    private TextView mDestArticleTitle;
    private ImageButton mBackButton;
    private List<Article> articles = new ArrayList<>();

    private ArticleAsyncTask wikiQueryTask = new ArticleAsyncTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        wikiQueryTask.delegate = this;
        wikiQueryTask.execute("Execute");
    }


    @Override
    public void processFinish(List<Article> output) {
        articles = output;
        mWikiWebView = (WebView) findViewById(R.id.wiki_web_view);
        WebSettings webSettings = mWikiWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mBackButton = (ImageButton) findViewById(R.id.back_button);
        mDestArticleTitle = (TextView) findViewById(R.id.dest_article_title_textview);
        mDestArticleTitle.setText(articles.get(1).getTitle());

        mWikiWebView.loadUrl(articles.get(0).getUrl());
        GameWebViewClient client = new GameWebViewClient(articles.get(1), this);
        mWikiWebView.setWebViewClient(client);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWikiWebView.canGoBack()) {
                    mWikiWebView.goBack();
                }
            }
        });
    }
}
