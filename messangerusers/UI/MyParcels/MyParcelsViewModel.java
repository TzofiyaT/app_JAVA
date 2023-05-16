package com.example.messangerusers.UI.MyParcels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.messangerusers.Entities.Parcel;
import com.example.messangerusers.data.Repository;

import java.util.List;

public class MyParcelsViewModel extends ViewModel {

    private Repository repository;
    private LiveData<List<Parcel>> parcels;

    public MyParcelsViewModel() {
        repository = Repository.getRepositoryInstance();
        parcels = repository.getMyParcels();
    }

    public LiveData<List<Parcel>> getMyParcels() {
        return parcels;
    }

    public String getEmail()
    {
        return repository.email;
    }

    public void updateParcel(Parcel parcel)
    {
        repository.updateDeliveryPersonName(parcel);
        repository.updatePackageStatus(parcel);
        repository.updateShippingDate(parcel);
    }
}