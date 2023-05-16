package com.example.messangerusers.UI.FriendsParcels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.messangerusers.Entities.Parcel;
import com.example.messangerusers.data.Repository;

import java.util.List;

public class FriendParcelsViewModel extends ViewModel {

    private Repository repository;
    private LiveData<List<Parcel>> parcels;

    public FriendParcelsViewModel() {
        repository = Repository.getRepositoryInstance();
        parcels = repository.getNoDeliveryPersonParcels();
    }

    public LiveData<List<Parcel>> getNoDeliveryPersonParcels() {
        return parcels;
    }

    public String getName()
    {
        return repository.username;
    }

    public void updateParcel(Parcel parcel)
    {
        repository.updatePossibleDeliveryPersonsList(parcel);
        repository.updatePackageStatus(parcel);
    }
}