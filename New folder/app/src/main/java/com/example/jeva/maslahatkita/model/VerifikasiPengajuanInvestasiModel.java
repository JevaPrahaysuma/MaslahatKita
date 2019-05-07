package com.example.jeva.maslahatkita.model;

public class VerifikasiPengajuanInvestasiModel {
    String idInvestor;
    String namaInverstor;
    String namaUsaha;
    String idUsaha;
    String email;
    double jumlahInvestasi;
    int status;
    public VerifikasiPengajuanInvestasiModel(){}

    public VerifikasiPengajuanInvestasiModel(String idInvestor, String namaInverstor, String namaUsaha, String idUsaha, String email, double jumlahInvestasi, int status) {
        this.idInvestor = idInvestor;
        this.namaInverstor = namaInverstor;
        this.namaUsaha = namaUsaha;
        this.idUsaha = idUsaha;
        this.email = email;
        this.jumlahInvestasi = jumlahInvestasi;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNamaUsaha() {
        return namaUsaha;
    }

    public void setNamaUsaha(String namaUsaha) {
        this.namaUsaha = namaUsaha;
    }

    public String getIdUsaha() {
        return idUsaha;
    }

    public void setIdUsaha(String idUsaha) {
        this.idUsaha = idUsaha;
    }

    public String getIdInvestor() {
        return idInvestor;
    }

    public void setIdInvestor(String idInvestor) {
        this.idInvestor = idInvestor;
    }

    public String getNamaInverstor() {
        return namaInverstor;
    }

    public void setNamaInverstor(String namaInverstor) {
        this.namaInverstor = namaInverstor;
    }

    public double getJumlahInvestasi() {
        return jumlahInvestasi;
    }

    public void setJumlahInvestasi(double jumlahInvestasi) {
        this.jumlahInvestasi = jumlahInvestasi;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
