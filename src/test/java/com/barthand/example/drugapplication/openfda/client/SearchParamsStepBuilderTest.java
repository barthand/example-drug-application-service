package com.barthand.example.drugapplication.openfda.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.Stream;

class SearchParamsStepBuilderTest {

    public static final String DEFAULT_SORT = "application_number:asc";
    public static final int DEFAULT_LIMIT = 10;
    public static final long DEFAULT_SKIP = 0L;
    public static final String DEFAULT_SEARCH_IF_NOT_SPECIFIED = "";

    private final SearchParams.SearchStep subject = SearchParamsStepBuilder.search();

    @Test
    void shouldPutReasonableDefaultsIfNoneDetailsAreSpecified() {
        // when
        SearchParams build = subject.paging().and().build();
        
        // then
        Assertions.assertEquals(new SearchParams(DEFAULT_SEARCH_IF_NOT_SPECIFIED, DEFAULT_LIMIT, DEFAULT_SKIP, DEFAULT_SORT), build);
    }

    @Test
    void shouldCorrectlySetTermMatch() {
        // when
        SearchParams build = subject.termMatchIfNotBlank("term", "value").paging().and().build();
        
        // then
        Assertions.assertEquals(new SearchParams("term:\"value\"", DEFAULT_LIMIT, DEFAULT_SKIP, DEFAULT_SORT), build);
    }

    @Test
    void shouldCorrectlySetExactMatch() {
        // when
        SearchParams build = subject.exactMatchIfNotBlank("term", "value").paging().and().build();
        
        // then
        Assertions.assertEquals(new SearchParams("term.exact:\"value\"", DEFAULT_LIMIT, DEFAULT_SKIP, DEFAULT_SORT), build);
    }

    @Test
    void shouldCorrectlySetPaging() {
        // when
        SearchParams build = subject.paging().limit(20).skip(10).and().build();
        
        // then
        Assertions.assertEquals(new SearchParams(DEFAULT_SEARCH_IF_NOT_SPECIFIED, 20, 10L, DEFAULT_SORT), build);
    }

    @Test
    void shouldThrowIfExceededLimitThreshold() {
        // when & then
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, 
                () -> subject.paging().limit(100));
        
        Assertions.assertEquals("Limit (100) was specified which is higher than max allowed limit (99)", exception.getMessage());
    }

    @Test
    void shouldCorrectlySetDescendingSort() {
        // when
        SearchParams build = subject.paging().and().sortDescending("field").build();

        // then
        Assertions.assertEquals(new SearchParams(DEFAULT_SEARCH_IF_NOT_SPECIFIED, DEFAULT_LIMIT, DEFAULT_SKIP, "field:desc"), build);
    }

    @Test
    void shouldCorrectlySetAscendingSort() {
        // when
        SearchParams build = subject.paging().and().sortAscending("field").build();

        // then
        Assertions.assertEquals(new SearchParams(DEFAULT_SEARCH_IF_NOT_SPECIFIED, DEFAULT_LIMIT, DEFAULT_SKIP, "field:asc"), build);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @ParameterizedTest
    @MethodSource("sortOrderParameters")
    void shouldCorrectlySetSort(Optional<Sort.Order> order, String expectedSort) {
        // when
        SearchParams build = subject.paging().and().sortUsing(order).build();

        // then
        Assertions.assertEquals(new SearchParams(DEFAULT_SEARCH_IF_NOT_SPECIFIED, DEFAULT_LIMIT, DEFAULT_SKIP, expectedSort), build);
    }

    private static Stream<Arguments> sortOrderParameters() {
        return Stream.of(
                Arguments.of(
                        Optional.of(new Sort.Order(Sort.Direction.ASC, "field")),
                        "field:asc"
                ),
                Arguments.of(
                        Optional.of(new Sort.Order(Sort.Direction.DESC, "field")),
                        "field:desc"
                )
        );
    }

}

