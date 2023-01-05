package com.kaiserdev.bustrips.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface Passenger_Dao {

    @Query("SELECT * FROM Passenger")
    List<Passenger>getAllPassengers();

    @Insert
    void insertPassenger(Passenger... passengers);

    @Delete
    void delete(Passenger passenger);
}
