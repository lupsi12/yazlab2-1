package com.example.yazlab2_1.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "referans")
public class Referans {
    @Transient
    public static final String SEQUENCE_NAME = "referans_sequence";
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    /*
    @OneToMany(mappedBy = "referans")// verinin birden çok referansı olabilir
    @JoinColumn(name = "id",nullable = false)
    @OnDelete(action =  OnDeleteAction.CASCADE)
     private List<Veri> veri ;
    */
    @Column(columnDefinition="text")
    private String referans;

    @OneToOne(fetch =  FetchType.EAGER)//lazy user objeai gelmesin diye
    @JoinColumn(name = "veri_id",nullable = false)
    @OnDelete(action =  OnDeleteAction.CASCADE)
    private Veri veri;

}
