package com.example.Tiber.ui.dstatus;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.Tiber.R;
import com.example.Tiber.data.SQLite.DbHandler;
import com.example.Tiber.data.account.Driver;
import com.example.Tiber.ui.home.HomeFragment;
import com.example.Tiber.data.SQLite.DbHandler;

public class driver_status extends DialogFragment {

    Button btnAvailable;
    Button btnOutOfService;
    Driver driver = new Driver(null, null);
    final int driverAvailable = 1;
    final int driverUnavailable = 0;

    //to set driver availability on Driver Class
    HomeFragment homeFragment = new HomeFragment();


    public driver_status() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_status, container, false);

        btnAvailable = view.findViewById(R.id.btnAvailable);
        btnOutOfService = view.findViewById(R.id.btnUnavailable);

        btnAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //calling method to set driver as available for new loadings on database
                SetDriverAsAvailable();

                //to set driver availability on Home Fragment, Driver Class
                homeFragment.driver.setDriverStatus(driverAvailable);

                //calling method to update marker (vehicle) on map.
                homeFragment.ResetMarkerOption();


                dismiss();
            }
        });

        btnOutOfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //calling method to set driver as available for new loadings on database
                SetDriverAsUnavailable();

                //to set driver availability on Home Fragment, Driver Class
                homeFragment.driver.setDriverStatus(driverUnavailable);

                //calling method to update marker (vehicle) on map.
                homeFragment.ResetMarkerOption();


                dismiss();
            }
        });

        return view;
    }

    //method to set driver as available update Account database table
    private boolean SetDriverAsAvailable() {

        //calling method to retrieve account number from database and set it on Driver Class.
        GetAccountNumber();

        driver.setDriverStatus(driverAvailable);

        boolean accountUpdated = false;

        DbHandler dbHandler = new DbHandler(getActivity());

        accountUpdated = dbHandler.updateDriverAvailability(driver.getAccountId(), driver.getDriverStatus());

        dbHandler.close();

        return accountUpdated;
    }

    //method to set driver as unavailable update Account database table
    private boolean SetDriverAsUnavailable() {

        //calling method to retrieve account number from database and set it on Driver Class.
        GetAccountNumber();

        driver.setDriverStatus(driverUnavailable);

        boolean accountUpdated = false;

        DbHandler dbHandler = new DbHandler(getActivity());

        accountUpdated = dbHandler.updateDriverAvailability(driver.getAccountId(), driver.getDriverStatus());

        dbHandler.close();

        return accountUpdated;
    }

    //retrieve account number from database and set it on Driver Class.
    private void GetAccountNumber() {

        //Instantiating DbHandler
        DbHandler dbHandler = new DbHandler(getActivity());

        //get opened session (equal 1) on app by account id
        driver.setAccountId(dbHandler.getOpenedSession());

        //close dbHandler
        dbHandler.close();
    }


}