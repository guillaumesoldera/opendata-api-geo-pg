package com.example.demo.repositories.pprn;

import com.example.demo.models.tiles.TilePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class PPRNRepository {

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public PPRNRepository(NamedParameterJdbcTemplate template) {
        this.template = template;
    }


    public byte[] tileFor(TilePath tilePath) {
        String sql = "WITH mvtgeom AS " +
                "( " +
                "  SELECT ST_AsMVTGeom(t.area, ST_Transform(ST_TileEnvelope(:zoom, :x, :y), 4326), extent => 4096, buffer => 64) AS geom, rtr.code, rtr.color " +
                "  FROM zone_pprn t " +
                "  INNER JOIN ref_typereg rtr ON t.typereg = rtr.code " +
                "  WHERE t.area && ST_Transform(ST_TileEnvelope(:zoom, :x, :y, margin => (64.0 / 4096)), 4326) " +
                ") " +
                "SELECT ST_AsMVT(mvtgeom.*, 'pprn') " +
                "FROM mvtgeom;";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("zoom", tilePath.zoom)
                .addValue("x", tilePath.x)
                .addValue("y", tilePath.y);
        return template.queryForObject(sql, param, (rs, rowNum) -> rs.getBytes(1));

    }
}
