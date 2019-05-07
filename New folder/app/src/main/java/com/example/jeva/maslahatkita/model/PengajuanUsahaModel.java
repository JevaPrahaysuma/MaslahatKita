package com.example.jeva.maslahatkita.model;

import java.io.Serializable;
import java.util.HashMap;

public class PengajuanUsahaModel implements Serializable {
    String mitraId;
    String nama;
    String tanggalPengajuan;
    String jangkaWaktu;
    double danaDibutuhkan;
    double margin;
    String tujuanPengajuan;
    String namaUsaha;
    String alamatUsaha;
    String bidangUsaha;
    String diskripsiUsaha;
    String foto1;
    String foto2;
    String proposal;
    String key;
    String email;
    double danaYangTerkumpul;
    int statusUsaha;
    HashMap<String, InvestorApproval> investor;

    public String getMitraId() {
        return mitraId;
    }

    public void setMitraId(String mitraId) {
        this.mitraId = mitraId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTanggalPengajuan() {
        return tanggalPengajuan;
    }

    public void setTanggalPengajuan(String tanggalPengajuan) {
        this.tanggalPengajuan = tanggalPengajuan;
    }

    public String getJangkaWaktu() {
        return jangkaWaktu;
    }

    public void setJangkaWaktu(String jangkaWaktu) {
        this.jangkaWaktu = jangkaWaktu;
    }

    public double getDanaDibutuhkan() {
        return danaDibutuhkan;
    }

    public void setDanaDibutuhkan(double danaDibutuhkan) {
        this.danaDibutuhkan = danaDibutuhkan;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public String getTujuanPengajuan() {
        return tujuanPengajuan;
    }

    public void setTujuanPengajuan(String tujuanPengajuan) {
        this.tujuanPengajuan = tujuanPengajuan;
    }

    public String getNamaUsaha() {
        return namaUsaha;
    }

    public void setNamaUsaha(String namaUsaha) {
        this.namaUsaha = namaUsaha;
    }

    public String getAlamatUsaha() {
        return alamatUsaha;
    }

    public void setAlamatUsaha(String alamatUsaha) {
        this.alamatUsaha = alamatUsaha;
    }

    public String getBidangUsaha() {
        return bidangUsaha;
    }

    public void setBidangUsaha(String bidangUsaha) {
        this.bidangUsaha = bidangUsaha;
    }

    public String getDiskripsiUsaha() {
        return diskripsiUsaha;
    }

    public void setDiskripsiUsaha(String diskripsiUsaha) {
        this.diskripsiUsaha = diskripsiUsaha;
    }

    public String getFoto1() {
        return foto1;
    }

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }

    public String getFoto2() {
        return foto2;
    }

    public void setFoto2(String foto2) {
        this.foto2 = foto2;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getDanaYangTerkumpul() {
        return danaYangTerkumpul;
    }

    public void setDanaYangTerkumpul(double danaYangTerkumpul) {
        this.danaYangTerkumpul = danaYangTerkumpul;
    }

    public int getStatusUsaha() {
        return statusUsaha;
    }

    public void setStatusUsaha(int statusUsaha) {
        this.statusUsaha = statusUsaha;
    }

    public HashMap<String, InvestorApproval> getInvestor() {
        return investor;
    }

    public void setInvestor(HashMap<String, InvestorApproval> investor) {
        this.investor = investor;
    }

    public PengajuanUsahaModel() {
    }

    public PengajuanUsahaModel(String mitraId, String nama, String tanggalPengajuan, String jangkaWaktu, double danaDibutuhkan, double margin, String tujuanPengajuan, String namaUsaha, String alamatUsaha, String bidangUsaha, String diskripsiUsaha, String foto1, String foto2, String proposal, String key, String email, double danaYangTerkumpul, int statusUsaha) {
        this.mitraId = mitraId;
        this.nama = nama;
        this.tanggalPengajuan = tanggalPengajuan;
        this.jangkaWaktu = jangkaWaktu;
        this.danaDibutuhkan = danaDibutuhkan;
        this.margin = margin;
        this.tujuanPengajuan = tujuanPengajuan;
        this.namaUsaha = namaUsaha;
        this.alamatUsaha = alamatUsaha;
        this.bidangUsaha = bidangUsaha;
        this.diskripsiUsaha = diskripsiUsaha;
        this.foto1 = foto1;
        this.foto2 = foto2;
        this.proposal = proposal;
        this.key = key;
        this.email = email;
        this.danaYangTerkumpul = danaYangTerkumpul;
        this.statusUsaha = statusUsaha;
        this.investor = new HashMap<>();
    }
}
