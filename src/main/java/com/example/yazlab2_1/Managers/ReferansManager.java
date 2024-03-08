package com.example.yazlab2_1.Managers;

import com.example.yazlab2_1.Entities.Referans;
import com.example.yazlab2_1.Entities.Veri;
import com.example.yazlab2_1.Repository.ReferansRepo;
import com.example.yazlab2_1.Requests.ReferansCreateRequest;
import com.example.yazlab2_1.Requests.ReferansUpdateRequest;
import com.example.yazlab2_1.Responses.ReferansResponse;
import com.example.yazlab2_1.Responses.VeriResponse;
import com.example.yazlab2_1.Services.ReferansService;
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
public class ReferansManager implements ReferansService {
    private ReferansRepo referansRepo;
    private VeriService veriService;
    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    public ReferansManager(ReferansRepo referansRepo, VeriService veriService, SequenceGeneratorService sequenceGeneratorService) {
        this.referansRepo = referansRepo;
        this.veriService = veriService;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public List<ReferansResponse> getAllReferans(Optional<Long> veriId) {
        List<Referans> referansList;
        if(veriId.isPresent())
            referansList = referansRepo.findByVeriId(veriId);
        else
            referansList = referansRepo.findAll();
        return referansList.stream().map(p -> new ReferansResponse(p)).collect(Collectors.toList());
    }

    @Override
    public ReferansResponse getReferansById(Long referansId) {
        Referans referans = referansRepo.findById(referansId).orElse(null);
        ReferansResponse referansResponse = new ReferansResponse(referans);
        return referansResponse;
    }

    @Override
    public Referans addReferans(ReferansCreateRequest referansCreateRequest) {

        VeriResponse veriResponse = veriService.getVeriById(referansCreateRequest.getVeri_id());
        Veri veri = new Veri(veriResponse);
        if(veriResponse == null)
            return null;
        Referans referans = new Referans();
        referans.setVeri(veri);
        referans.setId(sequenceGeneratorService.getSquenceNumber(referansCreateRequest.SEQUENCE_NAME));
        referans.setReferans(referansCreateRequest.getReferans());
        return referansRepo.save(referans);
    }

    @Override
    public void deleteReferans(Long id) {
        referansRepo.deleteById(id);
    }

    @Override
    public void deleteAllReferans() {
        referansRepo.deleteAll();
    }

    @Override
    public Referans updateReferans(Long id, ReferansUpdateRequest referansUpdateRequest) {
        Optional<Referans> referans = referansRepo.findById(id);
        if (referans.isPresent()){
            Referans foundReferans = referans.get();
            VeriResponse veriResponse = veriService.getVeriById(referansUpdateRequest.getVeri_id());
            Veri veri = new Veri(veriResponse);
            if(veriResponse == null)
                return null;
            foundReferans.setVeri(veri);
            foundReferans.setReferans(referansUpdateRequest.getReferans());
            referansRepo.save(foundReferans);
            return foundReferans;
        }else
            return null;
    }
}
