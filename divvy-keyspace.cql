DROP KEYSPACE Divvy;

CREATE KEYSPACE Divvy WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE Divvy.trips (
  trip_id int,
  start_time timestamp,
  end_time timestamp,
  bike_id int,
  trip_duration int,
  user_type text,
  to_station_id int,
  from_station_id int,
  PRIMARY KEY (start_time, end_time, trip_id)
);

CREATE INDEX trips_bike_id ON Divvy.trips (bike_id);
CREATE INDEX trips_user_type ON Divvy.trips (user_type);
CREATE INDEX trips_from_station ON Divvy.trips (from_station_id);
CREATE INDEX trips_to_station ON Divvy.trips (to_station_id);
