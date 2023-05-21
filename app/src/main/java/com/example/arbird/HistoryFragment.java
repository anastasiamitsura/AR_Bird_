package com.example.arbird;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arbird.AddressDataBase.AdresAdapter;
import com.example.arbird.AddressDataBase.AdressRepository;
import com.example.arbird.databinding.FragmentHistoryBinding;


public class HistoryFragment extends Fragment{

    private FragmentHistoryBinding binding;
    private AdressRepository repository;
    private final AdresAdapter adapter = new AdresAdapter();
    private final ItemTouchHelper.SimpleCallback swipeToDelete = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(
                @NonNull RecyclerView recyclerView,
                @NonNull RecyclerView.ViewHolder viewHolder,
                @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            repository.removeByPosition(viewHolder.getAdapterPosition());
            adapter.removeItemByPosition(viewHolder.getAdapterPosition());
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        repository = new AdressRepository(requireContext());
        binding.container.setAdapter(adapter);
        new ItemTouchHelper(swipeToDelete).attachToRecyclerView(binding.container);
        adapter.setData(repository.getAdresss());
        return binding.getRoot();
    }

}