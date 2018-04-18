package com.dannextech.apps.kuzatalent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;


public class SignUp extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    EditText etName, etEmail, etPhone, etPassword, etPasswordConfirm, etWebsite, etLocation;
    Spinner spCategory;
    Button btnSignUp;

    String category = "Youth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etName = (EditText) findViewById(R.id.etNames);
        etEmail = (EditText) findViewById(R.id.etEmailAddress);
        etPhone = (EditText) findViewById(R.id.etPhoneNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordConfirm = (EditText) findViewById(R.id.etPasswordConfirm);
        etWebsite = (EditText) findViewById(R.id.etWebsite);
        etLocation = (EditText) findViewById(R.id.etLocation);

        spCategory = (Spinner) findViewById(R.id.spCategory);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        List<String> list = new ArrayList<String>();
        list.add("Select Category");
        list.add("Youth");
        list.add("Organization");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUp.this,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCategory.setAdapter(adapter);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spCategory.getItemAtPosition(position).toString();

                if (category.equals("Organization")){
                    etWebsite.setVisibility(View.VISIBLE);
                    etLocation.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUserDetails(etName.getText().toString(), etEmail.getText().toString(), etPhone.getText().toString(), etPassword.getText().toString(), category, etWebsite.getText().toString(), etLocation.getText().toString());
            }
        });
    }

    private void uploadUserDetails(String name, String email, String phone, String password, String category, String website, String location) {
        //creating a reference to the folder users where the details will be saved
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Users").push();

        //creating references to where specific details will be saved
        DatabaseReference nameRef = databaseReference.child("Name");
        DatabaseReference emailRef = databaseReference.child("Email");
        DatabaseReference phoneRef = databaseReference.child("Phone");
        DatabaseReference passwordRef = databaseReference.child("Password");
        DatabaseReference categoryRef = databaseReference.child("Category");
        DatabaseReference websiteRef = databaseReference.child("Website");
        DatabaseReference locationRef = databaseReference.child("Location");

        nameRef.setValue(name);
        emailRef.setValue(email);
        phoneRef.setValue(phone);
        passwordRef.setValue(password);
        categoryRef.setValue(category);
        websiteRef.setValue(website);
        locationRef.setValue(location, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(getApplicationContext(),"User Saved Successfully",Toast.LENGTH_SHORT).show();
            }
        });



}
}
