package com.example.yazlab2_1.Entities;

import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "referans")
public class Referans {
    @Id
    private Integer id;
    @OneToMany
    private Veri veri;
    private String referans;
}
