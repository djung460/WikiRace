package com.example.android.wikirace;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.URI;

/**
 * Created by David Jung on 2016-02-17.
 *
 */
public class GameWebViewClient extends WebViewClient {

    private Article mDestArticle;
    private Context mContext;

    public GameWebViewClient(Article destArticle, Context context) {
        mDestArticle = destArticle;
        mContext = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        String[] splitUrl = url.split("/");

        Log.i("Host Name", Uri.parse(url).getHost());

        if(Uri.parse(url).getHost().contains("wikipedia.org")) {
            if(splitUrl[splitUrl.length - 1].replace('_',' ').equals(mDestArticle.getTitle())) {
                Toast.makeText(mContext,"WOO YOU WIN!",Toast.LENGTH_SHORT).show();
                Log.i("YOU WIN","YAAAAAY");
            }
            return false;
        } else {
            Toast.makeText(mContext,"Can't Leave Wikipedia",Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        hideElements(view);
    }

    public static void hideElements(WebView view) {
        //Remove Header
        view.loadUrl("javascript:(function() { " +
                "document.getElementsByClassName('header')[0].remove(); })()");
        //Remove Edit buttons
        view.loadUrl(
                "javascript:(function() { " +
                        "var editButtons = document.getElementsByClassName('mw-ui-icon mw-ui-icon-element mw-ui-icon-edit-enabled');" +
                        "var i; " +
                        "for(i = 0; i < editButtons.length; i++) { " +
                                "editButtons[i].remove(); " +
                            "}" +
                        "})()"
        );


        //Remove Language Selector
        view.loadUrl(
                "javascript:(function() { " +
                        "document.getElementsByClassName('languageSelector mw-ui-button button')[0].remove(); " +
                        "})()"
        );

        //Remove Last Modified By
        view.loadUrl(
                "javascript:(function() { " +
                        "document.getElementsByClassName('truncated-text last-modified-bottom')[0].remove(); " +
                        "})()"
        );
        //Remove Footer
        view.loadUrl(
                "javascript:(function() { " +
                    "document.getElementsByClassName('footer-info')[0].remove(); " +
                "})()"
        );
        //Remove Footer
        view.loadUrl(
                "javascript:(function() { " +
                        "document.getElementsByClassName('footer-places')[0].remove(); " +
                        "})()"
        );
    }
}
