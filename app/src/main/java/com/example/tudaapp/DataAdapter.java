package com.example.tudaapp;

import com.google.gson.annotations.SerializedName;

public class DataAdapter {

    private String sino;
    @SerializedName("BillNo")
    private String billno;
    private String UId;
    @SerializedName("property")
    private String Property;

    @SerializedName("rental_period")
    private String rperiod;

    private String ramnt;

    /*@SerializedName("Total")
    private String tamnt;
    @SerializedName("Due")
    private String tdue;*/


    @SerializedName("Total")
    private double tamnt;
    @SerializedName("Due")
    private double tdue;

    private double total;

    private String status;
    private String sdate;
    private String edate;
    //private String rentalsdate;
    @SerializedName("Rental_lease_amount_permonth")
    private String rentalleaseamt;

    @SerializedName("GST")
    private String gst;


    private String penalityfreq;
    private String penalitypermnth;
    @SerializedName("CurrentPaid")
    private String currentpaid;

    private String id;
    @SerializedName("ReceiptNo")
    private String receiptno;
    private String name;
    @SerializedName("User")
    private String User;
    private Double GstAmount;
    private Double TotalPenalityAmt;

//    public String getName() {return name;}
//
//    public void setName(String Name) {
//        this.name = Name;
//    }

    public String getUser() {return User;}

    public void setUser(String User) {
        this.User = User;
    }

    public String getReceiptno() {return receiptno;}

    public void setReceiptno(String receiptno) {
        this.receiptno = receiptno;
    }

    public String getId() {return id;}

    public void setId(String ID) {this.id = ID;}

    public String getSino() {return sino;}

    public void setSino(String sno) {this.sino = sno;}

    public String getBillno() {return billno;}

    public void setBillno(String bno) {this.billno = bno;}

    public String getUId() {return UId;}

    public void setUId(String userid) {this.UId = userid;}

    public String getProperty(){return Property;}

    public void setProperty(String property) {
        Property = property;
    }

    public String getRperiod(){return rperiod;}

    public void setRperiod(String rperiod) {
        this.rperiod = rperiod;
    }

    public String getRamnt(){return ramnt;}

    public void setRamnt(String ramnt) {
        this.ramnt = ramnt;
    }

    public double getTamnt(){return tamnt;}

    public void setTamnt(double tamnt) {
        this.tamnt = tamnt;
    }

    public double getTdue(){return tdue;}

    public void setTdue(double tdue) {
        this.tdue = tdue;
    }

    public String getStatus(){return status;}

    public void setStatus(String status) {
        this.status = status;
    }


    public String getSDate(){return sdate;}

    public void setSDate(String startdate) {
        this.sdate = startdate;
    }

    public String getEDate(){return edate;}

    public void setEDate(String startdate) {
        this.edate = startdate;
    }

  /*  public String getRSDate(){return rentalsdate;}

    public void setRSDate(String rentalstartdate) {
        this.rentalsdate = rentalstartdate;
    }*/

    public String getRentalamt(){return rentalleaseamt;}
    public void setRentalamt(String Leaseamt) {
        this.rentalleaseamt = Leaseamt;
    }

    public String getGST(){return gst;}
    public void setGST(String GST) {
        this.gst = GST;
    }

    public double getTotal(){return total;}
    public void setTotal(double Total) {
        this.total = Total;
    }

    public String getPenalityfreq(){return penalityfreq;}
    public void setPenalityfreq(String Penality) {
        this.penalityfreq = Penality;
    }

    public String getPenalitypmnth(){return penalitypermnth;}
    public void setPenalitypmnth(String Penalitymnth) {
        this.penalitypermnth = Penalitymnth;
    }

    public String getCurrentpaid(){return currentpaid;}
    public void setCurrentpaid(String current) {
        this.currentpaid = current;
    }

    public Double getGstAmtount(){return GstAmount;}
    public void setGstAmount(Double gstamt) {
        this.GstAmount = gstamt;
    }
    public Double getTotalPenalityAmt(){return TotalPenalityAmt;}
    public void setTotalPenalityAmt(Double totalpenamt) {
        this.TotalPenalityAmt = totalpenamt;
    }


}
