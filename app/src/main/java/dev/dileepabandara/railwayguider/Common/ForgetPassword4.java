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

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import dev.dileepabandara.railwayguider.R;

public class ForgetPassword4 extends AppCompatActivity {

    ProgressBar progressBarResetPassOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password4);

        progressBarResetPassOK = findViewById(R.id.progressBarResetPassOK);

        Button btnUpdatePassComplete = findViewById(R.id.btnUpdatePassComplete);
        btnUpdatePassComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarResetPassOK.setVisibility(View.VISIBLE);
                startActivity(new Intent(getApplicationContext(),Login.class));
                progressBarResetPassOK.setVisibility(View.GONE);
                finishAffinity();
            }
        });

    }
}
