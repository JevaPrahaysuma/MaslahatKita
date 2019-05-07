package com.example.jeva.maslahatkita.model;

import java.io.Serializable;

public class InvestorManagementModel implements Serializable {
    String email;
    String nama;
    String alamat;
    String noHp;
    String noTelp;
    String noKTP;
    String foto;
    String fotoKtp;
    String userId;
    int statusInvestor;
    public InvestorManagementModel(){}

    public InvestorManagementModel(String nama, String alamat, String noHp, String noTelp, String noKTP, String foto, String fotoKtp) {
        this.nama = nama;
        this.alamat = alamat;
        this.noHp = noHp;
        this.noTelp = noTelp;
        this.noKTP = noKTP;
        this.foto = foto;
        this.fotoKtp = fotoKtp;
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

    public int getStatusInvestor() {
        return statusInvestor;
    }

    public void setStatusInvestor(int statusInvestor) {
        this.statusInvestor = statusInvestor;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getNoKTP() {
        return noKTP;
    }

    public void setNoKTP(String noKTP) {
        this.noKTP = noKTP;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFotoKtp() {
        return fotoKtp;
    }

    public void setFotoKtp(String fotoKtp) {
        this.fotoKtp = fotoKtp;
    }


}
