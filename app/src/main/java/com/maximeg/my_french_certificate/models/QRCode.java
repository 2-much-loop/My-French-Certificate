package com.maximeg.my_french_certificate.models;

public class QRCode {
    private long id;
    private String firstName;
    private String lastName;
    private String fileName;
    private String date;
    private String time;

    public QRCode(long id, String firstName, String lastName, String fileName, String date, String time){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fileName = fileName;
        this.date = date;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
