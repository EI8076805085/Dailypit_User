package com.dailypit.dp.Utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.dailypit.dp.Interface.PackageInterface;
import com.dailypit.dp.Model.Payment.PackageCartResponse;


@Database(entities = PackageCartResponse.class, exportSchema = false , version = 1)

public abstract class PackageDB extends RoomDatabase {

    private static final String DBNAME ="package";
    private static PackageDB instanse;
    public abstract PackageInterface packageInterface();

}

