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

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.Prevalent.Prevalent;
import dev.dileepabandara.railwayguider.R;

import dev.dileepabandara.railwayguider.User.UserDashboard;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {

    Button callSignUp, login, forgetPassword;
    ImageView image;
    TextView logOption, welcome;
    TextInputLayout logUserMobile, logPassword;
    ProgressBar progressBar;
    CheckBox chkBoxRememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        ///Hooks
        callSignUp = findViewById(R.id.btncallSignUp);
        login = findViewById(R.id.btn_login);
        forgetPassword = findViewById(R.id.forgotPassword);
        image = findViewById(R.id.logoImage2);
        logOption = findViewById(R.id.text1);
        welcome = findViewById(R.id.text2);
        logUserMobile = findViewById(R.id.username);
        logPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        chkBoxRememberMe = findViewById(R.id.rememberMeCheckbox);
        Paper.init(this);

        //Switch to SignUp
        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(callSignUp, "btn_change");
                pairs[1] = new Pair<View, String>(login, "btn_go");
                pairs[2] = new Pair<View, String>(logOption, "text1");
                pairs[3] = new Pair<View, String>(welcome, "text2");
                pairs[4] = new Pair<View, String>(logUserMobile, "txt_mobile");
                pairs[5] = new Pair<View, String>(logPassword, "txt_password");
                pairs[6] = new Pair<View, String>(image, "logoImage");

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, activityOptions.toBundle());
                //finish();
            }
        });

        //Login event
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validatePassword() | !validateUsername()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                    toast.show();
                    return;

                } else {
                    final String userEnteredMobile = logUserMobile.getEditText().getText().toString().trim();
                    Paper.book().write(Prevalent.UserMobileKey, userEnteredMobile);

                    saveToPrevalent();
                    sendUserDetails();

                }
            }
        });


        //Forget Password
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
            }
        });


        //If remember me checked get username and password from Prevalent class
        final String UserMobileKey = Paper.book().read(Prevalent.UserMobileKey);
        final String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserMobileKey !="" && UserPasswordKey !=""){
            if (!TextUtils.isEmpty(UserMobileKey)  &&  !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess();
            }
        }

    }

    private void AllowAccess() {

        progressBar.setVisibility(View.VISIBLE);

        //Get username and password from Prevalent class
        final String userEnteredMobile = Paper.book().read(Prevalent.UserMobileKey);
        final String userEnteredPassword = Paper.book().read(Prevalent.UserPasswordKey);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("mobile").equalTo(userEnteredMobile);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(userEnteredMobile).exists()) {

                    logUserMobile.setError(null);
                    logUserMobile.setErrorEnabled(false);

                    //Get values from database
                    String passwordFromDB = dataSnapshot.child(userEnteredMobile).child("password").getValue(String.class);
                    String nameFromDB = dataSnapshot.child(userEnteredMobile).child("name").getValue(String.class);
                    String mobileFromDB = dataSnapshot.child(userEnteredMobile).child("mobile").getValue(String.class);
                    String emailFromDB = dataSnapshot.child(userEnteredMobile).child("email").getValue(String.class);

                    try {
                        if (passwordFromDB.equals(userEnteredPassword)) {

                            logUserMobile.setError(null);
                            logUserMobile.setErrorEnabled(false);

                            //Send user profile details to UserDashboardActivity
                            Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                            intent.putExtra("name", nameFromDB);            //Hold value to pass data
                            intent.putExtra("email", emailFromDB);          //Hold value to pass data
                            intent.putExtra("mobile", mobileFromDB);        //Hold value to pass data
                            intent.putExtra("password", passwordFromDB);    //Hold value to pass data
                            Paper.book().write(Prevalent.UserNameKey, nameFromDB);
                            startActivity(intent);
                            finishAffinity();

                        } else {
                            progressBar.setVisibility(View.GONE);
                            logPassword.setError("Wrong Password");
                            logPassword.requestFocus();

                        }
                    } catch (Exception e) {
                        Toast.makeText(Login.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    logUserMobile.setError("Mobile number is not registered");
                    logUserMobile.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Boolean validateUsername() {
        String val = logUserMobile.getEditText().getText().toString();

        if (val.isEmpty()) {
            logUserMobile.setError("Mobile number cannot be empty");
            return false;
        } else {
            logUserMobile.setError(null);
            logUserMobile.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = logPassword.getEditText().getText().toString();

        if (val.isEmpty()) {
            logPassword.setError("Password cannot be empty");
            return false;
        } else {
            logPassword.setError(null);
            logPassword.setErrorEnabled(false);
            return true;
        }
    }

    //Send user profile to activity
    private void sendUserDetails() {

        progressBar.setVisibility(View.VISIBLE);

        final String userEnteredMobile = logUserMobile.getEditText().getText().toString().trim();
        final String userEnteredPassword = logPassword.getEditText().getText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("mobile").equalTo(userEnteredMobile);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(userEnteredMobile).exists()) {

                    logUserMobile.setError(null);
                    logUserMobile.setErrorEnabled(false);

                    //Get values from database
                    String passwordFromDB = dataSnapshot.child(userEnteredMobile).child("password").getValue(String.class);
                    String nameFromDB = dataSnapshot.child(userEnteredMobile).child("name").getValue(String.class);
                    String mobileFromDB = dataSnapshot.child(userEnteredMobile).child("mobile").getValue(String.class);
                    String emailFromDB = dataSnapshot.child(userEnteredMobile).child("email").getValue(String.class);

                    try {
                        if (passwordFromDB.equals(userEnteredPassword)) {

                            logUserMobile.setError(null);
                            logUserMobile.setErrorEnabled(false);

                            //Send user profile details to UserDashboardActivity
                            Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                            intent.putExtra("name", nameFromDB);            //Hold value to pass data
                            intent.putExtra("email", emailFromDB);          //Hold value to pass data
                            intent.putExtra("mobile", mobileFromDB);        //Hold value to pass data
                            intent.putExtra("password", passwordFromDB);    //Hold value to pass data
                            Paper.book().write(Prevalent.UserNameKey, nameFromDB);
                            startActivity(intent);
                            finishAffinity();

                        } else {
                            progressBar.setVisibility(View.GONE);
                            logPassword.setError("Wrong Password");
                            logPassword.requestFocus();

                        }
                    } catch (Exception e) {
                        Toast.makeText(Login.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    logUserMobile.setError("Mobile is not registered");
                    logUserMobile.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Save Username and UserPassword to Prevalent Class
    private void saveToPrevalent(){

        if (chkBoxRememberMe.isChecked()){

            final String userEnteredMobile = logUserMobile.getEditText().getText().toString().trim();
            final String userEnteredPassword = logPassword.getEditText().getText().toString().trim();
            Paper.book().write(Prevalent.UserMobileKey, userEnteredMobile);
            Paper.book().write(Prevalent.UserPasswordKey, userEnteredPassword);

        }

    }

}
