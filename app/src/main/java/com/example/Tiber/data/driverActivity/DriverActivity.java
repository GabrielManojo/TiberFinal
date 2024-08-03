package com.example.Tiber.data.driverActivity;

public class DriverActivity implements DriverActivity_Interface {

    private int rideId;
    private String driver;
    private String passengers;
    private String driverPhone;
    private String destination;
    private String vehicleType;
    private String rideOffer;
    private int rideStatus;
    private String rideDate;
    private int accountId;

    @Override
    public void setRideId(int Entry) {
        rideId = Entry;
    }

    @Override
    public int getRideId() {
        return rideId;
    }

    @Override
    public void setDriver(String Entry) {
        driver = Entry;
    }

    @Override
    public String getDriver() {
        return driver;
    }

    @Override
    public void setPassengers(String Entry) {
        passengers = Entry;
    }

    @Override
    public String getPassengers() {
        return passengers;
    }

    @Override
    public void setDriverPhone(String Entry) {
        driverPhone = Entry;
    }

    @Override
    public String getDriverPhone() {
        return driverPhone;
    }

    @Override
    public void setDestination(String Entry) {
        destination = Entry;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    public void setVehicleType(String Entry) {
        vehicleType = Entry;
    }

    @Override
    public String getVehicleType() {
        return vehicleType;
    }

    @Override
    public void setRideOffer(String Entry) {
        rideOffer = Entry;
    }

    @Override
    public String getRideOffer() {
        return rideOffer;
    }

    @Override
    public void setRideStatus(int Entry) {
        rideStatus = Entry;
    }

    @Override
    public int getRideStatus() {
        return rideStatus;
    }

    @Override
    public void setRideDate(String Entry) {
        rideDate = Entry;
    }

    @Override
    public String getRideDate() {
        return rideDate;
    }

    @Override
    public void setAccountId(int Entry) {
        accountId = Entry;
    }

    @Override
    public int getAccountId() {
        return accountId;
    }
}
