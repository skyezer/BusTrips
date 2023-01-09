package com.kaiserdev.bustrips.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Passenger {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "conductor_id")
    public String conductor_id;

    @ColumnInfo(name = "vehicle_id")
    public String vehicle_id;

    @ColumnInfo(name = "destination_id")
    public String destination_id;

    @ColumnInfo(name = "passenger_id")
    public String passenger_id;

    @ColumnInfo(name = "passenger_first_name")
    public String passenger_first_name;

    @ColumnInfo(name = "passenger_last_name")
    public String passenger_last_name;

    @ColumnInfo(name = "timestamp")
    public String timestamp;



}
