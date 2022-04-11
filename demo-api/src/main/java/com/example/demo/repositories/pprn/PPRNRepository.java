package com.example.demo.repositories.pprn;

import com.example.demo.mappers.pprn.ZonePPRNRowMapper;
import com.example.demo.models.pprn.ZonePPRN;
import com.example.demo.models.tiles.TilePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PPRNRepository {

    private final NamedParameterJdbcTemplate template;
    private final ZonePPRNRowMapper zonePPRNRowMapper;

    @Autowired
    public PPRNRepository(NamedParameterJdbcTemplate template, ZonePPRNRowMapper zonePPRNRowMapper) {
        this.template = template;
        this.zonePPRNRowMapper = zonePPRNRowMapper;
    }

    public List<ZonePPRN> zonesFor(Double lat, Double lon) {
        String sql = "" +
                "SELECT id_zone, id_gaspar, nom, codezone, r.code as typereg_code, r.label as typereg_label, r.color as typereg_color, urlfic, ST_AsGeoJSON(area)::text as jsonArea " +
                "FROM zone_pprn z " +
                "LEFT JOIN ref_typereg r ON z.typereg = r.code " +
                "WHERE ST_Intersects(area, ST_SetSRID(ST_MakePoint(:lon,:lat), 4326)) " +
                "ORDER BY r.code ";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("lon", lon)
                .addValue("lat", lat);
        return template.query(sql, param, zonePPRNRowMapper);
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
