package com.ashish.classroomelite.Models;

import android.os.Parcel;
import android.os.Parcelable;


public class Student implements Parcelable {
    private String uid;
    private String Name;
    private String Roll_no;
    private String studentClass;

    public Student() {
    }

    public Student(String uid, String name, String roll_no, String studentClass) {
        this.uid = uid;
        Name = name;
        Roll_no = roll_no;
        this.studentClass = studentClass;
    }

    protected Student(Parcel in) {
        uid = in.readString();
        Name = in.readString();
        Roll_no = in.readString();
        studentClass = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
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

    public String getRoll_no() {
        return Roll_no;
    }

    public void setRoll_no(String roll_no) {
        Roll_no = roll_no;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(Name);
        dest.writeString(Roll_no);
        dest.writeString(studentClass);
    }
}
