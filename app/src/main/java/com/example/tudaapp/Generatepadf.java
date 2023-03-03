package com.example.tudaapp;

import androidx.appcompat.app.AppCompatActivity;

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
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Generatepadf extends AppCompatActivity {

    Button generate;
    Bundle b;
    String CustName,Userid,ReceiptNo,Property,Period,Paid,MobileNO,PANno,authToken;
    TextView nametv,uidtv,pantv,mbo,receipttv,periodtv,propertytv,totaltv,paidtv,duetv;
    Double Due,Total,GStamt,Penalityamt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generatepadf);

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

        generate = findViewById(R.id.generateBtn);




        b = new Bundle();
        b = getIntent().getExtras();

        CustName = b.getString("name");
        Userid = b.getString("uid");
        ReceiptNo = b.getString("recptno");
        Property = b.getString("property");
        Period = b.getString("Rperiod");
        Total = b.getDouble("Total");
        Paid = b.getString("paid");
        Due = b.getDouble("due");
       GStamt = b.getDouble("ReceiptgstAmt");
       Penalityamt = b.getDouble("Receiptpenality");
        authToken = b.getString("authToken");

        Log.i("details",CustName);
        Log.i("Userid",Userid);
        Log.i("ReceiptNo",ReceiptNo);
        Log.i("Property",Property);
        Log.i("Period",Period);
        Log.i("Total", String.valueOf(Total));
        Log.i("Paid",Paid);
        Log.i("Due", String.valueOf(Due));
        Log.i("GStamt", String.valueOf(GStamt));
        Log.i("Penalityamt", String.valueOf(Penalityamt));
        Log.i("recauthToken",authToken);


        for (String key : b.keySet())
        {
            Log.i("Bundle Debug", key + " = \"" + b.get(key) + "\"");
        }


        nametv.setText(CustName);
        uidtv.setText(Userid);
        receipttv.setText(ReceiptNo);
        propertytv.setText(Property);
        periodtv.setText(Period);
        totaltv.setText(Total.toString());
        paidtv.setText(Paid);
        duetv.setText(Due.toString());


        getusedatails();


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createpdf();
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
                   // mbo.setText(MobileNO);
                  //  pantv.setText(PANno);
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

    private void createpdf() throws FileNotFoundException {

        Toast.makeText(this, "entered into Createpdf Method", Toast.LENGTH_SHORT).show();
        String pdfpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfpath,"mustcome.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);



        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(2,12,0,12);

        Paragraph Receipt = new Paragraph("Receipt").setBold().setFontSize(24).setTextAlignment(TextAlignment.CENTER);

        Paragraph group2 = new Paragraph("Receipt Number:"+""+ReceiptNo).setFontSize(20).setTextAlignment(TextAlignment.RIGHT);
        Paragraph custDetails = new Paragraph("Customer Details:").setFontSize(20).setTextAlignment(TextAlignment.JUSTIFIED);

        Paragraph group = new Paragraph("Name:"+""+CustName+"\n"+"User Id:"+Userid+"\n"+"PAN No:"+PANno+"\n"+"Mobile No:"+MobileNO).setFontSize(20);

        float[] columnWidtha = new float[]{62, 162, 112, 112, 112, 112};

        Table table = new Table(columnWidtha);

        table.addCell(new Cell().add(new Paragraph("Property"))).setFontSize(20);
        table.addCell(new Cell().add(new Paragraph("Period"))).setFontSize(20);
        table.addCell(new Cell().add(new Paragraph("Rent"))).setFontSize(20);
        table.addCell(new Cell().add(new Paragraph("Total"))).setFontSize(20);
        table.addCell(new Cell().add(new Paragraph("Paid"))).setFontSize(20);
        table.addCell(new Cell().add(new Paragraph("Due"))).setFontSize(20);
      //  table.addCell(new Cell().add(new Paragraph("Penality"))).setFontSize(20);
      //  table.addCell(new Cell().add(new Paragraph("GSTAmt"))).setFontSize(20);


        table.addCell(new Cell().add(new Paragraph(Property))).setFontSize(20);
        table.addCell(new Cell().add(new Paragraph("2022-01-10"))).setFontSize(20);
        table.addCell(new Cell().add(new Paragraph(String.valueOf(Total)))).setFontSize(20);
        table.addCell(new Cell().add(new Paragraph(Paid))).setFontSize(20);
        table.addCell(new Cell().add(new Paragraph(String.valueOf(Due)))).setFontSize(20);
        table.addCell(new Cell().add(new Paragraph(String.valueOf(Due)))).setFontSize(20);
        //table.addCell(new Cell().add(new Paragraph(String.valueOf(Penalityamt)))).setFontSize(20);
       // table.addCell(new Cell().add(new Paragraph(String.valueOf(GStamt)))).setFontSize(20);



        document.add(Receipt);
        document.add(group2);
        document.add(custDetails);
        document.add(group);

        document.add(table);
        document.close();

        Toast.makeText(this, "pdf created", Toast.LENGTH_SHORT).show();


    }

}