# demo MLR

# https://miller.readthedocs.io/en/latest/reference-verbs

# wc -l catnat_gaspar.csv -> 179392

# cat

./miller-6.2.0-macos-amd64/mlr cat catnat_gaspar.csv
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' cat catnat_gaspar.csv

# head / tail
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' head -n 10 catnat_gaspar.csv
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' tail -n 10 catnat_gaspar.csv

# output
## pretty print (--opprint)
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --opprint head -n 10 catnat_gaspar.csv
## CSV
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --ocsv --ofs ';' head -n 10 catnat_gaspar.csv
## JSON
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --ojson head -n 10 catnat_gaspar.csv

# sort

./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --opprint sort -t dat_pub_jo then head -n 5 catnat_gaspar.csv
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --opprint sort -tr dat_pub_jo then head -n 5 catnat_gaspar.csv

# filter
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --opprint filter '$cod_commune == "86194"' catnat_gaspar.csv
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --opprint filter '
  strptime($dat_pub_jo, "%Y-%m-%d %H:%M:%S") > strptime("2021-01-01", "%Y-%m-%d")
' then count catnat_gaspar.csv

# cut
# enlever la colonne qui sert pas
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --opprint filter '$cod_commune == "86194"' then cut -x -f dat_maj catnat_gaspar.csv

# put
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --opprint put '
$departement = regextract($cod_nat_catnat, "[0-9]*")
' then filter '$departement == "86"' then cut -f cod_nat_catnat,cod_commune,lib_commune,num_risque_jo,lib_risque_jo,departement catnat_gaspar.csv


# count
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --opprint filter '$cod_commune == "86194"' then count catnat_gaspar.csv
./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --opprint filter '$cod_commune == "86194"' then count -g lib_risque_jo catnat_gaspar.csv


# stats

./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --opprint count -g cod_commune,lib_commune \
then stats1 -a max -f count catnat_gaspar.csv

#./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --opprint count -g cod_commune,lib_commune \
#then sort -nr count then head -n 3 catnat_gaspar.csv

./miller-6.2.0-macos-amd64/mlr --icsv --ifs ';' --opprint count -g cod_commune,lib_commune \
then stats1 -a max,mean,p10,p90 -f count catnat_gaspar.csv
