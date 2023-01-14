package com.barthand.example.drugapplication.api;

import com.barthand.example.drugapplication.application.dto.CreateDrugApplicationRequest;
import com.barthand.example.drugapplication.domain.DrugApplicationAggregate;
import com.barthand.example.drugapplication.infrastructure.BaseIntegrationTest;
import com.barthand.example.drugapplication.test.DrugApplicationAggregateTestSubject;
import com.barthand.example.drugapplication.test.TestFixtures;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class DrugApplicationsControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Nested
    @DisplayName("Write APIs")
    class WriteApisTests {

        @Test
        void shouldCreateDrugApplication() throws Exception {
            // given
            CreateDrugApplicationRequest createRequest = TestFixtures.fullyPopulatedCreateDrugApplicationRequest();
            String request = objectMapper.writeValueAsString(createRequest);

            DrugApplicationAggregate expectedResult = new DrugApplicationAggregate("create-app-number", "create-manufacturer-name",
                    "create-substance-name", List.of("create-product-001", "create-product-002"));

            // when
            ResultActions result = mockMvc.perform(post("/v1/applications")
                            .content(request).contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // then
            result.andExpect(status().isOk());
            String response = result.andReturn().getResponse().getContentAsString();
            Assertions.assertEquals(expectedResult, objectMapper.readValue(response, DrugApplicationAggregate.class));

            // and
            DrugApplicationAggregate createdInRepository = drugApplicationMongoRepository
                    .findById(createRequest.getApplicationNumber()).orElseThrow();
            Assertions.assertEquals(expectedResult, createdInRepository);
        }

        @Test
        void shouldRejectCreateRequestWhenMissingApplicationNumber() throws Exception {
            // given
            CreateDrugApplicationRequest createRequest = TestFixtures.fullyPopulatedCreateDrugApplicationRequest()
                    .toBuilder()
                    .applicationNumber(null)
                    .build();

            String request = objectMapper.writeValueAsString(createRequest);

            // when
            ResultActions result = mockMvc.perform(post("/v1/applications")
                            .content(request).contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // then
            result.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Read APIs")
    class ReadApisTests {

        private final DrugApplicationAggregateTestSubject fullyPopulatedTestSubject = new DrugApplicationAggregateTestSubject(TestFixtures.fullyPopulatedDrugApplicationAggregate());
        private final DrugApplicationAggregate second = fullyPopulatedTestSubject.withAppNumber("app-02").get();
        private final DrugApplicationAggregate first = fullyPopulatedTestSubject.withAppNumber("app-01").get();

        @BeforeEach
        void saveAggregates() {
            drugApplicationMongoRepository.save(first);
            drugApplicationMongoRepository.save(second);
        }
        
        @Test
        void shouldListExistingDrugApplications() throws Exception {
            listApplicationsAndExpect(0L, 5L, "applicationNumber,asc", List.of(first, second), 1L);
        }

        @Test
        void shouldListExistingDrugApplicationsInReverseOrder() throws Exception {
            listApplicationsAndExpect(0L, 5L, "applicationNumber,desc", List.of(second, first), 1L);
        }

        @Test
        void shouldListApplicationsWithPaging() throws Exception {
            listApplicationsAndExpect(0L, 1L, "applicationNumber,asc", List.of(first), 2L);
            listApplicationsAndExpect(1L, 1L, "applicationNumber,asc", List.of(second), 2L);
        }

        @Test
        void shouldFindDrugApplicationByApplicationNumber() throws Exception {
            ResultActions result = mockMvc.perform(get("/v1/applications/app-01")).andDo(print());

            // then
            result.andExpect(status().isOk());
            String response = result.andReturn().getResponse().getContentAsString();
            DrugApplicationAggregate actual = objectMapper.readValue(response, DrugApplicationAggregate.class);

            Assertions.assertEquals(first, actual);
        }

        private void listApplicationsAndExpect(long pageNumber, long pageSize, String sortOrder, List<DrugApplicationAggregate> expectedContent, long expectedTotalPages) throws Exception {
            ResultActions result = mockMvc.perform(get("/v1/applications")
                    .param("page", String.valueOf(pageNumber))
                    .param("size", String.valueOf(pageSize))
                    .param("sort", sortOrder)
            ).andDo(print());

            // then
            result.andExpect(status().isOk());
            String response = result.andReturn().getResponse().getContentAsString();
            Page<DrugApplicationAggregate> actual = objectMapper.readValue(response, new TypeReference<>() {
            });

            Assertions.assertEquals(2L, actual.getTotalElements());
            Assertions.assertEquals(expectedTotalPages, actual.getTotalPages());
            Assertions.assertEquals(expectedContent, actual.getContent());
        }
        
    }
    
}