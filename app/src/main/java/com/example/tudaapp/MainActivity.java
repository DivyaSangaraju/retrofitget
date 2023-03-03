package com.example.tudaapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    Button btn;

    EditText uname,pwd;
    String getuname,getpwd,logintoken;
    TextInputLayout etPasswordLayout;
    Bundle bundle;

    SharedPrefs sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getUserListData();
        bundle=new Bundle();
        sp=new SharedPrefs(Objects.requireNonNull(MainActivity.this));

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                Log.i("token", "retrieve token successful : " + token);
                sp.setfcm(token);

            } else{
                Log.i("null", "token should not be null...");
            }
        }).addOnFailureListener(e -> {
            //handle e
        }).addOnCanceledListener(() -> {
            //handle cancel
        }).addOnCompleteListener(
                task -> Log.i("generated", "This is the token : " + task.getResult())
        );
        FirebaseMessaging.getInstance().getToken();


       // inserttoken();

       btn = findViewById(R.id.nxtBtn);
       uname = findViewById(R.id.UnameEt);
       pwd = findViewById(R.id.PwdEt);
       etPasswordLayout = findViewById(R.id.pwdTIL);


       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               getuname = uname.getText().toString();
               getpwd = pwd.getText().toString();

               Log.i("getuname", getuname);
               Log.i("getpwd", getpwd);

               getloggedin();
           }
       });
    }

    private void getloggedin() {

        String postUrl = "http://tuda-env.eba-d3qkpvh2.ap-south-1.elasticbeanstalk.com/loginuser";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("username", getuname);
            postData.put("password", getpwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // Toast.makeText(getApplicationContext(), "loginResponse: "+response, Toast.LENGTH_LONG).show();
                Log.i("loginResponse", String.valueOf(response));


                try {
                    String status = response.getString("status");
                    logintoken = response.getString("token");
                    Log.i("logintoken", logintoken);
                    if(status.equals("200") ){

                        Intent intent = new Intent(getApplicationContext(),UserRecycler.class);
                        bundle.putString("username", getuname);
                        bundle.putString("authtoken", logintoken);

                        intent.putExtras(bundle);
                        startActivity(intent);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    /*private void inserttoken() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://localhost:3000/insert", response -> {

            Log.i("token",response);


        }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong in token generation .", Toast.LENGTH_LONG).show();
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token_value",FcmToken );
                Log.i("params", String.valueOf(params));
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        requestQueue.add(stringRequest);


    }*/

}