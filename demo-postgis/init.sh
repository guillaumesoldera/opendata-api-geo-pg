export pg_cmd_base="psql -h localhost -p 5432 -d demo_postgis -U demo_postgis_user"

./download.sh
./transform.sh
./up-schemas.sh
./insert.sh
./post-traitement.sh
./clean.sh
