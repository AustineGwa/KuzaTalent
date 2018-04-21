package com.dannextech.apps.kuzatalent;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewCall4Talent extends AppCompatActivity {


    ProgressDialog progressDialog;

    List<Call4TalentModel> list = new ArrayList<>();

    RecyclerView rvViewCall4Talent;
    RecyclerView.Adapter adapter;

    public DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_call4_talent);

        rvViewCall4Talent = (RecyclerView) findViewById(R.id.rvCall4Talent);

        mReference = FirebaseDatabase.getInstance().getReference().child("Call4Talent");

        mReference.keepSynced(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        rvViewCall4Talent.setHasFixedSize(true);
        rvViewCall4Talent.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Call4TalentModel call4TalentModel = dataSnapshot.getValue(Call4TalentModel.class);
                    call4TalentModel.setUrl(dataSnapshot.getRef().toString());
                    list.add(call4TalentModel);
                }

                adapter = new Call4TalentAdapter(ViewCall4Talent.this, list);

                rvViewCall4Talent.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}
