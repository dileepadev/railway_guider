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
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgetPassword3 extends AppCompatActivity {

    TextInputLayout newPass1, newPass2;
    ProgressBar progressBarResetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password3);

        newPass1 = findViewById(R.id.newPass1);
        newPass2 = findViewById(R.id.newPass2);
        progressBarResetPass = findViewById(R.id.progressBarResetPass);

        Intent intent2 = getIntent();
        final String userEnteredMobile = intent2.getStringExtra("userEnteredMobile");
        final String emailFromDB = intent2.getStringExtra("emailFromDB");

        Button btnUpdatePass = findViewById(R.id.btnUpdatePass);
        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPassword = newPass1.getEditText().getText().toString();
                progressBarResetPass.setVisibility(View.VISIBLE);

                try {
                    if (validatePassword1() && validatePassword2() && validatePasswordsEqual()) {

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                        reference.child(userEnteredMobile).child("password").setValue(newPassword);
                        notification();
                        Toast.makeText(ForgetPassword3.this, "Your password has updated", Toast.LENGTH_LONG).show();
                        progressBarResetPass.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), ForgetPassword4.class));
                        finish();
                        sendEmail();
                    } else {
                        Toast.makeText(ForgetPassword3.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ForgetPassword3.this, "" + e, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private Boolean validatePassword1() {
        String val = newPass1.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            newPass1.setError("Password cannot be empty");
            progressBarResetPass.setVisibility(View.GONE);
            return false;
        } else if (!val.matches(passwordVal)) {
            newPass1.setError("Password is too weak. Use uppercase, lowercase letters, numbers and special characters");
            progressBarResetPass.setVisibility(View.GONE);
            return false;
        } else if (val.length() < 6) {
            progressBarResetPass.setVisibility(View.GONE);
            newPass1.setError("Use at least 6 characters");
            return false;
        } else {
            progressBarResetPass.setVisibility(View.GONE);
            newPass1.setError(null);
            newPass1.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword2() {
        String val = newPass2.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            progressBarResetPass.setVisibility(View.GONE);
            newPass2.setError("Password cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            progressBarResetPass.setVisibility(View.GONE);
            newPass2.setError("Password is too weak. Use uppercase, lowercase letters, numbers and special characters");
            return false;
        } else if (val.length() < 6) {
            progressBarResetPass.setVisibility(View.GONE);
            newPass2.setError("Use at least 6 characters");
            return false;
        } else {
            progressBarResetPass.setVisibility(View.GONE);
            newPass2.setError(null);
            newPass2.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePasswordsEqual() {
        String val1 = newPass1.getEditText().getText().toString();
        String val2 = newPass2.getEditText().getText().toString();

        if (!val1.equals(val2)) {
            progressBarResetPass.setVisibility(View.GONE);
            newPass1.setError("Passwords not equal");
            newPass2.setError("Passwords not equal");
            return false;
        } else {
            progressBarResetPass.setVisibility(View.GONE);
            newPass1.setError(null);
            newPass1.setErrorEnabled(false);
            newPass2.setError(null);
            newPass2.setErrorEnabled(false);
            return true;
        }
    }

    //Send notification
    private void notification() {

        //Show notification
        String notificationTitle = "Password Updated";
        String notificationMessage = "Your account password has been updated";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                ForgetPassword3.this
        )
                .setSmallIcon(R.drawable.icon_notification_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND);

        //OnTouch goto activity
        Intent intent = new Intent(ForgetPassword3.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", notificationMessage);

        PendingIntent pendingIntent = PendingIntent.getActivity(ForgetPassword3.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE
        );
        notificationManager.notify(0, builder.build());

    }

    public void sendEmail() {
        Intent intent2 = getIntent();
        String emailFromDB = intent2.getStringExtra("emailFromDB");
        //Toast.makeText(ForgetPassword3.this, ""+emailFromDB, Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().sendPasswordResetEmail(emailFromDB)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgetPassword3.this, "An email has been sent to you.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
