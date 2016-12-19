package com.example.android.book_store;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Books>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Constant value for the books loader ID. We can choose any integer.
     */
    private static final int BOOKS_LOADER_ID = 1;
    private static final String SEARCH_STRING =
            "https://www.googleapis.com/books/v1/volumes?maxResults=20&q=";
    private static final String BOOK_LIST = "list_of_books";
    String searchQuery;
    private ImageView searchButton;
    private TextView mEmptyStateTextView;
    private String GOOGLE_REQUEST_URL = " ";
    /**
     * Adapter for the list of Books
     */
    private BooksAdapter mAdapter;
    private EditText mEditText;
    private View loadingIndicator;
    private ListView booksListView;
    private ArrayList<Books> mBookArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find a reference to the {@link ListView} in the layout
        booksListView = (ListView) findViewById(R.id.list);
        mBookArrayList = new ArrayList<>();
        // Create a new adapter that takes an empty list of Books as input
        mAdapter = new BooksAdapter(this, mBookArrayList);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        booksListView.setAdapter(mAdapter);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        booksListView.setEmptyView(mEmptyStateTextView);
        mEmptyStateTextView.setText(R.string.search_string);
        loadingIndicator = findViewById(R.id.loading_spinner);
        mEditText = (EditText) findViewById(R.id.searchText);
        loadingIndicator.setVisibility(View.GONE);
        searchButton = (ImageView) findViewById(R.id.search_button);

        getLoaderManager();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchQuery = mEditText.getText().toString().replaceAll(" ", "+");
                GOOGLE_REQUEST_URL = SEARCH_STRING + searchQuery;
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                searchGoogleBook();

            }
        });

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current Books that was clicked on
                Books currentBooks = mAdapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri BooksUri = Uri.parse(currentBooks.getmURL());
                // Create a new intent to view the Books URL link
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, BooksUri);
                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int i, Bundle bundle) {
        loadingIndicator.setVisibility(View.VISIBLE);
        return new BooksLoader(this, GOOGLE_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> y_books) {
        // Clear the adapter of previous Books data
        loadingIndicator.setVisibility(View.GONE);
        // If there is a valid list of {@link Books}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (y_books != null && !y_books.isEmpty()) {
            mAdapter.addAll(y_books);
            y_books.clear();
        } else {
            // Set empty state text to display "No Books found."
            mEmptyStateTextView.setText(R.string.no_books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    private void searchGoogleBook() {
        if (searchQuery != "") {
            //Check internet connectivity
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // Get a reference to the LoaderManager, in order to interact with loaders.
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(BOOKS_LOADER_ID, null, this);
                getLoaderManager().restartLoader(0, null, this);
                mEmptyStateTextView.setText(R.string.retreive_books);
                loadingIndicator.setVisibility(View.VISIBLE);
            } else {
                loadingIndicator.setVisibility(View.GONE);
                // Update empty state with no connection error message
                mEmptyStateTextView.setText(R.string.no_internet_connection);
            }
        } else {
            mAdapter.clear();
            mEmptyStateTextView.setText(R.string.search_string);
            loadingIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putParcelableArrayList(BOOK_LIST, mBookArrayList);
        Log.d(LOG_TAG, "onSaveInstanceState");
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            Log.d(LOG_TAG, "onViewStateRestored");
            mBookArrayList = savedInstanceState.getParcelableArrayList(BOOK_LIST);
            mAdapter.clear();
            mAdapter.addAll(mBookArrayList);
            booksListView.setAdapter(mAdapter);
        }
    }
}
