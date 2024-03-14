package com.example.yazlab2_1.Managers;

import com.example.yazlab2_1.Entities.Veri;
import com.example.yazlab2_1.Repository.VeriRepo;
import com.example.yazlab2_1.Requests.VeriCreateRequest;
import com.example.yazlab2_1.Requests.VeriUpdateRequest;
import com.example.yazlab2_1.Responses.VeriResponse;
import com.example.yazlab2_1.Services.SequenceGeneratorService;
import com.example.yazlab2_1.Services.VeriService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VeriManager implements VeriService {
    private VeriRepo veriRepo;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public VeriManager(VeriRepo veriRepo) {
        this.veriRepo = veriRepo;
    }

    @Override
    public List<VeriResponse> getAllVeri() {
        List<Veri> veriList = veriRepo.findAll();
        return veriList.stream().map(p -> new VeriResponse(p)).collect(Collectors.toList());
    }

    @Override
    public VeriResponse getVeriById(Long veriId) {
        Veri veri = veriRepo.findById(veriId).orElse(null);
        VeriResponse veriResponse = new VeriResponse(veri);
        return veriResponse;
    }

    @Override
    public Veri addVeri(VeriCreateRequest veriCreateRequest) {
        Veri veri = new Veri();
        veri.setId(sequenceGeneratorService.getSquenceNumber(veriCreateRequest.SEQUENCE_NAME));
        veri.setAlintiSayisi(veriCreateRequest.getAlintiSayisi());
        veri.setDoiNumarasi(veriCreateRequest.getDoiNumarasi());
        veri.setOzet(veriCreateRequest.getOzet());
        veri.setYayinAd(veriCreateRequest.getYayinAd());
        veri.setUrlAdresi(veriCreateRequest.getUrlAdresi());
        veri.setAramaAnahtarKelime(veriCreateRequest.getAramaAnahtarKelime());
        veri.setYayinTarih(veriCreateRequest.getYayinTarih());
        veri.setMakaleAnahtarKelime(veriCreateRequest.getMakaleAnahtarKelime());
        veri.setYazarIsim(veriCreateRequest.getYazarIsim());
        veri.setYayinciAdi(veriCreateRequest.getYayinciAdi());
        veri.setYayinTur(veriCreateRequest.getYayinTur());
        veri.setUrlLink(veriCreateRequest.getUrlLink());
        return veriRepo.save(veri);
    }

    @Override
    public void deleteVeri(Long id) {
        veriRepo.deleteById(id);
    }

    @Override
    public void deleteAllVeri() {
        veriRepo.deleteAll();
    }

    @Override
    public Veri updateVeri(Long id, VeriUpdateRequest veriUpdateRequest) {
        Optional<Veri> veri = veriRepo.findById(id);
        if (veri.isPresent()){
            Veri foundVeri = veri.get();
            foundVeri.setAlintiSayisi(veriUpdateRequest.getAlintiSayisi());
            foundVeri.setDoiNumarasi(veriUpdateRequest.getDoiNumarasi());
            foundVeri.setOzet(veriUpdateRequest.getOzet());
            foundVeri.setYayinAd(veriUpdateRequest.getYayinAd());
            foundVeri.setUrlAdresi(veriUpdateRequest.getUrlAdresi());
            foundVeri.setAramaAnahtarKelime(veriUpdateRequest.getAramaAnahtarKelime());
            foundVeri.setYayinTarih(veriUpdateRequest.getYayinTarih());
            foundVeri.setMakaleAnahtarKelime(veriUpdateRequest.getMakaleAnahtarKelime());
            foundVeri.setYazarIsim(veriUpdateRequest.getYazarIsim());
            foundVeri.setYayinciAdi(veriUpdateRequest.getYayinciAdi());
            foundVeri.setYayinTur(veriUpdateRequest.getYayinTur());
            foundVeri.setUrlLink(veriUpdateRequest.getUrlLink());
            veriRepo.save(foundVeri);
            return foundVeri;
        }else
            return null;
    }
}
