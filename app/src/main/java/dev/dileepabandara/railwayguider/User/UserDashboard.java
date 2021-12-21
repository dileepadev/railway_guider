/*
   --------------------------------------
      Developed by
      Dileepa Bandara
      https://dileepabandara.github.io
      contact.dileepabandara@gmail.com
      ©dileepabandara.dev
      2020
   --------------------------------------
*/

package dev.dileepabandara.railwayguider.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.Common.Login;
import dev.dileepabandara.railwayguider.Common.ReadingWall;
import dev.dileepabandara.railwayguider.Prevalent.Prevalent;

import dev.dileepabandara.railwayguider.R;
import com.google.android.material.navigation.NavigationView;


import io.paperdb.Paper;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //DrawerMenu Variables
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    //User profile variables
    TextView lbl_name, lbl_navHeader_name, lbl_navHeader_username;
    ImageView userImage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        //DrawerMenu Hooks
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);

        //DrawerMenu Tool Bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        //toolbar.setNavigationIcon(R.drawable.icon_menu);
        //toolbar.setLogo(R.drawable.icon_menu);

        //DrawerMenu navigation drawer menu
        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //User profile hooks
        lbl_name = findViewById(R.id.txt_name);

        //Set user details to UserDashboard and Drawer Menu
        setUserDetails();

    }

    //Set user details to UserDashboard and Drawer Menu
    private void setUserDetails() {


        //Get username and password from Prevalent class
        Paper.init(this);
        final String user_mobile = Paper.book().read(Prevalent.UserMobileKey);
        final String user_name = Paper.book().read(Prevalent.UserNameKey);
        String Welcome = "Hi " +user_name +"!";

        try {
            lbl_name.setText(Welcome);
        } catch (Exception e){
            Toast.makeText(this, "Error pass data" +e, Toast.LENGTH_SHORT).show();
        }
    }

    //Back paused error of drawer menu is close
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    //Navigation Drawer Menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.menu_account:
                Intent intent1 = new Intent(UserDashboard.this, UserAccount.class);
                startActivity(intent1);
                break;

            case R.id.menu_logout:
                Paper.book().destroy();
                Intent intent2 = new Intent(UserDashboard.this, Login.class);
                startActivity(intent2);
                finish();
                break;

            case R.id.menu_notes:
                Intent intent3 = new Intent(UserDashboard.this, ReadingWall.class);
                startActivity(intent3);
                break;

            case R.id.menu_online_support:
                Intent intent4 = new Intent(UserDashboard.this, OnlineSupport.class);
                startActivity(intent4);
                break;

            case R.id.menu_settings:
                Intent intent5 = new Intent(UserDashboard.this, UserSettings.class);
                startActivity(intent5);
                break;

            case R.id.menu_share:
                Intent intent6 = new Intent(UserDashboard.this, UserShare.class);
                startActivity(intent6);
                break;

            case R.id.menu_about_us:
                Intent intent7 = new Intent(UserDashboard.this, AboutUs.class);
                startActivity(intent7);
                break;
        }

        return true;
    }


    //Dashboard Schedule Activity
    public void onClickSchedule(View view) {
        Intent i = new Intent(this, TrainSchedule.class);
        startActivity(i);
    }

    //Dashboard Books Activity
    public void onClickBooks(View view) {
        Intent i = new Intent(this, BookedTrains.class);
        startActivity(i);
    }

    //Dashboard Location Activity
    public void onClickLocation(View view) {
        Intent i = new Intent(this, Location.class);
        startActivity(i);
    }

    //Dashboard Tickets Activity
    public void onClickReadingWall(View view) {
        Intent i = new Intent(this, ReadingWall.class);
        startActivity(i);
    }

    //Dashboard Gifts Activity
    public void onClickGifts(View view) {
        Intent i = new Intent(this, RailwayGuiderGifts.class);
        startActivity(i);
    }

    //Dashboard Account Activity
    public void onClickAccount(View view) {
        Intent i = new Intent(UserDashboard.this, UserAccount.class);
        startActivity(i);
    }

    //Dashboard Payments Activity
    public void onClickPayments(View view) {
        Intent i = new Intent(this, PaymentHistory.class);
        startActivity(i);
    }

    //Dashboard Scan Activity
    public void onClickScan(View view) {
        Intent i = new Intent(this, QRScan.class);
        startActivity(i);
    }

}
