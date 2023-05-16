package com.example.messangerusers.UI.HistoryOfMyParcels;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerusers.Entities.Enums;
import com.example.messangerusers.Entities.Parcel;
import com.example.messangerusers.R;
import com.example.messangerusers.Utils.Converters;

import java.util.ArrayList;
import java.util.List;

public class HistoryOfMyParcelsAdapter extends RecyclerView.Adapter<HistoryOfMyParcelsAdapter.HistoryOfMyParcelsHolder> {

    private List<Parcel> parcels = new ArrayList<>();
    private Context context;
    private String email;
    String[] colors;

    public HistoryOfMyParcelsAdapter(String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public HistoryOfMyParcelsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parcel_item,parent,false);
        context = parent.getContext();
        colors = new String[5];
        colors[0] = "#a8e6cf";
        colors[1] = "#dcedc1";
        colors[2] = "#ffd3b6";
        colors[3] = "#ffaaa5";
        colors[4] = "#ff8b94";
        return new HistoryOfMyParcelsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryOfMyParcelsHolder holder, int position) {
        ((CardView)holder.itemView).setCardBackgroundColor(Color.parseColor(colors[position%5]));
        Parcel currentParcel = parcels.get(position);
        holder.recipientName.setText(currentParcel.getRecipientName());
        holder.recipientAddress.setText(Converters.getStringAddressFromLatLng(context,currentParcel.getRecipientAddress()));
        holder.recipientEmail.setText(currentParcel.getRecipientEmail());
        holder.recipientPhone.setText(currentParcel.getRecipientPhone());
        holder.dateReceived.setText(currentParcel.getDateReceived());
        holder.parcelType.setText(currentParcel.getParcelType().toString());
        holder.deliveryPersonName.setText(currentParcel.getDeliveryPersonName());
        holder.shippingDate.setText(currentParcel.getShippingDate());
    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }

    public void setFriendsParcels(List<Parcel> parcels)
    {
        this.parcels.clear();
        for (Parcel parcel: parcels) {
            if (parcel.getParcelStatus() == Enums.ParcelStatus.received && parcel.getRecipientEmail().equals(email))
                this.parcels.add(parcel);
        }
        notifyDataSetChanged();
    }

    class HistoryOfMyParcelsHolder extends RecyclerView.ViewHolder{
        private TextView recipientName;
        private TextView recipientAddress;
        private TextView recipientEmail;
        private TextView recipientPhone;
        private TextView dateReceived;
        private TextView parcelType;
        private TextView deliveryPersonName;
        private TextView shippingDate;


        public HistoryOfMyParcelsHolder(@NonNull View itemView) {
            super(itemView);
            recipientName = itemView.findViewById(R.id.recipientName);
            recipientAddress = itemView.findViewById(R.id.recipientAddress);
            recipientEmail = itemView.findViewById(R.id.recipientEmail);
            recipientPhone = itemView.findViewById(R.id.recipientPhone);
            dateReceived = itemView.findViewById(R.id.dateReceived);
            parcelType = itemView.findViewById(R.id.parcelType);
            deliveryPersonName = itemView.findViewById(R.id.deliveryPersonName);
            shippingDate = itemView.findViewById(R.id.shippingDate);
        }
    }
}
