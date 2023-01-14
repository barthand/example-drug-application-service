package com.barthand.example.drugapplication.infrastructure;

import com.barthand.example.drugapplication.domain.DrugApplicationAggregate;
import com.barthand.example.drugapplication.test.SpringBootTestcontainersTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.stream.Stream;

@SpringBootTestcontainersTest
public abstract class BaseIntegrationTest {

    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4");

    static {
        Startables.deepStart(Stream.of(mongoDBContainer)).join();
    }

    @Autowired
    protected MongoRepository<DrugApplicationAggregate, String> drugApplicationMongoRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    protected void cleanupMongo() {
        drugApplicationMongoRepository.deleteAll();
    }

}
