package com.example.messangerusers.UI.FriendsParcels;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerusers.Entities.Enums;
import com.example.messangerusers.Entities.Parcel;
import com.example.messangerusers.R;
import com.example.messangerusers.UI.MainActivity;
import com.example.messangerusers.Utils.Converters;

import java.util.ArrayList;
import java.util.List;

public class FriendsParcelsAdapter extends RecyclerView.Adapter<FriendsParcelsAdapter.FriendParcelHolder> {

    private List<Parcel> parcels = new ArrayList<>();
    String myName;
    private Context context;
    private OnTakeClickListener onTakeClickListener;
    String[] colors;

    public FriendsParcelsAdapter(String myName) {
        this.myName = myName;
    }

    @NonNull
    @Override
    public FriendParcelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_parcel_item,parent,false);
        context = parent.getContext();

        colors = new String[5];
        colors[0] = "#a8e6cf";
        colors[1] = "#dcedc1";
        colors[2] = "#ffd3b6";
        colors[3] = "#ffaaa5";
        colors[4] = "#ff8b94";
        return new FriendParcelHolder(itemView);
    }

    //get data to view
    @Override
    public void onBindViewHolder(@NonNull FriendParcelHolder holder, int position) {
        ((CardView)holder.itemView).setCardBackgroundColor(Color.parseColor(colors[position%5]));

        Parcel currentParcel = parcels.get(position);
        holder.friendNameTextView.setText(currentParcel.getRecipientName());

        Location warehouse_location = new Location("Warehouse Location");
        warehouse_location.setLatitude(currentParcel.getWarehouseLocation().getLatitude());
        warehouse_location.setLongitude(currentParcel.getWarehouseLocation().getLongitude());

        double distance = MainActivity.currentLocation.distanceTo(warehouse_location);

        holder.distanceTextView.setText(Integer.toString(((int)distance)/10));
        holder.addressTextView.setText(Converters.getStringAddressFromLatLng(context,currentParcel.getRecipientAddress()));
        holder.parcelTypeTextView.setText(Converters.parcelTypeEnumToString(currentParcel.getParcelType()));
        if(currentParcel.isFragile())
            holder.isFragileTextView.setText("Yes");
        else
            holder.isFragileTextView.setText("No");
        holder.takeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parcel updatedParcel = new Parcel(currentParcel);
                updatedParcel.addPossibleDeliveryPerson(myName);
                updatedParcel.setParcelStatus(Enums.ParcelStatus.someoneOfferedToTake);
                onTakeClickListener.OnTakeClick(updatedParcel);
                notifyDataSetChanged();
            }
        });
    }

    //How many items we want to display?
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


    class FriendParcelHolder extends RecyclerView.ViewHolder{
        private TextView friendNameTextView;
        private TextView distanceTextView;
        private TextView addressTextView;
        private TextView parcelTypeTextView;
        private TextView isFragileTextView;
        private Button takeButton;

        public FriendParcelHolder(@NonNull View itemView) {
            super(itemView);
            friendNameTextView = itemView.findViewById(R.id.text_view_friend_name);
            distanceTextView= itemView.findViewById(R.id.text_view_distance);
            addressTextView = itemView.findViewById(R.id.text_view_fill_address);
            parcelTypeTextView = itemView.findViewById(R.id.text_view_fill_parcelType);
            isFragileTextView = itemView.findViewById(R.id.text_view_fill_fragile);
            takeButton = itemView.findViewById(R.id.button_take);
        }
    }

    public interface OnTakeClickListener
    {
        void OnTakeClick(Parcel parcel);
    }

    public void setOnTakeClickListener(OnTakeClickListener listener)
    {
        this.onTakeClickListener = listener;
    }
}
