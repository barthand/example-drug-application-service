package com.barthand.example.drugapplication.openfda.client;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public final class SearchParams {
    private final String search;
    private final Integer limit;
    private final Long skip;
    private final String sort;

    public static SearchStep start() {
        return SearchParamsStepBuilder.search();
    }

    public interface SearchStep {
        SearchStep exactMatchIfNotBlank(String field, String value);

        SearchStep termMatchIfNotBlank(String field, String value);

        PagingStep paging();
    }

    public interface SortOrBuildStep {
        SortOrBuildStep sortUsing(Optional<Sort.Order> order);

        SortOrBuildStep sortAscending(String field);

        SortOrBuildStep sortDescending(String field);

        SearchParams build();
    }

    public interface PagingStep {
        PagingStep limit(int limit);

        PagingStep skip(long skip);

        SortOrBuildStep and();
    }

    @FunctionalInterface
    public interface FilterBuilder {
        String buildFilter(String field, String value);
    }

    @UtilityClass
    public static final class AvailableFilters {
        public static final FilterBuilder EXACT_MATCH = (field, value) -> String.format("%s.exact:\"%s\"", field, value);
        public static final FilterBuilder TERM_MATCH = (field, value) -> String.format("%s:\"%s\"", field, value);
    }
}
