package com.example.tudaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Receipts extends AppCompatActivity {

    RecyclerView rview;
    Bundle b;
    String billno,jbill,custname,UserId,Recptno,Property,Paid,authToken,jsonStr4;
    ProgressDialog progressDialog;
    double Total,Due,GSTAmt,PenalityAmt;

   // List<UserListResponse> userListResponseData;
    List<DataAdapter> myheroList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts);

       /* progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();*/

        rview = findViewById(R.id.recycler);

        b = new Bundle();
        b = getIntent().getExtras();
        billno = b.getString("billno");
        custname = b.getString("custname");
        UserId = b.getString("uid");

        GSTAmt = b.getDouble("GSTAmt");
        PenalityAmt = b.getDouble("TotalPenalityAmt");
        authToken = b.getString("headerToken");
        Log.i("rbillno", billno);
        Log.i("custname", custname);
        Log.i("userid", UserId);
        Log.i("RecGSTAmt", String.valueOf(GSTAmt));
        Log.i("RecPenalityAmt", String.valueOf(PenalityAmt));
        Log.i("authToken", authToken);

        jsonStr4 = billno.replace("\\","");
        Log.i("jsonStr4", jsonStr4);
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


                Log.i("onSuccess", response.body().toString());

                String jsonresponse = response.body().toString();

               // Toast.makeText(Receipts.this, jsonresponse, Toast.LENGTH_SHORT).show();

                //List<DataAdapter> myheroList = response.body();
                myheroList = response.body();
                for (int i = 0; i < myheroList.size(); i++) {
                   // Double penality = myheroList.get(p);
                    rview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rview.setAdapter(new EmployeeAdapter(myheroList,Receipts.this));

                }


            }




            @Override
            public void onFailure(Call<List<DataAdapter>> call, Throwable t) {
                //resTv.setText(t.getMessage());
                //Toast.makeText(Receipts.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

   public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder>{

       String RPeriod;

       List<DataAdapter> emplist;
       Context context;
       public EmployeeAdapter(List<DataAdapter> productlist,Context context){
           this.emplist = productlist;
           this.context = context;
       }


       @NonNull
       @Override
       public EmployeeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View itemview;
           itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cust,parent,false);
           return new MyViewHolder(itemview);
       }

       @Override
       public void onBindViewHolder(@NonNull EmployeeAdapter.MyViewHolder holder, int position) {

           final DataAdapter adapter = emplist.get(position);

         //  holder.snotv.setText(adapter.getSino());
           holder.bnotv.setText(adapter.getBillno());
           holder.receiptno.setText(adapter.getReceiptno());
         //  holder.uid.setText(adapter.getUId());
           holder.property.setText(adapter.getProperty());
          // holder.rperiod.setText(adapter.getRperiod());
           //holder.ramnt.setText(adapter.getRamnt());
           holder.tamnt.setText(String.valueOf(adapter.getTamnt()));
           holder.tdue.setText(String.valueOf(adapter.getTdue()));
           holder.gstTv.setText(adapter.getGST());
           holder.ramnt.setText(adapter.getRentalamt());
           holder.paidamt.setText(adapter.getCurrentpaid());


         /*  Recptno = adapter.getReceiptno();
           Property = adapter.getProperty();
           RPeriod = adapter.getRperiod();
           Total = adapter.getTamnt();
           Paid = adapter.getCurrentpaid();
           Due = adapter.getTdue();


           Log.i("recDue", String.valueOf(Due));
           Log.i("RPeriod", String.valueOf(RPeriod));

*/
           String string = "2018-04-10T04:00:00.000Z";
           DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
           LocalDate date = LocalDate.parse(string, formatter);
          // Toast.makeText(context, String.valueOf(date), Toast.LENGTH_SHORT).show();

           /*DateTimeFormatter formatter = null;
           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
               formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
           }
           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
               date = LocalDate.parse(adapter.getRperiod(), formatter);
               Log.i("date", String.valueOf(date));
               holder.rperiod.setText(String.valueOf(date));
               //RPeriod = LocalDate.parse(adapter.getRperiod(), formatter);
           }*/
           Bundle bundle;
           bundle=new Bundle();

           holder.print.setOnClickListener(view -> {
               Intent intent = new Intent(context,Generatepadf.class);
               bundle.putString("name", custname);
               bundle.putString("uid", UserId);
               bundle.putString("recptno", emplist.get(holder.getAdapterPosition()).getReceiptno());
               bundle.putString("property", emplist.get(holder.getAdapterPosition()).getProperty());
               bundle.putString("Rperiod", emplist.get(holder.getAdapterPosition()).getRperiod());
               bundle.putDouble("Total",emplist.get(holder.getAdapterPosition()).getTamnt());
               bundle.putString("paid", emplist.get(holder.getAdapterPosition()).getCurrentpaid());
               bundle.putDouble("due", emplist.get(holder.getAdapterPosition()).getTdue());
               bundle.putDouble("Receiptpenality",PenalityAmt);
               bundle.putDouble("ReceiptgstAmt",GSTAmt);
               bundle.putString("authToken", authToken);
               intent.putExtras(bundle);

               context.startActivity(intent);
           });




       }

       @Override
       public int getItemCount() {
           return emplist.size();
       }

       public class MyViewHolder extends RecyclerView.ViewHolder {

           TextView snotv,bnotv,uid,property,rperiod,ramnt,tamnt,tdue,receiptno,paidamt,gstTv;
           Button print;
           LinearLayout receiptlyt,paidlyt,printbtnlyt,snoLyt;




           private static final int PERMISSION_REQUEST_CODE = 200;

           public MyViewHolder(@NonNull View itemView) {
               super(itemView);

               //bundle = new Bundle();

               context = itemView.getContext();
               snotv = itemView.findViewById(R.id.SNoTv);
               bnotv = itemView.findViewById(R.id.BNoTv);
               uid = itemView.findViewById(R.id.uidTv);
               property = itemView.findViewById(R.id.propertyTv);
               rperiod = itemView.findViewById(R.id.rperiodTv);
               ramnt = itemView.findViewById(R.id.RamntTv);
               tamnt = itemView.findViewById(R.id.TamntTv);
               tdue = itemView.findViewById(R.id.TDueTv);
               receiptno = itemView.findViewById(R.id.ReceiptNoTv);
               paidamt = itemView.findViewById(R.id.paidamntTv);
               gstTv = itemView.findViewById(R.id.GstTv);
               receiptlyt=itemView.findViewById(R.id.Receiptllyt);
               paidlyt = itemView.findViewById(R.id.paidamtllyt);
               printbtnlyt = itemView.findViewById(R.id.printllyt);
               snoLyt = itemView.findViewById(R.id.snoLlyt);
               print = itemView.findViewById(R.id.PrintBtn);


               receiptlyt.setVisibility(View.VISIBLE);
               paidlyt.setVisibility(View.VISIBLE);
               printbtnlyt.setVisibility(View.VISIBLE);
               snoLyt.setVisibility(View.GONE);


//               print.setOnClickListener(new View.OnClickListener() {
//                   @Override
//                   public void onClick(View view) {
//                       Intent intent = new Intent(context,Generatepadf.class);
//                       bundle.putString("name", custname);
//                       bundle.putString("uid", UserId);
//                       bundle.putString("recptno", Recptno);
//                       bundle.putString("property", Property);
//                       bundle.putString("Rperiod", RPeriod);
//                       bundle.putDouble("Total", Total);
//                       bundle.putString("paid", Paid);
//                       bundle.putDouble("due", emplist.get(getAdapterPosition()).getTdue());
//                       bundle.putString("authToken", authToken);
//                       intent.putExtras(bundle);
//                       context.startActivity(intent);
//                   }
//               });

           }
       }



   }


}