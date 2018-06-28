package com.dannextech.apps.kuzatalent;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadVideoFragment extends Fragment {

    private FirebaseStorage storage;
    private StorageReference storageReference,videoRef;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;

    private static final String TAG = "uPLOADvIDEO";
    private static final int REQUEST_CODE_VIDEO = 0;

    String filePath,talent;

    Button btnSelect, btnUpload;
    VideoView videoView;
    EditText etTitle,etDesc;
    Spinner spTalent;
    TextView tvFile;
    ProgressDialog progressDialog;

    public UploadVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_upload_video, container, false);

        if (isNetworkAvailable()){
            mAuth = FirebaseAuth.getInstance();
            firebaseUser = mAuth.getCurrentUser();
            Log.e(TAG, "onCreateView: User is "+firebaseUser.getUid());
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
        }else
            Snackbar.make(view,"Failed to load: Check your internet Connection",Snackbar.LENGTH_SHORT).show();


        etDesc = (EditText) view.findViewById(R.id.etVidDescription);
        etTitle = (EditText) view.findViewById(R.id.etVidTitle);

        tvFile = (TextView) view.findViewById(R.id.tvVidName);

        spTalent = (Spinner) view.findViewById(R.id.spVidTalent);

        btnSelect = (Button) view.findViewById(R.id.btnSelectVideo);
        btnUpload = (Button) view.findViewById(R.id.btnUploadVideo);

        videoView = (VideoView) view.findViewById(R.id.videoView);

        spTalent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                talent = spTalent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                Log.e(TAG, "onClick: filepath is "+filePath);
                Uri file = Uri.fromFile(new File(filePath));
                videoRef = storageReference.child("videos/"+file.getLastPathSegment());
                UploadTask uploadTask = videoRef.putFile(file);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hideProgressDialog();
                        // Handle unsuccessful uploads
                        Log.e(TAG, "onFailure: upload failed");
                        Toast.makeText(view.getContext(),"upload Failed",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e(TAG, "onSuccess: video uploaded successfully");
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(getContext(),"Video success",Toast.LENGTH_LONG).show();
                        uploadDetails(firebaseUser,downloadUrl,etDesc.getText().toString(),etTitle.getText().toString());
                    }
                });

            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                }else {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI);
                }
                intent.setType("video/*");
                startActivityForResult(intent,REQUEST_CODE_VIDEO);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_VIDEO){

            if (data.getData()!=null){
                //if media gallery was used
                filePath = getVideoPath(data.getData());
                Log.e(TAG,"onActivityResult: media gallery "+filePath);
            }
            Log.e(TAG, "onActivityResult: "+filePath);
            if (filePath != null){
                tvFile.setText(data.getData().getLastPathSegment().toString());
                playVideo(filePath);
            }else{
                Toast.makeText(getContext(),"Can't open video please select again",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void playVideo(String mgPath) {

        videoView.setVideoPath(mgPath);
        videoView.setMediaController(new MediaController(getContext()));
        videoView.requestFocus();
        videoView.start();

        if (videoView.isPlaying()){
            Log.e(TAG, "playVideo: is successfull" );
        }else
            Log.e(TAG, "playVideo: is not successfull" );
    }

    private String getVideoPath(Uri selectedVideo) {
        String [] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(selectedVideo,projection,null,null,null);
        if (cursor != null){
            //if you used IO File manager you will get a null pointer in the cursor.
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }else
            return null;
    }

    public void uploadDetails(FirebaseUser user, Uri url, String desc, String title){
        Log.e(TAG, "uploadDetails: saving video details");
        //creating a reference to the folder users where the details will be saved
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Videos/"+user.getUid()).push();

        //creating references to where specific details will be saved
        DatabaseReference titleRef = databaseReference.child("title");
        DatabaseReference talentRef = databaseReference.child("talent");
        DatabaseReference descRef = databaseReference.child("description");
        DatabaseReference urlRef = databaseReference.child("path");
        DatabaseReference senderRef = databaseReference.child("sender");
        DatabaseReference senderMailRef = databaseReference.child("email");
        DatabaseReference senderPhoneRef = databaseReference.child("phone");

        //getting the user details
        DatabaseReference userDatabaseReference = database.getReference().child("Users/"+user.getUid());
        DatabaseReference orgRef = userDatabaseReference.getRef().child("Name");
        DatabaseReference phoneRef = userDatabaseReference.getRef().child("Phone");
        DatabaseReference emailRef = userDatabaseReference.getRef().child("Email");

        final String[] name = new String[1];
        final String[] phones = new String[1];
        final String[] email = new String[1];

        orgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name[0] = dataSnapshot.getValue().toString();
                Toast.makeText(getContext(),"name = "+name[0],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        phoneRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                phones[0] = dataSnapshot.getValue().toString();
                Toast.makeText(getContext(),"phone = "+phones[0],Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        emailRef.setValue(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email[0] = dataSnapshot.getValue().toString();
                Toast.makeText(getContext(),"email = "+email[0],Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        senderPhoneRef.setValue(phones[0]);
        senderMailRef.setValue(email[0]);
        senderRef.setValue(name[0]);
        titleRef.setValue(title);
        descRef.setValue(desc);
        urlRef.setValue(url.toString());
        talentRef.setValue(talent, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                hideProgressDialog();
                Log.e(TAG, "onComplete: saved successfully");
                Toast.makeText(getContext(),"upload succeeded",Toast.LENGTH_SHORT).show();

                Fragment fragment = new ViewVideoUploadsFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.flYouthFragment,fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
    }


    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(getContext(),"Uploading Video","Please Wait",true);
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
