package com.example.messangerusers.UI.HistoryOfMyParcels;

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

public class HistoryOfMyParcelsFragment extends Fragment {

    private HistoryOfMyParcelsViewModel historyOfMyParcelsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history_of_my_parcels, container, false);


        RecyclerView recyclerView = root.findViewById(R.id.history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        historyOfMyParcelsViewModel = ViewModelProviders.of(this).get(HistoryOfMyParcelsViewModel.class);

        final HistoryOfMyParcelsAdapter adapter = new HistoryOfMyParcelsAdapter(historyOfMyParcelsViewModel.getEmail());
        recyclerView.setAdapter(adapter);

        historyOfMyParcelsViewModel.getHistoryOfMyParcels().observe(getViewLifecycleOwner(), new Observer<List<Parcel>>() {
            @Override
            public void onChanged(@Nullable List<Parcel> newParcels) {
                adapter.setFriendsParcels(newParcels);
            }
        });
        return root;
    }
}
