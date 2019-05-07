package com.example.jeva.maslahatkita.model;

import java.io.Serializable;

public class InvestorApproval implements Serializable {
    Double jumlah;
    Boolean status;

    public InvestorApproval() {
    }

    public InvestorApproval(Double jumlah, Boolean status) {
        this.jumlah = jumlah;
        this.status = status;
    }

    public Double getJumlah() {
        return jumlah;
    }

    public void setJumlah(Double jumlah) {
        this.jumlah = jumlah;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
