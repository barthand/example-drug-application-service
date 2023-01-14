package com.barthand.example.drugapplication.infrastructure;

import com.barthand.example.drugapplication.domain.DrugApplicationAggregate;
import com.barthand.example.drugapplication.domain.port.DrugApplicationRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface DrugApplicationMongoRepository extends MongoRepository<DrugApplicationAggregate, String>, DrugApplicationRepository {
    
}
