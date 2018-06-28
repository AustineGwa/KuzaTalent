package com.dannextech.apps.kuzatalent;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCall4TalentFragment extends Fragment {

    private static final String TAG = "CALL_4";

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    TextView tvOrg, tvPhone, tvEmail, tvwebsite,tvLocation;
    Spinner spTalent;
    EditText etDesc;
    Button btnSubmit;
    ProgressDialog progressDialog;

    String talent,email,org,web,phone,location;

    public AddCall4TalentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_call4_talent, container, false);

        if (isNetworkAvailable())
            retrieveUserDetails(FirebaseAuth.getInstance().getUid());
        else
            Snackbar.make(view,"Failed to load: Check your internet Connection",Snackbar.LENGTH_SHORT).show();
        Log.e(TAG, "onCreate: id = "+FirebaseAuth.getInstance().getUid() );
        tvOrg = (TextView) view.findViewById(R.id.tvOrganization);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvwebsite = (TextView) view.findViewById(R.id.tvWebsite);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);

        spTalent = (Spinner) view.findViewById(R.id.spTalent);

        etDesc = (EditText) view.findViewById(R.id.etDescription);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmitCall4Talent);

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
                if (isNetworkAvailable())
                    saveDetails(v,tvOrg.getText().toString(),tvPhone.getText().toString(),tvEmail.getText().toString(),tvwebsite.getText().toString(),etDesc.getText().toString(),talent,tvLocation.getText().toString());
                else
                    Snackbar.make(v,"Failed: Check your internet Connection",Snackbar.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    private void saveDetails(final View view, String org, String phone, String email, String website, String desc, String talent, String location) {
        showProgressDialog();
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

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

        organizationRef.setValue(org);
        phoneRef.setValue(phone);
        emailRef.setValue(email);
        websiteRef.setValue(website);
        talentRef.setValue(talent);
        datePostedRef.setValue(format.format(date));
        locationRef.setValue(location);
        descriptionRef.setValue(desc, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideProgressDialog();
                Toast.makeText(view.getContext(),"Saved Successful",Toast.LENGTH_SHORT);
                Fragment fragment = new ViewCall4TalentFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.flOrganizationFragment,fragment);
                fragmentTransaction.commitAllowingStateLoss();
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


    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(getContext(),"Saving Call for Talent","Please Wait",true);
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
