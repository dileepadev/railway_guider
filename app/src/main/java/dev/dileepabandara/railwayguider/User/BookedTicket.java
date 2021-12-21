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

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import dev.dileepabandara.railwayguider.Prevalent.PrevalentBookingTicket;
import dev.dileepabandara.railwayguider.R;

import io.paperdb.Paper;

public class BookedTicket extends AppCompatActivity {

    TextView bookedTicketTrainFrom, bookedTicketTrainTo, bookedTicketTrainName, bookedTicketTrainNo, bookedTicketClass;
    TextView bookedTicketTrainFromTime, bookedTicketTrainToTime, bookedTicketPrice, bookedTicketQty, bookedTicketTotalPrice, bookedTicketDate;

    Button confirmBooking, cancelBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_booked_ticket);

        //Hooks
        bookedTicketTrainFrom = findViewById(R.id.bookedTicketTrainFrom);
        bookedTicketTrainTo = findViewById(R.id.bookedTicketTrainTo);
        bookedTicketTrainName = findViewById(R.id.bookedTicketTrainName);
        bookedTicketTrainNo = findViewById(R.id.bookedTicketTrainNo);
        bookedTicketClass = findViewById(R.id.bookedTicketClass);
        bookedTicketTrainFromTime = findViewById(R.id.bookedTicketTrainFromTime);
        bookedTicketTrainToTime = findViewById(R.id.bookedTicketTrainToTime);
        bookedTicketPrice = findViewById(R.id.bookedTicketPrice);
        bookedTicketQty = findViewById(R.id.bookedTicketQty);
        bookedTicketTotalPrice = findViewById(R.id.bookedTicketTotalPrice);
        bookedTicketDate = findViewById(R.id.bookedTicketDate);
        confirmBooking = findViewById(R.id.confirmBooking);
        cancelBooking = findViewById(R.id.cancelBooking);

        //Paper
        Paper.init(this);
        String paperTicketTrainFrom = Paper.book().read(PrevalentBookingTicket.trainFromKey);
        String paperTicketTrainTo = Paper.book().read(PrevalentBookingTicket.trainToKey);
        String paperTicketTrainName = Paper.book().read(PrevalentBookingTicket.trainNameKey);
        String paperTicketTrainNo = Paper.book().read(PrevalentBookingTicket.trainNoKey);
        String paperTicketClass = Paper.book().read(PrevalentBookingTicket.ticketClassKey);
        String paperTicketTrainFromTime = Paper.book().read(PrevalentBookingTicket.trainFromTimeKey);
        String paperTicketTrainToTime = Paper.book().read(PrevalentBookingTicket.trainToTimeKey);
        String paperTicketPrice = Paper.book().read(PrevalentBookingTicket.ticketPriceKey);
        String paperTicketQty = Paper.book().read(PrevalentBookingTicket.ticketQtyKey);
        String paperTicketTotalPrice = Paper.book().read(PrevalentBookingTicket.trainTicketTotalPriceKey);
        String paperTicketDate = Paper.book().read(PrevalentBookingTicket.trainDateKey);

        //Set Value
        bookedTicketTrainFrom.setText(paperTicketTrainFrom);
        bookedTicketTrainTo.setText(paperTicketTrainTo);
        bookedTicketTrainName.setText(paperTicketTrainName);
        bookedTicketTrainNo.setText(paperTicketTrainNo);
        bookedTicketClass.setText(paperTicketClass);
        bookedTicketTrainFromTime.setText(paperTicketTrainFromTime);
        bookedTicketTrainToTime.setText(paperTicketTrainToTime);
        bookedTicketPrice.setText("Rs." +paperTicketPrice+ "/=");
        bookedTicketQty.setText(paperTicketQty);
        bookedTicketTotalPrice.setText("Rs." +paperTicketTotalPrice+ "/=");
        bookedTicketDate.setText(paperTicketDate);

//        Toast.makeText(this, "" +paperTicketTrainFrom+ " "+paperTicketTrainTo+ " "+paperTicketTrainName+ " "
//                +paperTicketTrainNo+ " "+paperTicketClass+ " "+paperTicketTrainFromTime+ " "
//                +paperTicketTrainToTime+ " "+paperTicketPrice+ " "+paperTicketQty+ " "+paperTicketTotalPrice+" "
//                +paperTicketDate, Toast.LENGTH_SHORT).show();

        confirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PaymentMethod.class);
                startActivity(i);
            }
        });


        cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
