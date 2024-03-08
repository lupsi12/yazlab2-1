package com.example.yazlab2_1.Requests;

import com.example.yazlab2_1.Entities.Referans;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeriUpdateRequest {
    private String yayinAd;
    private String yazarIsim;
    private String yayinTur;
    //@Temporal(TemporalType.DATE)
    private String yayinTarih;
    private String yayinciAdi;
    private String aramaAnahtarKelime;
    private String makaleAnahtarKelime;
    private String ozet;
    private int alintiSayisi;
    private int doiNumarasi;
    private String urlAdresi;
    /*
    @ManyToOne
    @JoinColumn(name = "referans_id" , referencedColumnName = "id")
    @JsonBackReference
    private Referans referans;

     */
}

