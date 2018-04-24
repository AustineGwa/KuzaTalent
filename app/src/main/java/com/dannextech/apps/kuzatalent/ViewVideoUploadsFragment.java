package com.dannextech.apps.kuzatalent;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
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
public class ViewVideoUploadsFragment extends Fragment {

    ProgressDialog progressDialog;

    List<UploadVideoModel> list = new ArrayList<UploadVideoModel>();

    RecyclerView rvVideos;
    RecyclerView.Adapter adapter;

    public DatabaseReference mReference,vidReference;


    public ViewVideoUploadsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_view_video_uploads, container, false);

        rvVideos = (RecyclerView) view.findViewById(R.id.rvVideo);

        mReference = FirebaseDatabase.getInstance().getReference().child("Videos");

        mReference.keepSynced(true);

        GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(),2);
        //layoutManager.setReverseLayout(true);
        //layoutManager.setStackFromEnd(true);

        rvVideos.setHasFixedSize(true);
        rvVideos.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(view.getContext());

        progressDialog.setMessage("Loading Videos from Firebase Database");

        progressDialog.show();



        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.e("Dannex", "onDataChange: layer 2 "+dataSnapshot.getRef());


                    vidReference = FirebaseDatabase.getInstance().getReferenceFromUrl(dataSnapshot.getRef().toString());
                    vidReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshots) {
                            Log.e("Dannex", "onDataChange layer 3 main: "+vidReference);
                            for (DataSnapshot snapshot1 : dataSnapshots.getChildren()){
                                UploadVideoModel uploadVideoModel = snapshot1.getValue(UploadVideoModel.class);
                                uploadVideoModel.setUrl(snapshot1.getRef().toString());
                                list.add(uploadVideoModel);
                                Log.e("Dannex", "onDataChange: layer 5 "+uploadVideoModel.getTalent());
                            }
                            Log.e("Dannex", "onDataChange: all of these"+list.size() );
                            adapter = new UploadVideoAdapter(getContext(), list);

                            rvVideos.setAdapter(adapter);

                            progressDialog.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Dannex", "onDataChange: all of these"+list.size() );
        adapter = new UploadVideoAdapter(getContext(), list);

        rvVideos.setAdapter(adapter);

        progressDialog.dismiss();
    }
}
