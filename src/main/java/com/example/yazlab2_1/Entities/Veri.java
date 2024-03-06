package com.example.yazlab2_1.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "veri")
public class Veri {
    @Id
    private Integer id;
    private String yayinAd;
    private String yazarIsim;
    private String yayinTur;
    private String yayinTarih;
    private String yayinciAdi;
    private String aramaAnahtarKelime;
    private String makaleAnahtarKelime;
    private String ozet;
    private int alintiSayisi;
    private int doiNumarasi;
    private String urlAdresi;

}
