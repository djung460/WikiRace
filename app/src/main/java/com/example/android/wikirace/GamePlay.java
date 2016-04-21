package com.example.android.wikirace;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GamePlay extends AppCompatActivity implements ArticleAsyncTask.AsyncResponse, GameWebViewClient.WebViewClientResponse, View.OnClickListener {
    private WebView mStartWebView;
    private WebView mDestinationWebView;
    private TextView mDestArticleTitle;
    private TextView mPagesVisited;
    private ImageButton mBackButton;
    private ImageButton mRestartButton;
    private List<Article> articles = new ArrayList<>();

    private Article mStartArticle;
    private Article mDestArticle;

    private ArticleAsyncTask wikiQueryTask;

    private int numPagesVisited = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        mPagesVisited = (TextView) findViewById(R.id.num_pages_visited);

        switchToScreen(R.id.game_screen);

        wikiQueryTask = new ArticleAsyncTask();
        wikiQueryTask.delegate = this;
        wikiQueryTask.execute("Execute");
    }


    @Override
    public void processFinish(List<Article> output) {
        articles = output;

        mStartArticle = articles.get(0);
        mDestArticle = articles.get(1);

        mStartWebView = (WebView) findViewById(R.id.wiki_web_view);
        mDestinationWebView = (WebView) findViewById(R.id.dest_wiki_web_view);

        // disable clicking on destination web view
        mDestinationWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        WebSettings startWebSettings = mStartWebView.getSettings();
        startWebSettings.setJavaScriptEnabled(true);
        WebSettings destWebSettings = mDestinationWebView.getSettings();
        destWebSettings.setJavaScriptEnabled(true);

        //Loads url onto the start webview
        mStartWebView.loadUrl(mStartArticle.getUrl());
        mDestinationWebView.loadUrl(mDestArticle.getUrl());

        GameWebViewClient startingArticleClient = new GameWebViewClient(articles.get(1), this);
        startingArticleClient.delegate = this;
        mStartWebView.setWebViewClient(startingArticleClient);

        GameWebViewClient destinationArticleClient = new GameWebViewClient(articles.get(0),this);
        destinationArticleClient.delegate = this;
        mDestinationWebView.setWebViewClient(destinationArticleClient);

        //Deals with the back button
        mBackButton = (ImageButton) findViewById(R.id.back_button);
        mBackButton.setOnClickListener(this);

        //Deals with the restart button
        mRestartButton = (ImageButton) findViewById(R.id.restart_button);
        mRestartButton.setOnClickListener(this);

        //Sets the destination article title
        mDestArticleTitle = (TextView) findViewById(R.id.dest_article_title_textview);
        mDestArticleTitle.setText(mDestArticle.getTitle());
        mDestArticleTitle.setOnClickListener(this);
    }

//    private void showArticleDialog() {
//        DestArticleFragment dialogFragment = DestArticleFragment.newInstance(mDestArticle.getExtract());
//        dialogFragment.show(getFragmentManager(),"dialog");
//    }

    private void displayRestartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.restart_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.restart_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setMessage(R.string.restart_dialog_title);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // This array lists all the individual screens our game has.
    final static int[] SCREENS = {
            R.id.dest_article_screen, R.id.game_screen
    };

    private int mCurrentScreen = -1;

    private void switchToScreen(int screenId) {
        for(int id : SCREENS) {
            findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
        }
        mCurrentScreen = screenId;
    }

    //TODO: MOVE THIS INTO MODEL
    @Override
    public void pageLoaded() {
        numPagesVisited++;
        mPagesVisited.setText(Integer.toString(numPagesVisited));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_button:
                if (mStartWebView.canGoBack()) {
                    mStartWebView.goBack();
                }
                break;
            case R.id.dest_article_title_textview:
                switchToScreen(R.id.dest_article_screen);
                break;
            case R.id.restart_button:
                displayRestartDialog();
                break;
        }
    }
}
