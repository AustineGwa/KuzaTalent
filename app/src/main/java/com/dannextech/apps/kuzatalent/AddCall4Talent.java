package com.dannextech.apps.kuzatalent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCall4Talent extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    TextView tvOrg, tvPhone, tvEmail, tvwebsite;
    Spinner spTalent;
    EditText etDesc;
    Button btnSubmit;

    String talent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_call4_talent);

        tvOrg = (TextView) findViewById(R.id.tvOrganization);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvwebsite = (TextView) findViewById(R.id.tvWebsite);

        spTalent = (Spinner) findViewById(R.id.spTalent);

        etDesc = (EditText) findViewById(R.id.etDescription);

        btnSubmit = (Button) findViewById(R.id.btnSubmitCall4Talent);

        spTalent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                talent = spTalent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails(tvOrg.getText().toString(),tvPhone.getText().toString(),tvEmail.getText().toString(),tvwebsite.getText().toString(),etDesc.getText().toString(),talent);
            }
        });
    }

    private void saveDetails(String org, String phone, String email, String website, String desc, String talent) {
        //creating a reference to the folder users where the details will be saved
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Call4Talent").push();

        DatabaseReference organizationRef = databaseReference.child("organization");
        DatabaseReference phoneRef = databaseReference.child("phone");
        DatabaseReference emailRef = databaseReference.child("email");
        DatabaseReference websiteRef = databaseReference.child("website");
        DatabaseReference talentRef = databaseReference.child("talent");
        DatabaseReference descriptionRef = databaseReference.child("description");
        DatabaseReference datePostedRef = databaseReference.child("datePosted");

        organizationRef.setValue(org);
        phoneRef.setValue(phone);
        emailRef.setValue(email);
        websiteRef.setValue(website);
        talentRef.setValue(talent);
        datePostedRef.setValue("19-April-2018");
        descriptionRef.setValue(desc, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(getApplicationContext(),"Saved Successful",Toast.LENGTH_SHORT);
            }
        });
    }
}
