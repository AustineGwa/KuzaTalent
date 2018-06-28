package com.dannextech.apps.kuzatalent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Call4TalentAdapter extends RecyclerView.Adapter<Call4TalentAdapter.ViewHolder>{

    Context context;
    List<Call4TalentModel> call4Talents;

    ColorGenerator generator = ColorGenerator.MATERIAL;

    public Call4TalentAdapter(Context context, List<Call4TalentModel> TempList) {

        this.call4Talents = TempList;

        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_4_talent_sub_details, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Call4TalentModel call4TalentModel = call4Talents.get(position);

        holder.tvOrg.setText(call4TalentModel.getOrganization());

        holder.tvTalent.setText(call4TalentModel.getTalent());

        holder.tvDate.setText(call4TalentModel.getDatePosted());

        //Get the first letter of list item
        String letter = String.valueOf(holder.tvTalent.getText().charAt(0));
        //Create a new TextDrawable for our image's background
        final TextDrawable drawable = TextDrawable.builder().buildRound(letter,generator.getRandomColor());
        holder.ivlog.setImageDrawable(drawable);

        holder.cvCall4Talent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor= preferences.edit();

                editor.putString("ref",call4TalentModel.getUrl());

                editor.apply();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                retrieveUserDetails(mAuth.getUid());


            }
        });
    }

    @Override
    public int getItemCount() {
        return call4Talents.size();
    }

    private void retrieveUserDetails(String uid) {

        //creating a reference to the folder users where the details will be saved
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference().child("Users/"+uid);

        DatabaseReference categoryRef = databaseRef.getRef().child("Category");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("Dannex", "onDataChange: details are "+dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String category = dataSnapshot.getValue().toString();
                Log.e("Dannex", "onDataChange: "+category);

                if (category.equals("Youth")){
                    Fragment fragment = new ViewSpecificCall4TalentFragment();
                    AppCompatActivity activity = (AppCompatActivity) context;
                    FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.flYouthFragment,fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }else {
                    Fragment fragment = new ViewSpecificCall4TalentFragment();
                    AppCompatActivity activity = (AppCompatActivity) context;
                    FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.flOrganizationFragment,fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(context,"Something went wrong. Please try again",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrg,tvTalent,tvDate,tvLocation;
        CardView cvCall4Talent;
        ImageView ivlog;
        public ViewHolder(View itemView) {
            super(itemView);
            tvOrg = (TextView) itemView.findViewById(R.id.tvOrganizationNeed);
            tvTalent = (TextView) itemView.findViewById(R.id.tvTalentNeed);
            tvDate = (TextView) itemView.findViewById(R.id.tvDateTalentNeed);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocationTalentNeed);

            cvCall4Talent = (CardView) itemView.findViewById(R.id.cvCall4Talent);
            ivlog = (ImageView) itemView.findViewById(R.id.ivLog);
        }
    }
}
