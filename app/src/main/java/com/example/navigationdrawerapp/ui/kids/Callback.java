package com.example.navigationdrawerapp.ui.kids;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Kids_details_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public interface Callback {
    void onSuccess(JSONObject motherData);

    void onError(String errorMessage);
}
