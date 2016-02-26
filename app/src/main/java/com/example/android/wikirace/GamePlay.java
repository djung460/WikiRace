package com.example.android.wikirace;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GamePlay extends AppCompatActivity implements ArticleAsyncTask.AsyncResponse, GameWebViewClient.WebViewClientResponse {
    private WebView mStartWebView;
    private TextView mDestArticleTitle;
    private TextView mPagesVisited;
    private ImageButton mBackButton;
    private List<Article> articles = new ArrayList<>();

    private Article mStartArticle;
    private Article mDestArticle;

    private ArticleAsyncTask wikiQueryTask = new ArticleAsyncTask();

    private int numPagesVisited = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        mBackButton = (ImageButton) findViewById(R.id.back_button);
        mPagesVisited = (TextView) findViewById(R.id.num_pages_visited);

        wikiQueryTask.delegate = this;
        wikiQueryTask.execute("Execute");
    }


    @Override
    public void processFinish(List<Article> output) {
        articles = output;

        mStartArticle = articles.get(0);
        mDestArticle = articles.get(1);

        mStartWebView = (WebView) findViewById(R.id.wiki_web_view);
        WebSettings webSettings = mStartWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Loads url onto the start webview
        mStartWebView.loadUrl(mStartArticle.getUrl());

        GameWebViewClient client = new GameWebViewClient(articles.get(1), this);
        client.delegate = this;
        mStartWebView.setWebViewClient(client);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStartWebView.canGoBack()) {
                    mStartWebView.goBack();
                }
            }
        });

        //Sets the destination article title
        mDestArticleTitle = (TextView) findViewById(R.id.dest_article_title_textview);
        mDestArticleTitle.setText(mDestArticle.getTitle());

        mDestArticleTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        DestArticleFragment dialogFragment = DestArticleFragment.newInstance(mDestArticle.getExtract());
        dialogFragment.show(getFragmentManager(),"dialog");
    }

    @Override
    public void pageLoaded() {
        numPagesVisited++;
        mPagesVisited.setText(Integer.toString(numPagesVisited));
    }
}
