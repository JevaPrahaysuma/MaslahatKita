package com.example.jeva.maslahatkita.model.nonFirebase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class InvestorModel implements Serializable {
    HashMap<String,String> listInvestorNama;

    public InvestorModel(HashMap<String, String> listInvestorNama) {
        this.listInvestorNama = listInvestorNama;
    }

    public HashMap<String, String> getListInvestorNama() {
        return listInvestorNama;
    }

    public void setListInvestorNama(HashMap<String, String> listInvestorNama) {
        this.listInvestorNama = listInvestorNama;
    }
}
