package com.barthand.example.drugapplication.openfda.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class SearchParamsStepBuilder implements SearchParams.SearchStep, SearchParams.PagingStep, SearchParams.SortOrBuildStep {

    private static final String AND_SEPARATOR = " AND ";
    private static final int DEFAULT_LIMIT = 10;

    private int limit = DEFAULT_LIMIT;
    private long skip = 0;
    private boolean sortAscending = true;
    private String sortField;
    private final List<String> searchExpressions = new ArrayList<>();

    @Override
    public SearchParams.SearchStep exactMatchIfNotBlank(String field, String value) {
        addMatchIfValueNotBlank(field, value, SearchParams.AvailableFilters.EXACT_MATCH);
        return this;
    }

    @Override
    public SearchParams.SearchStep termMatchIfNotBlank(String field, String value) {
        addMatchIfValueNotBlank(field, value, SearchParams.AvailableFilters.TERM_MATCH);
        return this;
    }

    private void addMatchIfValueNotBlank(String field, String value, SearchParams.FilterBuilder filterBuilder) {
        Optional.ofNullable(value)
                .filter(not(String::isBlank))
                .ifPresent(v -> searchExpressions.add(filterBuilder.buildFilter(field, value)));
    }

    @Override
    public SearchParams.PagingStep paging() {
        return this;
    }

    @Override
    public SearchParams.PagingStep limit(int limit) {
        if (limit > OpenFDAClient.PagingAndSortingConstants.MAX_LIMIT) {
            throw new IllegalArgumentException(
                    String.format("Limit (%d) was specified which is higher than max allowed limit (%d)", limit, OpenFDAClient.PagingAndSortingConstants.MAX_LIMIT)
            );
        }
        this.limit = limit;
        return this;
    }

    @Override
    public SearchParams.PagingStep skip(long skip) {
        this.skip = skip;
        return this;
    }

    @Override
    public SearchParams.SortOrBuildStep and() {
        return this;
    }

    @Override
    public SearchParams.SortOrBuildStep sortUsing(Optional<Sort.Order> order) {
        order.ifPresent(o -> {
            this.sortAscending = o.isAscending();
            this.sortField = o.getProperty();
        });
        return this;
    }

    @Override
    public SearchParams.SortOrBuildStep sortAscending(String field) {
        this.sortAscending = true;
        this.sortField = field;
        return this;
    }

    @Override
    public SearchParams.SortOrBuildStep sortDescending(String field) {
        this.sortAscending = false;
        this.sortField = field;
        return this;
    }

    @Override
    public SearchParams build() {
        String search = String.join(AND_SEPARATOR, searchExpressions);
        return new SearchParams(
                search,
                limit, skip,
                resolveSortParam());
    }

    private String resolveSortParam() {
        String field = Optional.ofNullable(this.sortField)
                .filter(not(String::isBlank))
                .orElse(OpenFDAClient.PagingAndSortingConstants.DEFAULT_SORT_FIELD);
        return String.format("%s:%s", field, sortAscending ? OpenFDAClient.SortOrders.ASC : OpenFDAClient.SortOrders.DESC);
    }
    
    static SearchParams.SearchStep search() {
        return new SearchParamsStepBuilder();
    }

}
