package com.example.messangerusers.UI.FriendsParcels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerusers.Entities.Parcel;
import com.example.messangerusers.R;

import java.util.List;

public class FriendsParcelsFragment extends Fragment {

    private FriendParcelsViewModel friendParcelsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.friends_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        friendParcelsViewModel = ViewModelProviders.of(this).get(FriendParcelsViewModel.class);

        final FriendsParcelsAdapter adapter = new FriendsParcelsAdapter(friendParcelsViewModel.getName());
        recyclerView.setAdapter(adapter);

        friendParcelsViewModel.getNoDeliveryPersonParcels().observe(getViewLifecycleOwner(), new Observer<List<Parcel>>() {
            @Override
            public void onChanged(@Nullable List<Parcel> newParcels) {
                adapter.setFriendsParcels(newParcels);
            }
        });

        adapter.setOnTakeClickListener(new FriendsParcelsAdapter.OnTakeClickListener() {
            @Override
            public void OnTakeClick(Parcel parcel) {
                friendParcelsViewModel.updateParcel(parcel);
            }
        });

        return root;
    }
}