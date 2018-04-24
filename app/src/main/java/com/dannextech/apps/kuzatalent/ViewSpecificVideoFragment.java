package com.dannextech.apps.kuzatalent;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSpecificVideoFragment extends Fragment {

    private static final String TAG = "VIEW_SPECIFIC_VIDEO";

    TextView tvTalent, tvTitle, tvDesc, tvSender;

    Button btnDownload;
    ProgressDialog progressDialog;

    DatabaseReference database;

    FirebaseStorage storage;
    StorageReference storageReference;

    public ViewSpecificVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_specific_video, container, false);

        tvDesc = (TextView) view.findViewById(R.id.tvVidDesc);
        tvTitle = (TextView) view.findViewById(R.id.tvVidTitle);
        tvSender = (TextView) view.findViewById(R.id.tvVidSender);
        tvTalent = (TextView) view.findViewById(R.id.tvVidTalent);

        btnDownload = (Button) view.findViewById(R.id.btnVidDownload);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        String url = preferences.getString("vid_ref","dlfjaldfjasdlfj");
        Log.e(TAG, "onCreate: ref = " +url );

        database = FirebaseDatabase.getInstance().getReferenceFromUrl(url);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.e(TAG, "onDataChange: snapshot"+dataSnapshot.getValue() );
                    ViewSpecificVideoModel specificVideo = dataSnapshot.getValue(ViewSpecificVideoModel.class);
                    Log.e(TAG, "onDataChange: video"+specificVideo.toString());
                    setTexts(specificVideo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    try {
                        downloadVideo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                Snackbar.make(v,"Failed: Check your internet Connection",Snackbar.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void downloadVideo() throws IOException {
        showProgressDialog();
        File rootPath;
        rootPath = new File(Environment.getExternalStorageDirectory(), "KuzaVideos");

        if (!rootPath.exists()){
            rootPath.mkdirs();
        }

        File localFile = new File(rootPath,tvTitle.getText().toString()+".mp4");

        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                hideProgressDialog();
                Toast.makeText(getContext(),"Download Successfull",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Toast.makeText(getContext(),"Download Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTexts(ViewSpecificVideoModel specificVideo) {
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(specificVideo.getPath());
        tvSender.setText(specificVideo.getSender());
        tvTitle.setText(specificVideo.getTitle());
        tvDesc.setText(specificVideo.getDescription());
        tvTalent.setText(specificVideo.getTalent());
    }


    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(getContext(),"Downloading Video","Please Wait",true);
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
