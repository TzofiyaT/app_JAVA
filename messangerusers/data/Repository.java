package com.example.messangerusers.data;

import android.app.Application;
import android.os.AsyncTask;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import com.example.messangerusers.Entities.Enums;
import com.example.messangerusers.Entities.Parcel;

import java.util.List;

public class Repository {

    private FirebaseManager fireBaseManager;
    private ParcelDao parcelDao;
    private LiveData<List<Parcel>> allParcels;
    private LiveData<List<Parcel>> noDeliveryPersonParcels;
    private LiveData<List<Parcel>> myParcels;
    private static Repository repositoryInstance;
    public String username;
    public String email;

    private Repository(Application application, String username, String usermail) {
        fireBaseManager = FirebaseManager.getInstance();
        ParcelDatabase db = ParcelDatabase.getInstance(application);
        parcelDao = db.parcelDao();
        allParcels = parcelDao.getParcels();
        noDeliveryPersonParcels = parcelDao.getFriendsParcels("NO", Enums.ParcelStatus.sent);
        this.username = username;
        this.email = usermail;
        updateLists();
        myParcels = parcelDao.getMyParcels(usermail,Enums.ParcelStatus.someoneOfferedToTake);
        deleteAllParcels();
    }

    public static Repository createRepositoryInstance(Application application, String username,String usermail) {
        if(repositoryInstance == null)
            repositoryInstance = new Repository(application,username,usermail);
        return repositoryInstance;
    }

    public static Repository getRepositoryInstance() {
        return repositoryInstance;
    }

    private void updateLists()
    {
        fireBaseManager.notifyToDeliveryList(new FirebaseManager.NotifyDataChange<Parcel>() {

            @Override
            public void OnDataAdded(Parcel obj) {
                if(noDeliveryPersonParcels.getValue() != null)
                {
                for (Parcel parcel: noDeliveryPersonParcels.getValue()) {
                    if(parcel.getFireBasePushId().equals(obj.getFireBasePushId()))
                        return;
                }
                }
                insert(obj);
            }

            @Override
            public void OnDataChanged(Parcel obj) {
                if(noDeliveryPersonParcels.getValue() != null)
                {
                    for (Parcel parcel: noDeliveryPersonParcels.getValue()) {
                        if(parcel.getFireBasePushId().equals(obj.getFireBasePushId()))
                        {
                            parcel.setPossibleDeliveryPersonsList(obj.getPossibleDeliveryPersonsList());
                            return;
                        }

                    }
                }
            }

            @Override
            public void OnDataRemoved(Parcel obj) {

            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }


    public LiveData<List<Parcel>> getAllParcels() {
        return allParcels;
    }
    public LiveData<List<Parcel>> getNoDeliveryPersonParcels() { return noDeliveryPersonParcels;}
    public LiveData<List<Parcel>> getMyParcels() { return myParcels; }

    private void insert(Parcel parcel) {
        new InsertAsyncTask(parcelDao,fireBaseManager).execute(parcel);
    }

    public void updateShippingDate(Parcel parcel) {
        new UpdateShippingDateAsyncTask(parcelDao,fireBaseManager).execute(parcel);
    }

    public void updatePackageStatus(Parcel parcel) {
        new UpdatePackageStatusAsyncTask(parcelDao,fireBaseManager).execute(parcel);
    }

    public void updateDeliveryPersonName(Parcel parcel) {
        new UpdateDeliveryPersonNameAsyncTask(parcelDao, fireBaseManager).execute(parcel);
    }

    public void updatePossibleDeliveryPersonsList(Parcel parcel) {
        new UpdatePossibleDeliveryPersonsListAsyncTask(parcelDao, fireBaseManager).execute(parcel);
    }

    public void delete(Parcel parcel, FirebaseManager.Action action) {
        new DeleteNoteAsyncTask(parcelDao,fireBaseManager, action).execute(parcel);
    }

    public void deleteAllParcels() {
        new DeleteAllParcelsAsyncTask(parcelDao).execute();
    }



    //region AsyncTask implementation


    private static class InsertAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDao dao;
        private FirebaseManager firebaseManager;

        private InsertAsyncTask(ParcelDao dao, FirebaseManager fireBaseManager) {
            this.dao = dao;
            this.firebaseManager = fireBaseManager;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            dao.insert(parcels[0]);
            return null;
        }
    }


    private static class UpdateShippingDateAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDao dao;
        private FirebaseManager firebaseManager;

        private UpdateShippingDateAsyncTask(ParcelDao dao, FirebaseManager fireBaseManager) {
            this.dao = dao;
            this.firebaseManager = fireBaseManager;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            dao.updateShippingDate(parcels[0].getShippingDate(),parcels[0].getFireBasePushId());
            firebaseManager.updatePackageShippingDate(parcels[0].getShippingDate(),parcels[0].getFireBasePushId());
            return null;
        }
    }


    private static class UpdatePackageStatusAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDao dao;
        private FirebaseManager firebaseManager;

        private UpdatePackageStatusAsyncTask(ParcelDao dao, FirebaseManager fireBaseManager) {
            this.dao = dao;
            this.firebaseManager = fireBaseManager;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            dao.updatePackageStatus(parcels[0].getParcelStatus(),parcels[0].getFireBasePushId());
            firebaseManager.updatePackageStatus(parcels[0].getParcelStatus(),parcels[0].getFireBasePushId());
            return null;
        }
    }


    private static class UpdateDeliveryPersonNameAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDao dao;
        private FirebaseManager firebaseManager;

        private UpdateDeliveryPersonNameAsyncTask(ParcelDao dao, FirebaseManager fireBaseManager) {
            this.dao = dao;
            this.firebaseManager = fireBaseManager;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            dao.updateDeliveryPersonName(parcels[0].getDeliveryPersonName(),parcels[0].getFireBasePushId());
            firebaseManager.updateDeliveryPersonName(parcels[0].getDeliveryPersonName(),parcels[0].getFireBasePushId());
            return null;
        }
    }


    private static class UpdatePossibleDeliveryPersonsListAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDao dao;
        private FirebaseManager firebaseManager;

        private UpdatePossibleDeliveryPersonsListAsyncTask(ParcelDao dao, FirebaseManager fireBaseManager) {
            this.dao = dao;
            this.firebaseManager = fireBaseManager;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            String listToString = TextUtils.join(",", parcels[0].getPossibleDeliveryPersonsList());
            dao.updatePossibleDeliveryPersonsList(listToString, parcels[0].getFireBasePushId());
            firebaseManager.updatePossibleDeliveryPersonsList(parcels[0].getPossibleDeliveryPersonsList(),parcels[0].getFireBasePushId());
            return null;
        }
    }


    private static class DeleteNoteAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDao dao;
        private FirebaseManager firebaseManager;
        private FirebaseManager.Action action;

        private DeleteNoteAsyncTask(ParcelDao dao, FirebaseManager fireBaseManager, FirebaseManager.Action action) {
            this.dao = dao;
            this.firebaseManager = fireBaseManager;
            this.action = action;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            dao.delete(parcels[0]);
            firebaseManager.removeParcel(parcels[0].getFireBasePushId(),action);
            return null;
        }
    }

    private static class DeleteAllParcelsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ParcelDao dao;
        private DeleteAllParcelsAsyncTask(ParcelDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllParcels();
            return null;
        }
    }

    //endregion
}

