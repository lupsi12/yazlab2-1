package com.example.yazlab2_1.Repository;

import com.example.yazlab2_1.Entities.Part;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartRepo extends MongoRepository<Part, Long> {
}
