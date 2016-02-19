package com.example.android.wikirace;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class WikiQueriesTest {

    @Test
    public void testGetTwoRandomArticles() throws Exception {

    }

    @Test
    public void testReadArticleArray() throws Exception {

    }

    @Test
    public void testGetArticle() throws Exception {

    }

    @Test
    public void testGetArticleUrl() throws Exception {
        assertEquals("https://en.wikipedia.org/wiki/BOSU",WikiQueries.getArticleUrl(2267142));
    }
}