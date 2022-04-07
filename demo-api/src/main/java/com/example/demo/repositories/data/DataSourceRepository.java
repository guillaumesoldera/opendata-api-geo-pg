package com.example.demo.repositories.data;

import com.example.demo.models.data.DataSource;
import com.example.demo.repositories.mappers.data.DataSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Repository
public class DataSourceRepository {

    private final NamedParameterJdbcTemplate template;
    private final DataSourceMapper dataSourceMapper;

    @Autowired
    public DataSourceRepository(
            NamedParameterJdbcTemplate template,
            DataSourceMapper dataSourceMapper
    ) {
        this.template = template;
        this.dataSourceMapper = dataSourceMapper;
    }

    @Async
    public CompletableFuture<Optional<DataSource>> findById(String id) {
        String sql = "SELECT ds.id, ds.name, ds.url, ds.description, ds.publication_date, " +
                "dp.id as data_provider_id, dp.name as data_provider_name, dp.url as data_provider_url, dp.description as data_provider_description " +
                "FROM Data_Source ds " +
                "  INNER JOIN Data_Provider dp ON dp.id = ds.data_provider_id " +
                "WHERE ds.id = :id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        List<DataSource> dataSourceList = template.query(sql, param, dataSourceMapper);

        return CompletableFuture.completedFuture(
                dataSourceList.stream().findFirst()
        );
    }

    @Async
    public CompletableFuture<List<DataSource>> findAll() {
        String sql = "SELECT ds.id, ds.name, ds.url, ds.description, ds.publication_date, " +
                "dp.id as data_provider_id, dp.name as data_provider_name, dp.url as data_provider_url, dp.description as data_provider_description " +
                "FROM Data_Source ds " +
                "  INNER JOIN Data_Provider dp ON dp.id = ds.data_provider_id\n" +
                "ORDER BY ds.id ASC";

        return CompletableFuture.completedFuture(
                template.query(sql, dataSourceMapper)
        );
    }

    @Async
    public CompletableFuture<List<DataSource>> findByTables(String... tables) {
        String sql = "SELECT DISTINCT(ds.id), ds.name, ds.url, ds.description, ds.publication_date, " +
                "dp.id as data_provider_id, dp.name as data_provider_name, dp.url as data_provider_url, dp.description as data_provider_description " +
                "FROM Data_Source ds " +
                "  INNER JOIN Data_Provider dp ON dp.id = ds.data_provider_id " +
                "  INNER JOIN Table_Data_Source tds ON tds.data_source_id = ds.id " +
                "WHERE LOWER(table_name) IN (:table_names)\n" +
                "ORDER BY ds.id ASC";

        SqlParameterSource param = new MapSqlParameterSource().addValue("table_names", Arrays.stream(tables).map(String::toLowerCase).collect(Collectors.toList()));
        return CompletableFuture.completedFuture(
                template.query(sql, param, dataSourceMapper)
        );
    }

}
