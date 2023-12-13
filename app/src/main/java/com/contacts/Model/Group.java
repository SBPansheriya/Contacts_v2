package com.contacts.Model;

import java.io.Serializable;

public class Group implements Serializable {
    String groupId;
    String groupName;
    int groupMemberNumber;


    public Group() {
    }

    public Group(String groupId, String groupName, int groupMemberNumber) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupMemberNumber = groupMemberNumber;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupMemberNumber() {
        return groupMemberNumber;
    }

    public void setGroupMemberNumber(int groupMemberNumber) {
        this.groupMemberNumber = groupMemberNumber;
    }

}
