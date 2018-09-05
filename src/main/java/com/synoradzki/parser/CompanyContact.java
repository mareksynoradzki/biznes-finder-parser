package com.synoradzki.parser;

public class CompanyContact {

    public CompanyContact(String name, String phone, String email, String city, String street, String buildingNo, String category, String pageUrl) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.street = street;
        this.buildingNo = buildingNo;
        this.category = category;
        this.pageUrl = pageUrl;
    }

    String name;
    String phone;
    String email;
    String city;
    String street;
    String buildingNo;
    String category;
    String pageUrl;

    @Override
    public String toString() {
        return "CompanyContact{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", buildingNo='" + buildingNo + '\'' +
                '}';
    }
}
