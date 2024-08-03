package com.example.Tiber.ui.driverActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.Tiber.R;
import com.example.Tiber.data.RideHistory.RideHistory;

import com.example.Tiber.data.SQLite.DbHandler;

import java.util.ArrayList;

public class DriverHistoryDetails extends DialogFragment {

    Button btnClose;

    TextView txtRideId;
    TextView txtDriver;
    TextView txtPassengers;
    TextView txtPhone;
    TextView txtDestination;
    TextView txtVehicleType;
    TextView txtOffer;
    TextView txtStatus;
    TextView txtDate;

    ArrayList<RideHistory> rideHistory;

    public DriverHistoryDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_history_details, container, false);

        btnClose = view.findViewById(R.id.btnClose);

        txtRideId = view.findViewById(R.id.txtRideId);
        txtDriver = view.findViewById(R.id.txtDriver);
        txtPassengers = view.findViewById(R.id.txtPassengers);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtDestination = view.findViewById(R.id.txtDestination);
        txtVehicleType = view.findViewById(R.id.txtVehicleType);
        txtOffer = view.findViewById(R.id.txtOffer);
        txtStatus = view.findViewById(R.id.txtStatus);
        txtDate = view.findViewById(R.id.txtDate);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // load ride history
        loadRideHistory();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
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
            txtRideId.setText(String.valueOf(rideHistory.get(0).getRideId()));
            txtDriver.setText(String.valueOf(rideHistory.get(0).getDriver()));
            txtPassengers.setText(String.valueOf(rideHistory.get(0).getPassengers()));
            txtPhone.setText(String.valueOf(rideHistory.get(0).getDriverPhone()));
            txtDestination.setText(String.valueOf(rideHistory.get(0).getDestination()));
            txtVehicleType.setText(String.valueOf(rideHistory.get(0).getVehicleType()));
            txtOffer.setText(String.valueOf(rideHistory.get(0).getRideOffer()));
            txtStatus.setText(String.valueOf(rideHistory.get(0).getRideStatus()));
            txtDate.setText(String.valueOf(rideHistory.get(0).getRideDate()));
        } else {
            // Handle case when there is no history available
            txtRideId.setText("No ride history available");
            txtDriver.setText("");
            txtPassengers.setText("");
            txtPhone.setText("");
            txtDestination.setText("");
            txtVehicleType.setText("");
            txtOffer.setText("");
            txtStatus.setText("");
            txtDate.setText("");
        }
    }
}
