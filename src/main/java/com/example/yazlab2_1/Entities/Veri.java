package com.example.yazlab2_1.Entities;

import com.example.yazlab2_1.Responses.VeriResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "veri")
public class Veri {
    @Transient
    public static final String SEQUENCE_NAME = "veri_sequence";

    @Id
    //@GeneratedValue(strategy =  GenerationType.IDENTITY)
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
    private String doiNumarasi;
    private String urlAdresi;
    private String urlLink;

    public Veri(VeriResponse veriEntity) {
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
        this.urlLink = veriEntity.getUrlLink();
    }
    /*
    @ManyToOne
    @JoinColumn(name = "referans_id" , referencedColumnName = "id")
    @JsonBackReference
    private Referans referans;

    @DBRef
    private List<Referans> referans;*/
}
