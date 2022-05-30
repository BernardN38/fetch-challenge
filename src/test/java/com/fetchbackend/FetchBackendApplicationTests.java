package com.fetchbackend;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class FetchBackendApplicationTests {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Test
    void contextLoads() {
    }

}
