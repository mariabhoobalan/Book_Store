package com.example.android.book_store;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

public class BooksLoader extends AsyncTaskLoader<List<Books>> {
    private String mUrl;

    public BooksLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Books> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of Books.
        List<Books> books = QueryUtil.fetchBooksData(mUrl);
        return books;
    }
}

