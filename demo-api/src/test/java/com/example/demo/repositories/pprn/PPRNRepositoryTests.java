package com.example.demo.repositories.pprn;

import com.example.demo.mappers.pprn.ZonePPRNRowMapper;
import com.example.demo.models.pprn.ZonePPRN;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class PPRNRepositoryTests {

    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("postgis/postgis").asCompatibleSubstituteFor("postgres");

    @Container
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(DEFAULT_IMAGE_NAME)
            .withUsername("api_demo_test_user")
            .withPassword("api_demo_test_password")
            .withDatabaseName("api_demo_test")
            .withExposedPorts(5432)
            .withInitScript("database/pprn/zones-pprn-17300.sql");


    @Test
    public void testZoneForCodeInsee99999() {
        PPRNRepository pprnRepository = new PPRNRepository(
                new NamedParameterJdbcTemplate(getTestContainerDataSource()),
                new ZonePPRNRowMapper()
        );

        List<ZonePPRN> zones = pprnRepository.zonesFor("99999");
        assertEquals(0, zones.size());
    }

    @Test
    public void testZoneForCodeInsee17300() {
        PPRNRepository pprnRepository = new PPRNRepository(
                new NamedParameterJdbcTemplate(getTestContainerDataSource()),
                new ZonePPRNRowMapper()
        );

        List<ZonePPRN> zones = pprnRepository.zonesFor("17300");
        assertEquals(411, zones.size());
    }

    @Test
    public void testZoneForLatLonLaRochelle() {
        PPRNRepository pprnRepository = new PPRNRepository(
                new NamedParameterJdbcTemplate(getTestContainerDataSource()),
                new ZonePPRNRowMapper()
        );

        List<ZonePPRN> zones = pprnRepository.zonesFor(46.156546675036125, -1.154201042114158);
        assertEquals(1, zones.size());
        ZonePPRN zone = zones.get(0);
        assertEquals("RNATZR000000000041350", zone.idZone);
        assertEquals("17DDTM20100014", zone.idGaspar);
        assertTrue(zone.nomOpt.isPresent());
        assertEquals("Zone orange Os1: ensemble des zones fortement urbanisees en alea faible a modere a court terme", zone.nomOpt.get());
        assertTrue(zone.codezoneOpt.isPresent());
        assertEquals("Os1", zone.codezoneOpt.get());
        assertEquals("03", zone.typeReg.code);
        assertEquals("Interdiction", zone.typeReg.label);
        assertEquals("#ff6060", zone.typeReg.color);
    }

    @Test
    public void testZoneForLatLon0() {
        PPRNRepository pprnRepository = new PPRNRepository(
                new NamedParameterJdbcTemplate(getTestContainerDataSource()),
                new ZonePPRNRowMapper()
        );

        List<ZonePPRN> zones = pprnRepository.zonesFor(0.0, 0.0);
        assertEquals(0, zones.size());
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
