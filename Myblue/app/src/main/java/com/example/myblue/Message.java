package com.example.myblue;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    private String userName;
    private String gender;
    private int age;

    public Message(String name, String gender, int age) {
        this.userName = name;
        this.gender = gender;
        this.age = age;
    }

    public Message(Parcel in) {
        userName = in.readString();
        gender = in.readString();
        age = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(gender);
        dest.writeInt(age);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}