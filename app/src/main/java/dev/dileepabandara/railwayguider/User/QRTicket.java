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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.R;
import com.google.android.material.snackbar.Snackbar;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class QRTicket extends AppCompatActivity {

    Bitmap bitmap;
    ImageView QRCode;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/Railway Guider QR Tickets/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_q_r_ticket);

        QRCode = findViewById(R.id.QRCode);

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

        final String inputValue = " Railway Guider QR Ticket\n" +"-------------------------------------------\n"
                + "Ticket ID : " + ticketID  + "\n" + "Train Name : " + ticketTrainName + "\n"
                + "Train No : " + ticketTrainNo + " \n"  + "Train From : " + ticketTrainFrom + "\n"
                + "Start Time : " + ticketTrainFromTime + "\n"  + "Train To : " + ticketTrainTo + "\n"
                + "End Time : " + ticketTrainToTime + "\n"  + "Booked Date : " + bookedDate + "\n"
                + "Ticket Class : " + ticketClass + "\n"  + "Ticket Price : Rs." + ticketPrice  + "/=" + "\n"
                + "Total Tickets : " + ticketQty + "\n"  + "Total Price : Rs." + ticketTotalPrice + "/=" + "\n"
                + "-------------------------------------------\n\t\t   Thank You ";


        //Create QR Code
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width<height ? width:height;
        smallerDimension = smallerDimension*3/4;
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
        try {
            bitmap = qrgEncoder.getBitmap();
            QRCode.setImageBitmap(bitmap);

        } catch (Exception e) {
            Toast.makeText(QRTicket.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

        //Save Code
        Button saveQRCode = findViewById(R.id.saveQRCode);
        saveQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        boolean save = new QRGSaver().save(savePath, ticketID, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                        String result = save ? "QR Ticket: " +ticketID + " is Saved" : "QR Ticket: " +ticketID + " is Not Saved";
                        Snackbar.make(v, result + " Save Path: " + savePath, Snackbar.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(QRTicket.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }

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
