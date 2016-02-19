package com.example.android.wikirace;

import android.content.Context;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by David Jung on 2016-02-17.
 *
 */
public class GameWebViewClient extends WebViewClient {

    private Article mDestArticle;

    public GameWebViewClient(Article destArticle) {
        mDestArticle = destArticle;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {


//        if() {
//            Toast.makeText(mContext, "Game Done!", Toast.LENGTH_SHORT).show();
//        }

        return false;
    }
}
