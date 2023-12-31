package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

public class UserQuery extends BaseQuery {
    // ⽤户名
    private String userName;
    // 邮箱
    private String email;
    // 电话
    private String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}