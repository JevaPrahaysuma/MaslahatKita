package com.example.jeva.maslahatkita.model;

public class VirtualAccountInvestorModel {
    double danaInvestasi;
    double danaPengembalian;
    String idInvestor;
    double saldo;

    public VirtualAccountInvestorModel() {
    }

    public VirtualAccountInvestorModel(double saldo, double danaInvestasi, double danaPengembalian, String idInvestor) {
        this.saldo = saldo;
        this.danaInvestasi = danaInvestasi;
        this.danaPengembalian = danaPengembalian;
        this.idInvestor = idInvestor;
    }

    public String getIdInvestor() {
        return idInvestor;
    }

    public void setIdInvestor(String idInvestor) {
        this.idInvestor = idInvestor;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getDanaInvestasi() {
        return danaInvestasi;
    }

    public void setDanaInvestasi(double danaInvestasi) {
        this.danaInvestasi = danaInvestasi;
    }

    public double getDanaPengembalian() {
        return danaPengembalian;
    }

    public void setDanaPengembalian(double danaPengembalian) {
        this.danaPengembalian = danaPengembalian;
    }

}
