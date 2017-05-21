package com.example.saathi;

/**
 * Created by HP on 18-05-2017.
 */

public class Courses {

    private String courseCode;
    private String courseName;
    private String courseCredits;
    private String courseInstructor;
    private String courseLocation;
    private String coursePreReq;
    private String courseTimeTable;


    public Courses() {

    }
    public void setcourseCode(String courseNo)
    {
        this.courseCode = courseNo;
    }

    public String getcourseCode()
    {
        return courseCode;
    }

    public void setCourseName(String name)
    {
        this.courseName = name;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setcourseCredits(String cred)
    {
        this.courseCredits = cred;
    }

    public String getcourseCredits()
    {
        return courseCredits;
    }

    public String getCourseTimeTable()
    {
        return courseTimeTable;
    }

    public String getCourseInstructor()
    {
        return courseInstructor;
    }

    public String getCourseLocation()
    {
        return courseLocation;
    }

    public String getCoursePreReq()
    {
        return coursePreReq;
    }
}
