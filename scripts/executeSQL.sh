echo $1 | tee | psql -qtAX -U $POSTGRES_USER -h $POSTGRES_HOST
