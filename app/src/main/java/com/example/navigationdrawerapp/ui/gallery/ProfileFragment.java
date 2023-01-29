package com.example.navigationdrawerapp.ui.gallery;

import static com.example.navigationdrawerapp.MainActivity.userDataJs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.navigationdrawerapp.MainActivity;
import com.example.navigationdrawerapp.R;
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
        ImageView logo = binding.profileImage;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.africanwoman);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        logo.setImageDrawable(roundedBitmapDrawable);
        String phoneNum =null;
        String email = null;
        String firstName;
        String lastName;
        String role;
        String fullName = null;
        Log.d("user here", String.valueOf(userDataJs));
        try {

            email=userDataJs.getString("email");
            firstName=userDataJs.getString("firstName");
            lastName=userDataJs.getString("lastName");
            role=userDataJs.getString("role");
            fullName=firstName+"  "+lastName;
            System.out.println("Email: "+email+", fullName: "+fullName);
            phoneNum=userDataJs.getString("phone");
        } catch (JSONException e) {
            phoneNum = "0788483455";
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