echo "create schemas"
file=schemas/up.sql

CMD="$pg_cmd_base -f $file"
eval "$CMD"

echo "schemas creation done"
