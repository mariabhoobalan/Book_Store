package com.example.android.book_store;

//Import Statement
import android.os.Parcel;
import android.os.Parcelable;

public class Books implements Parcelable {
    private String mBooks;
    private String mAuthors;
    private String mURL;

    //Class definition
    public Books(String a_books, String a_authors, String a_url) {
        mAuthors = a_authors;
        mBooks = a_books;
        mURL = a_url;
    }

    //Getter methods
    public String getmBooks() {
        return mBooks;
    }
    public String getmAuthors() {
        return mAuthors;
    }
    public String getmURL() {
        return mURL;
    }

    //Parcelling concept to restore the result after the screen orientation is changed
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mBooks);
        dest.writeString(this.mAuthors);
        dest.writeString(this.mURL);
    }

    protected Books(Parcel in) {
        this.mBooks = in.readString();
        this.mAuthors = in.readString();
        this.mURL = in.readString();
    }

    public static final Parcelable.Creator<Books> CREATOR = new Parcelable.Creator<Books>() {
        @Override
        public Books createFromParcel(Parcel source) {
            return new Books(source);
        }
        @Override
        public Books[] newArray(int size) {
            return new Books[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
