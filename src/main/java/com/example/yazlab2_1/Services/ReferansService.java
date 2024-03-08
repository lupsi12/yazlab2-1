package com.example.yazlab2_1.Services;
import com.example.yazlab2_1.Entities.Referans;
import com.example.yazlab2_1.Entities.Veri;
import com.example.yazlab2_1.Requests.ReferansCreateRequest;
import com.example.yazlab2_1.Requests.ReferansUpdateRequest;
import com.example.yazlab2_1.Responses.ReferansResponse;
import com.example.yazlab2_1.Responses.VeriResponse;
import java.util.List;
import java.util.Optional;

public interface ReferansService {
    List<ReferansResponse> getAllReferans(Optional<Long> veriId);
    ReferansResponse getReferansById(Long referansId);
    Referans addReferans(ReferansCreateRequest referansCreateRequest);
    void deleteReferans(Long referansId);
    void deleteAllReferans();
    Referans updateReferans(Long id, ReferansUpdateRequest referansUpdateRequest);
}
