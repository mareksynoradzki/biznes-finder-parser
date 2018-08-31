package com.synoradzki.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CompanyContact {

    public CompanyContact(String name, String phone, String email, String city, String street, String buildingNo) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.street = street;
        this.buildingNo = buildingNo;
    }

    String name;
    String phone;
    String email;
    String city;
    String street;
    String buildingNo;

    List<String> propertiesToList() {
        return Arrays.asList(
                eNull(name),
                eNull(phone),
                eNull(email),
                eNull(city),
                eNull(street),
                eNull(buildingNo));
//        String separator = ";";
//        return new StringBuilder()
//                .append(Objects.isNull(name) ? "" : this.name)
//                .append(separator)
//                .append(Objects.isNull(phone) ? "" : this.phone)
//                .append(separator)
//                .append(Objects.isNull(email) ? "" : this.email)
//                .append(separator)
//                .append(Objects.isNull(city) ? "" : this.city)
//                .append(separator)
//                .append(Objects.isNull(street) ? "" : this.street)
//                .append(separator)
//                .append(Objects.isNull(buildingNo) ? "" : this.buildingNo)
//                .append(separator)
//                .toString();
    }
    private String eNull(String value){
        return Objects.isNull(value)? "": value;
    }

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
