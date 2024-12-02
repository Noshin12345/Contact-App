package com.example.contactapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class login extends AppCompatActivity {
    private EditText etUserId, etPassword;
    private CheckBox RemUser, Rempass;
    private Button buttonsignup, buttonExit, buttongo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserId= findViewById(R.id.etUserId);
        etPassword = findViewById(R.id.etPassword);
        RemUser = findViewById(R.id.RemUser);
        Rempass = findViewById(R.id.Rempass);

        // Call prefillLoginFields method
        Login();

        findViewById(R.id.buttonExit).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.buttonsignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, Signup.class);
                startActivity(i);
                finish();
            }
        });
        findViewById(R.id.buttongo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                processlogin();
            }
        });
    }

    private void Login() {
        // Retrieve user information from SharedPreferences
        SharedPreferences sp = this.getSharedPreferences("user_info", MODE_PRIVATE);
        String userId = sp.getString("USER_ID", "");
        String userPassword = sp.getString("USER_PASSWORD", "");
        boolean remUserId = sp.getBoolean("REM_USER", false);
        boolean remPass = sp.getBoolean("REM_PASS", false);

        // Pre-fill login fields if the user has chosen to remember them
        if (remUserId) {
            etUserId.setText(userId);
            RemUser.setChecked(true);
        }

        if (remPass) {
            etPassword.setText(userPassword);
            Rempass.setChecked(true);
        }
    }

    void processlogin() {
        String userId = etUserId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String errMsg = "";

        if (userId.isEmpty() || password.isEmpty()) {
            errMsg += "User ID or Password cannot be empty";
        }

        if (!errMsg.isEmpty()) {
            showErrorDialog(errMsg);
            return;
        }

        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        //String storedUserId: This declares a variable named storedUserId of type String.
        // This variable will hold the user ID retrieved from shared preferences.
        String storedUserId = sp.getString("USER_ID", "");
        String storedPassword = sp.getString("USER_PASSWORD", "");
        boolean remUserId = sp.getBoolean("REM_USER", false);
        boolean remPass = sp.getBoolean("REM_PASS", false);

        if (userId.equals(storedUserId) && password.equals(storedPassword)) {
            Intent intent = new Intent(login.this, ContactListActivity.class);
            startActivity(intent);
            finish();
        } else {
            showErrorDialog("Invalid credentials. Please try again.");
        }

        // Save the user ID and password to shared preferences for future login
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_ID", userId);
        editor.putString("USER_PASSWORD", password);
        editor.putBoolean("REM_USER", RemUser.isChecked());
        editor.putBoolean("REM_PASS", Rempass.isChecked());
        editor.apply();
    }

    private void showErrorDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage);
        builder.setTitle("Error");
        builder.setCancelable(true);

        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog Alert = builder.create();
        Alert.show();
    }
}

