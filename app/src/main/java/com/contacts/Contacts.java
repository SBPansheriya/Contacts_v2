package com.contacts;

import java.io.Serializable;

public class Contacts implements Serializable {
    String Id;
    String Image;
    String Name;
    String MemberNumber;

    public Contacts(String id, String image, String name, String memberNumber) {
        Id = id;
        Image = image;
        Name = name;
        MemberNumber = memberNumber;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMemberNumber() {
        return MemberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        MemberNumber = memberNumber;
    }
}
