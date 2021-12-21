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

package dev.dileepabandara.railwayguider.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.Prevalent.Prevalent2;
import dev.dileepabandara.railwayguider.R;
import dev.dileepabandara.railwayguider.User.UserDashboard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class WelcomeScreen extends AppCompatActivity {

    TextView lblname, lblmobile, lblemail;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome_screen);

        lblname = findViewById(R.id.welcomeName);
        lblmobile = findViewById(R.id.welcomeMobile);
        lblemail = findViewById(R.id.welcomeEmail);
        progressBar = findViewById(R.id.pBar);

        //Get user details from Prevalent2 class
        Paper.init(this);
        String name = Paper.book().read(Prevalent2.NameKey);
        String mobile = Paper.book().read(Prevalent2.UserMobileKey);
        String email = Paper.book().read(Prevalent2.UserEmailKey);

        lblname.setText("Hi " + name + "!");
        lblemail.setText(email);
        lblmobile.setText(mobile);

    }

    public void onClickGoDashboard(View view) {
        sendUserDetails();
    }

    //Send user profile details to activity
    private void sendUserDetails() {

        progressBar.setVisibility(View.VISIBLE);
        final String userEnteredMobile = Paper.book().read(Prevalent2.UserMobileKey);
        final String userEnteredPassword = Paper.book().read(Prevalent2.UserPasswordKey);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("mobile").equalTo(userEnteredMobile);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(userEnteredMobile).exists()) {

                    String passwordFromDB = dataSnapshot.child(userEnteredMobile).child("password").getValue(String.class);
                    String nameFromDB = dataSnapshot.child(userEnteredMobile).child("name").getValue(String.class);
                    String mobileFromDB = dataSnapshot.child(userEnteredMobile).child("mobile").getValue(String.class);
                    String emailFromDB = dataSnapshot.child(userEnteredMobile).child("email").getValue(String.class);

                    try {
                        if (passwordFromDB.equals(userEnteredPassword)) {

                            //Send user profile details to UserAccountActivity
                            Intent intent2 = new Intent(getApplicationContext(), UserDashboard.class);
                            intent2.putExtra("name", nameFromDB);            //Hold value to pass data
                            intent2.putExtra("email", emailFromDB);          //Hold value to pass data
                            intent2.putExtra("mobile", mobileFromDB);        //Hold value to pass data
                            startActivity(intent2);
                            finish();

                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        Toast.makeText(WelcomeScreen.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
