package com.contacts.Model;

import android.graphics.Bitmap;

import java.io.File;
import java.io.Serializable;

public class Recent implements Serializable {
    public String contactId;
    public String iamge;
    public String contactname;
    public String contactnumber;
    public String date;
    public String status;

    public Recent(String contactId, String iamge, String contactname, String contactnumber, String date, String status) {
        this.contactId = contactId;
        this.iamge = iamge;
        this.contactname = contactname;
        this.contactnumber = contactnumber;
        this.date = date;
        this.status = status;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getIamge() {
        return iamge;
    }

    public void setIamge(String iamge) {
        this.iamge = iamge;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
