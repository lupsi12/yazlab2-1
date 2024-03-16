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
    private String duzelenKelime = kelime+" -- yazımı düzeltilmiş sonuçları görüyorsunuz";
    private boolean autoPdf;
    private boolean hazir;

    public Part(Part p) {
    }
}
