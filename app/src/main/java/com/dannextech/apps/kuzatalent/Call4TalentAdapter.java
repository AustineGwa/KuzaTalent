package com.dannextech.apps.kuzatalent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Call4TalentAdapter extends RecyclerView.Adapter<Call4TalentAdapter.ViewHolder>{

    Context context;
    List<Call4TalentModel> call4Talents;

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

        holder.cvCall4Talent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, call4TalentModel.getUrl(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return call4Talents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrg,tvTalent,tvDate;
        CardView cvCall4Talent;
        public ViewHolder(View itemView) {
            super(itemView);
            tvOrg = (TextView) itemView.findViewById(R.id.tvOrganizationNeed);
            tvTalent = (TextView) itemView.findViewById(R.id.tvTalentNeed);
            tvDate = (TextView) itemView.findViewById(R.id.tvDateTalentNeed);

            cvCall4Talent = (CardView) itemView.findViewById(R.id.cvCall4Talent);
        }
    }
}
