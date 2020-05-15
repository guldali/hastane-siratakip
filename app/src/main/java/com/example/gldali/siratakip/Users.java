package com.example.gldali.siratakip;

public class Users {
    public String ad;
    public String soyAd;
    public String tc;
    public String poliklinik;
    public String doktorAdi;
    public String zaman;
    public  String sira;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Users(String ad, String soyAd,String tc, String poliklinik,String doktorAdi,String zaman,String sira) {
        this.ad = ad;
        this.soyAd = soyAd;
        this.tc = tc;
        this.poliklinik=poliklinik;
        this.doktorAdi=doktorAdi;
        this.zaman=zaman;
        this.sira=sira;
    }
}
