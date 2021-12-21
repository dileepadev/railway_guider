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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
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

public class ForgetPassword2 extends AppCompatActivity {

    String verificationOTPbySystem;
    PinView txtOTP;
    Button btnVerify;
    LinearLayout otpNotReceived;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password2);

        txtOTP = findViewById(R.id.txtOPTForgetPassword);
        btnVerify = findViewById(R.id.btnNext2);
        progressBar = findViewById(R.id.progressBarVerifyFo);

        Intent intent = getIntent();
        final String userEnteredMobile = intent.getStringExtra("userEnteredMobile");
        final String mobileNoWithCountryCode = intent.getStringExtra("userMobile");
        final String emailFromDB = intent.getStringExtra("emailFromDB");

        Toast.makeText(ForgetPassword2.this, ""+mobileNoWithCountryCode, Toast.LENGTH_SHORT).show();

        sendVerificationOTPtoUser(mobileNoWithCountryCode);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP = txtOTP.getText().toString();

                progressBar.setVisibility(View.VISIBLE);
                verifyOTP(OTP);
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
        Toast.makeText(ForgetPassword2.this, "Your OTP send to " + mobileNoWithCountryCode, Toast.LENGTH_LONG).show();

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        //If OTP send to other device
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationOTPbySystem = s;
            Toast.makeText(ForgetPassword2.this, "OTP send to other device", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(ForgetPassword2.this, "OTP send to your own device", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgetPassword2.this, "OTP cannot received. Please register later.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(ForgetPassword2.this, "" + e, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(ForgetPassword2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyOTP(String OTPbyUser) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationOTPbySystem, OTPbyUser);
        signInTheUserByCredential(credential);
        Toast.makeText(ForgetPassword2.this, "Checking OTP", Toast.LENGTH_SHORT).show();

    }

    private void signInTheUserByCredential(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(ForgetPassword2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), ForgetPassword3.class);

                            Intent intent2 = getIntent();
                            final String userEnteredMobile = intent2.getStringExtra("userEnteredMobile");
                            final String emailFromDB = intent.getStringExtra("emailFromDB");
                            intent.putExtra("userEnteredMobile", userEnteredMobile);
                            intent.putExtra("emailFromDB", emailFromDB);

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(ForgetPassword2.this, "Verification Success", Toast.LENGTH_SHORT).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(ForgetPassword2.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(ForgetPassword2.this, "Verification not success. Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }

}
