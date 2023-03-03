package com.example.tudaapp;

public class Post {

    private String BillNo;
    private String User;
    private String property;
    private String start_date;
    private String end_date;
    private String rental_start_date;
    private String Rental_lease_amount_permonth;
    private String GST;

    private double Total;

    private String penality_frequency;
    private String penality_percent_per_month;
    private double CurrentPaid;
    private double TotalPaid;

    private double Due;
    private String Status;
    private double GSTAmt;
    private double TotalPenalityAmt;
    private String ID;

    public Post(String ID, String BillNo, String User, String property, String start_date, String end_date,
                String rental_start_date, String Rental_lease_amount_permonth, String GST, double GSTAmt, double TotalPenalityAmt, double Total, String penality_frequency, String penality_percent_per_month,
                double CurrentPaid, double TotalPaid, double Due, String Status) {
        this.ID = ID;
        this.BillNo = BillNo;
        this.User=User;
        this.property = property;
        this.start_date = start_date;
        this.end_date = end_date;
        this.rental_start_date = rental_start_date;
        this.Rental_lease_amount_permonth = Rental_lease_amount_permonth;
        this.GST = GST;
        this.GSTAmt = GSTAmt;
        this.TotalPenalityAmt = TotalPenalityAmt;
        this.Total = Total;
        this.penality_frequency = penality_frequency;
        this.penality_percent_per_month = penality_percent_per_month;
        this.CurrentPaid = CurrentPaid;
        this.TotalPaid = TotalPaid;

        this.Due = Due;
        this.Status = Status;



    }

   /* private String penalityfreq;
    private String penalitypermnth;
    private String currentpaid;
    private String User;*/





}
