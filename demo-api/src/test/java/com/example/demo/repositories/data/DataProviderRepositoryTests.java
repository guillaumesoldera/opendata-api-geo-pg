package com.example.demo.repositories.data;

import com.example.demo.models.data.DataProvider;
import com.example.demo.mappers.data.DataProviderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class DataProviderRepositoryTests {

    @Container
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:12")
            .withUsername("api_demo_test_user")
            .withPassword("api_demo_test_password")
            .withDatabaseName("api_demo_test")
            .withExposedPorts(5432)
            .withInitScript("database/data/providers-tests-data.sql");


    @Test
    public void testFindAll() {
        DataProviderRepository dataProviderRepository = new DataProviderRepository(
                new NamedParameterJdbcTemplate(getTestContainerDataSource()),
                new DataProviderMapper()
        );

        List<DataProvider> allProviders = dataProviderRepository.findAll();
        assertEquals(1, allProviders.size());
    }

    @Test
    public void testFindByIdNotFound() {
        DataProviderRepository dataProviderRepository = new DataProviderRepository(
                new NamedParameterJdbcTemplate(getTestContainerDataSource()),
                new DataProviderMapper()
        );

        Optional<DataProvider> providerOpt = dataProviderRepository.findById("xxx");
        assertTrue(providerOpt.isEmpty());
    }

    @Test
    public void testFindById() {
        DataProviderRepository dataProviderRepository = new DataProviderRepository(
                new NamedParameterJdbcTemplate(getTestContainerDataSource()),
                new DataProviderMapper()
        );

        Optional<DataProvider> providerOpt = dataProviderRepository.findById("ministere-de-la-transition-ecologique");
        assertTrue(providerOpt.isPresent());
        DataProvider provider = providerOpt.get();
        assertEquals("ministere-de-la-transition-ecologique", provider.id);
        assertEquals("Ministère de la Transition écologique", provider.name);
        assertTrue(provider.descriptionOpt.isEmpty());
        assertTrue(provider.urlOpt.isPresent());
        assertEquals("https://www.ecologie.gouv.fr/", provider.urlOpt.get());
    }

    private DataSource getTestContainerDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url(POSTGRE_SQL_CONTAINER.getJdbcUrl());
        dataSourceBuilder.username("api_demo_test_user");
        dataSourceBuilder.password("api_demo_test_password");
        return dataSourceBuilder.build();
    }
}
