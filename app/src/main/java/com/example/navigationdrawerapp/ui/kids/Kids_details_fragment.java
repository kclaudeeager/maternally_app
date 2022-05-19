package com.example.navigationdrawerapp.ui.kids;

import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.navigationdrawerapp.R;
import com.example.navigationdrawerapp.databinding.FragmentGalleryBinding;
import com.example.navigationdrawerapp.databinding.FragmentKidsDetailsFragmentBinding;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Kids_details_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Kids_details_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentKidsDetailsFragmentBinding binding;
    LinearLayoutCompat linearLayoutCompat;
    public Kids_details_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Kids_details_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Kids_details_fragment newInstance(String param1, String param2) {
        Kids_details_fragment fragment = new Kids_details_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentKidsDetailsFragmentBinding.inflate(inflater, container, false);
        View root=binding.getRoot();
        linearLayoutCompat=binding.kidLinearLayout;
        LayoutInflater kidsDetailInflater=LayoutInflater.from(getContext());
       linearLayoutCompat.setGravity(Gravity.CENTER);

       int i=0;
       while (i<5) {
           View kidsDetailsView= kidsDetailInflater.inflate(R.layout.kids_details, container,false);
           TextView firstnameView=kidsDetailsView.findViewById(R.id.first_name);
           TextView lastnameView=kidsDetailsView.findViewById(R.id.lastname);
           TextView dateOfBirthView=kidsDetailsView.findViewById(R.id.birthDate);
           firstnameView.setText("First name "+(i+1));
           lastnameView.setText("Last name "+(i+1));
           dateOfBirthView.setText("Date of birth "+(i+1));
           linearLayoutCompat.addView(kidsDetailsView);
           i++;
       }
        return root;
    }
}