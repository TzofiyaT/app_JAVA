package com.example.messangerusers.UI.MyParcels;

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

public class MyParcelsFragment extends Fragment {

    private MyParcelsViewModel myParcelsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_parcels, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.my_parcels_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        myParcelsViewModel = ViewModelProviders.of(this).get(MyParcelsViewModel.class);

        final MyParcelsAdapter adapter = new MyParcelsAdapter(myParcelsViewModel.getEmail());
        recyclerView.setAdapter(adapter);

        myParcelsViewModel.getMyParcels().observe(getViewLifecycleOwner(), new Observer<List<Parcel>>() {
            @Override
            public void onChanged(@Nullable List<Parcel> newParcels) {
                adapter.setFriendsParcels(newParcels);
            }
        });

        adapter.setOnAcceptClickListener(new MyParcelsAdapter.OnAcceptClickListener() {
            @Override
            public void OnAcceptClick(Parcel parcel) {
                myParcelsViewModel.updateParcel(parcel);
            }
        });

        return root;
    }
}
