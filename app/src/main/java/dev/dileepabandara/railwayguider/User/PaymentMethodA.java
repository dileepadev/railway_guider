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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.Prevalent.Prevalent;
import dev.dileepabandara.railwayguider.Prevalent.Prevalent2;
import dev.dileepabandara.railwayguider.Prevalent.PrevalentBookingTicket;
import dev.dileepabandara.railwayguider.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import io.paperdb.Paper;

public class PaymentMethodA extends AppCompatActivity {

    Button confirmPayment, cancelPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_payment_method_a);

        confirmPayment = findViewById(R.id.confirmPayment);
        cancelPayment = findViewById(R.id.cancelPayment);

        confirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmMessage();
            }
        });

        cancelPayment.setOnClickListener(new View.OnClickListener() {
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

    public void createTicketID() {

        try {
            //Paper
            Paper.init(this);
            String trainFrom = Paper.book().read(PrevalentBookingTicket.trainFromKey);
            String trainTo = Paper.book().read(PrevalentBookingTicket.trainToKey);
            String UserMobile = Paper.book().read(Prevalent.UserMobileKey);

            Random r = new Random();
            int rNum = r.nextInt(200);
            String rNumS = String.valueOf(rNum);

            String a = trainFrom.substring(0, 3).toUpperCase();
            String b = trainTo.substring(0, 3).toUpperCase();
            String c = a + b;

            String mob = UserMobile.substring(3, 6);

            String trainID = rNumS + c + mob;
            Paper.book().write(PrevalentBookingTicket.trainIDKey, trainID);
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void confirmMessage() {

        Paper.init(this);
        final String ticketID = Paper.book().read(PrevalentBookingTicket.trainIDKey);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm before booking");
        builder.setMessage("Are you sure to book this train?");

        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                createTicketID();
                saveToFirebase();
                Intent i = new Intent(getApplicationContext(), BookedTicketSummary.class);
                i.putExtra("ticketID", ticketID);
                startActivity(i);
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

    public void saveToFirebase() {

        Paper.init(this);
        final String userID = Paper.book().read(Prevalent.UserMobileKey);
        final String ticketID = Paper.book().read(PrevalentBookingTicket.trainIDKey);
        final String ticketTrainName = Paper.book().read(PrevalentBookingTicket.trainNameKey);
        final String ticketTrainNo = Paper.book().read(PrevalentBookingTicket.trainNoKey);
        final String ticketTrainFrom = Paper.book().read(PrevalentBookingTicket.trainFromKey);
        final String ticketTrainFromTime = Paper.book().read(PrevalentBookingTicket.trainFromTimeKey);
        final String ticketTrainTo = Paper.book().read(PrevalentBookingTicket.trainToKey);
        final String ticketTrainToTime = Paper.book().read(PrevalentBookingTicket.trainToTimeKey);
        final String bookedDate = Paper.book().read(PrevalentBookingTicket.trainDateKey);
        final String ticketClass = Paper.book().read(PrevalentBookingTicket.ticketClassKey);
        final String ticketPrice = Paper.book().read(PrevalentBookingTicket.ticketPriceKey);
        final String ticketQty = Paper.book().read(PrevalentBookingTicket.ticketQtyKey);
        final String ticketTotalPrice = Paper.book().read(PrevalentBookingTicket.trainTicketTotalPriceKey);

        //Save to tickets table
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("tickets").child(userID).child(ticketID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    dataSnapshot.getRef().child("ticketID").setValue(ticketID);
                    dataSnapshot.getRef().child("ticketTrainName").setValue(ticketTrainName);
                    dataSnapshot.getRef().child("ticketTrainNo").setValue(ticketTrainNo);
                    dataSnapshot.getRef().child("ticketTrainFrom").setValue(ticketTrainFrom);
                    dataSnapshot.getRef().child("ticketTrainFromTime").setValue(ticketTrainFromTime);
                    dataSnapshot.getRef().child("ticketTrainTo").setValue(ticketTrainTo);
                    dataSnapshot.getRef().child("ticketTrainToTime").setValue(ticketTrainToTime);
                    dataSnapshot.getRef().child("bookedDate").setValue(bookedDate);
                    dataSnapshot.getRef().child("ticketClass").setValue(ticketClass);
                    dataSnapshot.getRef().child("ticketPrice").setValue(ticketPrice);
                    dataSnapshot.getRef().child("ticketQty").setValue(ticketQty);
                    dataSnapshot.getRef().child("ticketTotalPrice").setValue(ticketTotalPrice);

                } catch (Exception e) {
                    Toast.makeText(PaymentMethodA.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PaymentMethodA.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Save to users tables
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("tickets").child(ticketID);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    dataSnapshot.getRef().child("ticketID").setValue(ticketID);
                    dataSnapshot.getRef().child("ticketTrainName").setValue(ticketTrainName);
                    dataSnapshot.getRef().child("ticketTrainNo").setValue(ticketTrainNo);
                    dataSnapshot.getRef().child("ticketTrainFrom").setValue(ticketTrainFrom);
                    dataSnapshot.getRef().child("ticketTrainFromTime").setValue(ticketTrainFromTime);
                    dataSnapshot.getRef().child("ticketTrainTo").setValue(ticketTrainTo);
                    dataSnapshot.getRef().child("ticketTrainToTime").setValue(ticketTrainToTime);
                    dataSnapshot.getRef().child("bookedDate").setValue(bookedDate);
                    dataSnapshot.getRef().child("ticketClass").setValue(ticketClass);
                    dataSnapshot.getRef().child("ticketPrice").setValue(ticketPrice);
                    dataSnapshot.getRef().child("ticketQty").setValue(ticketQty);
                    dataSnapshot.getRef().child("ticketTotalPrice").setValue(ticketTotalPrice);

                } catch (Exception e) {
                    Toast.makeText(PaymentMethodA.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PaymentMethodA.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Save to trains tables
        Paper.init(this);
        final String nameID = Paper.book().read(Prevalent2.NameKey);

        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("trains").child(ticketTrainNo).child("users").child(userID);
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    dataSnapshot.getRef().child("mobile").setValue(userID);
                    dataSnapshot.getRef().child("name").setValue(nameID);
                    dataSnapshot.getRef().child("ticketID").setValue(ticketID);

                } catch (Exception e) {
                    Toast.makeText(PaymentMethodA.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PaymentMethodA.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
