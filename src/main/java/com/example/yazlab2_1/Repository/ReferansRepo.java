package com.example.yazlab2_1.Repository;

import com.example.yazlab2_1.Entities.Referans;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReferansRepo extends MongoRepository<Referans, Long> {
    List<Referans> findByVeriId(Optional<Long> veriId);
}
