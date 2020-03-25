package com.example.kylewai.a2uf.com.example.kylewai.firebasemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class Comment implements Parcelable {
    @DocumentId
    String docId;
    String author;
    Timestamp dateCreated;
    Integer likes;
    String text;

    public Comment(){}

    public Comment(String docId, String author, Timestamp dateCreated, String text, Integer likes){
        this.docId = docId;
        this.likes = likes;
        this.author = author;
        this.dateCreated = dateCreated;
        this.text = text;
    }

    protected Comment(Parcel in) {
        docId = in.readString();
        likes = in.readInt();
        author = in.readString();
        dateCreated = in.readParcelable(Timestamp.class.getClassLoader());
        text = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(docId);
        parcel.writeInt(likes);
        parcel.writeString(author);
        parcel.writeParcelable(dateCreated, i);
        parcel.writeString(text);
    }

    public String getAuthor() {
        return author;
    }

    public Integer getLikes() {
        return likes;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public String getDocId() {
        return docId;
    }

    public String getText() {
        return text;
    }

}
