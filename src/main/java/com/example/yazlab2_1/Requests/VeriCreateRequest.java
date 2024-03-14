package com.example.yazlab2_1.Requests;

import com.example.yazlab2_1.Entities.Referans;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeriCreateRequest {
    public static final String SEQUENCE_NAME = "veri_sequence";
    private Long id;
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
    private String doiNumarasi;
    private String urlAdresi;
    private String urlLink;
    /*
    @ManyToOne
    @JoinColumn(name = "referans_id" , referencedColumnName = "id")
    @JsonBackReference
    private Referans referans;

     */
}
