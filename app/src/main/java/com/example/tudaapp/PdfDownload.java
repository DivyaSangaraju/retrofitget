package com.example.tudaapp;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class PdfDownload extends AppCompatActivity {
    Bundle b;
    String CustName,Userid,MobileNO,PANno,Property,Period,Total,Paid,Rent,Due,ReceiptNo,authToken;
    Button generatePDFbtn;
    TextView nametv,uidtv,pantv,mbo,receipttv,periodtv,propertytv,totaltv,paidtv,duetv;
    private static final int PERMISSION_REQUEST_CODE = 200;
    LocalDate date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_download);
        generatePDFbtn = findViewById(R.id.idBtnGeneratePDF);
        nametv = findViewById(R.id.custname);
        uidtv = findViewById(R.id.useridTv);
        pantv = findViewById(R.id.Pan);
        mbo = findViewById(R.id.MNoTv);
        receipttv = findViewById(R.id.receiptTv);
        propertytv = findViewById(R.id.PropertyTv);
        periodtv = findViewById(R.id.PeriodTv);
        totaltv = findViewById(R.id.totalTv);
        paidtv = findViewById(R.id.paidTv);
        duetv = findViewById(R.id.dueTv);

        b = new Bundle();
        b = getIntent().getExtras();




        CustName = b.getString("name");
        Userid = b.getString("uid");
        ReceiptNo = b.getString("recptno");
        Property = b.getString("property");
        Period = b.getString("Rperiod");
        Total = b.getString("Total");
        Paid = b.getString("paid");
        Due = b.getString("due");
        authToken = b.getString("authToken");



        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            date = LocalDate.parse(Period, formatter);
            Log.i("date", String.valueOf(date));

        }
        Log.i("cus", CustName);
        Log.i("Userid", Userid);

        nametv.setText(CustName);
        uidtv.setText(Userid);
        receipttv.setText(ReceiptNo);
        propertytv.setText(Property);
        periodtv.setText(Property);
        totaltv.setText(Total);
        paidtv.setText(Paid);
        duetv.setText(Due);

        getusedatails();

        generatePDFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    generatePDF();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }



    private void getusedatails() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://tuda-env.eba-d3qkpvh2.ap-south-1.elasticbeanstalk.com/getuserdetailsById/"+Userid, response -> {

            Log.i("username",response);

            try {
                JSONArray jsonarray = new JSONArray(response);
                for(int i=0; i < jsonarray.length(); i++) {

                    JSONObject jobj = jsonarray.getJSONObject(i);

                    MobileNO = jobj.getString("MOBILE_NUM");
                    PANno = jobj.getString("ID_No");
                    mbo.setText(MobileNO);
                    pantv.setText(PANno);
                   // username.setText(name);

                   /* MobileNo = jobj.getString("");
                    PANNo = jobj*/

                    Log.i("MobileNO", MobileNO);
                    Log.i("PANno", PANno);
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
                String bearer = "Bearer ".concat(authToken);
                params.put("Authorization", bearer);

                Log.i("userautparams", String.valueOf(params));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        requestQueue.add(stringRequest);


    }

    private void generatePDF() throws FileNotFoundException {

      //  Toast.makeText(this, "generate pdf called", Toast.LENGTH_SHORT).show();
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath,"Receipt.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter pdfWriter = new PdfWriter(file);
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);


        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(2,8,0,8);

        Paragraph Receipt = new Paragraph("Receipt").setBold().setFontSize(24).setTextAlignment(TextAlignment.CENTER);

        Paragraph group2 = new Paragraph("Receipt Number:"+""+ReceiptNo).setFontSize(20).setTextAlignment(TextAlignment.RIGHT);
        Paragraph custDetails = new Paragraph("Customer Details").setFontSize(20).setTextAlignment(TextAlignment.JUSTIFIED);

        Paragraph group = new Paragraph("Name:"+""+CustName+"\n"+"User Id:"+Userid+"\n"+"PAN No:"+PANno+"\n"+"Mobile No:"+MobileNO).setFontSize(16);

        float[] columnWidtha = new float[]{62, 162, 112, 112, 112, 112};

        Table table = new Table(columnWidtha);

        table.addCell(new Cell().add(new Paragraph("Property")));
        table.addCell(new Cell().add(new Paragraph("Period")));
        table.addCell(new Cell().add(new Paragraph("Rent")));
        table.addCell(new Cell().add(new Paragraph("Total")));
        table.addCell(new Cell().add(new Paragraph("Paid")));
        table.addCell(new Cell().add(new Paragraph("Due")));

        table.addCell(new Cell().add(new Paragraph(Property)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(date))));
        table.addCell(new Cell().add(new Paragraph(Total)));
        table.addCell(new Cell().add(new Paragraph(Paid)));
        table.addCell(new Cell().add(new Paragraph(Due)));
        table.addCell(new Cell().add(new Paragraph(Due)));



        document.add(Receipt);


        document.add(group2);

        document.add(custDetails);
        document.add(group);

        document.add(table);

        document.close();

        Toast.makeText(this, "pdf created", Toast.LENGTH_SHORT).show();


    }
}