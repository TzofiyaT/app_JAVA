package com.example.messangerusers.UI.HistoryOfMyParcels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.messangerusers.Entities.Parcel;
import com.example.messangerusers.data.Repository;

import java.util.List;

public class HistoryOfMyParcelsViewModel extends ViewModel {
    private Repository repository;
    private LiveData<List<Parcel>> parcels;

    public HistoryOfMyParcelsViewModel() {
        repository = Repository.getRepositoryInstance();
        parcels = repository.getAllParcels();
    }

    public LiveData<List<Parcel>> getHistoryOfMyParcels() {
        return parcels;
    }

    public String getEmail()
    {
        return repository.email;
    }
}