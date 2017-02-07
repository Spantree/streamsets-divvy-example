CREATE KEYSPACE Divvy
  WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE Divvy.trips (
  trip_id int,
  start_time timestamp,
  end_time timestamp,
  bike_id int,
  trip_duration int,
  user_type text,
  gender text,
  birth_year int
  PRIMARY_KEY(start_time, end_time, trip_id)
);

CREATE INDEX trips_bike_id ON Divvy.trips (bike_id);

CREATE INDEX trips_user_type ON Divvy.trips (user_type);

CREATE INDEX trips_gender ON Divvy.trips (gender);
