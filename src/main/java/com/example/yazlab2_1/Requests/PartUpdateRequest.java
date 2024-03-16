package com.example.yazlab2_1.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartUpdateRequest {
    private String kelime;
    private String duzelenKelime = kelime+" -- yazımı düzeltilmiş sonuçları görüyorsunuz";
    private boolean autoPdf;
    private boolean hazir;
}
