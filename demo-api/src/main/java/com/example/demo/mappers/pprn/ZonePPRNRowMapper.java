package com.example.demo.mappers.pprn;

import com.example.demo.models.pprn.TypeRegZonePPRN;
import com.example.demo.models.pprn.ZonePPRN;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class ZonePPRNRowMapper implements RowMapper<ZonePPRN> {
    @Override
    public ZonePPRN mapRow(ResultSet rs, int i) throws SQLException {
        String typeregCode = rs.getString("typereg_code");
        TypeRegZonePPRN typeRegZonePPR;
        if (typeregCode == null) {
            typeRegZonePPR = TypeRegZonePPRN.defaultTypeRef();
        } else {
            typeRegZonePPR = new TypeRegZonePPRN(
                    typeregCode,
                    rs.getString("typereg_label"),
                    rs.getString("typereg_color")
            );
        }
        return new ZonePPRN(
                rs.getString("id_zone"),
                rs.getString("id_gaspar"),
                Optional.ofNullable(rs.getString("nom")),
                Optional.ofNullable(rs.getString("codezone")),
                typeRegZonePPR,
                Optional.ofNullable(rs.getString("urlfic")),
                rs.getString("jsonArea")
        );
    }
}
