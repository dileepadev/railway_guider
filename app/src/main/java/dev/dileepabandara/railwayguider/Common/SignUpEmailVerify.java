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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.Databases.UserHelperClass;
import dev.dileepabandara.railwayguider.Prevalent.Prevalent2;
import dev.dileepabandara.railwayguider.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SignUpEmailVerify extends AppCompatActivity {

    Button button;
    ProgressBar progressBar;
    TextView lblName, lblMessage;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_email_verify);

        lblName = findViewById(R.id.getUsername);
        lblMessage = findViewById(R.id.activeMessage);
        progressBar = findViewById(R.id.progressBarEmail);

        //Get user details from Prevalent2 class
        Paper.init(this);
        String user_name = Paper.book().read(Prevalent2.NameKey);
        String user_email = Paper.book().read(Prevalent2.UserEmailKey);

        //Text view
        String userWelcome = "Hi " + user_name + "!";
        lblName.setText(userWelcome);
        String message = "Please verify your email " + user_email + " to active your account";
        lblMessage.setText(message);

        //Button Verify Email
        button = findViewById(R.id.btnSendEmail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authUser();
            }
        });
    }

    //Set authentication
    private void authUser() {

        try {
            progressBar.setVisibility(View.VISIBLE);

            Paper.init(this);
            final String email = Paper.book().read(Prevalent2.UserEmailKey);
            final String password = Paper.book().read(Prevalent2.UserPasswordKey);

            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpEmailVerify.this, "Your email is registered", Toast.LENGTH_LONG).show();
                        sendEmail();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpEmailVerify.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }

    }

    //send the email
    private void sendEmail() {
        try {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            Paper.init(this);
            final String user_email = Paper.book().read(Prevalent2.UserEmailKey);
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpEmailVerify.this, "Email send to " + user_email, Toast.LENGTH_SHORT).show();
                                saveUserDetailsToFirebase();
                            } else {
                                Toast.makeText(SignUpEmailVerify.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }

    }

    //Save account to firebase
    private void saveUserDetailsToFirebase() {

        try {
            //Get User Details from Prevalent2 Paper
            Paper.init(this);
            String name = Paper.book().read(Prevalent2.NameKey);
            String email = Paper.book().read(Prevalent2.UserEmailKey);
            String mobile = Paper.book().read(Prevalent2.UserMobileKey);
            String password = Paper.book().read(Prevalent2.UserPasswordKey);

            database = FirebaseDatabase.getInstance();
            reference = database.getReference("users");

            progressBar.setVisibility(View.VISIBLE);
            final UserHelperClass helperClass = new UserHelperClass(name, email, mobile, password);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    reference.child(helperClass.getMobile()).setValue(helperClass);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SignUpEmailVerify.this, "Your account successfully created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SignUpMobileVerify.class));
                    finishAffinity();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SignUpEmailVerify.this, "Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void next2(View view) {
        startActivity(new Intent(getApplicationContext(), SignUpMobileVerify.class));
    }
}
