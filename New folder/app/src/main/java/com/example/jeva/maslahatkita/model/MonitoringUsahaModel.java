package com.example.jeva.maslahatkita.model;

import java.io.Serializable;

public class MonitoringUsahaModel implements Serializable  {
    String idUsaha;
    String tanggal;
    double labaBersih;
    double labaKotor;
    double biayaOprasional;
    int totalPenjualan;
    int angsuran;
    public  MonitoringUsahaModel(){}

    public MonitoringUsahaModel(String idUsaha, String tanggal, double labaBersih, double labaKotor, double biayaOprasional, int totalPenjualan, int angsuran) {
        this.idUsaha = idUsaha;
        this.tanggal = tanggal;
        this.labaBersih = labaBersih;
        this.labaKotor = labaKotor;
        this.biayaOprasional = biayaOprasional;
        this.totalPenjualan = totalPenjualan;
        this.angsuran = angsuran;
    }

    public int getAngsuran() {
        return angsuran;
    }

    public void setAngsuran(int angsuran) {
        this.angsuran = angsuran;
    }

    public String getIdUsaha() {
        return idUsaha;
    }

    public void setIdUsaha(String idUsaha) {
        this.idUsaha = idUsaha;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public double getLabaBersih() {
        return labaBersih;
    }

    public void setLabaBersih(double labaBersih) {
        this.labaBersih = labaBersih;
    }

    public double getLabaKotor() {
        return labaKotor;
    }

    public void setLabaKotor(double labaKotor) {
        this.labaKotor = labaKotor;
    }

    public double getBiayaOprasional() {
        return biayaOprasional;
    }

    public void setBiayaOprasional(double biayaOprasional) {
        this.biayaOprasional = biayaOprasional;
    }

    public int getTotalPenjualan() {
        return totalPenjualan;
    }

    public void setTotalPenjualan(int totalPenjualan) {
        this.totalPenjualan = totalPenjualan;
    }
}
