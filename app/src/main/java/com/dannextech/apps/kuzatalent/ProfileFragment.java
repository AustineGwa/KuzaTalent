package com.dannextech.apps.kuzatalent;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "CALL_4";

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    TextView tvName, tvCat, tvEmail, tvPhone, tvLoc, tvWebsite;
    ProgressDialog progressDialog;

    String category,email, name,web,phone,location;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (isNetworkAvailable())
            retrieveUserDetails(FirebaseAuth.getInstance().getUid());
        else
            Snackbar.make(view,"Failed to load: Check your internet Connection",Snackbar.LENGTH_SHORT).show();
        tvCat = (TextView) view.findViewById(R.id.tvCategoryProfile);
        tvName = (TextView) view.findViewById(R.id.tvNameProfile);
        tvEmail = (TextView) view.findViewById(R.id.tvEmailProfile);
        tvPhone = (TextView) view.findViewById(R.id.tvPhoneProfile);
        tvLoc = (TextView) view.findViewById(R.id.tvLocationProfile);
        tvWebsite = (TextView) view.findViewById(R.id.tvWebsiteProfile);

        return view;
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
        DatabaseReference categoryRef = databaseReference.getRef().child("Category");

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
                name = dataSnapshot.getValue().toString();
                Log.e(TAG, "onDataChange: "+ name);
                setOrganization(name);
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

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                category = dataSnapshot.getValue().toString();
                Log.e(TAG, "onDataChange: "+category );
                setCategory(category);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setCategory(String category) {
        tvCat.setText(category);
    }

    private void setOrganization(String org) {
        tvName.setText(org);
    }
    private void setPhone(String phone) {
        tvPhone.setText(phone);
    }
    private void setWebsite(String website) {
            tvWebsite.setText(website);
    }
    private void setEmail(String email) {
        tvEmail.setText(email);
    }
    private void setLocation(String location){
            tvLoc.setText(location);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
