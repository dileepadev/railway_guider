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
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import dev.dileepabandara.railwayguider.Common.Login;
import dev.dileepabandara.railwayguider.Prevalent.Prevalent;
import dev.dileepabandara.railwayguider.Prevalent.Prevalent2;
import dev.dileepabandara.railwayguider.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import io.paperdb.Paper;

public class UserAccount extends AppCompatActivity {

    TextInputLayout txtName, txtEmail, txtMobile;
    TextView lblName, lblMobile;
    ImageView userImage;
    ProgressBar progressBar_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_account);

        //Hooks
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobile = findViewById(R.id.txtMobile);
        lblName = findViewById(R.id.lblName);
        lblMobile = findViewById(R.id.lblMobile);
        userImage = findViewById(R.id.userImage);
        progressBar_account = findViewById(R.id.progressBar_account);

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

        //Show all data
        setUserDetails();

        try {
            //Paper
            Paper.init(this);
            String paperUserImage = Paper.book().read(Prevalent2.UserImageKey);
            String profilePic = String.valueOf(paperUserImage);
            //Toast.makeText(this, ""+profilePic, Toast.LENGTH_SHORT).show();

            if (profilePic.equals("null")) {
                userImage.setImageResource(R.drawable.icon_user_profile_pic);
            } else {
                userImage.setImageURI(Uri.parse(profilePic));
            }

        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
    }


    //Send user details to UserAccount
    private void setUserDetails() {

        Paper.init(this);
        final String user_mobile = Paper.book().read(Prevalent.UserMobileKey);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("mobile").equalTo(user_mobile);

        progressBar_account.setVisibility(View.VISIBLE);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(user_mobile).exists()) {

                    //Get values from database
                    String passwordFromDB = dataSnapshot.child(user_mobile).child("password").getValue(String.class);
                    String nameFromDB = dataSnapshot.child(user_mobile).child("name").getValue(String.class);
                    String mobileFromDB = dataSnapshot.child(user_mobile).child("mobile").getValue(String.class);
                    String emailFromDB = dataSnapshot.child(user_mobile).child("email").getValue(String.class);

                    try {

                        txtName.getEditText().setText(nameFromDB);
                        txtEmail.getEditText().setText(emailFromDB);
                        txtMobile.getEditText().setText(mobileFromDB);
                        lblName.setText(nameFromDB);
                        lblMobile.setText(mobileFromDB);
                        progressBar_account.setVisibility(View.GONE);

                    } catch (Exception e) {
                        Toast.makeText(UserAccount.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserAccount.this, "E2 ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onClickUpdate(View view) {

        if (!validateName()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm before update");
            builder.setMessage("Are you sure to update?");

            builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    updateProfile();
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

    private void updateProfile() {

        Paper.init(this);
        final String user_mobile = Paper.book().read(Prevalent.UserMobileKey);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user_mobile);
        progressBar_account.setVisibility(View.VISIBLE);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    dataSnapshot.getRef().child("name").setValue(txtName.getEditText().getText().toString());
                    dataSnapshot.getRef().child("email").setValue(txtEmail.getEditText().getText().toString());
                    dataSnapshot.getRef().child("mobile").setValue(txtMobile.getEditText().getText().toString());

                    progressBar_account.setVisibility(View.GONE);
                    Toast.makeText(UserAccount.this, "Name Updated", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent2);
                    finishAffinity();

                } catch (Exception e) {
                    Toast.makeText(UserAccount.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Validations
    private Boolean validateName() {
        String val = txtName.getEditText().getText().toString();

        if (val.isEmpty()) {
            txtName.setError("Name cannot be empty");
            return false;
        } else if (val.length() > 20) {
            txtName.setError("Use 0-20 characters for name");
            return false;
        } else {
            txtName.setError(null);
            txtName.setErrorEnabled(false);
            return true;
        }
    }


    public void onClickSelectImage(View view) {

        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i, 12);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK && data != null) {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                userImage.setImageURI(resultUri);

                String uriPaper = String.valueOf(resultUri);
                Paper.init(this);
                Paper.book().write(Prevalent2.UserImageKey, uriPaper);
                Toast.makeText(this, "Profile photo updated", Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, ""+resultUri, Toast.LENGTH_SHORT).show();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void onClickDeleteAccount(View view) {

        final String name = Paper.book().read(Prevalent.UserNameKey);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Hi " +name+ "! Are you sure to delete your account?");
        builder.setMessage("We still develop our system and sorry about the mistakes of the process. We hope to give amazing service by our next updates. Thank you for use Railway Guider service.");

        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), UserAccountDelete.class));
                dialog.dismiss();
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
