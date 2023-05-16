package com.example.messangerusers.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.messangerusers.Entities.Parcel;
import com.example.messangerusers.Utils.Converters;

@Database(entities = {Parcel.class}, version = 7, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ParcelDatabase extends RoomDatabase {

    private static ParcelDatabase instance;

    public abstract ParcelDao parcelDao();

    public static synchronized ParcelDatabase getInstance(final Context context) {
        if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            ParcelDatabase.class, "parcel_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
        return instance;
    }
}

