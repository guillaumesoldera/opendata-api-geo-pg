
echo "post traitement - start $(date)"
file=schemas/post-update.sql

CMD="$pg_cmd_base -f $file"
eval "$CMD"

echo "post traitement done - end $(date)"