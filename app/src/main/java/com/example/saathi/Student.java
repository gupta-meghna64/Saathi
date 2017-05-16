package com.example.saathi;


public class Student {
    //name and address string
    private String studentName;
    private String studentMobileNumber;
    private String studentMajor;
    private String studentBatch;
    private String studentRollNo;

    public Student() {
      /*Blank default constructor essential for Firebase*/
    }

    //Getters and setters
    public String getName() {
        return studentName;
    }

    public void setName(String name) {
        this.studentName = name;
    }

    public String getMobile() {
        return studentMobileNumber;
    }

    public void setMobile(String number) {
        this.studentMobileNumber = number;
    }

    public String getMajor() {
        return studentMajor;
    }

    public void setMajor(String major) {
        this.studentMajor = major;
    }

    public String getBatch() {
        return studentBatch;
    }

    public void setBatch(String batch) {
        this.studentBatch = batch;
    }

    public String getRollNo() {
        return studentRollNo;
    }

    public void setRollNo(String rollNo) {
        this.studentRollNo = rollNo;
    }    


}