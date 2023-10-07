package com.contacts.Model;

import java.util.List;

public class Header {

    String header;
    private List<Users> ChildItemList;

    public Header(String header, List<Users> childItemList) {
        this.header = header;
        ChildItemList = childItemList;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Users> getChildItemList() {
        return ChildItemList;
    }

    public void setChildItemList(List<Users> childItemList) {
        ChildItemList = childItemList;
    }
}
