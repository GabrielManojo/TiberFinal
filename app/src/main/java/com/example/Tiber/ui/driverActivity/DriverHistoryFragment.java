package com.example.Tiber.ui.driverActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.Tiber.data.RideHistory.RideHistory;

import com.example.Tiber.data.SQLite.DbHandler;
import com.example.Tiber.R;

import java.util.ArrayList;

public class DriverHistoryFragment extends Fragment {

    Button btnActivity;
    TextView txtHistory;

    // get ride history
    ArrayList<RideHistory> rideHistory;

    public static DriverHistoryFragment newInstance() {
        return new DriverHistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver_history, container, false);

        btnActivity = view.findViewById(R.id.btnActivity);

        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriverHistoryDetails rideHistoryDetails = new DriverHistoryDetails();
                rideHistoryDetails.show(getActivity().getSupportFragmentManager(), "fragment_ride_history_details");
            }
        });

        // method to load ride history
        loadRideHistory();

        return view;
    }

    private void loadRideHistory() {
        // Instantiating DbHandler
        DbHandler dbHandler = new DbHandler(getActivity());

        // get opened session (equal 1) on app by account id
        int accountId = dbHandler.getOpenedSession();

        // get ride history
        rideHistory = dbHandler.getRideHistory(accountId);

        // close dbHandler
        dbHandler.close();

        if (rideHistory != null && !rideHistory.isEmpty()) {
            StringBuilder historyText = new StringBuilder();
            for (RideHistory ride : rideHistory) {
                historyText.append(ride.getRideId()).append(" | ")
                        .append(ride.getRideDate()).append(" | ")
                        .append(ride.getDriver()).append("\n");
            }
            btnActivity.setText(historyText.toString());
        } else {
            btnActivity.setText("No ride history found");
        }
    }
}
