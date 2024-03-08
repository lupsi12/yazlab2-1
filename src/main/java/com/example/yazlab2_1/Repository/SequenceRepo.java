package com.example.yazlab2_1.Repository;

import com.example.yazlab2_1.Entities.DatabaseSequence;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SequenceRepo extends MongoRepository<DatabaseSequence,String> {
}
