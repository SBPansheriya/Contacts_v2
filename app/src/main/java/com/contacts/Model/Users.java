package com.contacts.Model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Users implements Serializable {
    public String contactId;
    public String image;
    public String fullName;
    public String first;
    public String last;
    public String personPhone;
    public String officePhone;
    private boolean isSelected;

    public Users() {
    }

    public Users(String contactId, String image, String fullName,String first, String last, String personPhone, String officePhone) {
        this.contactId = contactId;
        this.image = image;
        this.fullName = fullName;
        this.first = first;
        this.last = last;
        this.personPhone = personPhone;
        this.officePhone = officePhone;
        isSelected = false;

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
