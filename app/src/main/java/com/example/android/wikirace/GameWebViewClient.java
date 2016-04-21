package com.example.android.wikirace;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by David Jung on 2016-02-17.
 *
 */
public class GameWebViewClient extends WebViewClient {

    private Article mDestArticle;
    private Context mContext;
    public WebViewClientResponse delegate;

    ProgressDialog mProgress;


    public interface WebViewClientResponse {
        void pageLoaded();
    }

    public GameWebViewClient(Article destArticle, Context context) {
        mDestArticle = destArticle;
        mContext = context;
        mProgress = new ProgressDialog(context);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        String[] splitUrl = url.split("/");

        Log.i("Host Name", Uri.parse(url).getHost());

        //MOVE THIS INTO MODEL
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
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        mProgress.setMessage("Fetching Article");
        mProgress.show();
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        hideElements(view);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // Get rid of progress
        mProgress.dismiss();
        delegate.pageLoaded();
    }

    private void hideElements(WebView view) {
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
