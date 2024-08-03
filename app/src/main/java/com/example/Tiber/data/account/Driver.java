package com.example.Tiber.data.account;

public class Driver implements Account_Interface {

    private int accountId;
    private String firstName;
    private String lastName;
    private long phoneNumber;
    private String eMail;
    private String passWord;
    private int isActive;
    private int driverStatus;

    // to validate sign in
    private boolean isAuthenticated;
    private boolean isValidated;

    public Driver(int accountId, String firstName, String lastName, long phoneNumber, String eMail, String passWord, int isActive, int driverStatus) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
        this.passWord = passWord;
        this.isActive = isActive;
        this.driverStatus = driverStatus;
    }

    // constructor to sign in
    public Driver(String email, String password) {
        this.eMail = email;
        this.passWord = password;
    }

    @Override
    public void setAccountId(int Entry) {
        this.accountId = Entry;
    }

    @Override
    public int getAccountId() {
        return this.accountId;
    }

    @Override
    public void setFirstName(String Entry) {
        this.firstName = Entry;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public void setLastName(String Entry) {
        this.lastName = Entry;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }

    @Override
    public void setPhone(long Entry) {
        this.phoneNumber = Entry;
    }

    @Override
    public long getPhone() {
        return this.phoneNumber;
    }

    @Override
    public void setEmail(String Entry) {
        this.eMail = Entry;
    }

    @Override
    public String getEmail() {
        return this.eMail;
    }

    @Override
    public void setPassword(String Entry) {
        this.passWord = Entry;
    }

    @Override
    public String getPassword() {
        return this.passWord;
    }

    @Override
    public int getActiveAccount() {
        return isActive;
    }

    @Override
    public void setActiveAccount(int Entry) {
        isActive = Entry;
    }

    @Override
    public int getDriverStatus() {
        return driverStatus;
    }

    @Override
    public void setDriverStatus(int Entry) {
        driverStatus = Entry;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }
}
