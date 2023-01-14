package com.barthand.example.drugapplication.test;

import com.barthand.example.drugapplication.domain.DrugApplicationAggregate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@RequiredArgsConstructor
public class DrugApplicationAggregateTestSubject {

    private final DrugApplicationAggregate subject;

    public DrugApplicationAggregateTestSubject withRandomAppNumber() {
        return withAppNumber(RandomStringUtils.random(10));
    }

    public DrugApplicationAggregateTestSubject withAppNumber(String appNumber) {
        return new DrugApplicationAggregateTestSubject(subject.toBuilder()
                .applicationNumber(appNumber)
                .build()
        );
    }

    public DrugApplicationAggregate get() {
        return subject;
    }


}
