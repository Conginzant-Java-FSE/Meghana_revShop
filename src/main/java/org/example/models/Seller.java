package org.example.models;

public class Seller {

    private int sellerId;
    private String businessName;
    private String ownerName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private boolean active;


    public Seller() {}


    public Seller(String businessName, String ownerName, String email, String password,
                  String phone, String address){

        this.businessName = businessName;
        this.ownerName = ownerName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.active = true;
    }



    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
