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
import android.widget.TextView;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.Databases.SchedulesHelperClass;
import dev.dileepabandara.railwayguider.HelperClasses.SchedulesAdapter;
import dev.dileepabandara.railwayguider.Prevalent.PrevalentBookingTicket;
import dev.dileepabandara.railwayguider.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.paperdb.Paper;

public class TrainSchedule2 extends AppCompatActivity {

    TextView txtFrom, txtTo, no_train;
    DatabaseReference reference;
    RecyclerView schedulesRecycler;
    ArrayList<SchedulesHelperClass> arrayList;
    SchedulesAdapter schedulesAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_train_schedule2);

        txtFrom = findViewById(R.id.fromTrain);
        txtTo = findViewById(R.id.toTrain);
        no_train = findViewById(R.id.no_train);
        schedulesRecycler = findViewById(R.id.result_list);
        progressBar = findViewById(R.id.progressBarTrainSearch);

        //Set User details to UserDashboard
        Intent intent = getIntent();
        String train_route = intent.getStringExtra("route");

        //Paper
        Paper.init(this);
        String paperTrainFrom = Paper.book().read(PrevalentBookingTicket.trainFromKey);
        String paperTrainTo = Paper.book().read(PrevalentBookingTicket.trainToKey);

        txtFrom.setText(paperTrainFrom);
        txtTo.setText(paperTrainTo);

        schedulesRecycler.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<SchedulesHelperClass>();

        //Get data from firebase
        progressBar.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference().child("schedules").child(train_route).child("trains");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        SchedulesHelperClass p = dataSnapshot1.getValue(SchedulesHelperClass.class);
                        arrayList.add(p);
                    }
                    schedulesAdapter = new SchedulesAdapter(TrainSchedule2.this, arrayList);
                    schedulesRecycler.setAdapter(schedulesAdapter);
                    schedulesAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    no_train.setVisibility(View.VISIBLE);
                    Toast.makeText(TrainSchedule2.this, "No train", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TrainSchedule2.this, "No Train", Toast.LENGTH_SHORT).show();
            }
        });


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
