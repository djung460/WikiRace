package com.example.android.wikirace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;

public class GamePlay extends AppCompatActivity {
    private WebView mWikiWebView;
    private List<Article> articles = new ArrayList<>();
    private Bundle savedInstace;
    private ArticleAsyncTask wikiQueryTask = (ArticleAsyncTask) new ArticleAsyncTask(new ArticleAsyncTask.AsyncResponse() {
        @Override
        public void processFinish(List<Article> output) {
            articles = output;

            mWikiWebView = (WebView) findViewById(R.id.wiki_web_view);
            mWikiWebView.loadUrl(articles.get(0).getUrl());
            GameWebViewClient client = new GameWebViewClient(articles.get(1));
            mWikiWebView.setWebViewClient(client);
//            mWikiWebView.loadUrl("http://google.ca");
//            mWikiWebView.setWebViewClient(new WebViewClient());
//            mWikiWebView.loadUrl(articles.get(0).getUrl());
        }
    }).execute("EXECUTE");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        savedInstace = savedInstanceState;
    }
}
