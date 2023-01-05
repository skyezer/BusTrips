package com.kaiserdev.bustrips.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Passenger.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract Passenger_Dao qr_bus_trips_dao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Android_DB")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
