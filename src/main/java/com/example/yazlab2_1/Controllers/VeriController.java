package com.example.yazlab2_1.Controllers;

import com.example.yazlab2_1.Entities.Veri;
import com.example.yazlab2_1.Managers.VeriManager;
import com.example.yazlab2_1.Repository.VeriRepo;
import com.example.yazlab2_1.Requests.VeriCreateRequest;
import com.example.yazlab2_1.Requests.VeriUpdateRequest;
import com.example.yazlab2_1.Responses.VeriResponse;
import com.example.yazlab2_1.Services.VeriService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/veri")
@AllArgsConstructor
@NoArgsConstructor
public class VeriController {
    /*
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
     */
    @Autowired
    private VeriService veriService;


    @RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public List<VeriResponse> getAllVeri(){
        return veriService.getAllVeri();
    }


    @RequestMapping(method = RequestMethod.GET, path = "/{veriId}")
    @GetMapping("/{veriId}")
    public VeriResponse getOneVeri(@PathVariable Long veriId){
        return veriService.getVeriById(veriId);
    }


    @RequestMapping(method = RequestMethod.POST)
    @PostMapping
    public Veri addVeri(@RequestBody VeriCreateRequest veriCreateRequest){
        return veriService.addVeri(veriCreateRequest);
    }


    @RequestMapping(method = RequestMethod.PUT, path = "/{veriId}")
    @PutMapping("/{veriId}")
    public  Veri updateVeri(@PathVariable Long veriId, @RequestBody VeriUpdateRequest veriUpdateRequest){
        return veriService.updateVeri(veriId,veriUpdateRequest);
    }


    @RequestMapping(method = RequestMethod.DELETE, path = "/{veriId}")
    @DeleteMapping("/{veriId}")
    public void deleteOneVeri(@PathVariable Long veriId){
        veriService.deleteVeri(veriId);
    }


    @RequestMapping(method = RequestMethod.DELETE)
    @DeleteMapping
    public void deleteAllVeri(){
        veriService.deleteAllVeri();
    }

}
