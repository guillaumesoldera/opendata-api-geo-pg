CREATE INDEX zone_pprn_id_gaspar_idx ON zone_pprn (id_gaspar);
CREATE INDEX zone_pprn_area_idx ON zone_pprn USING gist (area);