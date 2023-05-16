package com.example.messangerusers.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.messangerusers.Entities.Enums;
import com.example.messangerusers.Entities.Parcel;

import java.util.List;

@Dao
public interface ParcelDao {
    @Insert
    void insert(Parcel parcel);

    @Delete
    void delete(Parcel parcel);

    @Query("UPDATE parcel_table SET shippingDate=:shippingDate WHERE fireBasePushId = :id")
    void updateShippingDate(String shippingDate, String id);

    @Query("UPDATE parcel_table SET parcelStatus=:status WHERE fireBasePushId = :id")
    void updatePackageStatus(Enums.ParcelStatus status, String id);

    @Query("UPDATE parcel_table SET deliveryPersonName=:deliveryPersonName WHERE fireBasePushId = :id")
    void updateDeliveryPersonName(String deliveryPersonName, String id);

    @Query("UPDATE parcel_table SET possibleDeliveryPersonsList = :possibleList WHERE fireBasePushId = :tid")
    void updatePossibleDeliveryPersonsList(String possibleList, String tid);

    @Query("DELETE FROM parcel_table")
    void deleteAllParcels();

    @Query("SELECT * from parcel_table")
    LiveData<List<Parcel>> getParcels();

    @Query("SELECT * from parcel_table WHERE deliveryPersonName LIKE :deliveryPersonName AND parcelStatus = :status ORDER BY dateReceived ASC")
    LiveData<List<Parcel>> getFriendsParcels(String deliveryPersonName, Enums.ParcelStatus status);

    @Query("SELECT * from parcel_table WHERE recipientEmail LIKE :recipientEmail AND parcelStatus = :status ORDER BY dateReceived ASC")
    LiveData<List<Parcel>> getMyParcels(String recipientEmail, Enums.ParcelStatus status);

}