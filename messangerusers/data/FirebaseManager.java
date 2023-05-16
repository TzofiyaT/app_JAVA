package com.example.messangerusers.data;

import androidx.annotation.NonNull;

import com.example.messangerusers.Entities.Enums;
import com.example.messangerusers.Entities.Parcel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FirebaseManager {
    private static FirebaseManager firebaseManagerInstance = null;

    private FirebaseManager(){}

    //singelton
    public static FirebaseManager getInstance()
    {
        if (firebaseManagerInstance == null)
            firebaseManagerInstance = new FirebaseManager();

        return firebaseManagerInstance;
    }

    //gives us information about insert, update and delete
    public interface Action<Parcel> {

        void onSuccess(Parcel obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }

    //listens for changes in updates. and makes sure non repeat.
    public interface NotifyDataChange<Parcel> {
       void OnDataAdded(Parcel obj);

        void OnDataChanged(Parcel obj);

        void OnDataRemoved(Parcel obj);

        void onFailure(Exception exception);
    }

    static DatabaseReference parcelsRef;//a reference to fireBase
    static List<Parcel> parcels;

    static {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        parcelsRef = database.getReference("parcels");//root
        parcels = new ArrayList<>();//initialization
    }


    public static String addParcel(final Parcel parcel, final Action<String> action) {
        String pushId = parcelsRef.push().getKey();
        parcel.setFireBasePushId(pushId);
        parcelsRef.child(pushId).setValue(parcel).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(parcel.getFireBasePushId());
                action.onProgress("upload package data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("Error upload package data", 100);

            }
        });
        return pushId;
    }


    public static void removeParcel(final String id, final Action<String> action) {

        parcelsRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Parcel value = dataSnapshot.getValue(Parcel.class);
                if (value == null)
                    action.onFailure(new Exception("Parcel didn't find ..."));
                else {
                    parcelsRef.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            action.onSuccess(value.getFireBasePushId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            action.onFailure(e);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                action.onFailure(databaseError.toException());
            }
        });
    }


    public static void updatePackage(final Parcel toUpdate, final Action<String> action) {

        removeParcel(toUpdate.getFireBasePushId(), new Action<String>() {
            @Override
            public void onSuccess(String obj) {
                addParcel(toUpdate, action);
            }

            @Override
            public void onFailure(Exception exception) {
                action.onFailure(exception);
            }

            @Override
            public void onProgress(String status, double percent) {
                action.onProgress(status, percent);
            }
        });
    }

    //listens to updates of elements in the database
    private static ChildEventListener PackageRefChildEventListener;

    //if the listener found that a change has happened it will delete the old one and create a new one instead.
    public static void notifyToDeliveryList(final NotifyDataChange<Parcel> notifyDataChange) {
        if (notifyDataChange != null) {

            if (PackageRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify Package list"));
                return;
            }
            parcels.clear();

            PackageRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Parcel newParcel = dataSnapshot.getValue(Parcel.class);
                    parcels.add(newParcel);

                    notifyDataChange.OnDataAdded(newParcel);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Parcel changeParcel = dataSnapshot.getValue(Parcel.class);

                    notifyDataChange.OnDataChanged(changeParcel);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Parcel removedParcel = dataSnapshot.getValue(Parcel.class);

                    notifyDataChange.OnDataRemoved(removedParcel);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            parcelsRef.addChildEventListener(PackageRefChildEventListener);
        }
    }

    //stopped listening to changes
    public static void stopNotifyToDelivery() {
        if (PackageRefChildEventListener != null) {
            parcelsRef.removeEventListener(PackageRefChildEventListener);
            PackageRefChildEventListener = null;
        }
    }


    public static void updatePackageShippingDate(String sd, String id)
    {
        parcelsRef.child(id).child("shippingDate").setValue(sd);
    }

    public static void updatePackageStatus(Enums.ParcelStatus status, String id)
    {
        parcelsRef.child(id).child("parcelStatus").setValue(status);
    }

    public static void updateDeliveryPersonName(String pn, String id)
    {
        parcelsRef.child(id).child("deliveryPersonName").setValue(pn);
    }

    public static void updatePossibleDeliveryPersonsList(List<String> list, String id)
    {
        parcelsRef.child(id).child("possibleDeliveryPersonsList").setValue(list);
    }

    public static List<String> getPossibleDeliveryPersonsList(String id)
    {
        final List<String>[] list = (List<String>[]) new Object[1];
        parcelsRef.child(id).child("possibleDeliveryPersonsList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
              list[0] = (List<String>) snapshot.getValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return list[0];
    }
}

