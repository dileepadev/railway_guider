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
import android.widget.Toast;

import dev.dileepabandara.railwayguider.Prevalent.PrevalentBookingTicket;
import dev.dileepabandara.railwayguider.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import io.paperdb.Paper;

public class TrainSchedule extends AppCompatActivity {

    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_train_schedule);

        btnSearch = findViewById(R.id.searchTrainButton);
        Paper.init(this);
        Paper.book().write(PrevalentBookingTicket.trainFromKey, "Kurunegala");
        Paper.book().write(PrevalentBookingTicket.trainToKey, "Colombo Fort");

        //From Spinner
        MaterialSpinner spinner = findViewById(R.id.spinnerFrom);
        spinner.setItems("Kurunegala", "Colombo Fort", "Kandy");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String itemFrom) {

                Paper.book().write(PrevalentBookingTicket.trainFromKey, itemFrom);

            }
        });

        //To Spinner
        MaterialSpinner spinner2 = findViewById(R.id.spinnerTo);
        spinner2.setItems("Colombo Fort", "Kurunegala", "Kandy");
        spinner2.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String itemTo) {

                Paper.book().write(PrevalentBookingTicket.trainToKey, itemTo);

            }
        });


        //Search
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = Paper.book().read(PrevalentBookingTicket.trainFromKey);
                String to = Paper.book().read(PrevalentBookingTicket.trainToKey);
                String category = from + " - " + to;

                //Intent
                Intent i = new Intent(getApplicationContext(), TrainSchedule2.class);
                i.putExtra("route", category);
                startActivity(i);

                //Snackbar.make(v, "Let's search " + category, Snackbar.LENGTH_LONG).show();
                Toast.makeText(TrainSchedule.this, "Let's search " + category, Toast.LENGTH_SHORT).show();
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
