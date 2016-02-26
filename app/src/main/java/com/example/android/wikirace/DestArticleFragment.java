package com.example.android.wikirace;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by David Jung on 2016-02-23.
 *
 * Fragment that displays the destination article
 */
public class DestArticleFragment extends DialogFragment{
    private TextView mDestArticle;

    static DestArticleFragment newInstance(String extract) {
        DestArticleFragment fragment = new DestArticleFragment();

        //Bundle to pass arguments to fragment
        Bundle args = new Bundle();
        args.putString("extract", extract);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dest_article_viewgroup, container, false);
        mDestArticle = (TextView) v.findViewById(R.id.dest_article_text_view);

        String extract = getArguments().getString("extract");
        mDestArticle.setText(extract);

        return v;
    }
}
