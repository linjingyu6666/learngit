package com.example.myblue;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Account implements Parcelable, Serializable {
    private String userName;
    private String gender;
    private int age;

    public Account(String name, String gender, int age) {
        this.userName = name;
        this.gender = gender;
        this.age = age;
    }

    public Account(Parcel in) {
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
}