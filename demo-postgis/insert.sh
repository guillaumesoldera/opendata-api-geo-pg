echo "insert data"

echo "copy arrondissements"
FILE="$(ls input/arrondissements_transformed.csv)"
FULL_PATH="$(pwd)/$FILE"
CMD="$pg_cmd_base -c \"\COPY arrondissements FROM '$FULL_PATH' DELIMITER ';' CSV HEADER;\""
eval "$CMD"

echo "copy arrondissements done"

echo "copy emplacements gares"
FILE="$(ls input/emplacement-des-gares-idf_transformed.csv)"
FULL_PATH="$(pwd)/$FILE"
CMD="$pg_cmd_base -c \"\COPY gares FROM '$FULL_PATH' DELIMITER ';' CSV HEADER;\""
eval "$CMD"

echo "copy emplacements gares done"

echo "copy espaces_verts"
FILE="$(ls input/espaces_verts_transformed.csv)"
FULL_PATH="$(pwd)/$FILE"
CMD="$pg_cmd_base -c \"\COPY espaces_verts FROM '$FULL_PATH' DELIMITER ';' CSV HEADER;\""
eval "$CMD"

echo "copy espaces_verts done"