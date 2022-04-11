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
        TilePath.Envelope envelope = tilePath.tileToEnvelope();
        String sql = "WITH\n" +
                "envelope AS (\n" +
                "    SELECT ST_Segmentize(ST_MakeEnvelope(:xmin, :ymin, :xmax, :ymax, 3857),:segSize) as env\n" +
                "  ),\n" +
                "bounds AS (\n" +
                "  SELECT e.env AS geom, e.env::box2d AS b2d\n" +
                "    FROM envelope e\n" +
                "  ),\n" +
                "  mvtgeom AS (\n" +
                "    SELECT ST_AsMVTGeom(ST_Transform(t.area, 3857), bounds.b2d) AS geom, \n" +
                "    t.nom, rtr.code, rtr.color\n" +
                "    FROM zone_pprn t \n" +
                "    INNER JOIN bounds ON ST_Intersects(t.area, ST_Transform(bounds.geom, 4326)) " +
                "    INNER JOIN ref_typereg rtr ON t.typereg = rtr.code " +
                "  ) \n" +
                "SELECT ST_AsMVT(mvtgeom.*, 'pprn') FROM mvtgeom";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("xmin", envelope.xmin)
                .addValue("ymin", envelope.ymin)
                .addValue("xmax", envelope.xmax)
                .addValue("ymax", envelope.ymax)
                .addValue("segSize", envelope.segmentSize());
        return template.queryForObject(sql, param, (rs, rowNum) -> rs.getBytes(1));

    }
}
