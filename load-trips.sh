#!/bin/bash

__dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

S3_URL_PREFIX="s3://divvy-data/tripdata"
TMP_DIR="${__dir}/tmp"
DESTINATION="${__dir}/data/input/divvy/trips"

mkdir -p $TMP_DIR $DESTINATION

FILES=("\
  Divvy_Stations_Trips_2013.zip \
  Divvy_Stations_Trips_2014_Q1Q2.zip \
  Divvy_Stations_Trips_2014_Q3Q4.zip \
  Divvy_Trips_2015-Q1Q2.zip \
  Divvy_Trips_2015_Q3Q4.zip \
  Divvy_Trips_2016_Q1Q2.zip \
  Divvy_Trips_2016_Q3Q4.zip \
")

for f in $FILES; do
  S3_URL="${S3_URL_PREFIX}/$f"
  if [ ! -f "${TMP_DIR}/$f" ]; then
    aws s3 cp $S3_URL $TMP_DIR
  fi
  cd $DESTINATION
  unzip -o "$TMP_DIR/*.zip" *.csv
done

cd $__dir
