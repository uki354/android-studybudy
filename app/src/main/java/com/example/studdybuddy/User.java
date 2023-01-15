package com.example.studdybuddy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class User implements Parcelable {

    private int id;
    private String email;
    private String password;
    private String name;
    private String lastname;
    private Boolean gender;
    private String university;
    private Date birthdate;
    private int age;
    private String currentAddress;
    private String lat;
    private String lng;
    private String imagePath;
    private String jwt;

    protected User(Parcel in) {
        id = in.readInt();
        email = in.readString();
        password = in.readString();
        name = in.readString();
        lastname = in.readString();
        byte tmpGender = in.readByte();
        gender = tmpGender == 0 ? null : tmpGender == 1;
        university = in.readString();
        age = in.readInt();
        currentAddress = in.readString();
        lat = in.readString();
        lng = in.readString();
        imagePath = in.readString();
        jwt = in.readString();
    }

    public User(){
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(name);
        parcel.writeString(lastname);
        parcel.writeByte((byte) (gender == null ? 0 : gender ? 1 : 2));
        parcel.writeString(university);
        parcel.writeInt(age);
        parcel.writeString(currentAddress);
        parcel.writeString(lat);
        parcel.writeString(lng);
        parcel.writeString(imagePath);
        parcel.writeString(jwt);
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
