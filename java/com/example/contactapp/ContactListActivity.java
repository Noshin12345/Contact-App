package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {
    private Button btnAddNew, btnBack;
    private TextView tvTitleContactList;
    private ArrayList<ContactList> contacts;
    private ContactListAdapter adapter;
    private ListView lvContactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list3);
        btnAddNew = findViewById(R.id.btnAddNew);
        btnBack = findViewById(R.id.btnBack);
        tvTitleContactList = findViewById(R.id.tvTitleContactList);
        lvContactList = findViewById(R.id.lvContactList);

//        Intent i =this.getIntent();
//        String uid= i.getStringExtra("UID");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactListActivity.this, login.class);
                startActivity(i);
            }
        });

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactListActivity.this, ContactForm.class);
                startActivity(i);
            }
        });
        contacts = new ArrayList<>();
        adapter = new ContactListAdapter(this, contacts);
        lvContactList.setAdapter(adapter);


        lvContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                Intent i = new Intent(ContactListActivity.this, ContactForm.class);
                String value = contacts.get(position).name + "---" + contacts.get(position).email + "---" + contacts.get(position).phoneHome + "---" + contacts.get(position).phoneOffice + "---" + contacts.get(position).imgText;
                System.out.println(value);
                System.out.println(contacts.get(position).imgText);
                System.out.println(contacts.get(position).uid);
                System.out.println(position);
                i.putExtra("UID", contacts.get(position).uid);
                i.putExtra("Value", value);
                startActivity(i);
            }
        });
        loadData();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    public void loadData(){
        contacts.clear();
        ContactListDB db=new ContactListDB(this);
//        String query= "SELECT * FROM ContactList";
        Cursor rows =db.selectContactList("SELECT * FROM ContactList");
//        Cursor rows = db.getWritableDatabase().rawQuery(query, null);
        if(rows.getCount()>0){
            while(rows.moveToNext()){
                String uid = rows.getString(0);
                String name = rows.getString(1);
                String email = rows.getString(2);
                String phoneHome = rows.getString(3);
                String phoneOffice = rows.getString(4);
                String imageText = rows.getString(5);

                ContactList ls = new ContactList(uid, name, email, phoneHome, phoneOffice, imageText);
                contacts.add(ls);
            }
            rows.close();
        }
        else{
            return;
        }
        db.close();
        adapter.notifyDataSetChanged();
    }
}