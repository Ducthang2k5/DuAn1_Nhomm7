package com.example.duan1_nhom7.home.model;

public class Account {
    private String id;
    private String fullName;
    private String email;
    private String avatarUrl;
    private String phone;
    private String birthday;
    private String address;

    public Account() {
        // Constructor rỗng để Firestore sử dụng
    }

    public Account(String id, String fullName, String email, String avatarUrl, String phone, String birthday, String address) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.phone = phone;
        this.birthday = birthday;
        this.address = address;
    }

    // Getter & Setter
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getPhone() { return phone; }
    public String getBirthday() { return birthday; }
    public String getAddress() { return address; }

    public void setId(String id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setAddress(String address) { this.address = address; }
}
