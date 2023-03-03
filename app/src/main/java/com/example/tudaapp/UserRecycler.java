package com.example.tudaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserRecycler extends AppCompatActivity {

    RecyclerView recyclerView;
    String userlogin,name,MobileNo,PANNo,RPerioddate,authToken,uapiname;
    ProgressDialog progressDialog;
    TextView username,TAmtTv,TDueTv;
    Bundle buser;
    double TotalAmount,TotalDue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recycler);
        recyclerView = findViewById(R.id.recyclerview);
        username = findViewById(R.id.nameTv);

        TAmtTv = findViewById(R.id.TotalAmtTv);
        TDueTv = findViewById(R.id.TotalDueTv);

        buser = new Bundle();
        buser = getIntent().getExtras();
        userlogin = buser.getString("username");
        authToken = buser.getString("authtoken");

        Log.i("uid",userlogin);
        Log.i("auttoken",authToken);
      //  Toast.makeText(this, userlogin+""+authToken, Toast.LENGTH_SHORT).show();


        /*progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();*/

        getapidata();
        getuname();
        gettotaldata();
    }

    private void getuname() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://tuda-env.eba-d3qkpvh2.ap-south-1.elasticbeanstalk.com/getuserdetailsById/"+userlogin, response -> {

            Log.i("username",response);

            try {
                JSONArray jsonarray = new JSONArray(response);
                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    name = jobj.getString("USER_NAME");
                    username.setText(name);

                   /* MobileNo = jobj.getString("");
                    PANNo = jobj*/

                    Log.i("name", name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong in save user receipts .", Toast.LENGTH_LONG).show();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("Authorization", "Bearer "+""+authToken);

                Log.i("params", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        requestQueue.add(stringRequest);
    }


    private void gettotaldata() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://tuda-env.eba-d3qkpvh2.ap-south-1.elasticbeanstalk.com/getuserbilltotalsById/"+userlogin, response -> {

            Log.i("totalres",response);

            try {
                JSONArray jsonarray = new JSONArray(response);
                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    TotalAmount = jobj.getDouble("TotalAmt");
                    TotalDue = jobj.getDouble("TotalDue");

                    int ta = (int) Math.round(TotalAmount);
                    int td = (int) Math.round(TotalDue);

                    TAmtTv.setText(String.valueOf(ta));
                    TDueTv.setText(String.valueOf(td));



                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong in Totalamount .", Toast.LENGTH_LONG).show();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("Authorization", "Bearer "+""+authToken);

                Log.i("params", String.valueOf(params));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        requestQueue.add(stringRequest);
    }



   private void getapidata(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://tuda-env.eba-d3qkpvh2.ap-south-1.elasticbeanstalk.com/getuserbillsById/"+userlogin, response -> {

              Log.i("res",response);


            try {

                recyclerView.setVisibility(View.VISIBLE);
                JSONArray jsonarray = new JSONArray(response);

                List<DataAdapter> list = new ArrayList<>();

                for(int i=0; i < jsonarray.length(); i++) {


                    DataAdapter adapter = new DataAdapter();
                    JSONObject jobj = jsonarray.getJSONObject(i);

                    adapter.setId(jobj.getString("ID"));
                    adapter.setBillno(jobj.getString("BillNo"));
                    adapter.setUId(jobj.getString("User"));
                    adapter.setProperty(jobj.getString("property"));
                    adapter.setRperiod(jobj.getString("rental_start_date"));
                    adapter.setRamnt(jobj.getString("Rental_lease_amount_permonth"));
                    /*adapter.setTamnt(jobj.getInt("Total"));
                    adapter.setTdue(jobj.getInt("Due"));*/


                    adapter.setTamnt(jobj.getDouble("Total"));
                    adapter.setTdue(jobj.getDouble("Due"));
                    adapter.setTotal(jobj.getDouble("TotalPaid"));

                    adapter.setStatus(jobj.getString("Status"));
                    adapter.setSDate(jobj.getString("start_date"));
                    adapter.setEDate(jobj.getString("end_date"));
                  //  adapter.setRSDate(jobj.getString("rental_start_date"));
                    adapter.setRentalamt(jobj.getString("Rental_lease_amount_permonth"));
                    adapter.setGST(jobj.getString("GST"));
                    adapter.setPenalityfreq(jobj.getString("penality_frequency"));
                    adapter.setPenalitypmnth(jobj.getString("penality_percent_per_month"));

                    adapter.setGstAmount(jobj.getDouble("GSTAmt"));
                    adapter.setTotalPenalityAmt(jobj.getDouble("TotalPenalityAmt"));


                    list.add(adapter);
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(new EmployeeAdapter(list,UserRecycler.this));
                recyclerView.setHasFixedSize(true);
            }
            catch (JSONException e) {
                recyclerView.setVisibility(View.GONE);
            }
             //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(getApplicationContext(), "Something went wrong.Network Issue", Toast.LENGTH_LONG).show();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                String bearer = "Bearer ".concat(authToken);
                params.put("Authorization", bearer);

                Log.i("params", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        requestQueue.add(stringRequest);
    }

    public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder>{

        LocalDate date;

        List<DataAdapter> emplist;
        Context context;
        public EmployeeAdapter(List<DataAdapter> productlist,Context context){
            this.emplist = productlist;
            this.context = context;
        }

        @NonNull
        @Override
        public EmployeeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View itemview;
            itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cust,parent,false);
            return new MyViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(@NonNull EmployeeAdapter.MyViewHolder holder, int position) {

            final DataAdapter adapter = emplist.get(position);
            String statushr;
           // holder.setIsRecyclable(false);
            holder.snotv.setText(adapter.getId());
            holder.bnotv.setText(adapter.getBillno());
            holder.uid.setText(adapter.getUId());
            holder.property.setText(adapter.getProperty());
           // holder.rperiod.setText(adapter.getRperiod());
            holder.ramnt.setText(adapter.getRamnt());
            holder.tamnt.setText(String.valueOf(adapter.getTamnt()));
            holder.tdue.setText(String.valueOf(adapter.getTdue()));
            holder.statustv.setText(adapter.getStatus());
            holder.gsttv.setText(adapter.getGST());

            statushr = adapter.getStatus();


            DateTimeFormatter formatter = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                date = LocalDate.parse(adapter.getRperiod(), formatter);
                Log.i("date", String.valueOf(date));
                holder.rperiod.setText(String.valueOf(date));
            }

            if(statushr.equals("FP")){
                holder.Pbtn.setVisibility(View.GONE);
            }
            if(statushr.equals("NP")){
                holder.recbtn.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return emplist.size();
        }



        public class MyViewHolder extends RecyclerView.ViewHolder {

           // LocalDate sdate,edate,Rentalstartdate;

            TextView snotv,bnotv,uid,property,rperiod,ramnt,tamnt,tdue,statustv,Duid,Dproperty,
                    DBillno,DRperiod,DRamnt,Dcancel,gsttv;
            Bundle bundle;
            Button Pbtn,recbtn,dpay,dproceed,dpaypartial;

            EditText DTamnt;

            String SelectedID,SelectedProperty,SelectedUser_ID,SelectedBillNo ,

            SelectedRental_Period,SelectedRental_Amount ,SelectedStartDate,SelectedEndDate ,sta,
                    Leaseamntpermnth  ,Selectedpenality_frequency,
                    Selectedpenality_percent_per_month ,SelectedStatus,
                    recyclersatus,SelectedGST;

            double newTamnt,NewPaid,SelectedDue,SelectedTotal_Amount,SelectedPaid,OldPaid,Due,SelectedGSTAmt,SelectedTotalPenalityAmt;
            LinearLayout btnlyt,statuslyt;

            Dialog dialog = new Dialog(UserRecycler.this);
            public MyViewHolder(@NonNull View iv) {
                super(iv);

                bundle=new Bundle();
                context = iv.getContext();
                snotv = iv.findViewById(R.id.SNoTv);
                bnotv = iv.findViewById(R.id.BNoTv);
                uid = iv.findViewById(R.id.uidTv);
                property = iv.findViewById(R.id.propertyTv);
                rperiod = iv.findViewById(R.id.rperiodTv);
                ramnt = iv.findViewById(R.id.RamntTv);
                tamnt = iv.findViewById(R.id.TamntTv);
                tdue = iv.findViewById(R.id.TDueTv);
                statustv = iv.findViewById(R.id.StatusTv);
                gsttv = iv.findViewById(R.id.GstTv);

                Pbtn = iv.findViewById(R.id.payBtn);
                recbtn = iv.findViewById(R.id.RecBtn);


                btnlyt = iv.findViewById(R.id.btnllyt);
                statuslyt = iv.findViewById(R.id.satusllyt);

                btnlyt.setVisibility(View.VISIBLE);
                statuslyt.setVisibility(View.VISIBLE);



                recbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context,Receipts.class);
                        bundle.putString("billno", emplist.get(getAdapterPosition()).getBillno());
                        bundle.putString("custname", name);
                        bundle.putString("uid", emplist.get(getAdapterPosition()).getUId());
                        bundle.putDouble("GSTAmt", emplist.get(getAdapterPosition()).getGstAmtount());
                        bundle.putDouble("TotalPenalityAmt", emplist.get(getAdapterPosition()).getTotalPenalityAmt());
                        bundle.putString("headerToken",authToken);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });

                Pbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        dialog.setContentView(R.layout.custdailogue);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(false);
                        //dialog.getWindow().getAttributes().windowAnimations = R.style.a;

                        //dpay = dialog.findViewById(R.id.Dpayamnt);
                        Duid = dialog.findViewById(R.id.DUidTv);
                        Dproperty = dialog.findViewById(R.id.DPropertyTv);
                        DRperiod = dialog.findViewById(R.id.DRperiodTv);
                        DRamnt = dialog.findViewById(R.id.DRamntTv);
                        DTamnt = dialog.findViewById(R.id.DTamntEt);
                        DBillno = dialog.findViewById(R.id.DBillnoTv);
                        Dcancel = dialog.findViewById(R.id.Dcanceltv);

                        dproceed = dialog.findViewById(R.id.proceedBtn);
                        dpaypartial = dialog.findViewById(R.id.paypartialBtn);

                        SelectedID = emplist.get(getAdapterPosition()).getId();
                        SelectedUser_ID = emplist.get(getAdapterPosition()).getUId();
                        SelectedProperty = emplist.get(getAdapterPosition()).getProperty();
                        SelectedBillNo  = emplist.get(getAdapterPosition()).getBillno();

                        SelectedRental_Period = emplist.get(getAdapterPosition()).getRperiod();
                        SelectedRental_Amount  = emplist.get(getAdapterPosition()).getRamnt();
                        OldPaid = emplist.get(getAdapterPosition()).getTotal();

                        Due  = emplist.get(getAdapterPosition()).getTdue();
                        Log.i("totaldue", String.valueOf(SelectedDue));
                        Log.i("totalpaid", String.valueOf(OldPaid));



                        SelectedStartDate = emplist.get(getAdapterPosition()).getSDate();
                        SelectedEndDate  = emplist.get(getAdapterPosition()).getEDate();
                        //  RentalSDate = emplist.get(getAdapterPosition()).getRSDate();

                        Log.i("edate",SelectedEndDate );
                        Log.i("sdate",SelectedStartDate);

                        SelectedGST = emplist.get(getAdapterPosition()).getGST();
                        Log.i("SelectedGST",SelectedGST);
                        Leaseamntpermnth = emplist.get(getAdapterPosition()).getRentalamt();
                        Log.i("lapm",Leaseamntpermnth);
                        SelectedGSTAmt   = emplist.get(getAdapterPosition()).getGstAmtount();
                        Log.i("gstamt", String.valueOf(SelectedGSTAmt));
                        Selectedpenality_frequency = emplist.get(getAdapterPosition()).getPenalityfreq();
                        Log.i("pfreq",Selectedpenality_frequency);
                        Selectedpenality_percent_per_month  = emplist.get(getAdapterPosition()).getPenalitypmnth();
                        Log.i("penalitypermnth",Selectedpenality_percent_per_month );
                        SelectedTotal_Amount = emplist.get(getAdapterPosition()).getTamnt();
                        Log.i("total", String.valueOf(SelectedTotal_Amount));
                        SelectedStatus  = emplist.get(getAdapterPosition()).getStatus();
                        Log.i("status",SelectedStatus );

                        SelectedTotalPenalityAmt = emplist.get(getAdapterPosition()).getTotalPenalityAmt();
                        Log.i("TotalPenalityAmt", String.valueOf(SelectedTotalPenalityAmt));






                        DTamnt.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void afterTextChanged(Editable s) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                Log.i("char", String.valueOf(s));
                                Log.i("start", String.valueOf(start));
                                Log.i("before", String.valueOf(before));
                                Log.i("count", String.valueOf(count));

                                try {
                                    newTamnt = Integer.parseInt(s.toString());
                                    Log.i("newamount", String.valueOf(newTamnt));
                                    NewPaid = newTamnt;
                                    Log.i("NewPaid", "onClick: " + NewPaid);

                                } catch (NumberFormatException ex) {
                                    Toast.makeText(UserRecycler.this, "Total Amount is empty", Toast.LENGTH_SHORT).show();
                                }

                                if(Due == newTamnt){
                                    dproceed.setVisibility(View.VISIBLE);
                                }else if(Due < newTamnt){
                                    DTamnt.setEnabled(false);
                                    dproceed.setVisibility(View.GONE);
                                    dpaypartial.setVisibility(View.GONE);
                                }else{
                                    dproceed.setVisibility(View.GONE);
                                    dpaypartial.setVisibility(View.VISIBLE);
                                }

                            }
                        });

                        Duid.setText(SelectedUser_ID);
                        Dproperty.setText(SelectedProperty);
                        DBillno.setText(SelectedBillNo);
                        DRperiod.setText(SelectedRental_Period);
                        DRamnt.setText(SelectedRental_Amount);
                        DTamnt.setText(String.valueOf(Due));


                        dproceed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                SelectedPaid = OldPaid + NewPaid;
                                SelectedDue = SelectedTotal_Amount-SelectedPaid;


                                Log.i("SelectedPaid", String.valueOf(SelectedPaid));
                                Log.i("SelectedDue", String.valueOf(SelectedDue));

                                if (SelectedTotal_Amount == SelectedPaid) {
                                    SelectedStatus = "FP";
                                } else {
                                    SelectedStatus = "PP";
                                }

                                getproceeddata();

                            }

                           

                        });

                        dpaypartial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                // Toast.makeText(UserRecycler.this, "calling proceed method", Toast.LENGTH_SHORT).show();
                                SelectedPaid = OldPaid + NewPaid;
                                SelectedDue = SelectedTotal_Amount-SelectedPaid;


                                Log.i("SelectedPaid", String.valueOf(SelectedPaid));
                                Log.i("SelectedDue", String.valueOf(SelectedDue));

                                if (SelectedTotal_Amount == SelectedPaid) {
                                    SelectedStatus = "FP";
                                } else {
                                    SelectedStatus = "PP";
                                }
                                getproceeddata();
                            }
                        });

                        Dcancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    }

                    private void getproceeddata(){

                        Log.i("postvalues", SelectedBillNo +"\n"+SelectedUser_ID+"\n"+SelectedProperty+"\n"+SelectedStartDate+"\n"+SelectedEndDate +"\n"+"\n"+SelectedRental_Period+"\n"+Leaseamntpermnth+"\n"+SelectedGSTAmt  +"\n"+SelectedTotal_Amount+"\n"+Selectedpenality_frequency+"\n"+Selectedpenality_percent_per_month +"\n"+newTamnt+"\n"+OldPaid+"\n"+SelectedDue +"\n"+SelectedStatus );

                       // Toast.makeText(context, SelectedUser_ID+""+"userid", Toast.LENGTH_SHORT).show();
                        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://tuda-env.eba-d3qkpvh2.ap-south-1.elasticbeanstalk.com/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        Post postValues=new Post(SelectedID,SelectedBillNo,SelectedUser_ID,SelectedProperty,SelectedStartDate,SelectedEndDate ,SelectedRental_Period,SelectedRental_Amount,SelectedGST,SelectedGSTAmt,SelectedTotalPenalityAmt,SelectedTotal_Amount,Selectedpenality_frequency,Selectedpenality_percent_per_month,NewPaid,SelectedPaid,SelectedDue,SelectedStatus);
                        ApiInterface methods = retrofit.create(ApiInterface.class);
                        Call<Post> call = methods.getUserData(postValues);

                        call.enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {

                                //  Toast.makeText(UserRecycler.this, response.code(), Toast.LENGTH_SHORT).show();

                                updateuserbilling();
                            }

                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                                Toast.makeText(UserRecycler.this,"Error Occurred"+t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    private void updateuserbilling(){
                        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://tuda-env.eba-d3qkpvh2.ap-south-1.elasticbeanstalk.com/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        Post postValues=new Post(SelectedID,SelectedBillNo,SelectedUser_ID,SelectedProperty,SelectedStartDate,SelectedEndDate ,SelectedRental_Period,SelectedRental_Amount,SelectedGST,SelectedGSTAmt,SelectedTotalPenalityAmt,SelectedTotal_Amount,Selectedpenality_frequency,Selectedpenality_percent_per_month,NewPaid,SelectedPaid,SelectedDue,SelectedStatus);
                        ApiInterface methods = retrofit.create(ApiInterface.class);
                        Call<Post> call = methods.updateUserData(postValues);

                        call.enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {

                                String responseString = new Gson().toJson(response.body());

                                getapidata();
                              //  Toast.makeText(UserRecycler.this, responseString, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                                Toast.makeText(UserRecycler.this,"Error Occurred"+t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }


        }
    }
}