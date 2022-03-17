CREATE INDEX arrondissements_geom_latlon_idx ON arrondissements USING gist (geom_latlon);

CREATE INDEX espaces_verts_geom_latlon_idx ON espaces_verts USING gist (geom_latlon);