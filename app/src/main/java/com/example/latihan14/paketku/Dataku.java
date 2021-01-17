package com.example.latihan14.paketku;

public class Dataku {
    String kunci;
    String isiNim;
    String isiNama;

    public Dataku(){

    }

    public Dataku(String kunci, String isiNim, String isiNama) {
        this.kunci = kunci;
        this.isiNim = isiNim;
        this.isiNama = isiNama;
    }

    public String getKunci() {
        return kunci;
    }

    public void setKunci(String kunci) {
        this.kunci = kunci;
    }

    public String getIsiNim() {
        return isiNim;
    }

    public void setIsiNim(String isiNim) {
        this.isiNim = isiNim;
    }

    public String getIsiNama() {
        return isiNama;
    }

    public void setIsiNama(String isiNama) {
        this.isiNama = isiNama;
    }
}
