package com.example.navigationdrawerapp.ui.tips;

import static com.example.navigationdrawerapp.MainActivity.token;
import static com.example.navigationdrawerapp.Urls.PARENT_URL;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.navigationdrawerapp.model.HealthTip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TipsViewModel extends ViewModel {
    private final MutableLiveData<List<HealthTip>> healthTips;
    private final RequestQueue requestQueue;
    private final String url = PARENT_URL + "HealthTips/all";

    public TipsViewModel(Context context) {
        healthTips = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(context);
    }
    public LiveData<List<HealthTip>> getHealthTips() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    List<HealthTip> tips = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            HealthTip tip = new HealthTip(
                                    jsonObject.getString("createdAt"),
                                    jsonObject.getString("updatedAt"),
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("tip_name"),
                                    jsonObject.getString("title"),
                                    jsonObject.getString("description"),
                                    jsonObject.getInt("createdBy")
                            );
                            tips.add(tip);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    healthTips.setValue(tips);
                },
                error -> {
                    // handle error
                })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
        return healthTips;
    }

}
