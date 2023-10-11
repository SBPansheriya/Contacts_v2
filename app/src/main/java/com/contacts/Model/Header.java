package com.contacts.Model;

import java.util.List;

public class Header {

    public String header;
//    public boolean isSection;
    private List<Users> usersList;

    public Header() {
    }


        public Header(String header, List<Users> usersList) {
        this.header = header;
        this.usersList = usersList;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }
}
