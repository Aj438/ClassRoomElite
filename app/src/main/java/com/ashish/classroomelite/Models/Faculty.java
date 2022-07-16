package com.ashish.classroomelite.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Faculty implements Parcelable {
private String uid;
private String Name;
private String ClassName;

    public Faculty() {
    }

    public Faculty(String uid, String name, String className) {
        this.uid = uid;
        Name = name;
        ClassName = className;
    }

    protected Faculty(Parcel in) {
        uid = in.readString();
        Name = in.readString();
        ClassName = in.readString();
    }

    public static final Creator<Faculty> CREATOR = new Creator<Faculty>() {
        @Override
        public Faculty createFromParcel(Parcel in) {
            return new Faculty(in);
        }

        @Override
        public Faculty[] newArray(int size) {
            return new Faculty[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(Name);
        dest.writeString(ClassName);
    }
}
