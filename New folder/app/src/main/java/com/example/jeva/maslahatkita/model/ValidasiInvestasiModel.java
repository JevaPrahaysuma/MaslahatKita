package com.example.jeva.maslahatkita.model;

import java.io.Serializable;

public class ValidasiInvestasiModel implements Serializable {
    String idUsaha;
    String tglPembiayaan;
    String namaUsaha;
    double danaInvestasi;
    public  ValidasiInvestasiModel(){}
    public ValidasiInvestasiModel(String idUsaha, String tglPembiayaan, String namaUsaha, double danaInvestasi) {
        this.idUsaha = idUsaha;
        this.tglPembiayaan = tglPembiayaan;
        this.namaUsaha = namaUsaha;
        this.danaInvestasi = danaInvestasi;
    }

    public String getIdUsaha() {
        return idUsaha;
    }

    public void setIdUsaha(String idUsaha) {
        this.idUsaha = idUsaha;
    }

    public String getTglPembiayaan() {
        return tglPembiayaan;
    }

    public void setTglPembiayaan(String tglPembiayaan) {
        this.tglPembiayaan = tglPembiayaan;
    }

    public String getNamaUsaha() {
        return namaUsaha;
    }

    public void setNamaUsaha(String namaUsaha) {
        this.namaUsaha = namaUsaha;
    }


    public double getDanaInvestasi() {
        return danaInvestasi;
    }

    public void setDanaInvestasi(double danaInvestasi) {
        this.danaInvestasi = danaInvestasi;
    }
}
