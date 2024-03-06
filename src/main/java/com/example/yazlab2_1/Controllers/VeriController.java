package com.example.yazlab2_1.Controllers;

import com.example.yazlab2_1.Entities.Veri;
import com.example.yazlab2_1.Repository.VeriRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VeriController {
    @Autowired
    VeriRepo veriRepo;
    @PostMapping("/addVeri")
    public void addVeri(@RequestBody Veri veri){
        veriRepo.save(veri);
    }
    @GetMapping("/getAllVeri")
    public List<Veri> getAllVeri(){
        return veriRepo.findAll();
    }
}
