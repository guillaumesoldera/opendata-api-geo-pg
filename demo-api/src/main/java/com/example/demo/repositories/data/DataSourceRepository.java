package com.example.demo.repositories.data;

import com.example.demo.models.data.DataSource;
import com.example.demo.repositories.mappers.data.DataSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public Optional<DataSource> findById(String id) {
        String sql = "SELECT ds.id, ds.name, ds.url, ds.description, ds.publication_date, " +
                "dp.id as data_provider_id, dp.name as data_provider_name, dp.url as data_provider_url, dp.description as data_provider_description " +
                "FROM Data_Source ds " +
                "  INNER JOIN Data_Provider dp ON dp.id = ds.data_provider_id " +
                "WHERE ds.id = :id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        List<DataSource> dataSourceList = template.query(sql, param, dataSourceMapper);

        return dataSourceList.stream().findFirst();
    }

    public List<DataSource> findAll() {
        String sql = "SELECT ds.id, ds.name, ds.url, ds.description, ds.publication_date, " +
                "dp.id as data_provider_id, dp.name as data_provider_name, dp.url as data_provider_url, dp.description as data_provider_description " +
                "FROM Data_Source ds " +
                "  INNER JOIN Data_Provider dp ON dp.id = ds.data_provider_id\n" +
                "ORDER BY ds.id ASC";

        return template.query(sql, dataSourceMapper);
    }

}
