package com.example.navigationdrawerapp.ui.tips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.navigationdrawerapp.adapters.TipsAdapter;
import com.example.navigationdrawerapp.databinding.FragmentTipsBinding;
import com.example.navigationdrawerapp.model.HealthTip;

import java.util.List;

public class TipFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentTipsBinding binding;
    private TipsAdapter adapter;
    ConstraintLayout linearLayoutCompat;
    public static TipFragment newInstance(String param1, String param2) {
        TipFragment fragment = new TipFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentTipsBinding.inflate(inflater, container, false);
        View root=binding.getRoot();
        linearLayoutCompat=binding.tipsLayout;
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new TipsAdapter();
        binding.healthTipsRecyclerView.setAdapter(adapter);
       binding.healthTipsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        TipsViewModel tipsViewModel = new TipsViewModel(requireContext());
        tipsViewModel.getHealthTips().observe(getViewLifecycleOwner(), this::updateTips);
    }

    private void updateTips(List<HealthTip> tips) {
        if (tips.isEmpty()) {
            binding.tipTitle.setVisibility(View.VISIBLE);
        } else {
            binding.tipTitle.setVisibility(View.GONE);
            adapter.setHealthTips(tips);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
