package com.example.yazlab2_1.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartUpdateRequest {
    private String kelime;
    private String duzelenKelime;
    private boolean autoPdf;
    private boolean hazir;
    private boolean enableSerpAPI;
    private int maxArticleCount;
    private int foundArticleCount;
}
