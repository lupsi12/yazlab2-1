package com.example.yazlab2_1.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartUpdateRequest {
    private String kelime;
    private boolean durum;
}
