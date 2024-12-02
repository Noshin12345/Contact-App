package com.example.contactapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

import android.os.Bundle;

public class ContactListAdapter extends ArrayAdapter<ContactList> {
    private final Context context;
    private final ArrayList<ContactList> values;
    public ContactListAdapter(@NonNull Context context, @NonNull ArrayList<ContactList> items){
        super(context, -1, items);
        this.context = context;
        this.values = items;
    }
    @Override
    public View getView(int position, View converView, ViewGroup parent){
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inf.inflate(R.layout.activity_contact_list_row, parent, false);

        TextView name = rowView.findViewById(R.id.tvName);
        TextView tvHome = rowView.findViewById(R.id.tvHome);
        TextView tvOffice = rowView.findViewById(R.id.tvOffice);
        ContactList c = values.get(position);
        name.setText(c.name);
        tvHome.setText(c.phoneHome);
        tvOffice.setText(c.phoneOffice);

        return rowView;
    }

}