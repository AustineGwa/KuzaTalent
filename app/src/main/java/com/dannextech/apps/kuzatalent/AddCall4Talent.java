package com.dannextech.apps.kuzatalent;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCall4Talent extends AppCompatActivity {

    private static final String TAG = "CALL_4";

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    TextView tvOrg, tvPhone, tvEmail, tvwebsite,tvLocation;
    Spinner spTalent;
    EditText etDesc;
    Button btnSubmit;

    String talent,email,org,web,phone,location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_call4_talent);

        retrieveUserDetails(FirebaseAuth.getInstance().getUid());
        Log.e(TAG, "onCreate: id = "+FirebaseAuth.getInstance().getUid() );
        tvOrg = (TextView) findViewById(R.id.tvOrganization);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvwebsite = (TextView) findViewById(R.id.tvWebsite);
        tvLocation = (TextView) findViewById(R.id.tvLocation);

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
                saveDetails(tvOrg.getText().toString(),tvPhone.getText().toString(),tvEmail.getText().toString(),tvwebsite.getText().toString(),etDesc.getText().toString(),talent,tvLocation.getText().toString());
            }
        });
    }

    private void saveDetails(String org, String phone, String email, String website, String desc, String talent,String location) {
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
        DatabaseReference locationRef = databaseReference.child("location");

        organizationRef.setValue(org);
        phoneRef.setValue(phone);
        emailRef.setValue(email);
        websiteRef.setValue(website);
        talentRef.setValue(talent);
        datePostedRef.setValue("19-April-2018");
        locationRef.setValue(location);
        descriptionRef.setValue(desc, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(AddCall4Talent.this,"Saved Successful",Toast.LENGTH_SHORT);
            }
        });
    }

    private void retrieveUserDetails(String uid) {

        //creating a reference to the folder users where the details will be saved
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Users/"+uid);
        DatabaseReference orgRef = databaseReference.getRef().child("Name");
        DatabaseReference phoneRef = databaseReference.getRef().child("Phone");
        DatabaseReference emailRef = databaseReference.getRef().child("Email");
        DatabaseReference websiteRef = databaseReference.getRef().child("Website");
        DatabaseReference locationRef = databaseReference.getRef().child("Location");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: details are "+dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        orgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                org = dataSnapshot.getValue().toString();
                Log.e(TAG, "onDataChange: "+org);
                setOrganization(org);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        phoneRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                phone = dataSnapshot.getValue().toString();
                Log.e(TAG, "onDataChange: "+phone);
                setPhone(phone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        emailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email = dataSnapshot.getValue().toString();
                Log.e(TAG, "onDataChange: "+email);
                setEmail(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        websiteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                web = dataSnapshot.getValue().toString();
                Log.e(TAG, "onDataChange: "+web);
                setWebsite(web);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        locationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                location = dataSnapshot.getValue().toString();
                Log.e(TAG, "onDataChange: "+location);
                setLocation(location);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setOrganization(String org) {
        tvOrg.setText(org);
    }
    private void setPhone(String phone) {
        tvPhone.setText(phone);
    }
    private void setWebsite(String website) {
        tvwebsite.setText(website);
    }
    private void setEmail(String email) {
        tvEmail.setText(email);
    }
    private void setLocation(String location){ tvLocation.setText(location);}
}
