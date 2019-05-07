package com.example.jeva.maslahatkita.model;

import java.io.Serializable;

public class UserManagementModel implements Serializable {
    String email;
    String namadepan;
    String namabelakang;
    String jenis;
    String userId;
    int statusVerifikasi;


    public  UserManagementModel(){}
    public UserManagementModel(String email, String namadepan, String namabelakang, String jenis,String userId) {
        this.email = email;
        this.namadepan = namadepan;
        this.namabelakang = namabelakang;
        this.jenis = jenis;
        this.userId = userId;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNamadepan() {
        return namadepan;
    }

    public void setNamadepan(String namadepan) {
        this.namadepan = namadepan;
    }

    public String getNamabelakang() {
        return namabelakang;
    }

    public void setNamabelakang(String namabelakang) {
        this.namabelakang = namabelakang;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public int getStatusVerifikasi() {
        return statusVerifikasi;
    }
    public void setStatusVerifikasi(int statusVerifikasi) {
        this.statusVerifikasi = statusVerifikasi;
    }
}
