#!/bin/sh
mkdir output
mkdir input
status_code=$(curl --write-out %{http_code} --silent --output /dev/null https://files.georisques.fr/ppr/pprn_17.zip)
if [[ "$status_code" -ne 200 ]]
then
  echo "No files for $i"
else
  curl https://files.georisques.fr/ppr/pprn_17.zip -o input/pprn_17.zip
fi

cd input
unzip pprn_17.zip

cd ..
echo "start $(date)"
cd input/n_zonages_risque_naturel
# Transform CSV files

mlr --icsv --ifs ';' --ocsv --ofs ';' cut -o -f id_gaspar,nom,etat,dateappro,datefinval,multi_risq,coderisque,site_web,uri_gaspar document_pprn_017.csv > document_pprn_light.csv

#if (coderisque === '9999999') {
#    code_risque = `${num_alea}00000`
#}
mlr --icsv --ifs ';' --ocsv --ofs ';' put '
if ($coderisque == 9999999) {
  $coderisque = "".$num_alea."00000"
} else {
  $coderisque = $coderisque
}
' multirisque_pprn_017.csv | mlr --icsv --ifs ';' --ocsv --ofs ';' cut -o -f id_gaspar,coderisque > multirisque_pprn_light.csv

# Transform shp to CSV
ogr2ogr -t_srs EPSG:4326 -f CSV zone_pprn_17.csv zone_pprn_s_017.shp -lco SEPARATOR=SEMICOLON -lco GEOMETRY=AS_WKT -lco GEOMETRY_NAME=geom_latlon -lco CREATE_CSVT=YES

mlr --icsv --ifs ';' --ocsv --ofs ';' cut -o -f id_zone,id_gaspar,nom,codezone,typereg,urlfic,geom_latlon zone_pprn_17.csv | mlr --icsv --ifs ';' --ocsv --ofs ';' put '
$geom_latlon = "SRID=4326; ". $geom_latlon .""
' > zone_pprn_transformed.csv


echo "end transformation ${date}"

export PGPASSWORD=
export pg_cmd_base="psql -h localhost -p 5432 -U demo_devoxx_user -d demo_devoxx"

cd ../..
echo "creation schemas without index ${date}"
file=../schemas/up-without-index.sql
CMD="$pg_cmd_base -f $file"
eval "$CMD"

echo "insert ref data ${date}"
file=../output/referentiel-data.sql
CMD="$pg_cmd_base -f $file"
eval "$CMD"


echo "start insert ${date}"

echo "copy document_pprn"
FILE="$(ls input/n_zonages_risque_naturel/document_pprn_light.csv)"
FULL_PATH="$(pwd)/$FILE"
CMD="$pg_cmd_base -c \"\COPY document_pprn FROM '$FULL_PATH' DELIMITER ';' CSV HEADER;\""
eval "$CMD"

echo "copy multirisque_pprn"
FILE="$(ls input/n_zonages_risque_naturel/multirisque_pprn_light.csv)"
FULL_PATH="$(pwd)/$FILE"
CMD="$pg_cmd_base -c \"\COPY multirisque_pprn FROM '$FULL_PATH' DELIMITER ';' CSV HEADER;\""
eval "$CMD"

echo "copy zone_pprn"
FILE="$(ls input/n_zonages_risque_naturel/zone_pprn_transformed.csv)"
FULL_PATH="$(pwd)/$FILE"
CMD="$pg_cmd_base -c \"\COPY zone_pprn FROM '$FULL_PATH' DELIMITER ';' CSV HEADER;\""
eval "$CMD"


echo "creation index"
file=../schemas/post-insert.sql
CMD="$pg_cmd_base -f $file"
eval "$CMD"


echo "end $(date)"
