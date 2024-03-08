package com.example.yazlab2_1.Repository;

import com.example.yazlab2_1.Entities.Referans;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReferansRepo extends MongoRepository<Referans, Long> {
}
