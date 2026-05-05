// model/AdminUser.java
package com.se1020.moviebooking.model;

public class AdminUser extends User {

    private String adminCode;  // unique code assigned to admin accounts

    public AdminUser() {
        super();
    }

    public AdminUser(int id, String name, String email, String password,
                     String phone, String adminCode) {
        super(id, name, email, password, phone, "ADMIN");
        this.adminCode = adminCode;
    }

    @Override
    public String getDashboardPath() {
        return "/admin/dashboard";  // admins go to a different dashboard
    }

    public String getAdminCode() { return adminCode; }
    public void setAdminCode(String adminCode) { this.adminCode = adminCode; }
}