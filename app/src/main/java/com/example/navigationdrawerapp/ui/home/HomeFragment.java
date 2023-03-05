package com.example.navigationdrawerapp.ui.home;

import static com.example.navigationdrawerapp.MainActivity.phoneNumber;
import static com.example.navigationdrawerapp.MainActivity.token;
import static com.example.navigationdrawerapp.ui.home.HomeViewModel.retrieveMotherData;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.navigationdrawerapp.databinding.FragmentHomeBinding;
import com.example.navigationdrawerapp.ui.kids.Callback;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textWelcome;
        final TextView childNumberView=binding.textChildrenNumber;
        final TextView tipsViewNumber=binding.textTipsNumber;

        retrieveMotherData(phoneNumber,requireContext(),new Callback() {
            @Override
            public void onSuccess(JSONObject motherData) {
                try {
                    int motherId = motherData.getInt("id");
                    homeViewModel.retrieveBabiesData(motherId, token,requireContext());
                    homeViewModel. getTipsLength(requireContext(),token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("Error", errorMessage);
            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        homeViewModel.getTipsNumber().observe(getViewLifecycleOwner(),tipsViewNumber::setText);
        homeViewModel.getChildNumber().observe(getViewLifecycleOwner(),childNumberView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}