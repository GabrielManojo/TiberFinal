package com.example.Tiber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.Tiber.ui.dstatus.driver_status;
import com.example.Tiber.R;
import com.example.Tiber.databinding.ActivityMainBinding;
import com.example.Tiber.R;
import com.example.Tiber.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    TextView lbl_SignedIn_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Button on bottom right corner to set availability
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Snackbar.make(view, "", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                //show driver status dialog fragment
                showDrivingStatusFragment();

            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_driver_activity, R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //get nav_header_main element
        View headerView = navigationView.getHeaderView(0);
        lbl_SignedIn_Name = headerView.findViewById(R.id.lbl_SignedIn_Name);

        //calling the method to set initial environment.
        setInitialEnvironment();

        //Start service that runs every minute.
        Intent serviceIntent = new Intent(this, serviceUpdate.class);
        startService(serviceIntent);

    }

    //Show the fragment to set driving availability
    private void showDrivingStatusFragment() {

        driver_status driverStatus = new driver_status();
        driverStatus.show(getSupportFragmentManager(), "fragment_DriverStatus");

    }

    //method to set initial environment.
    private void setInitialEnvironment() {

        Bundle extras = getIntent().getExtras();
        String auxiliar = extras.getString("FistName");
        lbl_SignedIn_Name.setText(auxiliar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}