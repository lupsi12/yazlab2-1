package com.example.yazlab2_1.Controllers;

import com.example.yazlab2_1.Entities.Part;
import com.example.yazlab2_1.Entities.Referans;
import com.example.yazlab2_1.Requests.PartUpdateRequest;
import com.example.yazlab2_1.Requests.ReferansCreateRequest;
import com.example.yazlab2_1.Requests.ReferansUpdateRequest;
import com.example.yazlab2_1.Responses.ReferansResponse;
import com.example.yazlab2_1.Services.PartService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/part")
@AllArgsConstructor
@NoArgsConstructor
public class PartController {
    @Autowired
    PartService partService;
    @GetMapping
    public List<Part> getAllPart(){
        return partService.getAllPart();
    }
    @GetMapping("/{partId}")
    public Part getOnePart(@PathVariable Long partId){
        return partService.getPartById(partId);
    }
    @PostMapping
    public Part addPart(@RequestBody Part part){
        return partService.addPart(part);
    }
    @PutMapping("/{partId}")
    public  Part updatePart(@PathVariable Long partId, @RequestBody PartUpdateRequest partUpdateRequest){
        return partService.updatePart(partId,partUpdateRequest);
    }
    @DeleteMapping("/{partId}")
    public void deleteOnePart(@PathVariable Long partId){
        partService.deletePart(partId);
    }
    @DeleteMapping
    public void deleteAllReferans(){
        partService.deleteAllPart();
    }

}
