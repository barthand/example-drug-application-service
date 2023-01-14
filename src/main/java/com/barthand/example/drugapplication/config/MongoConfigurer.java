package com.barthand.example.drugapplication.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoConfigurer {

    private final MongoTemplate mongoTemplate;
    private final MongoOperations mongoOperations;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        ensureCollectionsCreated();
        ensureIndexesCreated();
    }

    public void ensureCollectionsCreated() {
//        createCollectionIfDoesNotExist(XYZ);
    }

    private void createCollectionIfDoesNotExist(String collection) {
        if (!mongoTemplate.collectionExists(collection)) {
            mongoTemplate.createCollection(collection);
        }
    }

    private void ensureIndexesCreated() {
//        mongoOperations.indexOps(Protocol.class)
//                .ensureIndex(new Index().on(FIELD_XYZ, Sort.Direction.ASC));
    }

}
