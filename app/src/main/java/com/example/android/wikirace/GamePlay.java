package com.example.android.wikirace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GamePlay extends AppCompatActivity {
    private WebView mWikiWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        mWikiWebView = (WebView) findViewById(R.id.wiki_web_view);
        mWikiWebView.loadUrl("https://en.wikipedia.org/wiki/Main_Page");
        mWikiWebView.setWebViewClient(new WebViewClient());
    }
}
