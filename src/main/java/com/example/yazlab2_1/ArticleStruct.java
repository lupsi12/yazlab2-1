package com.example.yazlab2_1;

public class ArticleStruct
{
    public String yayinAd;
    public String yazarIsim;
    public String yayinTur;
    public String yayinTarih;
    public String yayinciAdi;
    public String aramaAnahtarKelime;
    public String makaleAnahtarKelime;
    public String ozet;
    public int alintiSayisi;
    public String doiNumarasi;
    public String urlAdresi;
    public String references;
    public String pdfLink;

    public ArticleStruct()
    {
        this.yayinAd = null;
        this.yazarIsim = null;
        this.yayinTur = null;
        this.yayinTarih = null;
        this.yayinciAdi = null;
        this.aramaAnahtarKelime = null;
        this.makaleAnahtarKelime = null;
        this.ozet = null;
        this.alintiSayisi = 0;
        this.doiNumarasi = null;
        this.urlAdresi = null;
        this.references = null;
        this.pdfLink = null;
    }

    @Override
    public String toString() {
        return "{" +
                "\n\"yayinAd\": \"" + yayinAd + '"' +
                ",\n\"yazarIsim\": \"" + yazarIsim + '"' +
                ",\n\"yayinTur\": \"" + yayinTur + '"' +
                ",\n\"yayinTarih\": \"" + yayinTarih + '"' +
                ",\n\"yayinciAdi\": \"" + yayinciAdi + '"' +
                ",\n\"aramaAnahtarKelime\": \"" + aramaAnahtarKelime + '"' +
                ",\n\"makaleAnahtarKelime\": \"" + makaleAnahtarKelime + '"' +
                ",\n\"ozet\": \"" + ozet + '"' +
                ",\n\"alintiSayisi\": " + alintiSayisi +
                ",\n\"doiNumarasi\": " + doiNumarasi +
                ",\n\"urlAdresi\": \"" + urlAdresi + '"' +
                ",\n\"pdfLink\": \"" + pdfLink + '"' +
                //",\n\"references\": \"" + references + '"' +
                "\n}";
    }
}
