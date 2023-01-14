package com.barthand.example.drugapplication.config;

import com.barthand.example.drugapplication.domain.DrugApplicationAggregate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
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
        createCollectionIfDoesNotExist(DrugApplicationAggregate.COLLECTION_NAME);
    }

    private void createCollectionIfDoesNotExist(String collection) {
        if (!mongoTemplate.collectionExists(collection)) {
            mongoTemplate.createCollection(collection);
        }
    }

    private void ensureIndexesCreated() {
        mongoOperations.indexOps(DrugApplicationAggregate.class)
                .ensureIndex(new Index().on(DrugApplicationAggregate.FIELD_MANUFACTURER_NAME, Sort.Direction.ASC));
    }

}
