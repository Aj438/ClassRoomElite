package com.ashish.classroomelite.Models;

public class Attendance {
    private String Roll_no;
    private int Present;

    public String getRoll_no() {
        return Roll_no;
    }

    public void setRoll_no(String roll_no) {
        Roll_no = roll_no;
    }

    public int getPresent() {
        return Present;
    }
   public int addPresent(){
        return ++Present;
   }
    public void setPresent(int present) {
        Present = present;
    }

    public Attendance() {
    }

    public Attendance(String roll_no, int present) {
        Roll_no = roll_no;
        Present = present;
    }
}
