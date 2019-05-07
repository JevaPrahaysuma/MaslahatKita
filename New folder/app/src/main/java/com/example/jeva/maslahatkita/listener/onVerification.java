package com.example.jeva.maslahatkita.listener;

import com.example.jeva.maslahatkita.model.ValidasiInvestasiModel;

public interface onVerification {
    void onApprove(String idInvestor, String idUsaha, double nilai, double danaYangTerkumpul, ValidasiInvestasiModel vi);
    void onReject(String idInvestor, String idUsaha);
}
