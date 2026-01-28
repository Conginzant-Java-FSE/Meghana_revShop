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


    private String gstNumber;
    private String businessType;
    private String website;
    private boolean verified;

    public Seller() {}


    public Seller(String businessName, String ownerName, String email, String password,
                  String phone, String address,
                  String gstNumber, String businessType, String website) {

        this.businessName = businessName;
        this.ownerName = ownerName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;

        this.gstNumber = gstNumber;
        this.businessType = businessType;
        this.website = website;

        this.verified = false; // default
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

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
