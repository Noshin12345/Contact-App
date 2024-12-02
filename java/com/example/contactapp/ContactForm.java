package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class ContactForm extends AppCompatActivity {
    private static final int REQUEST_CODE = 100;
    private TextView tvTitleContactForm, tvName, tvEmail, tvPhoneHome, tvPhoneOffice;
    private EditText etName, etEmail, etPhoneHome, etPhoneOffice;
    private Button btnSave, btnCancel, btnDelete;
    private ImageView ivImage;
    private String uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        Intent i = getIntent();

        tvTitleContactForm = findViewById(R.id.tvTitleContactForm);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        ivImage = findViewById(R.id.ivImage);
        tvPhoneHome = findViewById(R.id.tvPhoneHome);
        tvPhoneOffice = findViewById(R.id.tvPhoneOffice);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneHome = findViewById(R.id.etPhoneHome);
        etPhoneOffice = findViewById(R.id.etPhoneOffice);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);

        if (i.hasExtra("Value")) {
            String value = i.getStringExtra("Value");
            String[] values = value.split("---");

            etName.setText(values[0]);
            etEmail.setText(values[1]);
            etPhoneHome.setText(values[2]);
            etPhoneOffice.setText(values[3]);
            Bitmap bmp = decodeImageFromString(values[4]);
            ivImage.setImageBitmap(bmp);
            btnSave.setText("Update");
        }

        if (i.hasExtra("UID")) {
            uid = i.getStringExtra("UID");
        }

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validation();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid.isEmpty()) {
                    Toast.makeText(ContactForm.this, "No contact to delete", Toast.LENGTH_SHORT).show();
                } else {
                    ContactListDB DB = new ContactListDB(ContactForm.this);
                    DB.deleteContactList(uid);
                    DB.close();
                    Toast.makeText(ContactForm.this, "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void validation() {
        Bitmap imgBit = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneHome = etPhoneHome.getText().toString().trim();
        String phoneOffice = etPhoneOffice.getText().toString().trim();
        String imgText = encodeImageToBase64(imgBit);

        StringBuilder errorBuilder = new StringBuilder();

        if (name.isEmpty()) {
            errorBuilder.append("Name cannot be empty\n");
        }

        if (email.isEmpty()) {
            errorBuilder.append("User ID cannot be empty\n");
        }

        if (phoneHome.isEmpty()) {
            errorBuilder.append("You must enter your phone number.\n");
        }

        if (name.length() > 50) {
            errorBuilder.append("Name is too long.\n");
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorBuilder.append("Invalid email address.\n");
        }

        if (!isValidPhoneNumber(phoneHome)) {
            errorBuilder.append("Invalid phone number for Home.\n");
        }

        if (!isValidPhoneNumber(phoneOffice)) {
            errorBuilder.append("Invalid phone number for Office.\n");
        }

        String error = errorBuilder.toString().trim();

        if (!error.isEmpty()) {
            Toast.makeText(ContactForm.this, error, Toast.LENGTH_LONG).show();
        } else {
            ContactListDB DB = new ContactListDB(ContactForm.this);
            if (uid.isEmpty()) {
                uid = name + System.currentTimeMillis();
                DB.insertContactList(uid, name, email, phoneHome, phoneOffice, imgText);
                Toast.makeText(ContactForm.this, "Saved successfully.", Toast.LENGTH_LONG).show();
            } else {
                DB.updateContactList(uid, name, email, phoneHome, phoneOffice, imgText);
                Toast.makeText(ContactForm.this, "Updated", Toast.LENGTH_LONG).show();
            }
            DB.close();

            Intent i = new Intent(ContactForm.this, ContactList.class);
            startActivity(i);
            finish();
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return Patterns.PHONE.matcher(phoneNumber).matches() && (phoneNumber.startsWith("+880") || phoneNumber.startsWith("880") || phoneNumber.startsWith("01")) && (phoneNumber.length() == 13 || phoneNumber.length() == 14 || phoneNumber.length() == 11);
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public Bitmap decodeImageFromString(String encodedString) {
        byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE && data != null) {
                Bitmap img = (Bitmap) (data.getExtras().get("data"));
                img = Bitmap.createScaledBitmap(img, 100, 100, true);
                ivImage.setImageBitmap(img);
            }
        }
    }
}
