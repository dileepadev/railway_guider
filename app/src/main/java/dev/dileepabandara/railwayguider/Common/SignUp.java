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
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.Databases.UserHelperClass;
import dev.dileepabandara.railwayguider.Prevalent.Prevalent;
import dev.dileepabandara.railwayguider.Prevalent.Prevalent2;
import dev.dileepabandara.railwayguider.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SignUp extends AppCompatActivity {

    //Variables
    Button callLogin, signUp;
    ImageView image;
    TextView logOption, welcome;
    TextInputLayout regName, regEmail, regMobile, regPassword;
    ProgressBar progressBar;

    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        ///Hooks
        callLogin = findViewById(R.id.btncallLogin);
        signUp = findViewById(R.id.btn_signUp);
        image = findViewById(R.id.logoImage2);
        logOption = findViewById(R.id.text1);
        welcome = findViewById(R.id.text2);
        regPassword = findViewById(R.id.regPassword);
        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);
        regMobile = findViewById(R.id.regMobile);
        progressBar = findViewById(R.id.progressBar);

        Paper.init(this);

        //Switch to Login
        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(callLogin, "btn_change");
                pairs[1] = new Pair<View, String>(signUp, "btn_go");
                pairs[2] = new Pair<View, String>(logOption, "text1");
                pairs[3] = new Pair<View, String>(welcome, "text2");
                pairs[4] = new Pair<View, String>(regMobile, "txt_mobile");
                pairs[5] = new Pair<View, String>(regPassword, "txt_password");
                pairs[6] = new Pair<View, String>(image, "logoImage");

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
                startActivity(intent, activityOptions.toBundle());
                finish();
            }
        });

        //SignUp event
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateName() | !validatePassword() | !validateMobile() | !validateEmail()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                } else {
                    checkUserDetails();
                }
            }

            //Check mobile number already taken from firebase
            private void checkUserDetails() {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                //Get all the values
                String name = regName.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String mobile = regMobile.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();

                progressBar.setVisibility(View.VISIBLE);
                final UserHelperClass helperClass = new UserHelperClass(name, email, mobile, password);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(helperClass.getMobile()).exists()) {
                            progressBar.setVisibility(View.GONE);
                            regMobile.setError("This mobile number already registered");
                        } else {
                            regMobile.setErrorEnabled(false);
                            progressBar.setVisibility(View.GONE);
                            authUser();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SignUp.this, "Please try again", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
    }

    //Validations
    private Boolean validateName() {
        String val = regName.getEditText().getText().toString();

        if (val.isEmpty()) {
            regName.setError("Name cannot be empty");
            return false;
        } else if (val.length() > 20) {
            regName.setError("Use 0-20 characters for name");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Email cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email pattern");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateMobile() {
        String val = regMobile.getEditText().getText().toString();

        if (val.isEmpty()) {
            regMobile.setError("Mobile Number cannot be empty");
            return false;
        } else if (!(val.length() == 10)) {
            regMobile.setError("Invalid mobile number");
            return false;
        } else {
            regMobile.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
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
            regPassword.setError("Password cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak. Use uppercase, lowercase letters, numbers and special characters");
            return false;
        } else if (val.length() < 6) {
            regPassword.setError("Use at least 6 characters");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    //Verify Activity
    private void saveUserDetailsToPrevalent() {

        //Get all the values
        String name = regName.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String mobile = regMobile.getEditText().getText().toString();
        String password = regPassword.getEditText().getText().toString();

        //Save user details to Prevalent2
        Paper.book().write(Prevalent2.NameKey, name);
        Paper.book().write(Prevalent2.UserEmailKey, email);
        Paper.book().write(Prevalent2.UserMobileKey, mobile);
        Paper.book().write(Prevalent2.UserPasswordKey, password);
        Paper.book().write(Prevalent.UserNameKey, name);
        
        startActivity(new Intent(getApplicationContext(), SignUpEmailVerify.class));

    }

    //Set authentication
    private void authUser() {

        try {
            progressBar.setVisibility(View.VISIBLE);

            Paper.init(this);
            String email = regEmail.getEditText().getText().toString();
            String password = regPassword.getEditText().getText().toString();

            Paper.book().write(Prevalent2.UserEmailKey, email);
            Paper.book().write(Prevalent2.UserPasswordKey, password);

            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUp.this, "Your email is valid for register", Toast.LENGTH_LONG).show();
                        regEmail.setErrorEnabled(false);
                        deleteAccount();
                        saveUserDetailsToPrevalent();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        regEmail.setError("This email already registered");

                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }

    }

    //Delete Authenticated User By Email And Password
    private void deleteAccount() {

        try {
            //Get all the values
            String email = regEmail.getEditText().getText().toString();
            String password = regPassword.getEditText().getText().toString();

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = authResult.getUser();
                    final String userEmail = user.getEmail().toString();

                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUp.this, "Please verify your email: "+userEmail, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUp.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }


    }

    public void next(View view) {
        startActivity(new Intent(getApplicationContext(), SignUpEmailVerify.class));
    }
}
