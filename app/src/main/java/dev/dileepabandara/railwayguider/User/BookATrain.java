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

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.Prevalent.PrevalentBookingTicket;
import dev.dileepabandara.railwayguider.Prevalent.PrevalentTicketPrices;
import dev.dileepabandara.railwayguider.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;

import io.paperdb.Paper;

public class BookATrain extends AppCompatActivity {

    TextView txtTrainName, txtTrainFrom, txtTrainTo;
    TextView txtTicketPrice, setDateError, txtTrainFromTime, txtTrainToTime;
    EditText editTextDate, editTextTicketQty;
    DatePickerDialog datePickerDialog;
    Button checkBookTrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book_a_train);

        //Hooks
        txtTrainName = findViewById(R.id.txtTrainName);
        txtTrainFrom = findViewById(R.id.txtTrainFrom);
        txtTrainTo = findViewById(R.id.txtTrainTo);
        txtTicketPrice = findViewById(R.id.txtTicketPrice);
        setDateError = findViewById(R.id.setDateError);
        txtTrainFromTime = findViewById(R.id.txtTrainFromTime);
        txtTrainToTime = findViewById(R.id.txtTrainToTime);
        editTextDate = findViewById(R.id.searchDate);
        editTextTicketQty = findViewById(R.id.txtTicketQty);
        //spinner = findViewById(R.id.spinner1);
        checkBookTrain = findViewById(R.id.checkBookTrain);


        //Top Bar
        ImageView imgBack = findViewById(R.id.imgBack);
        ImageView imgHome = findViewById(R.id.imgHome);

        //Back Button
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Home Button
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                finishAffinity();
            }
        });

        //Paper
        Paper.init(this);
        String paperTrainFrom = Paper.book().read(PrevalentBookingTicket.trainFromKey);
        String paperTrainTo = Paper.book().read(PrevalentBookingTicket.trainToKey);
        txtTrainFrom.setText(paperTrainFrom);
        txtTrainTo.setText(paperTrainTo);

        //Get from Scheduler Adapter and set train name
        Intent intent = getIntent();
        String trainName = intent.getStringExtra("trainName");
        String trainStartTime = intent.getStringExtra("trainStart");
        String trainEndTime = intent.getStringExtra("trainEnd");
        String trainNo = intent.getStringExtra("trainNo");

        //Set Values
        txtTrainName.setText(trainName);
        txtTrainFromTime.setText(trainStartTime);
        txtTrainToTime.setText(trainEndTime);

        //Paper
        Paper.book().write(PrevalentBookingTicket.trainNameKey, trainName);
        Paper.book().write(PrevalentBookingTicket.trainFromTimeKey, trainStartTime);
        Paper.book().write(PrevalentBookingTicket.trainToTimeKey, trainEndTime);
        Paper.book().write(PrevalentBookingTicket.trainNoKey, trainNo);

        //Search Date
        editTextDate.setInputType(InputType.TYPE_NULL);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                datePickerDialog = new DatePickerDialog(BookATrain.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        String date = editTextDate.getText().toString();
                        Paper.book().write(PrevalentBookingTicket.trainDateKey, date);
                        if (date.equals("19/5/2020")) {
                            setDateError.setVisibility(View.VISIBLE);
                        } else {
                            setDateError.setVisibility(View.INVISIBLE);
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        getTicketClass();

        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner1);
        spinner.setItems("2nd Class", "3rd Class");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();

                Paper.book().write(PrevalentBookingTicket.ticketClassKey, item);
                String ticketPrice2 = Paper.book().read(PrevalentTicketPrices.secondClassPriceKey);
                String ticketPrice3 = Paper.book().read(PrevalentTicketPrices.thirdClassPriceKey);

                switch (item) {

                    case "2nd Class":
                        txtTicketPrice.setText("Rs." + ticketPrice2 + "/=");
                        Paper.book().write(PrevalentBookingTicket.ticketPriceKey, ticketPrice2);
                        break;

                    case "3rd Class":
                        txtTicketPrice.setText("Rs." + ticketPrice3 + "/=");
                        Paper.book().write(PrevalentBookingTicket.ticketPriceKey, ticketPrice3);
                        break;
                }

            }
        });

    }

    public void onClickBookTrain(View view) {

        if (editTextDate.getText().toString().equals("")) {
            Toast.makeText(BookATrain.this, "Please select date", Toast.LENGTH_SHORT).show();
        } else if (editTextTicketQty.getText().toString().equals("") || editTextTicketQty.getText().toString().equals("0")) {
            Toast.makeText(BookATrain.this, "Please enter ticket quantity", Toast.LENGTH_SHORT).show();
        } else {
            //Ticket Qty
            String ticketQtyS = editTextTicketQty.getText().toString();
            int ticketQtyInt = Integer.parseInt(ticketQtyS);
            Paper.book().write(PrevalentBookingTicket.ticketQtyKey, ticketQtyS);

            String paperTicketPrice = Paper.book().read(PrevalentBookingTicket.ticketPriceKey);
            int ticketPrice = Integer.parseInt(paperTicketPrice);
            int totalTicketPriceInt = ticketPrice * ticketQtyInt;
            String totalTicketPriceString = String.valueOf(totalTicketPriceInt);
            Paper.book().write(PrevalentBookingTicket.trainTicketTotalPriceKey, totalTicketPriceString);

            Intent i = new Intent(getApplicationContext(), BookedTicket.class);
            startActivity(i);
        }

    }


    //Get class ticket price from firebase
    public void getTicketClass() {

        try {
            //Paper
            Paper.init(this);
            String paperTrainFrom = Paper.book().read(PrevalentBookingTicket.trainFromKey);
            String paperTrainTo = Paper.book().read(PrevalentBookingTicket.trainToKey);
            final String route = paperTrainFrom + " - " + paperTrainTo;

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("schedules");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Integer secondClassDB = dataSnapshot.child(route).child("tickets").child("secondClass").getValue(Integer.class);
                        Integer thirdClassDB = dataSnapshot.child(route).child("tickets").child("thirdClass").getValue(Integer.class);

                        String secondClassDBs = String.valueOf(secondClassDB);
                        String thirdClassDBs = String.valueOf(thirdClassDB);

                        Paper.book().write(PrevalentTicketPrices.secondClassPriceKey, secondClassDBs);
                        Paper.book().write(PrevalentTicketPrices.thirdClassPriceKey, thirdClassDBs);

                        txtTicketPrice.setText("Rs." + secondClassDBs + "/=");

//                        Toast.makeText(BookATrain.this, ""+firstClassDBs +" " +secondClassDBs +" " +thirdClassDBs, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(BookATrain.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
    }

}
