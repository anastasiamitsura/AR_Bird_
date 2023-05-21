package com.example.arbird;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arbird.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    SharedPreferences sPref;
    final String SAVED_USER_NAME = "saved_user_name";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.edit.setOnClickListener(view -> {
            if(binding.userName.getVisibility() == View.VISIBLE){
                binding.userName.setVisibility(View.GONE);
                binding.etUserName.setVisibility(View.VISIBLE);
            }
            else {
                binding.userName.setVisibility(View.VISIBLE);
                binding.etUserName.setVisibility(View.GONE);
                saveText();
                loadText();
            }
        });
        binding.countScan.setText(ScanFragment.countScan + "");
        binding.level.setText((int)ScanFragment.countScan / 3 + "");
        loadText();
        return binding.getRoot();
    }

    private void saveText() {
        sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_USER_NAME, binding.etUserName.getText().toString());
        ed.commit();
    }

    private void loadText() {
        sPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String userName = sPref.getString(SAVED_USER_NAME, "");
        binding.userName.setText(userName);
    }


}