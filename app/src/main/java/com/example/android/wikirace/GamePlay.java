package com.example.android.wikirace;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
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

public class GamePlay extends AppCompatActivity implements ArticleAsyncTask.AsyncResponse, GameWebViewClient.WebViewClientResponse, View.OnClickListener, View.OnTouchListener {
    private WebView mStartWebView;
    private WebView mDestinationWebView;
    private TextView mDestArticleTitle;
    private View mSlideHandle;
    private TextView mPagesVisited;
    private ImageButton mBackButton;
    private ImageButton mRestartButton;
    private List<Article> articles = new ArrayList<>();

    private Article mStartArticle;
    private Article mDestArticle;

    private ArticleAsyncTask wikiQueryTask;

    //For dragging purposes
    private float mLastTouchY;
    private int mActivePointerId = -1;

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
        destinationArticleClient.setNoNavigate();
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

        //For swiped stuff
        mSlideHandle = (View) findViewById(R.id.slide_handle);
        mSlideHandle.setOnTouchListener(this);
        mStartWebView.setOnClickListener(this);
    }

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
            R.id.game_screen, R.id.dest_article_preview
    };

    private int mCurrentScreen = -1;

    private void switchToScreen(int screenId) {
        for(int id : SCREENS) {
            findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.INVISIBLE);
        }
        mCurrentScreen = screenId;
    }

    @Override
    public void pageLoaded() {
        numPagesVisited++;
        String pagesVisited = "Pages Visited: " + Integer.toString(numPagesVisited);
        mPagesVisited.setText(pagesVisited);
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
                findViewById(R.id.dest_article_preview).setVisibility(View.VISIBLE);
                mCurrentScreen = R.id.dest_article_preview;
                break;
            case R.id.restart_button:
                displayRestartDialog();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(mCurrentScreen != R.id.game_screen) {
                switchToScreen(R.id.game_screen);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final float y = MotionEventCompat.getY(event, pointerIndex);

                // Remember where we started (for dragging)
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(event, mActivePointerId);

                final float y = MotionEventCompat.getY(event, pointerIndex);

                // Calculate the distance moved
                final float dy = y - mLastTouchY;
                mLastTouchY = y;

                if (mCurrentScreen != R.id.game_screen && dy > 100) {
                    switchToScreen(R.id.game_screen);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                mActivePointerId = -1;
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = -1;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);

                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchY = MotionEventCompat.getY(event, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }
}
