package com.example.yazlab2_1.Services;

import com.example.yazlab2_1.Entities.Veri;
import com.example.yazlab2_1.Repository.VeriRepo;
import com.example.yazlab2_1.Requests.VeriCreateRequest;
import com.example.yazlab2_1.Requests.VeriUpdateRequest;
import com.example.yazlab2_1.Responses.VeriResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public interface VeriService {
    List<VeriResponse> getAllVeri();
    VeriResponse getVeriById(Long veriId);
    Veri addVeri(VeriCreateRequest veriCreateRequest);
    void deleteVeri(Long id);
    void deleteAllVeri();
    Veri updateVeri(Long id,VeriUpdateRequest veriUpdateRequest);

}
