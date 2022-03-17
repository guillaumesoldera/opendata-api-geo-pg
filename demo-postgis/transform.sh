if ! command -v unzip &> /dev/null
then
    echo "unzip could not be found, please install it"
    exit
fi

if ! command -v ogr2ogr &> /dev/null
then
    echo "ogr2ogr could not be found, please install it"
    exit
fi

if ! command -v mlr &> /dev/null
then
    echo "mlr could not be found, please install it"
    exit
fi

cd input
unzip arrondissements.zip

echo "Transform arrondissements file to CSV"
for i in $(ls *.shp)
do
  fileName=$(basename $i)
  realFileName="${fileName%.*}"
  echo "Transform to CSV"
  ogr2ogr -t_srs EPSG:4326 -f CSV $realFileName.csv $i -lco GEOMETRY=AS_WKT -lco SEPARATOR=SEMICOLON -lco GEOMETRY_NAME=geom_latlon -lco CREATE_CSVT=YES
done

echo "Transform file"
mlr --icsv --ifs ';' --ocsv --ofs ';' cut -o -f c_ar,c_arinsee,l_ar,l_aroff,geom_latlon arrondissements.csv | mlr --icsv --ifs ';' --ocsv --ofs ';' put '
  $geom_latlon = "SRID=4326; ". $geom_latlon .""
' | mlr --icsv --ifs ';' --ocsv --ofs ';' filter '$geom_latlon != "SRID=4326; "' > arrondissements_transformed.csv

unzip emplacement-des-gares-idf
for i in $(ls *.shp)
do
  fileName=$(basename $i)
  realFileName="${fileName%.*}"
  echo "Transform to CSV"
  ogr2ogr -t_srs EPSG:4326 -f CSV $realFileName.csv $i -lco GEOMETRY=AS_WKT -lco SEPARATOR=SEMICOLON -lco GEOMETRY_NAME=geom_latlon -lco CREATE_CSVT=YES
done

echo "Transform file"
mlr --icsv --ifs ';' --ocsv --ofs ';' cut -o -f gares_id,nom_long,nom,mode,ligne,reseau,exploitant,geom_latlon emplacement-des-gares-idf.csv | mlr --icsv --ifs ';' --ocsv --ofs ';' put '
  $geom_latlon = "SRID=4326; ". $geom_latlon .""
' | mlr --icsv --ifs ';' --ocsv --ofs ';' filter '$geom_latlon != "SRID=4326; "' > emplacement-des-gares-idf_transformed.csv


unzip espaces_verts.zip

for i in $(ls *.shp)
do
  fileName=$(basename $i)
  realFileName="${fileName%.*}"
  echo "Transform to CSV"
  ogr2ogr -t_srs EPSG:4326 -f CSV $realFileName.csv $i -lco GEOMETRY=AS_WKT -lco SEPARATOR=SEMICOLON -lco GEOMETRY_NAME=geom_latlon -lco CREATE_CSVT=YES
done

echo "Transform file"
mlr --icsv --ifs ';' --ocsv --ofs ';' cut -o -f nsq_espace_,nom_ev,type_ev,categorie,geom_latlon espaces_verts.csv | mlr --icsv --ifs ';' --ocsv --ofs ';' put '
  $geom_latlon = "SRID=4326; ". $geom_latlon .""
' | mlr --icsv --ifs ';' --ocsv --ofs ';' filter '$geom_latlon != "SRID=4326; "' > espaces_verts_transformed.csv

