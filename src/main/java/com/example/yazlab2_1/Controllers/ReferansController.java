package com.example.yazlab2_1.Controllers;

import com.example.yazlab2_1.Entities.Referans;
import com.example.yazlab2_1.Entities.Veri;
import com.example.yazlab2_1.Repository.ReferansRepo;
import com.example.yazlab2_1.Repository.VeriRepo;
import com.example.yazlab2_1.Requests.ReferansCreateRequest;
import com.example.yazlab2_1.Requests.ReferansUpdateRequest;
import com.example.yazlab2_1.Requests.VeriCreateRequest;
import com.example.yazlab2_1.Requests.VeriUpdateRequest;
import com.example.yazlab2_1.Responses.ReferansResponse;
import com.example.yazlab2_1.Responses.VeriResponse;
import com.example.yazlab2_1.Services.ReferansService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/referans")
@AllArgsConstructor
@NoArgsConstructor
public class ReferansController {
    /*
    @Autowired
    ReferansRepo referansRepo;
    @PostMapping("/addReferans")
    public void addVeri(@RequestBody Referans referans){
        referansRepo.save(referans);
    }
    @GetMapping("/getAllReferans")
    public List<Referans> getAllReferans(){
        return referansRepo.findAll();
    }
     */
    @Autowired
    ReferansService referansService;
    @GetMapping
    public List<ReferansResponse> getAllReferans(Optional<Long> veriId){
        return referansService.getAllReferans(veriId);
    }
    @GetMapping("/{referansId}")
    public ReferansResponse getOneReferans(@PathVariable Long referansId){
        return referansService.getReferansById(referansId);
    }
    @PostMapping
    public Referans addReferans(@RequestBody ReferansCreateRequest referansCreateRequest){
        return referansService.addReferans(referansCreateRequest);
    }
    @PutMapping("/{referansId}")
    public  Referans updateReferans(@PathVariable Long referansId, @RequestBody ReferansUpdateRequest referansUpdateRequest){
        return referansService.updateReferans(referansId,referansUpdateRequest);
    }
    @DeleteMapping("/{referansId}")
    public void deleteOneReferans(@PathVariable Long referansId){
        referansService.deleteReferans(referansId);
    }
    @DeleteMapping
    public void deleteAllReferans(){
        referansService.deleteAllReferans();
    }
}
