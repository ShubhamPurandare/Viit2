package college.root.viit2.Realm;

import io.realm.RealmObject;

/**
 * Created by root on 7/12/16.
 */

public class UserInfo extends RealmObject {

    int uid ;
    String name , year, branch , grnumber , contact;

    public UserInfo() {
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGrnumber() {
        return grnumber;
    }

    public void setGrnumber(String grnumber) {
        this.grnumber = grnumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
