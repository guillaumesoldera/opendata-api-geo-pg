#!/bin/sh
#mkdir output
#mkdir input
#status_code=$(curl --write-out %{http_code} --silent --output /dev/null https://files.georisques.fr/ppr/pprn_17.zip)
#if [[ "$status_code" -ne 200 ]]
#then
#  echo "No files for $i"
#else
#  curl https://files.georisques.fr/ppr/pprn_17.zip -o input/pprn_17.zip
#fi

cd input
#unzip pprn_17.zip

cd ..
echo "start $(date)"
echo "start SQL generation"
echo "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;" > output/document_pprn_17.sql
node --max-old-space-size=4096 document-pprn.js input/n_zonages_risque_naturel/document_pprn_017.csv input/n_zonages_risque_naturel/multirisque_pprn_017.csv >> output/document_pprn_17.sql
echo "COMMIT TRANSACTION;" >> output/document_pprn_17.sql
cd input/n_zonages_risque_naturel

ogr2ogr -t_srs EPSG:4326 -f GeoJSON ../zone_pprn_17.json zone_pprn_s_017.shp


# insert zone_pprn
cd ../../

echo "BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;" > output/zone_pprn_17.sql
node --max-old-space-size=4096 zone-pprn.js input/zone_pprn_17.json >> output/zone_pprn_17.sql
echo "" >> output/zone_pprn_17.sql
echo "COMMIT TRANSACTION;" >> output/zone_pprn_17.sql

echo "end SQL generation ${date}"

export PGPASSWORD=demopostgis
export pg_cmd_base="psql -h 192.168.86.52 -p 5400 -U postgres -d demo_devoxx"

echo "creation schemas ${date}"
file=../schemas/up.sql
CMD="$pg_cmd_base -f $file"
eval "$CMD"

echo "insert ref data ${date}"
file=../output/referentiel-data.sql
CMD="$pg_cmd_base -f $file"
eval "$CMD"


echo "start insert ${date}"
file=output/document_pprn_17.sql
CMD="$pg_cmd_base -f $file"
eval "$CMD"


file=output/zone_pprn_17.sql
CMD="$pg_cmd_base -f $file"
eval "$CMD"




echo "end $(date)"
