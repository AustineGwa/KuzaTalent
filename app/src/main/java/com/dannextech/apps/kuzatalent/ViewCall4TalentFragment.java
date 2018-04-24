package com.dannextech.apps.kuzatalent;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCall4TalentFragment extends Fragment {

    ProgressDialog progressDialog;

    List<Call4TalentModel> list = new ArrayList<>();

    RecyclerView rvViewCall4Talent;
    RecyclerView.Adapter adapter;

    public DatabaseReference mReference;


    public ViewCall4TalentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_view_call4_talent, container, false);

        rvViewCall4Talent = (RecyclerView) view.findViewById(R.id.rvCall4Talent);

        mReference = FirebaseDatabase.getInstance().getReference().child("Call4Talent");

        mReference.keepSynced(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        rvViewCall4Talent.setHasFixedSize(true);
        rvViewCall4Talent.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(view.getContext());

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Call4TalentModel call4TalentModel = dataSnapshot.getValue(Call4TalentModel.class);
                    call4TalentModel.setUrl(dataSnapshot.getRef().toString());
                    list.add(call4TalentModel);
                    Log.e("Dannex", "onDataChange: "+call4TalentModel.getUrl());
                }

                adapter = new Call4TalentAdapter(view.getContext(), list);

                rvViewCall4Talent.setAdapter(adapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });



        return view;
    }

}
