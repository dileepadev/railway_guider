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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dev.dileepabandara.railwayguider.Prevalent.Prevalent;
import dev.dileepabandara.railwayguider.Prevalent.PrevalentBookingTicket;
import dev.dileepabandara.railwayguider.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class BookedTicketView extends AppCompatActivity {

    TextView bookedTicketTrainFrom3, bookedTicketTrainTo3, bookedTicketTrainName3, bookedTicketTrainNo3,
            bookedTicketClass3, bookedTicketTrainFromTime3, bookedTicketTrainToTime3, bookedTicketPrice3,
            bookedTicketQty3, bookedTicketTotalPrice3, bookedTicketDate3, bookedTicketID3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_booked_ticket_view);

        Intent intent = getIntent();
        final String ticketID = intent.getStringExtra("ticketID");
        final String ticketTrainName = intent.getStringExtra("ticketTrainName");
        final String ticketTrainNo = intent.getStringExtra("ticketTrainNo");
        final String ticketTrainFrom = intent.getStringExtra("ticketTrainFrom");
        final String ticketTrainFromTime = intent.getStringExtra("ticketTrainFromTime");
        final String ticketTrainTo = intent.getStringExtra("ticketTrainTo");
        final String ticketTrainToTime = intent.getStringExtra("ticketTrainToTime");
        final String bookedDate = intent.getStringExtra("bookedDate");
        final String ticketClass = intent.getStringExtra("ticketClass");
        final String ticketPrice = intent.getStringExtra("ticketPrice");
        final String ticketQty = intent.getStringExtra("ticketQty");
        final String ticketTotalPrice = intent.getStringExtra("ticketTotalPrice");

        //Hooks
        bookedTicketID3 = findViewById(R.id.bookedTicketID3);
        bookedTicketTrainFrom3 = findViewById(R.id.bookedTicketTrainFrom3);
        bookedTicketTrainTo3 = findViewById(R.id.bookedTicketTrainTo3);
        bookedTicketTrainName3 = findViewById(R.id.bookedTicketTrainName3);
        bookedTicketTrainNo3 = findViewById(R.id.bookedTicketTrainNo3);
        bookedTicketClass3 = findViewById(R.id.bookedTicketClass3);
        bookedTicketTrainFromTime3 = findViewById(R.id.bookedTicketTrainFromTime3);
        bookedTicketTrainToTime3 = findViewById(R.id.bookedTicketTrainToTime3);
        bookedTicketPrice3 = findViewById(R.id.bookedTicketPrice3);
        bookedTicketQty3 = findViewById(R.id.bookedTicketQty3);
        bookedTicketTotalPrice3 = findViewById(R.id.bookedTicketTotalPrice3);
        bookedTicketDate3 = findViewById(R.id.bookedTicketDate3);

        //Set Value
        bookedTicketID3.setText(ticketID);
        bookedTicketTrainFrom3.setText(ticketTrainFrom);
        bookedTicketTrainTo3.setText(ticketTrainTo);
        bookedTicketTrainName3.setText(ticketTrainName);
        bookedTicketTrainNo3.setText(ticketTrainNo);
        bookedTicketClass3.setText(ticketClass);
        bookedTicketTrainFromTime3.setText(ticketTrainFromTime);
        bookedTicketTrainToTime3.setText(ticketTrainToTime);
        bookedTicketPrice3.setText("Rs." + ticketPrice + "/=");
        bookedTicketQty3.setText(ticketQty);
        bookedTicketTotalPrice3.setText("Rs." + ticketTotalPrice + "/=");
        bookedTicketDate3.setText(bookedDate);

        LinearLayout generateQRTicket = findViewById(R.id.generateQRTicket);
        generateQRTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), QRTicket.class);

                i.putExtra("ticketID", ticketID);
                i.putExtra("ticketTrainName", ticketTrainName);
                i.putExtra("ticketTrainNo", ticketTrainNo);
                i.putExtra("ticketTrainFrom", ticketTrainFrom);
                i.putExtra("ticketTrainFromTime", ticketTrainFromTime);
                i.putExtra("ticketTrainTo", ticketTrainTo);
                i.putExtra("ticketTrainToTime", ticketTrainToTime);
                i.putExtra("bookedDate", bookedDate);
                i.putExtra("ticketClass", ticketClass);
                i.putExtra("ticketPrice", ticketPrice);
                i.putExtra("ticketQty", ticketQty);
                i.putExtra("ticketTotalPrice", ticketTotalPrice);
                startActivity(i);
            }
        });

        LinearLayout clickGoLocation = findViewById(R.id.clickGoLocation);
        clickGoLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapsActivity2.class);
                i.putExtra("ticketTrainNo", ticketTrainNo);
                startActivity(i);
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

    public void onClickCancelTicket(View view) {
        Paper.init(this);
        final String ticketID = Paper.book().read(PrevalentBookingTicket.trainIDKey);
        final String trainNo = Paper.book().read(PrevalentBookingTicket.trainNoKey);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm before cancel");
        builder.setMessage("Are you sure to cancel your ticket? ");

        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Intent intent = getIntent();
                String trainID = intent.getStringExtra("ticketID");

                String userID = Paper.book().read(Prevalent.UserMobileKey);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                reference.child("tickets").child(userID).child(trainID).removeValue();
                reference.child("users").child(userID).child("tickets").child(trainID).removeValue();
                reference.child("trains").child(trainNo).child("users").child(userID).child(trainID).removeValue();

                Toast.makeText(BookedTicketView.this, "Your ticket has been canceled", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                finishAffinity();

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
