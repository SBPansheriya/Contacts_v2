package com.contacts.Model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Users {
    public String image;
    public String first;
    public String last;
    public String personPhone;
    public String officePhone;

//    public Users(String image, String first, String last, String personPhone, String officePhone) {
//        this.image = image;
//        this.first = first;
//        this.last = last;
//        this.personPhone = personPhone;
//        this.officePhone = officePhone;
//    }

    public Users() {
    }

    public Users(String first, String personPhone) {
        this.first = first;
        this.personPhone = personPhone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getPersonPhone() {
        return personPhone;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

}
