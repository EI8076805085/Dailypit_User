package com.dailypit.dp.Utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dailypit.dp.Interface.CartInterface;
import com.dailypit.dp.Model.CartResponse;

@Database(entities = CartResponse.class, exportSchema = false , version = 1)

public abstract class DatabaseHandler extends RoomDatabase {

    private static final String DBNAME="cart";
    private static DatabaseHandler instanse;
    public  abstract CartInterface cartInterface();

}

