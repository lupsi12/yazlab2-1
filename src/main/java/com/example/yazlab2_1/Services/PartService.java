package com.example.yazlab2_1.Services;

import com.example.yazlab2_1.Entities.Part;
import com.example.yazlab2_1.Entities.Referans;
import com.example.yazlab2_1.Requests.PartUpdateRequest;
import com.example.yazlab2_1.Requests.ReferansCreateRequest;
import com.example.yazlab2_1.Requests.ReferansUpdateRequest;
import com.example.yazlab2_1.Responses.ReferansResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface PartService {
    List<Part> getAllPart();
    Part getPartById(Long partId);
    Part addPart(Part Part);
    void deletePart(Long partId);
    void deleteAllPart();
    Part updatePart(Long id, PartUpdateRequest partUpdateRequest);
}
