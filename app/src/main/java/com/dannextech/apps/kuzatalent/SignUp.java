package com.dannextech.apps.kuzatalent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;


public class SignUp extends AppCompatActivity {

    private static final String TAG = "SIGN_UP_LOG";

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    FirebaseAuth mAuth;

    EditText etName, etEmail, etPhone, etPassword, etPasswordConfirm, etWebsite, etLocation;
    Spinner spCategory;
    Button btnSignUp;

    String category = "Youth";

    @Override
    protected void onStart() {
        super.onStart();

        //Check if the user is signed in and update accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            Log.e(TAG, "onStart: user "+currentUser.getUid()+" with email address "+currentUser.getEmail()+" is already signed in");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

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
                createUser(etEmail.getText().toString(),etPassword.getText().toString());
            }
        });
    }

    private FirebaseUser createUser(String email, String password){
        final FirebaseUser[] user = new FirebaseUser[1];
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.e(TAG, "onComplete: user created successfully");
                    user[0] = mAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                    uploadUserDetails(user[0],etName.getText().toString(), etPhone.getText().toString(), category, etWebsite.getText().toString(), etLocation.getText().toString());
                }else {
                    Log.e(TAG, "onComplete: user creation failed",task.getException());
                }
            }
        });
        return user[0];
    }
    private void uploadUserDetails(FirebaseUser user, String name, String phone, String category, String website, String location) {
        //creating a reference to the folder users where the details will be saved
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Users/"+user.getUid());

        //creating references to where specific details will be saved
        DatabaseReference nameRef = databaseReference.child("Name");
        DatabaseReference emailRef = databaseReference.child("Email");
        DatabaseReference phoneRef = databaseReference.child("Phone");
        DatabaseReference categoryRef = databaseReference.child("Category");
        DatabaseReference websiteRef = databaseReference.child("Website");
        DatabaseReference locationRef = databaseReference.child("Location");

        nameRef.setValue(name);
        emailRef.setValue(user.getEmail());
        phoneRef.setValue(phone);
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
