package com.example.contactapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Signup extends AppCompatActivity {
    private EditText etusername, etemail, etphone, etuserid, etpassword, etReenterPassword;
    private CheckBox RemUser, rempass;
    private Button buttonlogin, buttonExit, buttongo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etusername = findViewById(R.id.etusername);
        etemail = findViewById(R.id.etemail);
        etphone = findViewById(R.id.etphone);
        etuserid = findViewById(R.id.etuserid);
        etpassword = findViewById(R.id.etpassword);
        etReenterPassword = findViewById(R.id.etReenterPassword);
        decideNavigation();
        RemUser = findViewById(R.id.remUser);
        rempass = findViewById(R.id.rempass);

        buttonExit = findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        buttonlogin = findViewById(R.id.buttonlogin);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Signup.this, login.class);
                startActivity(i);
                finish();
            }
        });

        buttongo = findViewById(R.id.buttongo);
        buttongo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                processsignin();
            }
        });

        // Call prefillLoginFields method
        LoginFields();
    }

    private void LoginFields() {
        // Retrieve user information from SharedPreferences
        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        String userId = sp.getString("USER_ID", "");
        String password = sp.getString("USER_PASSWORD", "");
        boolean RemUserChecked = sp.getBoolean("REM_USER", false);
        boolean RemPassChecked = sp.getBoolean("REM_PASS", false);

        // Pre-fill login fields if the user has chosen to remember them
        if (RemUserChecked) {
            etuserid.setText(userId);
            RemUser.setChecked(true);
        }

        if (RemPassChecked) {
            etpassword.setText(password);
            etReenterPassword.setText(password);
            rempass.setChecked(true);
        }
    }

    private void processsignin() {
        String name = etusername.getText().toString().trim();
        String email = etemail.getText().toString().trim();
        String phone = etphone.getText().toString().trim();
        String userId = etuserid.getText().toString().trim();
        String password = etpassword.getText().toString().trim();
        String reEnterPass = etReenterPassword.getText().toString().trim();
        String errMsg = "";

        if (name.isEmpty()) {
            errMsg += "Name cannot be empty\n";
        }

        if (userId.isEmpty()) {
            errMsg += "User ID cannot be empty\n";
        }

        if (userId.length() < 4 || userId.length() > 8) {
            errMsg += "Invalid User Id, ";
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errMsg += "Invalid Email\n";
        }

        if (phone.isEmpty()) {
            errMsg += "Phone cannot be empty\n";
        }
        if ((phone.startsWith("+880") && phone.length() == 14)
                || (phone.startsWith("880") && phone.length() == 13)
                || (phone.startsWith("01") && phone.length() == 11)) {

        } else {
            errMsg += "Invalid Phone Number, ";
        }
        if (password.length() == 4 && password.equals(reEnterPass)) {
            // Password is valid
        } else {
            errMsg += "Invalid password, ";
        }

        if (password.isEmpty()) {
            errMsg += "Password cannot be empty\n";
        }

        if (!password.equals(reEnterPass)) {
            errMsg += "Passwords do not match\n";
        }

        if (!errMsg.isEmpty()) {
            showErrorDialog(errMsg);
            return;
        }

        // All inputs are valid, proceed with saving data and moving to MainActivity

        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("USER_NAME", name);
        e.putString("USER_ID", userId);
        e.putString("USER_EMAIL", email);
        e.putString("USER_PHONE", phone);
        e.putString("USER_PASSWORD", password);
        e.putBoolean("REM_PASS", rempass.isChecked());
        e.putBoolean("REM_USER", RemUser.isChecked());
        e.apply();//stored string value in shared prefernce

        Intent i = new Intent(Signup.this, login.class);
        startActivity(i);
        finish();
    }





    private void showErrorDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage);
        builder.setTitle("Error");
        builder.setCancelable(true);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void decideNavigation() {
        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        String userName = sp.getString("USER_NAME", "NOT-CREATED");
        if (!userName.equals("NOT-CREATED")) {
            Intent i = new Intent(Signup.this, login.class);
            startActivity(i);
            finish();
        }
    }
}



