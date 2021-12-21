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

package dev.dileepabandara.railwayguider.Common;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import dev.dileepabandara.railwayguider.HelperClasses.HomeAdapter.AboutAdapter;
import dev.dileepabandara.railwayguider.HelperClasses.HomeAdapter.AboutHelperClass;
import dev.dileepabandara.railwayguider.HelperClasses.HomeAdapter.FeaturedAdapter;
import dev.dileepabandara.railwayguider.HelperClasses.HomeAdapter.FeaturedHelperClass;
import dev.dileepabandara.railwayguider.HelperClasses.HomeAdapter.TrainsAdapter;
import dev.dileepabandara.railwayguider.HelperClasses.HomeAdapter.TrainsHelperClass;
import dev.dileepabandara.railwayguider.R;
import dev.dileepabandara.railwayguider.User.UserDashboard;

import java.util.ArrayList;

public class ReadingWall extends AppCompatActivity {

    //RecyclerViews
    RecyclerView featuredRecycler,trainsRecycler,aboutRecycler;
    RecyclerView.Adapter adapter,adapter2,adapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reading_wall);

        //RecyclerView Hooks
        featuredRecycler = findViewById(R.id.featured_recycler);
        trainsRecycler = findViewById(R.id.trains_recycler);
        aboutRecycler = findViewById(R.id.about_recycler);

        //Recycler View
        featuredRecycler();
        trainsRecycler();
        aboutRecycler();

        //Back Button
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Home Button
        ImageView imgHome = findViewById(R.id.imgHome);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                finishAffinity();
            }
        });

    }


    //Recycler View

    private void featuredRecycler() {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedHelperClass(R.drawable.train_img_railway1, "Simplicity", "Simple and easy way to travel"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.train_img_railway2, "Balanced", "Low cost of tickets and safe transports to passengers"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.train_img_railway3, "Friendly", "Railway transport help to eco friendly transport system"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.train_img_railway4, "Train Railway", "Use attractive trains and railway roads"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.train_img_railway5, "Comfortable", "No heavy tension and no way to waste the time to ride a vehicle"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.train_img_railway6, "Faster", "No rush time. This is very fast way to travel"));

        adapter = new FeaturedAdapter(featuredLocations);
        featuredRecycler.setAdapter(adapter);


    }

    private void trainsRecycler() {
        trainsRecycler.setHasFixedSize(true);
        trainsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<TrainsHelperClass> featuredTrains = new ArrayList<>();

        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_yal_devi, "Yal Devi", "Sri lankan trains have their own beautiful female names."));
        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_rajarata_rejini, "Rajarata Rejini", "Sri lankan trains have their own beautiful female names."));
        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_udarata_menike, "Udarata Manike", "Sri lankan trains have their own beautiful female names."));
        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_ruhunu_kumari, "Ruhunu Kumari", "Sri lankan trains have their own beautiful female names."));
        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_uttara_devi, "Uttara Devi", "Sri lankan trains have their own beautiful female names."));
        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_podi_menike, "Podi Manike", "Sri lankan trains have their own beautiful female names."));
        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_denuwara_manike, "Denuwara Manike", "Sri lankan trains have their own beautiful female names."));
        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_galu_kumari, "Galu Kumari", "Sri lankan trains have their own beautiful female names."));
        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_muthu_kumari, "Muthu Kumari", "Sri lankan trains have their own beautiful female names."));
        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_samudra_devi, "Samudra Devi", "Sri lankan trains have their own beautiful female names."));
        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_senkadagala_menike, "Senkadagala Manike", "Sri lankan trains have their own beautiful female names."));
        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_udaya_devi, "Udaya Devi", "Sri lankan trains have their own beautiful female names."));
        featuredTrains.add(new TrainsHelperClass(R.drawable.train_image_tikiri_menike, "Tikiri Manike", "Sri lankan trains have their own beautiful female names."));

        adapter2 = new TrainsAdapter(featuredTrains);
        trainsRecycler.setAdapter(adapter2);
    }

    private void aboutRecycler() {
        aboutRecycler.setHasFixedSize(true);
        aboutRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<AboutHelperClass> featuredAbout = new ArrayList<>();

        featuredAbout.add(new AboutHelperClass(R.drawable.train_img_tips1, "Contact Email", "If you are with any kind of problem just contact us and use email service to get more experience"));
        featuredAbout.add(new AboutHelperClass(R.drawable.train_img_tips2, "Scan Codes", "Scan our Railway Guider QR codes and earn Gift Cards and use Scanning methods if any case"));
        featuredAbout.add(new AboutHelperClass(R.drawable.train_img_tips3, "Use QR Tickets", "After your bookings you have to pay and get your ticket, so use our scanner machines to get easy services"));
        featuredAbout.add(new AboutHelperClass(R.drawable.train_img_tips4, "Visit Website", "Visit our official website to get our latest news and information about our products"));

        adapter3 = new AboutAdapter(featuredAbout);
        aboutRecycler.setAdapter(adapter3);

    }





}
