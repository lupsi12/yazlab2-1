package com.example.yazlab2_1.Repository;

import com.example.yazlab2_1.Entities.Veri;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VeriRepo extends MongoRepository<Veri, Long> {
}
