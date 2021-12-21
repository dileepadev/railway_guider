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
import android.widget.ImageView;
import android.widget.TextView;

import dev.dileepabandara.railwayguider.Prevalent.PrevalentBookingTicket;
import dev.dileepabandara.railwayguider.R;

import io.paperdb.Paper;

public class BookedTicketSummary extends AppCompatActivity {

    TextView bookedTicketTrainFrom2, bookedTicketTrainTo2, bookedTicketTrainName2, bookedTicketTrainNo2,
            bookedTicketClass2, bookedTicketTrainFromTime2, bookedTicketTrainToTime2, bookedTicketPrice2,
            bookedTicketQty2, bookedTicketTotalPrice2, bookedTicketDate2, bookedTicketID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_booked_ticket_summary);

        //Hooks
        bookedTicketID = findViewById(R.id.bookedTicketID);
        bookedTicketTrainFrom2 = findViewById(R.id.bookedTicketTrainFrom2);
        bookedTicketTrainTo2 = findViewById(R.id.bookedTicketTrainTo2);
        bookedTicketTrainName2 = findViewById(R.id.bookedTicketTrainName2);
        bookedTicketTrainNo2 = findViewById(R.id.bookedTicketTrainNo2);
        bookedTicketClass2 = findViewById(R.id.bookedTicketClass2);
        bookedTicketTrainFromTime2 = findViewById(R.id.bookedTicketTrainFromTime2);
        bookedTicketTrainToTime2 = findViewById(R.id.bookedTicketTrainToTime2);
        bookedTicketPrice2 = findViewById(R.id.bookedTicketPrice2);
        bookedTicketQty2 = findViewById(R.id.bookedTicketQty2);
        bookedTicketTotalPrice2 = findViewById(R.id.bookedTicketTotalPrice2);
        bookedTicketDate2 = findViewById(R.id.bookedTicketDate2);

        //Paper
        Paper.init(this);
        String paperTicketID = Paper.book().read(PrevalentBookingTicket.trainIDKey);
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
        bookedTicketID.setText(paperTicketID);
        bookedTicketTrainFrom2.setText(paperTicketTrainFrom);
        bookedTicketTrainTo2.setText(paperTicketTrainTo);
        bookedTicketTrainName2.setText(paperTicketTrainName);
        bookedTicketTrainNo2.setText(paperTicketTrainNo);
        bookedTicketClass2.setText(paperTicketClass);
        bookedTicketTrainFromTime2.setText(paperTicketTrainFromTime);
        bookedTicketTrainToTime2.setText(paperTicketTrainToTime);
        bookedTicketPrice2.setText("Rs." + paperTicketPrice + "/=");
        bookedTicketQty2.setText(paperTicketQty);
        bookedTicketTotalPrice2.setText("Rs." + paperTicketTotalPrice + "/=");
        bookedTicketDate2.setText(paperTicketDate);


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

    public void onClickOkTicket(View view) {

        Intent i = new Intent(getApplicationContext(), BookedTrains.class);
        startActivity(i);

    }

}
