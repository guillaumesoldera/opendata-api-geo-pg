if ! command -v wget &> /dev/null
then
    echo "wget could not be found, please install it"
    exit
fi


echo "Téléchargement des données sur les espaces verts"
wget https://opendata.paris.fr/explore/dataset/espaces_verts/download/\?format\=shp\&timezone\=Europe/Berlin\&lang\=fr -O input/espaces_verts.zip
echo "Téléchargement des données sur les espaces verts - DONE"