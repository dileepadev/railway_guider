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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import dev.dileepabandara.railwayguider.Prevalent.Prevalent2;
import dev.dileepabandara.railwayguider.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class SignUpMobileVerify extends AppCompatActivity {

    String verificationOTPbySystem;
    PinView txtOTP;
    Button btnVerify;
    LinearLayout otpNotReceived;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up_mobile_verify);

        txtOTP = findViewById(R.id.txtOPT);
        btnVerify = findViewById(R.id.btnVerify);
        progressBar = findViewById(R.id.progressBarVerify);
        otpNotReceived = findViewById(R.id.otpNotReceived);

        progressBar.setVisibility(View.GONE);

        //Get user mobile number from Prevalent2 class
        Paper.init(this);
        String mobileNo = Paper.book().read(Prevalent2.UserMobileKey);
        String mobileNo2 = mobileNo.substring(1, 10);
        String mobileNoWithCountryCode = "+94" + mobileNo2;

        sendVerificationOTPtoUser(mobileNoWithCountryCode);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP = txtOTP.getText().toString();

                    progressBar.setVisibility(View.VISIBLE);
                    verifyOTP(OTP);
            }
        });

        otpNotReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification();
                startActivity(new Intent(getApplicationContext(), WelcomeScreen.class));
            }
        });
    }

    private void sendVerificationOTPtoUser(String mobileNoWithCountryCode) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNoWithCountryCode,         // Phone number to verify
                120,                       // Timeout duration
                TimeUnit.SECONDS,           // Unit of timeout
                TaskExecutors.MAIN_THREAD,  // Activity (for callback binding)
                mCallbacks);                // OnVerificationStateChangedCallbacks
        Toast.makeText(SignUpMobileVerify.this, "Your OTP send to " + mobileNoWithCountryCode, Toast.LENGTH_LONG).show();

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        //If OTP send to other device
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationOTPbySystem = s;
            Toast.makeText(SignUpMobileVerify.this, "OTP send to other device", Toast.LENGTH_SHORT).show();

        }

        //If OTP send to own device
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            try {
                String OTP = phoneAuthCredential.getSmsCode();
                if (OTP != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    txtOTP.setText(OTP);
                    verifyOTP(OTP);
                    Toast.makeText(SignUpMobileVerify.this, "OTP send to your own device", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpMobileVerify.this, "OTP cannot received. Please register later.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(SignUpMobileVerify.this, "" + e, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(SignUpMobileVerify.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyOTP(String OTPbyUser) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationOTPbySystem, OTPbyUser);
        signInTheUserByCredential(credential);
        Toast.makeText(SignUpMobileVerify.this, "Checking OTP", Toast.LENGTH_SHORT).show();

    }

    private void signInTheUserByCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignUpMobileVerify.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            notification();
                            Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(SignUpMobileVerify.this, "Verification Success", Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(SignUpMobileVerify.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignUpMobileVerify.this, "Verification not success. Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }

    //Send notification
    private void notification() {

        //Show notification
        String name = Paper.book().read(Prevalent2.NameKey);
        String notificationTitle = "Welcome to Railway Guider";
        String notificationMessage = "Hi " + name + "! Thank you for join with us";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                SignUpMobileVerify.this
        )
                .setSmallIcon(R.drawable.icon_notification_24dp)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_SOUND);

        //OnTouch goto activity
        Intent intent = new Intent(SignUpMobileVerify.this, WelcomeScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", notificationMessage);

        PendingIntent pendingIntent = PendingIntent.getActivity(SignUpMobileVerify.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE
        );
        notificationManager.notify(0, builder.build());

    }

}
