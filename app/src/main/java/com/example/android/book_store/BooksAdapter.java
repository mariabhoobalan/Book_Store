package com.example.android.book_store;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class BooksAdapter extends ArrayAdapter<Books> {

    public BooksAdapter(Activity context, ArrayList<Books> x_books) {
        super(context, 0, x_books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list, parent, false);
        }

        //Retrieve the current instance of the list item by querying the position
        Books currentBooks = getItem(position);

        //get the data to be populated in the list view
        TextView bookView = (TextView) listItemView.findViewById(R.id.books);
        bookView.setText(currentBooks.getmBooks());
        View listLayout = listItemView.findViewById(R.id.mainlist);
        TextView authorView = (TextView) listItemView.findViewById(R.id.Author);
        authorView.setText(currentBooks.getmAuthors());

        //Alternate the list color for better readability
        if (position % 2 == 0) {
            listLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.EvenPanelColor));
        } else {
            listLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.OddPanelColor));
        }
        return listItemView;
    }
}
