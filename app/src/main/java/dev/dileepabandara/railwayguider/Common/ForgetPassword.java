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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgetPassword extends AppCompatActivity {

    TextInputLayout userMobile;
    ProgressBar progressBarForgetPassword;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);

        userMobile = findViewById(R.id.userMobile);
        progressBarForgetPassword = findViewById(R.id.progressBarForgetPassword);
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBarForgetPassword.setVisibility(View.VISIBLE);

                try {
                    // Check for Internet Connection
                    if (isConnected()) {

                        if (validatePhoneNumber()) {
                            final String userEnteredMobile = userMobile.getEditText().getText().toString();
                            String a = userEnteredMobile.substring(1, 10);
                            final String userMobile = "+94" + a;


                            Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("mobile").equalTo(userEnteredMobile);
                            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){

                                        String emailFromDB = snapshot.child(userEnteredMobile).child("email").getValue(String.class);

                                        Intent i = new Intent(getApplicationContext(),ForgetPassword2.class);
                                        i.putExtra("userMobile", userMobile);
                                        i.putExtra("userEnteredMobile", userEnteredMobile);
                                        i.putExtra("emailFromDB", emailFromDB);
                                        startActivity(i);
                                        progressBarForgetPassword.setVisibility(View.GONE);
                                        finish();

                                    } else {
                                        progressBarForgetPassword.setVisibility(View.GONE);
                                        Toast.makeText(ForgetPassword.this, "No such user exists", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {
                            Toast.makeText(ForgetPassword.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    Toast.makeText(ForgetPassword.this, ""+e, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    // Check for Internet Connection
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Connectivity Exception" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return connected;
    }

    // Validation
    private Boolean validatePhoneNumber() {
        String val = userMobile.getEditText().getText().toString();

        if (val.isEmpty()) {
            userMobile.setError("Mobile number cannot be empty");
            progressBarForgetPassword.setVisibility(View.GONE);
            return false;

        } else if (val.length() < 10) {
            userMobile.setError("Invalid Mobile Number");
            progressBarForgetPassword.setVisibility(View.GONE);
            return false;

        } else {
            userMobile.setError(null);
            userMobile.setErrorEnabled(false);
            return true;
        }
    }

}
