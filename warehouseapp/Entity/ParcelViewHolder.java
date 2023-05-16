package com.example.warehouseapp.Entity;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;


import com.example.warehouseapp.R;

public class ParcelViewHolder extends RecyclerView.ViewHolder {
    public TextView recipientName, recipientAddress, recipientEmail, recipientPhone, dateReceived, parcelType, deliveryPersonName, shippingDate;

    public ParcelViewHolder(@NonNull View itemView) {
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