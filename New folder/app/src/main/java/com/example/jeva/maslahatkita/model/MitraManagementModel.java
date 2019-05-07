package com.example.jeva.maslahatkita.model;

import java.io.Serializable;

public class MitraManagementModel implements Serializable {
    private String nama;
    private String alamat;
    private String noKtp;
    private String alamatKtp;
    private String noHp;
    private String userId;
    private String foto;
    private String fotoKtp;
    private String email;
    private int statusMitra;
    public  MitraManagementModel(){}

    public MitraManagementModel(String nama, String alamat, String noKtp, String alamatKtp, String userId, String noHp) {
        this.nama = nama;
        this.alamat = alamat;
        this.noKtp = noKtp;
        this.alamatKtp = alamatKtp;
        this.userId = userId;
        this.noHp = noHp;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public int getStatusMitra() {
        return statusMitra;
    }

    public void setStatusMitra(int statusMitra) {
        this.statusMitra = statusMitra;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFotoKtp() {
        return fotoKtp;
    }

    public void setFotoKtp(String fotoKtp) {
        this.fotoKtp = fotoKtp;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public String getNoKtp() {
        return noKtp;
    }

    public void setNoKtp(String noKtp) {
        this.noKtp = noKtp;
    }

    public String getAlamatKtp() {
        return alamatKtp;
    }

    public void setAlamatKtp(String alamatKtp) {
        this.alamatKtp = alamatKtp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
