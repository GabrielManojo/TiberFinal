package com.example.Tiber.data.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.Tiber.data.RideHistory.RideHistory;

import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "RideSharing_db";

    // Account Table
    private static final String TABLE_Account = "ACCOUNT";
    private static final String COLUMN_ACCOUNT_ID = "AccountId";
    private static final String COLUMN_FIRST_NAME = "FirstName";
    private static final String COLUMN_LAST_NAME = "LastName";
    private static final String COLUMN_PHONE_NUMBER = "PhoneNumber";
    private static final String COLUMN_EMAIL = "Email";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_ENABLE = "Enable";
    private static final String COLUMN_DRIVER_STATUS = "DriverStatus";

    // Session Table
    private static final String TABLE_Session = "Session";
    private static final String COLUMN_SESSION_ACCOUNT_ID = "AccountId";
    private static final String COLUMN_TIME_BEGIN = "TimeBegin";
    private static final String COLUMN_TIME_END = "TimeEnd";
    private static final String COLUMN_IS_ON = "IsON";

    // Ride History Table
    private static final String TABLE_Ride_History = "RIDE_HISTORY";
    private static final String COLUMN_RIDE_ID = "RideId";
    private static final String COLUMN_RIDE_DRIVER = "Driver";
    private static final String COLUMN_RIDE_PASSENGERS = "Passengers";
    private static final String COLUMN_RIDE_DRIVER_PHONE = "DriverPhone";
    private static final String COLUMN_RIDE_DESTINATION = "Destination";
    private static final String COLUMN_RIDE_VEHICLE_TYPE = "VehicleType";
    private static final String COLUMN_RIDE_OFFER = "RideOffer";
    private static final String COLUMN_RIDE_STATUS = "RideStatus";
    private static final String COLUMN_RIDE_DATE = "RideDate";
    private static final String COLUMN_RIDE_ACCOUNT_ID = "AccountId";

    // Vehicle Table
    private static final String TABLE_Vehicle = "Vehicle";
    private static final String COLUMN_VEHICLE_ID = "VehicleId";
    private static final String COLUMN_PLATE = "Plate";
    private static final String COLUMN_VIN = "Vin";
    private static final String COLUMN_MANUFACTURER = "Manufacturer";
    private static final String COLUMN_MODEL = "Model";
    private static final String COLUMN_COLOR = "Color";
    private static final String COLUMN_CAPACITY = "Capacity";
    private static final String COLUMN_TRAILER = "Trailer";
    private static final String COLUMN_VEHICLE_ACCOUNT_ID = "AccountId";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Account Table
        String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + TABLE_Account + "("
                + COLUMN_ACCOUNT_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_LAST_NAME + " TEXT,"
                + COLUMN_PHONE_NUMBER + " INTEGER,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_ENABLE + " INTEGER,"
                + COLUMN_DRIVER_STATUS + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_ACCOUNT);

        // Create Session Table
        String CREATE_TABLE_SESSION = "CREATE TABLE " + TABLE_Session + "("
                + COLUMN_SESSION_ACCOUNT_ID + " INTEGER,"
                + COLUMN_TIME_BEGIN + " TEXT,"
                + COLUMN_TIME_END + " TEXT,"
                + COLUMN_IS_ON + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_SESSION);

        // Create Ride History Table
        String CREATE_TABLE_RIDE_HISTORY = "CREATE TABLE " + TABLE_Ride_History + "("
                + COLUMN_RIDE_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_RIDE_DRIVER + " TEXT,"
                + COLUMN_RIDE_PASSENGERS + " TEXT,"
                + COLUMN_RIDE_DRIVER_PHONE + " TEXT,"
                + COLUMN_RIDE_DESTINATION + " TEXT,"
                + COLUMN_RIDE_VEHICLE_TYPE + " TEXT,"
                + COLUMN_RIDE_OFFER + " TEXT,"
                + COLUMN_RIDE_STATUS + " INTEGER,"
                + COLUMN_RIDE_DATE + " TEXT,"
                + COLUMN_RIDE_ACCOUNT_ID + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_RIDE_HISTORY);

        // Create Vehicle Table
        String CREATE_TABLE_VEHICLE = "CREATE TABLE " + TABLE_Vehicle + "("
                + COLUMN_VEHICLE_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PLATE + " TEXT,"
                + COLUMN_VIN + " TEXT,"
                + COLUMN_MANUFACTURER + " TEXT,"
                + COLUMN_MODEL + " TEXT,"
                + COLUMN_COLOR + " TEXT,"
                + COLUMN_CAPACITY + " TEXT,"
                + COLUMN_TRAILER + " TEXT,"
                + COLUMN_VEHICLE_ACCOUNT_ID + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_VEHICLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Session);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Ride_History);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Vehicle);
        onCreate(db);
    }

    public boolean createAccount(int accountId, String firstName, String lastName, long phoneNumber, String email, String password, int enable, int driverStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_ACCOUNT_ID, accountId);
        cValues.put(COLUMN_FIRST_NAME, firstName);
        cValues.put(COLUMN_LAST_NAME, lastName);
        cValues.put(COLUMN_PHONE_NUMBER, phoneNumber);
        cValues.put(COLUMN_EMAIL, email);
        cValues.put(COLUMN_PASSWORD, password);
        cValues.put(COLUMN_ENABLE, enable);
        cValues.put(COLUMN_DRIVER_STATUS, driverStatus);

        Log.d("DBHandler", "Creating account with ID: " + accountId + ", Email: " + email);

        long newRowId = db.insert(TABLE_Account, null, cValues);
        db.close();
        return newRowId != -1;
    }

    public String[] getAccountCredential(String email) {
        String[] credential = new String[5];
        String queryString = "SELECT " + COLUMN_PASSWORD + ", " +
                COLUMN_ENABLE + ", " +
                COLUMN_FIRST_NAME + ", " +
                COLUMN_ACCOUNT_ID +
                " FROM " + TABLE_Account +
                " WHERE " + COLUMN_EMAIL + " = '" + email + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            credential[0] = email;
            credential[1] = cursor.getString(0);
            credential[2] = String.valueOf(cursor.getInt(1));
            credential[3] = cursor.getString(2);
            credential[4] = String.valueOf(cursor.getInt(3));
        } else {
            credential[0] = null;
            credential[1] = null;
            credential[2] = "0";
            credential[3] = null;
            credential[4] = "0";
        }
        cursor.close();
        db.close();
        return credential;
    }

    public boolean startSession(int accountId, String time_begin, String time_end, int signedIn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_SESSION_ACCOUNT_ID, accountId);
        cValues.put(COLUMN_TIME_BEGIN, time_begin);
        cValues.put(COLUMN_TIME_END, time_end);
        cValues.put(COLUMN_IS_ON, signedIn);
        long newRowId = db.insert(TABLE_Session, null, cValues);
        db.close();
        return newRowId != -1;
    }

    public boolean endAllSession() {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE " + TABLE_Session +
                " SET " + COLUMN_IS_ON + " = 0" +
                " WHERE " + COLUMN_IS_ON + " = 1;";
        db.execSQL(queryString);
        db.close();
        return true;
    }

    public int getOpenedSession() {
        int accountNumber;
        String queryString = "SELECT " + COLUMN_SESSION_ACCOUNT_ID +
                " FROM " + TABLE_Session +
                " WHERE " + COLUMN_IS_ON + " = 1;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            accountNumber = cursor.getInt(0);
        } else {
            accountNumber = 0;
        }
        cursor.close();
        db.close();
        return accountNumber;
    }

    public String[] getAccountDetails(int accountNumber) {
        String[] accountData = new String[8];
        String queryString = "SELECT * FROM " +
                TABLE_Account +
                " WHERE " + COLUMN_ACCOUNT_ID + " = '" + accountNumber + "';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            accountData[0] = String.valueOf(cursor.getInt(0));
            accountData[1] = cursor.getString(1);
            accountData[2] = cursor.getString(2);
            accountData[3] = String.valueOf(cursor.getLong(3));
            accountData[4] = cursor.getString(4);
            accountData[5] = cursor.getString(5);
            accountData[6] = String.valueOf(cursor.getInt(6));
            accountData[7] = String.valueOf(cursor.getInt(7));
        } else {
            accountData[0] = "0";
            accountData[1] = null;
            accountData[2] = null;
            accountData[3] = "0";
            accountData[4] = null;
            accountData[5] = null;
            accountData[6] = "0";
            accountData[7] = "0";
        }
        cursor.close();
        db.close();
        return accountData;
    }

    public boolean updateAccount(int accountId, long phone, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_PHONE_NUMBER, phone);
        cValues.put(COLUMN_PASSWORD, password);
        cValues.put(COLUMN_EMAIL, email);
        String selectAccount = COLUMN_ACCOUNT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(accountId)};
        int count = db.update(TABLE_Account, cValues, selectAccount, selectionArgs);
        db.close();
        return count > 0;
    }

    public boolean addOrUpdateVehicle(String plate, String vin, String manufacturer, String model, String color, String capacity, int accountId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_PLATE, plate);
        cValues.put(COLUMN_VIN, vin);
        cValues.put(COLUMN_MANUFACTURER, manufacturer);
        cValues.put(COLUMN_MODEL, model);
        cValues.put(COLUMN_COLOR, color);
        cValues.put(COLUMN_CAPACITY, capacity);
        cValues.put(COLUMN_ACCOUNT_ID, accountId);

        // Check if the vehicle already exists
        String queryString = "SELECT " + COLUMN_VEHICLE_ID + " FROM " + TABLE_Vehicle + " WHERE " + COLUMN_ACCOUNT_ID + " = ?";
        Cursor cursor = db.rawQuery(queryString, new String[]{String.valueOf(accountId)});
        boolean vehicleExists = cursor.moveToFirst();
        cursor.close();

        long newRowId;
        if (vehicleExists) {
            newRowId = db.update(TABLE_Vehicle, cValues, COLUMN_ACCOUNT_ID + " = ?", new String[]{String.valueOf(accountId)});
        } else {
            newRowId = db.insert(TABLE_Vehicle, null, cValues);
        }

        db.close();
        return newRowId != -1;
    }

    public boolean addRideHistory(String driver, String passengers, String driverPhone, String destination, String vehicleType, String rideOffer, int status, String date, int accountId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_RIDE_DRIVER, driver);
        cValues.put(COLUMN_RIDE_PASSENGERS, passengers);
        cValues.put(COLUMN_RIDE_DRIVER_PHONE, driverPhone);
        cValues.put(COLUMN_RIDE_DESTINATION, destination);
        cValues.put(COLUMN_RIDE_VEHICLE_TYPE, vehicleType);
        cValues.put(COLUMN_RIDE_OFFER, rideOffer);
        cValues.put(COLUMN_RIDE_STATUS, status);
        cValues.put(COLUMN_RIDE_DATE, date);
        cValues.put(COLUMN_RIDE_ACCOUNT_ID, accountId);
        long newRowId = db.insert(TABLE_Ride_History, null, cValues);
        db.close();
        return newRowId != -1;
    }

    public boolean updateRideHistory(String driver, String passengers, String driverPhone, String destination, String vehicleType, String rideOffer, int status, String date, int accountId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_RIDE_DRIVER, driver);
        cValues.put(COLUMN_RIDE_PASSENGERS, passengers);
        cValues.put(COLUMN_RIDE_DRIVER_PHONE, driverPhone);
        cValues.put(COLUMN_RIDE_DESTINATION, destination);
        cValues.put(COLUMN_RIDE_VEHICLE_TYPE, vehicleType);
        cValues.put(COLUMN_RIDE_OFFER, rideOffer);
        cValues.put(COLUMN_RIDE_STATUS, status);
        cValues.put(COLUMN_RIDE_DATE, date);
        cValues.put(COLUMN_RIDE_ACCOUNT_ID, accountId);
        String selectRide = COLUMN_RIDE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(accountId)};
        int count = db.update(TABLE_Ride_History, cValues, selectRide, selectionArgs);
        db.close();
        return count > 0;
    }

    public ArrayList<RideHistory> getRideHistory(int accountNumber) {
        ArrayList<RideHistory> rideHistory = new ArrayList<>();
        String queryString = "SELECT * FROM " +
                TABLE_Ride_History +
                " WHERE " + COLUMN_RIDE_ACCOUNT_ID + " = " + accountNumber + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                RideHistory data = new RideHistory();
                data.setRideId(cursor.getInt(0));
                data.setDriver(cursor.getString(1));
                data.setPassengers(cursor.getString(2));
                data.setDriverPhone(cursor.getString(3));
                data.setDestination(cursor.getString(4));
                data.setVehicleType(cursor.getString(5));
                data.setRideOffer(cursor.getString(6));
                data.setRideStatus(cursor.getInt(7));
                data.setRideDate(cursor.getString(8));
                data.setAccountId(cursor.getInt(9));
                rideHistory.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return rideHistory;
    }


    public boolean addOrUpdateVehicle(String plate, String vin, String manufacturer, String model, String color, String capacity, String trailer, int accountId) {
        boolean addOrUpdateStatus;
        String queryString = "SELECT " + COLUMN_VEHICLE_ID +
                " FROM " + TABLE_Vehicle +
                " WHERE " + COLUMN_VEHICLE_ACCOUNT_ID + " = " + accountId + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            addOrUpdateStatus = updateVehicle(plate, vin, manufacturer, model, color, capacity, trailer, accountId);
        } else {
            addOrUpdateStatus = addVehicle(plate, vin, manufacturer, model, color, capacity, trailer, accountId);
        }
        cursor.close();
        db.close();
        return addOrUpdateStatus;
    }

    public boolean addVehicle(String plate, String vin, String manufacturer, String model, String color, String capacity, String trailer, int accountId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_PLATE, plate);
        cValues.put(COLUMN_VIN, vin);
        cValues.put(COLUMN_MANUFACTURER, manufacturer);
        cValues.put(COLUMN_MODEL, model);
        cValues.put(COLUMN_COLOR, color);
        cValues.put(COLUMN_CAPACITY, capacity);
        cValues.put(COLUMN_TRAILER, trailer);
        cValues.put(COLUMN_VEHICLE_ACCOUNT_ID, accountId);
        long newRowId = db.insert(TABLE_Vehicle, null, cValues);
        db.close();
        return newRowId != -1;
    }

    public boolean updateVehicle(String plate, String vin, String manufacturer, String model, String color, String capacity, String trailer, int accountId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_PLATE, plate);
        cValues.put(COLUMN_VIN, vin);
        cValues.put(COLUMN_MANUFACTURER, manufacturer);
        cValues.put(COLUMN_MODEL, model);
        cValues.put(COLUMN_COLOR, color);
        cValues.put(COLUMN_CAPACITY, capacity);
        cValues.put(COLUMN_TRAILER, trailer);
        cValues.put(COLUMN_VEHICLE_ACCOUNT_ID, accountId);
        String selectVehicle = COLUMN_VEHICLE_ACCOUNT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(accountId)};
        int count = db.update(TABLE_Vehicle, cValues, selectVehicle, selectionArgs);
        db.close();
        return count > 0;
    }

    public String[] getVehicleDetails(int accountId) {
        String[] vehicleData = new String[9];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_Vehicle,
                    new String[]{COLUMN_VEHICLE_ID, COLUMN_PLATE, COLUMN_VIN, COLUMN_MANUFACTURER, COLUMN_MODEL, COLUMN_COLOR, COLUMN_CAPACITY, COLUMN_TRAILER, COLUMN_ACCOUNT_ID},
                    COLUMN_ACCOUNT_ID + "=?",
                    new String[]{String.valueOf(accountId)}, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                vehicleData[0] = cursor.getString(0);
                vehicleData[1] = cursor.getString(1);
                vehicleData[2] = cursor.getString(2);
                vehicleData[3] = cursor.getString(3);
                vehicleData[4] = cursor.getString(4);
                vehicleData[5] = cursor.getString(5);
                vehicleData[6] = cursor.getString(6);
                vehicleData[7] = cursor.getString(7);
                vehicleData[8] = cursor.getString(8);
            } else {
                // Log para verificar se os dados estão faltando
                Log.d("DbHandler", "No vehicle data found for account ID: " + accountId);
                for (int i = 0; i < vehicleData.length; i++) {
                    vehicleData[i] = "";
                }
            }
        } catch (Exception e) {
            Log.e("DbHandler", "Error fetching vehicle data: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return vehicleData;
    }

    public boolean updateDriverAvailability(int accountId, int driverStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(COLUMN_DRIVER_STATUS, driverStatus);
        String selectAccount = COLUMN_ACCOUNT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(accountId)};
        int count = db.update(TABLE_Account, cValues, selectAccount, selectionArgs);
        db.close();
        return count > 0;
    }
}
