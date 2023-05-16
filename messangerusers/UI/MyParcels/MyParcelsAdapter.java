package com.example.messangerusers.UI.MyParcels;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerusers.Entities.Enums;
import com.example.messangerusers.Entities.Parcel;
import com.example.messangerusers.R;
import com.example.messangerusers.Utils.Converters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyParcelsAdapter extends RecyclerView.Adapter<MyParcelsAdapter.MyParcelHolder> {

    private List<Parcel> parcels = new ArrayList<>();
    private Context context;
    private OnAcceptClickListener onAcceptClickListener;
    private String email;
    String[] colors;

    public MyParcelsAdapter(String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public MyParcelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myparcels_parcel_item,parent,false);
        context = parent.getContext();
        colors = new String[5];
        colors[0] = "#a8e6cf";
        colors[1] = "#dcedc1";
        colors[2] = "#ffd3b6";
        colors[3] = "#ffaaa5";
        colors[4] = "#ff8b94";
        return new MyParcelHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyParcelHolder holder, int position) {


        ((CardView)holder.itemView).setCardBackgroundColor(Color.parseColor(colors[position%5]));


        Parcel currentParcel = parcels.get(position);
        holder.dateReceivedTextView.setText(currentParcel.getDateReceived());
        holder.parcelTypeTextView.setText(Converters.parcelTypeEnumToString(currentParcel.getParcelType()));
        if(currentParcel.isFragile())
            holder.isFragileTextView.setText("Yes");
        else
            holder.isFragileTextView.setText("No");
        holder.parcelWeightTextView.setText(Converters.parcelWeightEnumToString(currentParcel.getParcelWeight()));

        List<String> spinnerArray = currentParcel.getPossibleDeliveryPersonsList();
        spinnerArray.remove("null");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.possibleDeliveryPersonsSpinner.setAdapter(adapter);

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = holder.possibleDeliveryPersonsSpinner.getSelectedItem().toString();
                for (String str:currentParcel.getPossibleDeliveryPersonsList()) {
                    if (selected.equals(str)) {
                        Parcel updatedParcel = new Parcel(currentParcel);
                        updatedParcel.setDeliveryPersonName(str);
                        updatedParcel.setParcelStatus(Enums.ParcelStatus.received);
                        updatedParcel.setShippingDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
                        onAcceptClickListener.OnAcceptClick(updatedParcel);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }

    public void setFriendsParcels(List<Parcel> parcels)
    {
        this.parcels.clear();
        this.parcels = parcels;
        notifyDataSetChanged();
    }


    class MyParcelHolder extends RecyclerView.ViewHolder
    {
        private TextView dateReceivedTextView;
        private TextView parcelTypeTextView;
        private TextView isFragileTextView;
        private TextView parcelWeightTextView;
        private Spinner possibleDeliveryPersonsSpinner;
        private Button acceptButton;

        public MyParcelHolder(@NonNull View itemView) {
            super(itemView);
            dateReceivedTextView = itemView.findViewById(R.id.text_view_fill_date_received);
            parcelTypeTextView = itemView.findViewById(R.id.text_view_fill_parcelType);
            isFragileTextView = itemView.findViewById(R.id.text_view_fill_fragile);
            parcelWeightTextView = itemView.findViewById(R.id.text_view_fill_weight);
            possibleDeliveryPersonsSpinner = itemView.findViewById(R.id.spinner);
            acceptButton = itemView.findViewById(R.id.button_accept);
        }
    }
    public interface OnAcceptClickListener
    {
        void OnAcceptClick(Parcel parcel);
    }

    public void setOnAcceptClickListener(OnAcceptClickListener listener)
    {
        this.onAcceptClickListener = listener;
    }
}
