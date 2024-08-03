package com.example.Tiber.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Tiber.R;
import com.example.Tiber.data.SQLite.DbHandler;
import com.example.Tiber.data.account.Driver;
import com.example.Tiber.ui.login.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class activity_register extends AppCompatActivity {

    EditText txt_FirstName;
    EditText txt_LastName;
    EditText txt_Phone;
    EditText txt_Email;
    EditText txtPassword;
    EditText txtConfirmPassword;
    Button btn_Signup;

    int accountId;
    final int driverStatus = 1; //available
    final int isEnabled = 1;    //enable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txt_FirstName = findViewById(R.id.txt_FirstName);
        txt_LastName = findViewById(R.id.txt_LastName);
        txt_Phone = findViewById(R.id.txt_Phone);
        txt_Email = findViewById(R.id.txt_Email);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);

        btn_Signup = findViewById(R.id.btn_Signup);

        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstName = txt_FirstName.getText().toString();
                String lastName = txt_LastName.getText().toString();
                long phoneNumber = tryParseLong(txt_Phone.getText().toString());
                String email = txt_Email.getText().toString();
                String password = txtPassword.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();

                // Validating first and last names
                boolean nameValidated = nameValidation(firstName, lastName);
                if (!nameValidated) {
                    txt_FirstName.setHintTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txt_LastName.setHintTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txt_FirstName.setTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txt_LastName.setTextColor(getResources().getColor(R.color.Red_Alizarin));
                } else {
                    txt_FirstName.setHintTextColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                    txt_LastName.setHintTextColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                    txt_FirstName.setTextColor(getResources().getColor(R.color.black));
                    txt_LastName.setTextColor(getResources().getColor(R.color.black));
                }

                // Validating phone number
                boolean phoneValidated = phoneValidation(phoneNumber);
                if (!phoneValidated) {
                    txt_Phone.setHintTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txt_Phone.setTextColor(getResources().getColor(R.color.Red_Alizarin));
                } else {
                    txt_Phone.setHintTextColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                    txt_Phone.setTextColor(getResources().getColor(R.color.black));
                }

                // Validating email address
                boolean emailValidated = emailValidation(email);
                if (!emailValidated) {
                    txt_Email.setHintTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txt_Email.setTextColor(getResources().getColor(R.color.Red_Alizarin));
                } else {
                    txt_Email.setHintTextColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                    txt_Email.setTextColor(getResources().getColor(R.color.black));
                }

                // Validating password
                boolean passwordValidated = passwordValidation(password, confirmPassword);
                if (!passwordValidated) {
                    txtPassword.setHintTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txtConfirmPassword.setHintTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txtPassword.setTextColor(getResources().getColor(R.color.Red_Alizarin));
                    txtConfirmPassword.setTextColor(getResources().getColor(R.color.Red_Alizarin));
                } else {
                    txtPassword.setHintTextColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                    txtConfirmPassword.setHintTextColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
                    txtPassword.setTextColor(getResources().getColor(R.color.black));
                    txtConfirmPassword.setTextColor(getResources().getColor(R.color.black));
                }

                // If names, phone, email, and password are valid, set AccountID and register it.
                if (nameValidated && phoneValidated && emailValidated && passwordValidated) {
                    accountId = SetAccountID();
                    AccountRegistration(accountId, firstName, lastName, phoneNumber, email, password, view);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid personal data!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int SetAccountID() {
        String account_id;

        Random rd = new Random();
        String digit = String.valueOf(rd.nextInt(100));

        Date date = new Date();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat year = new SimpleDateFormat("yy");
        String twoDigitYear = year.format(date);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat month = new SimpleDateFormat("MM");
        String twoDigitMonth = month.format(date);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat day = new SimpleDateFormat("dd");
        String twoDigitDay = day.format(date);

        account_id = twoDigitYear + twoDigitMonth + twoDigitDay + digit;

        return Integer.parseInt(account_id);
    }

    private void AccountRegistration(int accountId, String firstName, String lastName, Long phoneNumber, String email, String password, View view) {
        Driver driverModel;
        try {
            driverModel = new Driver(accountId, firstName, lastName, phoneNumber, email, password, isEnabled, driverStatus);

            DbHandler dbHandler = new DbHandler(activity_register.this);

            boolean accountCreated = dbHandler.createAccount(
                    driverModel.getAccountId(),
                    driverModel.getFirstName(),
                    driverModel.getLastName(),
                    driverModel.getPhone(),
                    driverModel.getEmail(),
                    driverModel.getPassword(),
                    driverModel.getActiveAccount(),
                    driverModel.getDriverStatus());

            dbHandler.close();

            if (accountCreated) {
                Toast.makeText(activity_register.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                openSigInActivity(view, driverModel.getEmail(), driverModel.getPassword());
            } else {
                Toast.makeText(activity_register.this, "SignUp Failed", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(activity_register.this, "Error creating account.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean passwordValidation(String password, String confirmPassword) {
        return password.equals(confirmPassword) && password.length() > 7;
    }

    private boolean emailValidation(String email) {
        return (email.contains("@") && email.contains("."));
    }

    private boolean phoneValidation(long phoneNumber) {
        String textPhone = String.valueOf(phoneNumber);
        return (textPhone.length() > 9) && (textPhone.length() < 15);
    }

    private boolean nameValidation(String firstName, String lastName) {
        return hasText(firstName) && hasText(lastName);
    }

    private boolean hasText(String text) {
        return text != null && !text.isEmpty() && !text.trim().isEmpty();
    }

    private long tryParseLong(String Entry) {
        try {
            return Long.parseLong(Entry);
        } catch (Exception e) {
            return 0;
        }
    }

    public void openSigInActivity(View view, String email, String password) {
        Intent intent = new Intent(activity_register.this, LoginActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
    }
}
