package com.example.navigationdrawerapp.ui.kids;

import static com.example.navigationdrawerapp.MainActivity.phoneNumber;
import static com.example.navigationdrawerapp.MainActivity.token;
import static com.example.navigationdrawerapp.Urls.PARENT_URL;
import static com.example.navigationdrawerapp.ui.home.HomeViewModel.retrieveMotherData;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.navigationdrawerapp.R;
import com.example.navigationdrawerapp.databinding.FragmentKidsDetailsFragmentBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentKidsDetailsFragmentBinding.inflate(inflater, container, false);
        View root=binding.getRoot();
        linearLayoutCompat=binding.kidLinearLayout;
       linearLayoutCompat.setGravity(Gravity.CENTER);

        retrieveMotherData(phoneNumber,requireContext(),new Callback() {
            @Override
            public void onSuccess(JSONObject motherData) {
                try {
                    int motherId = motherData.getInt("id");
                    retrieveBabiesData(motherId, token, container);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("Error", errorMessage);
            }
        });

        return root;
    }

    private void retrieveBabiesData(int motherId, final String token,ViewGroup container) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = PARENT_URL+"Baby/mother/" + motherId;
        ProgressDialog dialog=new ProgressDialog(requireContext());
        dialog.setTitle("Babies");
        dialog.setMessage("Loading.....");
        dialog.show();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        LayoutInflater kidsDetailInflater = LayoutInflater.from(getContext());
                        linearLayoutCompat.setGravity(Gravity.CENTER);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject baby = response.getJSONObject(i);
                            String firstName = baby.getString("firstName");
                            String lastName = baby.getString("lastName");
                            String createdAt = baby.getString("createdAt");
                            View kidsDetailsView = kidsDetailInflater.inflate(R.layout.kids_details, container, false);
                            TextView firstNameView = kidsDetailsView.findViewById(R.id.first_name);
                            TextView lastNameView = kidsDetailsView.findViewById(R.id.lastname);
                            TextView birthDateView= kidsDetailsView.findViewById(R.id.birthDate);
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
                            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.US);

                            try {
                                Date date = inputFormat.parse(createdAt);
                                assert date != null;
                                String outputDate = outputFormat.format(date);
                                birthDateView.setText(outputDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            firstNameView.setText(firstName);
                            lastNameView.setText(lastName);
                            linearLayoutCompat.addView(kidsDetailsView);
                        }
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                    } catch (JSONException e) {
                        if(dialog.isShowing()){
                            dialog.dismiss();
                        }
                        e.printStackTrace();

                    }
                },
                error ->{
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                    Log.e("VolleyError", error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(jsonArrayRequest);
    }

}