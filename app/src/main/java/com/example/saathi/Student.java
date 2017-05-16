package com.example.saathi;


public class Student {
    //name and address string
    private String studentName;
    private String studentMobileNumber;
    private String studentMajor;
    private String studentBatch;
    private String studentRollNo;
    private String imageID;
    private String emailId;

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

    public void setImage(String imageId) {
        this.imageID = imageId;
    }

    public String getImage() {
        return imageID;
    }

    public void setEmail(String emailID) {
        this.emailId = emailID;
    }

    public String getEmailID() {
        return emailId;
    }


}
