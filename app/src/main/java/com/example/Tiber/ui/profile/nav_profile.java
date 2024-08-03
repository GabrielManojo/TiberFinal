package com.example.Tiber.ui.profile;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Tiber.R;
import com.example.Tiber.data.SQLite.DbHandler;
import com.example.Tiber.data.account.Driver;
import com.example.Tiber.data.vehicle.Truck;

public class nav_profile extends Fragment {

    EditText profile_firstName;
    EditText profile_lastName;
    EditText profile_accountNumber;
    EditText profile_phone;
    EditText profile_email;
    EditText profile_password;
    private int accountNumber;

    EditText profile_Plate;
    EditText profile_Vin;
    EditText profile_Manufacturer;
    EditText profile_Model;
    EditText profile_Color;
    EditText profile_Capacity;
    Button btn_Profile_Update;

    //instantiate driver class
    Driver driver = new Driver(null, null);

    //instantiate truck class
    Truck truck = new Truck();

    public static nav_profile newInstance() {
        return new nav_profile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Find elements by their IDs
        profile_firstName = view.findViewById(R.id.txt_profile_firstName);
        profile_lastName = view.findViewById(R.id.txt_profile_lastName);
        profile_accountNumber = view.findViewById(R.id.txt_profile_accountNumber);
        profile_phone = view.findViewById(R.id.txt_profile_phone);
        profile_email = view.findViewById(R.id.txt_profile_email);
        profile_password = view.findViewById(R.id.txt_profile_password);

        profile_Plate = view.findViewById(R.id.txt_profile_plate);
        profile_Vin = view.findViewById(R.id.txt_profile_vin);
        profile_Manufacturer = view.findViewById(R.id.txt_profile_manufecturer);
        profile_Model = view.findViewById(R.id.txt_profile_model);
        profile_Color = view.findViewById(R.id.txt_profile_color);
        profile_Capacity = view.findViewById(R.id.txt_profile_capacity);
        btn_Profile_Update = view.findViewById(R.id.btn_Profile);

        //button to update account profile (driver and vehicle)
        btn_Profile_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //calling method to add or update vehicle
                Boolean updateVehicle = AddOrUpdateVehicle();

                //calling method to add or update account
                Boolean updateAccount = UpdateAccount();

                if(updateVehicle && updateAccount){

                    Toast.makeText(getActivity(), "Update Successful", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "Update fail", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //calling method to retrieve account details from tables session and account on the database
        getAccountDetails();

        //calling method to retrieve vehicle details from database
        getVehicleDetails();

        return view;
    }

    private boolean UpdateAccount() {

        //get Account data from form and set at Driver Class
        driver.setPhone(Long.parseLong(profile_phone.getText().toString()));
        driver.setPassword(profile_password.getText().toString());
        driver.setEmail(profile_email.getText().toString());
        driver.setAccountId(Integer.parseInt(profile_accountNumber.getText().toString()));

        boolean accountUpdated = false;

        DbHandler dbHandler = new DbHandler(getActivity());

        accountUpdated = dbHandler.updateAccount(driver.getAccountId(), driver.getPhone(), driver.getPassword(), driver.getEmail());

        dbHandler.close();

        return accountUpdated;
    }

    //method to add or update vehicle.
    private boolean AddOrUpdateVehicle() {

        //get vehicle data from form and set at Truck Class
        truck.setPlate(profile_Plate.getText().toString());
        truck.setVin(profile_Vin.getText().toString());
        truck.setManufacturer(profile_Manufacturer.getText().toString());
        truck.setModel(profile_Model.getText().toString());
        truck.setVehicleColor(profile_Color.getText().toString());
        truck.setCapacity(profile_Capacity.getText().toString());
        driver.setAccountId(Integer.parseInt(profile_accountNumber.getText().toString()));

        boolean vehicleUpdated = false;

        DbHandler dbHandler = new DbHandler(getActivity());

        vehicleUpdated = dbHandler.addOrUpdateVehicle(truck.getPlate(), truck.getVin(), truck.getManufacturer(), truck.getModel(), truck.getVehicleColor(), truck.getCapacity(), driver.getAccountId());

        dbHandler.close();

        return vehicleUpdated;
    }

    private void getAccountDetails() {

        //Instantiating DbHandler
        DbHandler dbHandler = new DbHandler(getActivity());

        //get opened session (equal 1) on app by account id
        int accountId = dbHandler.getOpenedSession();

        //get account details
        String[] driverData;
        driverData = dbHandler.getAccountDetails(accountId);

        //close dbHandler
        dbHandler.close();

        //call method to fill up the form with account details
        fillUpAccountForm(driverData);
    }

    //method to fill up the form with account details
    private void fillUpAccountForm(String[] driverData) {

        profile_accountNumber.setText(driverData[0]);
        profile_firstName.setText(driverData[1]);
        profile_lastName.setText(driverData[2]);
        profile_phone.setText(driverData[3]);
        profile_email.setText(driverData[4]);
        profile_password.setText(driverData[5]);

        //store account number into global variable to be used in getVehicle method
        accountNumber = Integer.parseInt(driverData[0]);
    }

    //method to get vehicle detail
    private void getVehicleDetails() {

        //Instantiating DbHandler
        DbHandler dbHandler = new DbHandler(getActivity());

        //get account details
        String[] vehicleData;
        vehicleData = dbHandler.getVehicleDetails(accountNumber);

        //close dbHandler
        dbHandler.close();

        //call method to fill up the form with vehicle details
        fillUpVehicleForm(vehicleData);

    }

    private void fillUpVehicleForm(String[] vehicleData) {

        profile_Plate.setText(vehicleData[1]);
        profile_Vin.setText(vehicleData[2]);
        profile_Manufacturer.setText(vehicleData[3]);
        profile_Model.setText(vehicleData[4]);
        profile_Color.setText(vehicleData[5]);
        profile_Capacity.setText(vehicleData[6]);
    }

}
