package com.example.jeva.maslahatkita.model.nonFirebase;

public class ApproveModel {
    String namaUsaha, namaInvestor, idInvestor, idPengajuanUsaha;
    Double jumlahInvestasi, danaYangTerkumpul;

    public ApproveModel() {
    }

    public Double getDanaYangTerkumpul() {
        return danaYangTerkumpul;
    }

    public void setDanaYangTerkumpul(Double danaYangTerkumpul) {
        this.danaYangTerkumpul = danaYangTerkumpul;
    }

    public String getIdPengajuanUsaha() {
        return idPengajuanUsaha;
    }

    public void setIdPengajuanUsaha(String idPengajuanUsaha) {
        this.idPengajuanUsaha = idPengajuanUsaha;
    }

    public String getIdInvestor() {
        return idInvestor;
    }

    public void setIdInvestor(String idInvestor) {
        this.idInvestor = idInvestor;
    }

    public String getNamaUsaha() {
        return namaUsaha;
    }

    public void setNamaUsaha(String namaUsaha) {
        this.namaUsaha = namaUsaha;
    }

    public String getNamaInvestor() {
        return namaInvestor;
    }

    public void setNamaInvestor(String namaInvestor) {
        this.namaInvestor = namaInvestor;
    }

    public Double getJumlahInvestasi() {
        return jumlahInvestasi;
    }

    public void setJumlahInvestasi(Double jumlahInvestasi) {
        this.jumlahInvestasi = jumlahInvestasi;
    }
}
