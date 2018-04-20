package com.dannextech.apps.kuzatalent;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewCall4Talent extends AppCompatActivity {

    RecyclerView rvViewCall4Talent;

    public DatabaseReference mReference,itemRef;

    //List<MealInfoModel> myList;
    FirebaseRecyclerAdapter<Call4TalentModel,ViewCall4TalentHolder> firebaseRecyclerAdapter;

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

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Call4TalentModel, ViewCall4TalentHolder>(
                Call4TalentModel.class,
                R.layout.call_4_talent_sub_details,
                ViewCall4TalentHolder.class,
                mReference
        ) {
            @Override
            protected void populateViewHolder(ViewCall4TalentHolder viewHolder, Call4TalentModel model, int position) {
                viewHolder.setTalent(model.getTalent());
                viewHolder.setTalentOrg(model.getOrganization());
                viewHolder.setTalentDate(model.getDatePosted());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v,"you have clicked me",Snackbar.LENGTH_LONG).show();
                    }
                });

                rvViewCall4Talent.setAdapter(firebaseRecyclerAdapter);
            }
        };
    }

    public static class ViewCall4TalentHolder extends RecyclerView.ViewHolder {
        View view;
        public ViewCall4TalentHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setTalent(String talent){
            TextView tvTalent = (TextView) view.findViewById(R.id.tvTalentNeed);
            tvTalent.setText(talent);
        }

        public void setTalentOrg(String description){
            TextView tvTalentDesc = (TextView) view.findViewById(R.id.tvOrganizationNeed);
            tvTalentDesc.setText(description);
        }

        public void setTalentDate(String date){
            TextView tvTalentDate = (TextView) view.findViewById(R.id.tvDateTalentNeed);
            tvTalentDate.setText(date);
        }
    }
}
