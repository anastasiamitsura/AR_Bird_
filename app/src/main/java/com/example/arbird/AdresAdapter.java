package com.example.arbird;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arbird.databinding.AdresItemBinding;

import java.util.ArrayList;
import java.util.Collection;

public class AdresAdapter extends RecyclerView.Adapter<AdresAdapter.ViewHolder> {
    private final ArrayList<AdresData> data = new ArrayList<>();

    public void setData(Collection<AdresData> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public void removeItemByPosition(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("RECYCLER", "Create");
        return new ViewHolder(
                AdresItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                ).getRoot()
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("RECYCLER", "Bind: " + position);
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final AdresItemBinding itemBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBinding = AdresItemBinding.bind(itemView);
        }

        public void bind(AdresData data) {
            itemBinding.adress.setText(data.getAddres());
            itemBinding.city.setText(data.getCity());
            itemBinding.country.setText(data.getCountry());
            itemBinding.postInd.setText(data.getPostalCode());
            String knownName = data.getKnownName();
            if(knownName.isEmpty()) {
                itemBinding.namekn.setVisibility(View.GONE);
            } else {
                itemBinding.namekn.setVisibility(View.VISIBLE);
                itemBinding.namekn.setText(knownName);
            }
        }
    }
}
