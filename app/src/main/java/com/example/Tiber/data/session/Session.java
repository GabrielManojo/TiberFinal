package com.example.Tiber.data.session;

public class Session {

    private int accountId;
    private String time_begin;
    private String time_end;
    private int signedIn;

    public Session(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getTime_begin() {
        return time_begin;
    }

    public void setTime_begin(String time_begin) {
        this.time_begin = time_begin;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public int getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(int signedIn) {
        this.signedIn = signedIn;
    }
}
