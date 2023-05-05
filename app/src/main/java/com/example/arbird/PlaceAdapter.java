package com.example.arbird;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arbird.databinding.ItemPlaceBinding;

import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    private List<PlaceShortData> places = new ArrayList<>();

    public void setItems(List<PlaceShortData> newData){
        places = newData;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemPlaceBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(places.get(position));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ItemPlaceBinding binding;
        public ViewHolder(@NonNull ItemPlaceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PlaceShortData data){
            binding.namebr.setText(data.getName());
            binding.adress.setText(data.getAdress());
            binding.itaj.setText(data.getEtaj());
        }
    }

}
