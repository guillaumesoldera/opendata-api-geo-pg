package com.example.demo.repositories.data;

import com.example.demo.models.data.DataProvider;
import com.example.demo.models.data.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = {DataSourceRepositoryTests.Initializer.class})
public class DataSourceRepositoryTests {

    @Container
    public static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:12")
            .withUsername("api_demo_test_user")
            .withPassword("api_demo_test_password")
            .withDatabaseName("api_demo_test")
            .withExposedPorts(5432)
            .withInitScript("database/data/sources-tests-data.sql");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + POSTGRE_SQL_CONTAINER.getJdbcUrl(),
                    "spring.datasource.username=" + POSTGRE_SQL_CONTAINER.getUsername(),
                    "spring.datasource.password=" + POSTGRE_SQL_CONTAINER.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private DataSourceRepository dataSourceRepository;

    @Test
    public void testFindAll() {
        List<DataSource> allSources = dataSourceRepository.findAll();
        assertEquals(1, allSources.size());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<DataSource> sourceOpt = dataSourceRepository.findById("xxx");
        assertTrue(sourceOpt.isEmpty());
    }

    @Test
    public void testFindById() {
        Optional<DataSource> sourceOpt = dataSourceRepository.findById("georisques");
        assertTrue(sourceOpt.isPresent());
        DataSource source = sourceOpt.get();
        assertEquals("georisques", source.id);
        assertEquals("Géorisques", source.name);
        assertTrue(source.descriptionOpt.isPresent());
        assertEquals(
                "Géorisques est le site de référence sur les risques majeurs naturels et technologiques",
                source.descriptionOpt.get()
        );
        assertTrue(source.urlOpt.isPresent());
        assertEquals("https://www.georisques.gouv.fr/donnees/bases-de-donnees", source.urlOpt.get());
        assertTrue(source.publicationDateOpt.isPresent());
        assertEquals(LocalDate.parse("2020-09-01"), source.publicationDateOpt.get());
        DataProvider provider = source.dataProvider;
        assertEquals("ministere-de-la-transition-ecologique", provider.id);
        assertEquals("Ministère de la Transition écologique", provider.name);
        assertTrue(provider.descriptionOpt.isEmpty());
        assertTrue(provider.urlOpt.isPresent());
        assertEquals("https://www.ecologie.gouv.fr/", provider.urlOpt.get());
    }

}
