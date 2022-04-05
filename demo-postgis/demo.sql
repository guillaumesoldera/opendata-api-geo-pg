-- specific TABLES
select *
from geometry_columns;

select *
from spatial_ref_sys;


-- ST_AsGeoJSON
-- geometrie du 17e arrondissement
SELECT ST_AsGeoJSON(geom_latlon)
from arrondissements
where c_ar = '17';

-- Feature GeoJSON complète
SELECT ST_AsGeoJSON(a.*)
from arrondissements a
where c_ar = '17';

-- Features de tous les arrondissements
SELECT json_build_object(
               'type', 'FeatureCollection',
               'features', json_agg(ST_AsGeoJSON(a.*)::json)
           )
from arrondissements a;


-- ST_Envelope
SELECT ST_AsGeoJSON(ST_Envelope(geom_latlon))
from arrondissements
where c_ar = '17';

with features AS (
    SELECT ST_Envelope(geom_latlon) as feature, '#000000' as fill
    from arrondissements
    where c_ar = '17'
    UNION
    SELECT geom_latlon as feature, '#FFFFFF' as fill
    from arrondissements
    where c_ar = '17'
)
SELECT json_build_object(
               'type', 'FeatureCollection',
               'features', json_agg(ST_AsGeoJSON(f.*)::json)
           )
from features f;


-- ST_Distance
-- distance des stations de métro depuis le palais des congrès
select nom, ligne, ST_Distance('SRID=4326;POINT(2.282412153722246 48.878653091848705)', geom_latlon)
from gares
where mode = 'Metro';

-- distance en mètres
select nom, ligne, ST_Distance(ST_Transform('SRID=4326;POINT(2.282412153722246 48.878653091848705)'::geometry, 2154), ST_Transform(geom_latlon, 2154)), ST_AsGeoJSON(geom_latlon)
from gares
where mode = 'Metro';

-- les stations les plus proches (tri)
select nom, ligne, ST_Distance(ST_Transform('SRID=4326;POINT(2.282412153722246 48.878653091848705)'::geometry, 2154), ST_Transform(geom_latlon, 2154)), ST_AsGeoJSON(geom_latlon)
from gares
where mode = 'Metro'
order by ST_Transform(geom_latlon, 2154) <-> ST_Transform('SRID=4326;POINT(2.282412153722246 48.878653091848705)'::geometry, 2154);



-- ST_Area
-- l'arrondissement le plus vaste
select c_ar, l_ar, l_aroff, ST_Area(ST_Transform(geom_latlon, 2154)) / 1000000 as surface_km2
from arrondissements
ORDER by surface_km2 desc;

-- en castant en geography
select c_ar, l_ar, l_aroff, ST_Area(geom_latlon::geography) / 1000000 as surface_km2
from arrondissements
ORDER by surface_km2 desc;


-- ST_Length
-- l'arrondissement avec le plus grand périmètre
select c_ar, l_ar, l_aroff, ST_Perimeter(ST_Transform(geom_latlon, 2154)) / 1000 as perimetre_km
from arrondissements
ORDER by perimetre_km desc;

-- en castant en geography
select c_ar, l_ar, l_aroff, ST_Perimeter(geom_latlon::geography) / 1000 as perimetre_km
from arrondissements
ORDER by perimetre_km desc;



-- ST_DWithin
-- recherche dans un périmètre
select *
from gares
where mode = 'Metro' AND ST_DWithin(ST_Transform(geom_latlon, 2154),ST_Transform('SRID=4326;POINT(2.282412153722246 48.878653091848705)'::geometry, 2154), 200);


-- ST_Intersects
-- autre façon de faire (ST_Buffer, ST_MakePoint et cast en geography pour bien avoir des mètres)
select *
from gares
where mode = 'Metro' AND ST_Intersects(geom_latlon, ST_Buffer(ST_SetSRID(ST_MakePoint(2.282412153722246, 48.878653091848705),4326)::geography, 200)::geometry);

-- dans quel arrondissement se trouve le palais des congrès
select l_ar, l_aroff, c_ar
from arrondissements
where ST_Intersects(geom_latlon, 'SRID=4326;POINT(2.282412153722246 48.878653091848705)'::geometry);

-- les stations de métro du 17e
select g.nom, g.ligne
from gares g
         inner join arrondissements a on ST_Intersects(a.geom_latlon, g.geom_latlon)
where a.c_ar = 17 and g.mode = 'Metro';

-- les stations de métro de l'arrondissement du palais des congrès
select g.nom, g.ligne
from gares g
         inner join arrondissements a on ST_Intersects(a.geom_latlon, g.geom_latlon)
where ST_Intersects(a.geom_latlon, 'SRID=4326;POINT(2.282412153722246 48.878653091848705)'::geometry) and g.mode = 'Metro';

-- les stations de métro de l'arrondissement du palais des congrès, avec résultat en GeoJSON
WITH gares_tmp AS (
  select g.nom, g.ligne, g.geom_latlon
  from gares g
  inner join arrondissements a on ST_Intersects(a.geom_latlon, g.geom_latlon)
  where ST_Intersects(a.geom_latlon, 'SRID=4326;POINT(2.282412153722246 48.878653091848705)'::geometry) and g.mode = 'Metro'

)
SELECT json_build_object(
               'type', 'FeatureCollection',
               'features', json_agg(ST_AsGeoJSON(t.*)::json)
           )
FROM gares_tmp t;


-- les stations de métro de l'arrondissement du palais des congrès et l'arrondissement, avec résultat en GeoJSON
WITH arrondissement_devoxx AS (
  SELECT l_ar, l_aroff, geom_latlon
  FROM arrondissements
  where ST_Intersects(geom_latlon, 'SRID=4326;POINT(2.282412153722246 48.878653091848705)'::geometry)
),
gares_tmp AS (
  SELECT g.nom, g.ligne, g.mode, g.geom_latlon
  FROM gares g
  inner join arrondissement_devoxx a on ST_Intersects(a.geom_latlon, g.geom_latlon)
  WHERE g.mode = 'Metro'
),
features AS (
  SELECT ST_AsGeoJSON(t.*) as feature
  FROM gares_tmp t
  UNION
  SELECT ST_AsGeoJSON(a.*) as feature
  FROM arrondissement_devoxx a
)
SELECT json_build_object(
               'type', 'FeatureCollection',
               'features', json_agg(f.feature::json)
           )
FROM features f;


-- INDEX
-- espaces verts qui intersectent plusieurs arrondissements (25 résultats env. 400ms)
with espaces_verts_with_arr AS (
    select distinct ev.nom_ev, c_ar
    from espaces_verts ev
             inner join arrondissements a ON ST_Intersects(ev.geom_latlon, a.geom_latlon)
)
select ev.nom_ev, count(*)
from espaces_verts_with_arr ev
group by ev.nom_ev
having count(*) > 1;


-- demo avec overlap (1106 résultats en env. 80ms)
with espaces_verts_with_arr AS (
    select distinct ev.nom_ev, c_ar
    from espaces_verts ev
             inner join arrondissements a ON ev.geom_latlon && a.geom_latlon
)
select ev.nom_ev, count(*)
from espaces_verts_with_arr ev
group by ev.nom_ev
having count(*) > 1;


-- montrer le plan d'exécution

-- création de l'index
CREATE INDEX espaces_verts_geom_latlon_idx ON espaces_verts USING gist (geom_latlon);
-- réexécution de la requête sur les espaces verts qui intersectent plusieurs arrondissements (env. 70ms)


-- voir les résultats sur geojson.io
with features AS (
    select ST_AsGeoJSON(a.*) as feature, 1 as orderIdx
    from (
             SELECT arr.*, '#0000FF' as fill
             from arrondissements arr
             inner join espaces_verts ev ON ST_Intersects(ev.geom_latlon,arr.geom_latlon)
             where ev.nom_ev = 'JARDINIERES DE LA PLACE DE CLICHY'
         ) a
    UNION
    select ST_AsGeoJSON(ev.*) as feature, 2 as orderIdx
    from (
             select e.*, '#00FF00' as fill
             from espaces_verts e
             where nom_ev = 'JARDINIERES DE LA PLACE DE CLICHY'
         ) ev
    order by orderIdx
)
SELECT json_build_object(
               'type', 'FeatureCollection',
               'features', json_agg(f.feature::json)
           )
FROM features f;




with tmp_arr AS (
    SELECT arr.*, '#' || rpad(floor(random()*(999999+1))::text, 6, '0') as fill
    from arrondissements arr
    inner join espaces_verts ev ON ev.geom_latlon && arr.geom_latlon
    where ev.nom_ev = '00-00'
),
 features AS (
    select ST_AsGeoJSON(a.*) as feature, 1 as orderIdx
    from tmp_arr a
    UNION
    SELECT ST_AsGeoJSON(bb.*) as feature, 0 as orderIdx
    FROM (
        SELECT ST_Envelope(geom_latlon), fill as stroke
        FROM tmp_arr
    ) bb
    UNION
    select ST_AsGeoJSON(ev.*) as feature, 2 as orderIdx
    from (
             select e.*, '#00FF00' as fill
             from espaces_verts e
             where nom_ev = '00-00'
         ) ev
    order by orderIdx
)
SELECT json_build_object(
               'type', 'FeatureCollection',
               'features', json_agg(f.feature::json)
           )
FROM features f;


SELECT
    ST_Distance(
            ST_GeographyFromText('Point(-118.4079 33.9434)'), -- LAX
            ST_GeographyFromText('Point(139.733 35.567)'))    -- NRT (Tokyo/Narita)
        AS geography_distance,
    ST_DistanceSphere(
            ST_GeometryFromText('Point(-118.4079 33.9434)'),  -- LAX
            ST_GeometryFromText('Point(139.733 35.567)'))     -- NRT (Tokyo/Narita)
        AS geometry_distance_sphere,
    ST_DistanceSpheroid(
            ST_GeometryFromText('Point(-118.4079 33.9434)'),  -- LAX
            ST_GeometryFromText('Point(139.733 35.567)'),     -- NRT (Tokyo/Narita)
            'SPHEROID["WGS 84",6378137,298.257223563]')
        AS geometry_distance_spheroid;
