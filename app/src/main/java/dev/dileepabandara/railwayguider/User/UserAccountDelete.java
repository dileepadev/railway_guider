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
import android.widget.ProgressBar;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.Common.Login;
import dev.dileepabandara.railwayguider.Prevalent.Prevalent;
import dev.dileepabandara.railwayguider.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class UserAccountDelete extends AppCompatActivity {

    TextInputLayout delPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_account_delete);

        Paper.init(this);
        final String name = Paper.book().read(Prevalent.UserNameKey);
        final String paperUserMobile = Paper.book().read(Prevalent.UserMobileKey);

        delPassword = findViewById(R.id.passwordDelete);
        progressBar = findViewById(R.id.progressBarDeleteAccount);

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


        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userEnteredPassword = delPassword.getEditText().getText().toString().trim();

                if (userEnteredPassword.isEmpty()) {
                    delPassword.setError("Password cannot be empty");

                } else {

                    delPassword.setError(null);
                    delPassword.setErrorEnabled(false);

                    AlertDialog.Builder builder = new AlertDialog.Builder(UserAccountDelete.this);

                    builder.setTitle("Are you sure to delete your account?");
                    builder.setMessage("Hi " + name + "! By deleting your account, your Railway Guider account settings, booked tickets and all other records from our system will be permanently delete");

                    builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            deleteAccount();
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
            }
        });

    }

    //Check Details To Delete
    private void deleteAccount() {

        progressBar.setVisibility(View.VISIBLE);
        Paper.init(this);
        final String paperUserMobile = Paper.book().read(Prevalent.UserMobileKey);
        final String userEnteredPassword = delPassword.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("mobile").equalTo(paperUserMobile);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(paperUserMobile).exists()) {

                    //Get values from database
                    String passwordFromDB = dataSnapshot.child(paperUserMobile).child("password").getValue(String.class);
                    String nameFromDB = dataSnapshot.child(paperUserMobile).child("name").getValue(String.class);
                    String mobileFromDB = dataSnapshot.child(paperUserMobile).child("mobile").getValue(String.class);
                    String emailFromDB = dataSnapshot.child(paperUserMobile).child("email").getValue(String.class);

                    try {
                        if (passwordFromDB.equals(userEnteredPassword)) {

                            //Delete From Users and Tickets
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            reference.child("tickets").child(paperUserMobile).removeValue();
                            reference.child("users").child(paperUserMobile).removeValue();
                            reference.child("users").child(paperUserMobile).child("tickets").removeValue();


                            Toast.makeText(UserAccountDelete.this, "Deleted From Database" +passwordFromDB+" "+nameFromDB+" "+mobileFromDB+" "+emailFromDB, Toast.LENGTH_SHORT).show();

                            Paper.book().destroy();
                            startActivity(new Intent(UserAccountDelete.this, Login.class));
                            finishAffinity();
                            progressBar.setVisibility(View.GONE);

//                            //Delete From Authentication (Sign Out)
//                            assert emailFromDB != null;
//                            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailFromDB, passwordFromDB).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                                @Override
//                                public void onSuccess(AuthResult authResult) {
//                                    FirebaseUser user = authResult.getUser();
//
//                                    assert user != null;
//                                    user.delete()
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()) {
//                                                        Paper.book().destroy();
//                                                        startActivity(new Intent(UserAccountDelete.this, Login.class));
//                                                        finishAffinity();
//                                                        progressBar.setVisibility(View.GONE);
//                                                        Toast.makeText(UserAccountDelete.this, "Your account has been permanently deleted. Thank You.", Toast.LENGTH_SHORT).show();
//                                                    } else {
//                                                        progressBar.setVisibility(View.GONE);
//                                                        Toast.makeText(UserAccountDelete.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            });
//                                }
//                            });


                        } else {
                            progressBar.setVisibility(View.GONE);
                            delPassword.setError("Wrong Password");
                            delPassword.requestFocus();

                        }
                    } catch (Exception e) {
                        Toast.makeText(UserAccountDelete.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UserAccountDelete.this, "Can't delete account. Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserAccountDelete.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
