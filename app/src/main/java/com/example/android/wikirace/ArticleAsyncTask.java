package com.example.android.wikirace;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Jung on 2016-02-18.
 */
public class ArticleAsyncTask extends AsyncTask <String, Long, List<Article>>{
    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(List<Article> output);
    }

    @Override
    protected List<Article> doInBackground(String... params) {
        return WikiQueries.getTwoRandomArticles();
    }

    @Override
    protected void onPostExecute(List<Article> result) {
        delegate.processFinish(result);
    }
}
