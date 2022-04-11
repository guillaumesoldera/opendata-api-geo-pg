package com.example.demo.mappers.data;

import com.example.demo.models.data.DataProvider;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class DataProviderMapper implements RowMapper<DataProvider> {

    @Override
    public DataProvider mapRow(ResultSet resultSet, int i) throws SQLException {
        String id = resultSet.getString("id");
        String name = resultSet.getString("name");
        String url = resultSet.getString("url");
        String description = resultSet.getString("description");
        return new DataProvider(
                id,
                name,
                Optional.ofNullable(url),
                Optional.ofNullable(description)
        );
    }

}
