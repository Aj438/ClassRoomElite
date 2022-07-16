package com.ashish.classroomelite.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

public class Result implements Parcelable{
    private String studentClass;
    private String uid;
    private Map<String,String> Test_1;
    private Map<String,String> Test_2;
    private Map<String,String> Final_Result;

    protected Result(Parcel in) {
        studentClass = in.readString();
        uid = in.readString();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, String> getTest_1() {
        return Test_1;
    }

    public void setTest_1(Map<String, String> test_1) {
        Test_1 = test_1;
    }

    public Map<String, String> getTest_2() {
        return Test_2;
    }

    public void setTest_2(Map<String, String> test_2) {
        Test_2 = test_2;
    }

    public Map<String, String> getFinal_Result() {
        return Final_Result;
    }

    public void setFinal_Result(Map<String, String> final_Result) {
        Final_Result = final_Result;
    }

    public Result() {
    }

    public Result(String studentClass, String uid, Map<String, String> test_1, Map<String, String> test_2, Map<String, String> final_Result) {
        this.studentClass = studentClass;
        this.uid = uid;
        Test_1 = test_1;
        Test_2 = test_2;
        Final_Result = final_Result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentClass);
        dest.writeString(uid);
    }
}
