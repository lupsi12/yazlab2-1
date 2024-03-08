package com.example.yazlab2_1.Requests;

import com.example.yazlab2_1.Entities.Veri;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferansCreateRequest {
    public static final String SEQUENCE_NAME = "referans_sequence";
    private Long id;
    private String referans;
    private Long veri_id;
}
