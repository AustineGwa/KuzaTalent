package com.dannextech.apps.kuzatalent;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSpecificCall4TalentFragment extends Fragment {

    private static final String TAG = "VIEW_SPECIFIC_CALL_4";

    TextView tvEmail,tvOrg, tvTalent, tvDesc, tvLocation, tvWebsite, tvPhone,tvDate;
    ImageView ivEmail,ivCall,ivLocation,ivWebsite;
    Button btnBack,btnApply;

    ProgressDialog progressDialog;

    FirebaseDatabase databases;
    DatabaseReference database,databaseRef;

    String cat;

    public ViewSpecificCall4TalentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_specific_call4_talent, container, false);


        tvEmail = (TextView) view.findViewById(R.id.tvEmailNeedSpecific);
        tvOrg = (TextView) view.findViewById(R.id.tvOrganizationNeedSpecific);
        tvTalent = (TextView) view.findViewById(R.id.tvTalentNeedSpecific);
        tvDesc = (TextView) view.findViewById(R.id.tvdescriptionNeedSpecific);
        tvLocation = (TextView) view.findViewById(R.id.tvLocationNeedSpecific);
        tvPhone = (TextView) view.findViewById(R.id.tvPhoneNeedSpecific);
        tvWebsite = (TextView) view.findViewById(R.id.tvWebsiteNeedSpecific);
        tvDate = (TextView) view.findViewById(R.id.tvDateNeedSpecific);
        ivCall = (ImageView) view.findViewById(R.id.ivCall);
        ivEmail = (ImageView) view.findViewById(R.id.ivEmail);
        ivLocation = (ImageView) view.findViewById(R.id.ivLocation);
        ivWebsite = (ImageView) view.findViewById(R.id.ivWebsite);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnApply = (Button) view.findViewById(R.id.btnApply);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        String url = preferences.getString("ref","dlfjaldfjasdlfj");
        Log.e(TAG, "onCreate: ref = " +url );

        database = FirebaseDatabase.getInstance().getReferenceFromUrl(url);

        retrieveUserDetails(FirebaseAuth.getInstance().getCurrentUser().getUid());

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: "+dataSnapshot.getValue() );
                ViewSpecificCall4TalentModel call4TalentModel = dataSnapshot.getValue(ViewSpecificCall4TalentModel.class);
                Log.e(TAG, "onDataChange: "+call4TalentModel.toString());
                setTexts(call4TalentModel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{tvEmail.getText().toString()});
                intent.putExtra(Intent.EXTRA_SUBJECT,"APPLICATION FOR CALL FOR TALENT");
                intent.putExtra(Intent.EXTRA_TEXT,"I am applying for this call for talent");
                try{
                    startActivity(Intent.createChooser(intent,"Send mail..."));
                }catch (android.content.ActivityNotFoundException exception){
                    Snackbar.make(v,"There is no email clients installed",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cat.equals("Youth")){
                    Fragment fragment = new ViewCall4TalentFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.flYouthFragment,fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }else {
                    Fragment fragment = new ViewCall4TalentFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.flOrganizationFragment,fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }

            }
        });

        ivWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://"+tvWebsite.getText().toString())));
            }
        });

        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String map = "http://maps.google.co.in/maps?q"+tvLocation.getText().toString();
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(map)));
            }
        });

        ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{tvEmail.getText().toString()});
                intent.putExtra(Intent.EXTRA_SUBJECT,"APPLICATION FOR CALL FOR TALENT");
                intent.putExtra(Intent.EXTRA_TEXT,"I am applying for this call for talent");
                try{
                    startActivity(Intent.createChooser(intent,"Send mail..."));
                }catch (android.content.ActivityNotFoundException exception){
                    Snackbar.make(v,"There is no email clients installed",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:"+tvPhone.getText().toString());
                startActivity(new Intent(Intent.ACTION_DIAL,number));
            }
        });


        return view;
    }

    private void setTexts(ViewSpecificCall4TalentModel call4TalentModel) {
        tvWebsite.setText(call4TalentModel.getWebsite());
        tvPhone.setText(call4TalentModel.getPhone());
        tvLocation.setText(call4TalentModel.getLocation());
        tvDesc.setText(call4TalentModel.getDescription());
        tvTalent.setText(call4TalentModel.getTalent());
        tvEmail.setText(call4TalentModel.getEmail());
        tvOrg.setText(call4TalentModel.getOrganization());
        tvDate.setText(call4TalentModel.getDatePosted());
    }

    private void retrieveUserDetails(String uid) {
        final String[] category = new String[1];
        //creating a reference to the folder users where the details will be saved
        databases = FirebaseDatabase.getInstance();
        databaseRef = databases.getReference().child("Users/"+uid);

        DatabaseReference categoryRef = databaseRef.getRef().child("Category");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: details are "+dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cat = dataSnapshot.getValue().toString();

                Log.e(TAG, "onDataChange: "+ category);
                if (cat.equals("Organization")){
                    btnApply.setVisibility(View.GONE);
                }else {
                    btnApply.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgressDialog();
                Toast.makeText(getContext(),"Something went wrong. Please try again",Toast.LENGTH_SHORT).show();
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
