package com.example.yazlab2_1.Responses;

import com.example.yazlab2_1.Entities.Veri;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeriResponse {
    private Long id;
    private String yayinAd;
    private String yazarIsim;
    private String yayinTur;
    //@Temporal(TemporalType.DATE)
    //private Date yayinTarih;
    private String yayinTarih;
    private String yayinciAdi;
    private String aramaAnahtarKelime;
    private String makaleAnahtarKelime;
    private String ozet;
    private int alintiSayisi;
    private int doiNumarasi;
    private String urlAdresi;

    public VeriResponse(Veri veriEntity) {
        this.id = veriEntity.getId();
        this.yayinAd = veriEntity.getYayinAd();
        this.yazarIsim = veriEntity.getYazarIsim();
        this.yayinTur = veriEntity.getYayinTur();
        this.yayinTarih = veriEntity.getYayinTarih();
        this.yayinciAdi = veriEntity.getYayinciAdi();
        this.aramaAnahtarKelime = veriEntity.getAramaAnahtarKelime();
        this.makaleAnahtarKelime = veriEntity.getMakaleAnahtarKelime();
        this.ozet = veriEntity.getOzet();
        this.alintiSayisi = veriEntity.getAlintiSayisi();
        this.doiNumarasi = veriEntity.getDoiNumarasi();
        this.urlAdresi = veriEntity.getUrlAdresi();
    }
}
