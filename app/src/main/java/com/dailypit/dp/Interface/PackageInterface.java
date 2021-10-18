package com.dailypit.dp.Interface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.dailypit.dp.Model.Payment.PackageCartResponse;
import java.util.List;

@Dao
public interface PackageInterface {

    @Query("SELECT * FROM package")
    List<PackageCartResponse> getallpackagecartdata();
    @Insert
    void addpackagecart(PackageCartResponse packageCartResponse);

    @Query("UPDATE package SET qty = :qty,s_total=:total,s_fees=:fees WHERE s_id = :id")
    void setpackageQty(String id,String qty,String total,String fees);

    @Query("DELETE FROM package")
    void deleteallpackage();

    @Query("DELETE FROM package WHERE s_id= :id ")
    void deletebypackageid(String id);


    @Query("SELECT * FROM package WHERE s_id= :id ")
    boolean isserviceavailabepackage(String id);

}
