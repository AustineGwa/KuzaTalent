package com.dannextech.apps.kuzatalent;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class UploadVideo extends AppCompatActivity {

    private FirebaseStorage storage;
    private StorageReference storageReference,videoRef;

    private static final String TAG = "uPLOADvIDEO";
    private static final int REQUEST_CODE_VIDEO = 0;

    String filePath;

    Button btnSelect, btnUpload;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        btnSelect = (Button) findViewById(R.id.btnSelectVideo);
        btnUpload = (Button) findViewById(R.id.btnUploadVideo);

        videoView = (VideoView) findViewById(R.id.videoView);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: filepath is "+filePath);
                Uri file = Uri.fromFile(new File(filePath));
                videoRef = storageReference.child("videos/"+file.getLastPathSegment());
                UploadTask uploadTask = videoRef.putFile(file);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getApplicationContext(),"upload Failed",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(getApplicationContext(),"upload succeeded",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_CODE_VIDEO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_VIDEO){
            Uri selectedVideo = data.getData();

            //if OI File manager was used
            String fmPath = selectedVideo.getPath();
            Log.e(TAG, "onActivityResult: file manager"+fmPath);

            //if media gallery was used
            filePath = getPath(selectedVideo);
            Log.e(TAG,"onActivityResult: media gallery "+filePath);

            if (filePath != null){
                playVideo(filePath);
            }else{
                playVideo(fmPath);
            }
        }
    }

    private void playVideo(String mgPath) {
        videoView.setVideoPath(mgPath);
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();
    }

    private String getPath(Uri selectedVideo) {
        String [] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedVideo,projection,null,null,null);
        if (cursor != null){
            //if you used IO File manager you will get a null pointer in the cursor.
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }else
            return null;
    }
}
