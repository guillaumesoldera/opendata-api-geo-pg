package com.example.demo.repositories.mappers.data;

import com.example.demo.models.data.DataProvider;
import com.example.demo.models.data.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class DataSourceMapper implements RowMapper<DataSource> {

    @Override
    public DataSource mapRow(ResultSet resultSet, int i) throws SQLException {
        String id = resultSet.getString("id");
        String name = resultSet.getString("name");
        String url = resultSet.getString("url");
        String description = resultSet.getString("description");
        LocalDate publicationDate = resultSet.getDate("publication_date").toLocalDate();

        DataProvider dataProvider = new DataProvider(
                resultSet.getString("data_provider_id"),
                resultSet.getString("data_provider_name"),
                Optional.ofNullable(resultSet.getString("data_provider_url")),
                Optional.ofNullable(resultSet.getString("data_provider_description"))
        );

        return new DataSource(
                id,
                name,
                Optional.ofNullable(url),
                Optional.ofNullable(description),
                Optional.ofNullable(publicationDate),
                dataProvider
        );
    }

}
