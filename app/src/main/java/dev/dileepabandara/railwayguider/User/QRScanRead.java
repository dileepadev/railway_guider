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
import android.widget.Toast;

import dev.dileepabandara.railwayguider.HelperClasses.CaptureAct;
import dev.dileepabandara.railwayguider.Prevalent.Prevalent;
import dev.dileepabandara.railwayguider.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import io.paperdb.Paper;

public class QRScanRead extends AppCompatActivity {

    TextView scanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_q_r_scan_read);

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

        //QR Scanner
        Button scan_qr = findViewById(R.id.scan_qr);
        scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(QRScanRead.this);
                integrator.setCaptureActivity(CaptureAct.class);
                integrator.setOrientationLocked(false);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scanning...");
                integrator.initiateScan();
            }
        });


    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                ImageView imgScan = findViewById(R.id.imgScan);
                imgScan.setVisibility(View.GONE);
                Toast.makeText(this, "Successfully Scanned ", Toast.LENGTH_LONG).show();
                scanResult = findViewById(R.id.scanResult);
                scanResult.setText(result.getContents());

                String company = String.valueOf(result.getContents()).substring(35, 49);
                String codeSecret = String.valueOf(result.getContents()).substring(50, 52);
//                Toast.makeText(this, "" + company, Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "" + codeSecret, Toast.LENGTH_SHORT).show();
                saveToFirebase(company, codeSecret);

            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void saveToFirebase(String company, String codeSecret) {

        Paper.init(this);
        String userID = Paper.book().read(Prevalent.UserMobileKey);

        int int_codeSecret = Integer.parseInt(codeSecret);
        int totalCards = 2 + int_codeSecret;

        if (company.equals("Railway Guider")) {

            Toast.makeText(this, "Congratulations! " + int_codeSecret + " cards added to your account. Try to get more cards.", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Your total cards balance is " + totalCards, Toast.LENGTH_LONG).show();

//            final String gifts = String.valueOf(company);
//            //Save to tickets table
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("gifts").child(userID);
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    try {
//
//                        dataSnapshot.getRef().child("gifts").setValue(gifts);
//
//                    } catch (Exception e) {
//                        Toast.makeText(QRScanRead.this, "Error: " + e, Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Toast.makeText(QRScanRead.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });

        } else {

            Toast.makeText(this, "Sorry this QR Code is not valid", Toast.LENGTH_SHORT).show();

        }

    }

}
