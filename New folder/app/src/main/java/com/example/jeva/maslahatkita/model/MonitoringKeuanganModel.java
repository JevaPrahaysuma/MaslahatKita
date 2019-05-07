package com.example.jeva.maslahatkita.model;

public class MonitoringKeuanganModel {
    String tanggal;
    double danaKeluar;
    double danaMasuk;
    double saldoAkhir;
    String rincian;
    String idInvestor;

    public String getIdInvestor() {
        return idInvestor;
    }

    public void setIdInvestor(String idInvestor) {
        this.idInvestor = idInvestor;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public double getDanaKeluar() {
        return danaKeluar;
    }

    public void setDanaKeluar(double danaKeluar) {
        this.danaKeluar = danaKeluar;
    }

    public double getDanaMasuk() {
        return danaMasuk;
    }

    public void setDanaMasuk(double danaMasuk) {
        this.danaMasuk = danaMasuk;
    }

    public double getSaldoAkhir() {
        return saldoAkhir;
    }

    public void setSaldoAkhir(double saldoAkhir) {
        this.saldoAkhir = saldoAkhir;
    }

    public String getRincian() {
        return rincian;
    }

    public void setRincian(String rincian) {
        this.rincian = rincian;
    }
}
