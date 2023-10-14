package com.contacts.Model;

import java.util.ArrayList;
import java.util.List;

public class Header {

    public String header;
    public ArrayList<Users> usersList;


    public Header(String header, ArrayList<Users> usersList) {
        this.header = header;
        this.usersList = usersList;
    }
}
