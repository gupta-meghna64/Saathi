package com.example.saathi;

/**
 * Created by HP on 18-05-2017.
 */

public class Courses {

    private String courseNumber;
    private String courseName;
    private String credits;

    public Courses() {

    }
    public void setCourseNumber(String courseNo)
    {
        this.courseNumber = courseNo;
    }

    public String getCourseNumber()
    {
        return courseNumber;
    }

    public void setCourseName(String name)
    {
        this.courseName = name;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public void setCredits(String cred)
    {
        this.credits = cred;
    }

    public String getCredits()
    {
        return credits;
    }

}
