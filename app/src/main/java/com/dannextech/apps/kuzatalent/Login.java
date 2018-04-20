package com.dannextech.apps.kuzatalent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private static final String TAG = "LOGIN_LOG";

    EditText etEmail,etPassword;
    Button btSignIn;

    FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference databaseRef;

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
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        etEmail = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPasswordLogin);

        btSignIn = (Button) findViewById(R.id.btnLogin);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(etEmail.getText().toString(),etPassword.getText().toString()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.e(TAG, "onComplete: user created successfully");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),AddCall4Talent.class));
                        }else {
                            Log.e(TAG, "onComplete: user creation failed",task.getException());
                        }
                    }
                });
            }
        });
    }
}
