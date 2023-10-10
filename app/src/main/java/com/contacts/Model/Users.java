package com.contacts.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Users implements Parcelable {
    String image;
    String first;
    String last;
    String personPhone;
    String officePhone;

    public Users(String image, String first, String last, String personPhone, String officePhone) {
        this.image = image;
        this.first = first;
        this.last = last;
        this.personPhone = personPhone;
        this.officePhone = officePhone;
    }

    protected Users(Parcel in) {
        image = in.readString();
        first = in.readString();
        last = in.readString();
        personPhone = in.readString();
        officePhone = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(image);
        parcel.writeString(first);
        parcel.writeString(last);
        parcel.writeString(personPhone);
        parcel.writeString(officePhone);
    }
}
