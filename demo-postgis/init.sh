export PGPASSWORD=demopostgis
#export pg_cmd_base="psql -h localhost -p 5432 -d aux_alentours"
export pg_cmd_base="psql -h 192.168.86.52 -p 5400 -U postgres -d demo_postgis"


./download.sh
./transform.sh
./up-schemas.sh
./insert.sh
#./post-traitement.sh
./clean.sh
