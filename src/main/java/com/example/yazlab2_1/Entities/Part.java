package com.example.yazlab2_1.Entities;

import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "part")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Part {
    @Transient
    public static final String SEQUENCE_NAME = "part_sequence";
    @Id
    private Long id;

    private String kelime;
    private String duzelenKelime;
    private boolean autoPdf;
    private boolean hazir;
    private boolean enableSerpAPI;
    private boolean downloadPdfRequest;
    private int maxArticleCount;
    private int foundArticleCount;

    public Part(Part p) {
    }
}
