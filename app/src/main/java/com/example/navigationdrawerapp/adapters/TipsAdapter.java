package com.example.navigationdrawerapp.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navigationdrawerapp.R;
import com.example.navigationdrawerapp.model.HealthTip;

import java.util.ArrayList;
import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipViewHolder> {

    private List<HealthTip> tips = new ArrayList<>();

    @NonNull
    @Override
    public TipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.health_tip_view, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipViewHolder holder, int position) {
        HealthTip tip = tips.get(position);
        holder.bind(tip);
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHealthTips(List<HealthTip> healthTips) {
        tips = healthTips;
        notifyDataSetChanged();
    }

    static class TipViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView nameTextView;
        private final TextView descriptionTextView;
        private final ImageView expandIconImageView;

        private boolean isExpanded = false;

        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.healthTipName);
            descriptionTextView = itemView.findViewById(R.id.healthTipDescription);
            expandIconImageView = itemView.findViewById(R.id.expandIcon);
            itemView.setOnClickListener(this);
        }

        void bind(HealthTip tip) {
            nameTextView.setText(tip.getTitle());
            descriptionTextView.setText(tip.getDescription());
            setDescriptionVisibility(isExpanded);
        }

        private void setDescriptionVisibility(boolean isVisible) {
            if (isVisible) {
                descriptionTextView.setVisibility(View.VISIBLE);
                expandIconImageView.setImageResource(R.drawable.ic_baseline_remove_24);
            } else {
                descriptionTextView.setVisibility(View.GONE);
                expandIconImageView.setImageResource(R.drawable.ic_baseline_add_24);
            }
        }

        @Override
        public void onClick(View view) {
            isExpanded = !isExpanded;
            setDescriptionVisibility(isExpanded);
        }
    }
}
