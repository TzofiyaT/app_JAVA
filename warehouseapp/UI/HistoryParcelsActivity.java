package com.example.warehouseapp.UI;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warehouseapp.Entity.ParcelViewHolder;
import com.example.warehouseapp.Entity.Parcel;
import com.example.warehouseapp.R;
import com.example.warehouseapp.Utils.Converters;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class HistoryParcelsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Parcel> arrayList;

    private DatabaseReference databaseReference;
    private FirebaseRecyclerOptions<Parcel> options;
    private FirebaseRecyclerAdapter<Parcel, ParcelViewHolder> firebaseRecyclerAdapter;
    Context context;
    //initialize these variable



    @Override
    protected void onStart() {
        firebaseRecyclerAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        firebaseRecyclerAdapter.stopListening();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_parcels);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<Parcel>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("parcels");
        databaseReference.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<Parcel>().setQuery(databaseReference,Parcel.class).build();
        context = this;


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Parcel, ParcelViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ParcelViewHolder holder, int position, @NonNull final Parcel model) {
                //FirebaseRecyclerView main task where it fetching data from model
                holder.recipientName.setText(model.getRecipientName());
                holder.recipientAddress.setText(Converters.getStringAddressFromLatLng(context, model.getRecipientAddress()));
                holder.recipientEmail.setText(model.getRecipientEmail());
                holder.recipientPhone.setText(model.getRecipientPhone());
                holder.dateReceived.setText(model.getDateReceived());
                holder.parcelType.setText(model.getParcelType().toString());
                holder.deliveryPersonName.setText(model.getDeliveryPersonName());
                holder.shippingDate.setText(model.getShippingDate());
            }

            @NonNull
            @Override
            public ParcelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                //we need to return a object of our modelviewholder and modelview needs 2 parameter value
                //(Layout Inflater ,inflate)
                //Layout Inflater is the View Which will be replace
                //inflate which will take place
                //viewgroup means the recyclerview rows which will be replace
                //attachroot false as we dont want to show root

                return new ParcelViewHolder(LayoutInflater.from(HistoryParcelsActivity.this).inflate(R.layout.row,viewGroup,false));
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}