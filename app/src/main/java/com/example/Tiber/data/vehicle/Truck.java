package com.example.Tiber.data.vehicle;

public class Truck implements Vehicle {

    private int vehicleId;
    private String plate;
    private String vin;
    private String manufacturer;
    private String model;
    private String color;
    private String capacity;
    private int ownerAccountId;
    private String latitude;
    private String longitude;

    public Truck() {  // constructor
    }

    @Override
    public void setVehicleId(int Entry) {
        vehicleId = Entry;
    }

    @Override
    public int getVehicleId() {
        return vehicleId;
    }

    @Override
    public void setPlate(String Entry) {
        plate = Entry;
    }

    @Override
    public String getPlate() {
        return plate;
    }

    @Override
    public void setVin(String Entry) {
        vin = Entry;
    }

    @Override
    public String getVin() {
        return vin;
    }

    @Override
    public void setManufacturer(String Entry) {
        manufacturer = Entry;
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public void setModel(String Entry) {
        model = Entry;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setVehicleColor(String Entry) {
        color = Entry;
    }

    @Override
    public String getVehicleColor() {
        return color;
    }

    @Override
    public void setCapacity(String Entry) {
        capacity = Entry;
    }

    @Override
    public String getCapacity() {
        return capacity;
    }

    @Override
    public void setOwnerAccountNumber(int Entry) {
        ownerAccountId = Entry;
    }

    @Override
    public int getOwnerAccountNumber() {
        return ownerAccountId;
    }

    @Override
    public void setLatitude(String Entry) {
        latitude = Entry;
    }

    @Override
    public String getLatitude() {
        return latitude;
    }

    @Override
    public void setLongitude(String Entry) {
        longitude = Entry;
    }

    @Override
    public String getLongitude() {
        return longitude;
    }
}
