package com.barthand.example.drugapplication.openfda.client;

import com.barthand.example.drugapplication.config.OpenFDACacheConfiguration;
import lombok.experimental.UtilityClass;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "OpenFDADrugs", url = "${openfda.url}")
public interface OpenFDAClient {

    @UtilityClass
    class KnownSearchableFields {
        public static final String MANUFACTURER_NAME = "openfda.manufacturer_name";
        
        // there is also a non-harmonized `products.brand_name'
        public static final String BRAND_NAME = "openfda.brand_name";  
    }

    @UtilityClass
    class SortOrders {
        public static final String ASC = "asc";
        public static final String DESC = "desc";
    }


    @UtilityClass
    class PagingAndSortingConstants {
        public static final int MAX_LIMIT = 99;
        public static final String DEFAULT_SORT_FIELD = "application_number";
    }

    @GetMapping("/drug/drugsfda.json")
    @Cacheable(cacheNames = OpenFDACacheConfiguration.OPENFDA_DRUGS_CACHE, condition = "@'openfda-com.barthand.example.drugapplication.config.OpenFDAProperties'.enableCache ?: false")
    // or, when you are sure property is defined as part of environment
//    @Cacheable(cacheNames = OpenFDACacheConfiguration.OPENFDA_DRUGS_CACHE, condition = "@environment.getProperty('openfda.enable-cache') ?: false")
    OpenFDADrugsClientResponse searchDrugsEndpoint(@SpringQueryMap SearchParams params);

}
