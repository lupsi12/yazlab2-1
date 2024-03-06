package com.example.yazlab2_1.Controllers;

import com.example.yazlab2_1.Entities.Referans;
import com.example.yazlab2_1.Entities.Veri;
import com.example.yazlab2_1.Repository.ReferansRepo;
import com.example.yazlab2_1.Repository.VeriRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReferansController {
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
}
