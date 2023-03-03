package com.example.tudaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserReceipts extends AppCompatActivity {
    Bundle br;
    String rbillNo,rauthtoken,jsonStr4;
    TextView resTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_receipts);

        resTv = findViewById(R.id.restv);

        br = new Bundle();
        br = getIntent().getExtras();
        rbillNo = br.getString("billno");
        rauthtoken = br.getString("headerToken");
        Log.i("rbillNo", rbillNo);
        Log.i("rauthtoken", rauthtoken);


           // String jsonStr3 =postData2.put("billNo",rbillNo).toString();
            jsonStr4 = rbillNo.replace("\\","");
            Log.i("jsonStr4", jsonStr4);


     // getReceipts();
      // getRec();
      //  test();
     //   testtt();

        getUserListData();
    }




    private void getUserListData() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://tuda-env.eba-d3qkpvh2.ap-south-1.elasticbeanstalk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface jsonPlaceHolderApi = retrofit.create(ApiInterface.class);

        Call<List<DataAdapter>> listCall = jsonPlaceHolderApi.getPosts(jsonStr4);

        listCall.enqueue(new Callback<List<DataAdapter>>() {


            @Override
            public void onResponse(Call<List<DataAdapter>> call, retrofit2.Response<List<DataAdapter>> response) {


                List<DataAdapter> myheroList = response.body();
                String[] oneHeroes = new String[myheroList.size()];
                for (int i = 0; i < myheroList.size(); i++) {
                    oneHeroes[i] = myheroList.get(i).getUser();
                    Toast.makeText(UserReceipts.this, myheroList.get(i).getBillno(), Toast.LENGTH_SHORT).show();
                }




            }



            @Override
            public void onFailure(Call<List<DataAdapter>> call, Throwable t) {
                resTv.setText(t.getMessage());
            }
        });



    }









}
