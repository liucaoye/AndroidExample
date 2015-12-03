package com.example.app.contacts.model;

/**
 * @author LIUYAN
 * @date 2015/12/1
 * @time 16:31
 */
public class ContactItem {
    private String name;
    private String phone;

    public ContactItem(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
