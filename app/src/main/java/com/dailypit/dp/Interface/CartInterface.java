package com.dailypit.dp.Interface;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dailypit.dp.Model.CartResponse;

import java.util.List;

@Dao
public interface CartInterface {

    @Query("SELECT * FROM cart")
    List<CartResponse> getallcartdata();
    @Insert
    void addcart(CartResponse cartResponse);

    @Query("UPDATE cart SET qty = :qty,s_total=:total,s_fees=:fees WHERE s_id = :id")
    void setQty(String id,String qty,String total,String fees);

    /*@Query("UPDATE cart SET qty = :qty WHERE s_id = :id")
    void setQty(String id,String qty);
*/

    @Query("DELETE FROM cart")
     void deleteall();


    @Query("DELETE FROM cart WHERE s_id= :id ")
    void deletebyid(String id);


    @Query("SELECT * FROM cart WHERE s_id= :id ")
    boolean isserviceavailabe(String id);


    @Query("SELECT qty FROM cart WHERE s_id= :id ")
    String getqty(String id);

}



