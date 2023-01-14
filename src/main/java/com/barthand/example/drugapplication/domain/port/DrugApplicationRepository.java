package com.barthand.example.drugapplication.domain.port;

import com.barthand.example.drugapplication.domain.DrugApplicationAggregate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DrugApplicationRepository {

    DrugApplicationAggregate save(DrugApplicationAggregate entity);

    Page<DrugApplicationAggregate> findAll(Pageable pageable);
    
    Optional<DrugApplicationAggregate> findById(String id);
    
}
