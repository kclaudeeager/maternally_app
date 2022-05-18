package com.example.navigationdrawerapp.ui.gallery;

import static com.example.navigationdrawerapp.MainActivity.userDataJs;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.navigationdrawerapp.MainActivity;
import com.example.navigationdrawerapp.databinding.FragmentGalleryBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    private FragmentGalleryBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        String phoneNum = null;
        String email = null;
        String firstName;
        String lastName;
        String role;
        String fullName = null;
        try {
            phoneNum=userDataJs.getString("phone");
            email=userDataJs.getString("email");
            firstName=userDataJs.getString("firstName");
            lastName=userDataJs.getString("lastName");
            role=userDataJs.getString("role");
            fullName=firstName+"  "+lastName;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.email.setText(email);
        binding.fullName.setText(fullName);
        binding.phone.setText(phoneNum);
        View root = binding.getRoot();


//        final TextView textView = binding.profile;
//        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}