package com.example.Tiber.data.account;

public interface Account_Interface {

    void setAccountId(int Entry);
    int getAccountId();
    void setFirstName(String Entry);
    String getFirstName();
    void setLastName(String Entry);
    String getLastName();
    void setPhone(long Entry);
    long getPhone();
    void setEmail(String Entry);
    String getEmail();
    void setPassword(String Entry);
    String getPassword();
    int getActiveAccount();
    void setActiveAccount(int Entry);
    int getDriverStatus();
    void setDriverStatus(int Entry);

}
