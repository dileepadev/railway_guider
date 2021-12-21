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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.Databases.BookedTrainsHelperClass;
import dev.dileepabandara.railwayguider.HelperClasses.BookedTrainsAdapter;
import dev.dileepabandara.railwayguider.Prevalent.Prevalent;
import dev.dileepabandara.railwayguider.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.paperdb.Paper;

public class BookedTrains extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView bookedRecycler;
    ArrayList<BookedTrainsHelperClass> arrayList;
    BookedTrainsAdapter bookedTrainsAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_booked_trains);

        try {

            bookedRecycler = findViewById(R.id.bookedTrains_result_list);
            progressBar = findViewById(R.id.progressBarBookedTrains);

            //Paper
            Paper.init(this);
            String paperUserID = Paper.book().read(Prevalent.UserMobileKey);

            bookedRecycler.setLayoutManager(new LinearLayoutManager(this));
            arrayList = new ArrayList<BookedTrainsHelperClass>();

            //Get data from firebase
            progressBar.setVisibility(View.VISIBLE);
            reference = FirebaseDatabase.getInstance().getReference().child("tickets").child(paperUserID);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            BookedTrainsHelperClass p = dataSnapshot1.getValue(BookedTrainsHelperClass.class);
                            arrayList.add(p);
                        }
                        bookedTrainsAdapter = new BookedTrainsAdapter(BookedTrains.this, arrayList);
                        bookedRecycler.setAdapter(bookedTrainsAdapter);
                        bookedTrainsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(BookedTrains.this, "No Booked Tickets", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(BookedTrains.this, "No Booked Tickets", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }

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

}

