package mx.spin.mobile.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Entity mapped to table "DEALERS".
 */
public class dealers implements Parcelable{

    private Long id;
    private Integer dealer_id;
    private String dealer;
    private String dealer_address;
    private String dealer_zipcode;
    private String dealer_contact;
    private String dealer_email;
    private String dealer_lat;
    private String dealer_lon;
    private String dealer_phone;
    private String dealer_mobile;
    private String dealer_city;
    private String dealer_sale;
    private String distance;

    public dealers() {
    }

    public dealers(Long id) {
        this.id = id;
    }

    public dealers(Long id, Integer dealer_id, String dealer, String dealer_address, String dealer_zipcode, String dealer_contact, String dealer_email, String dealer_lat, String dealer_lon, String dealer_phone, String dealer_mobile, String dealer_city, String dealer_sale, String distance) {
        this.id = id;
        this.dealer_id = dealer_id;
        this.dealer = dealer;
        this.dealer_address = dealer_address;
        this.dealer_zipcode = dealer_zipcode;
        this.dealer_contact = dealer_contact;
        this.dealer_email = dealer_email;
        this.dealer_lat = dealer_lat;
        this.dealer_lon = dealer_lon;
        this.dealer_phone = dealer_phone;
        this.dealer_mobile = dealer_mobile;
        this.dealer_city = dealer_city;
        this.dealer_sale = dealer_sale;
        this.distance = distance;
    }

    protected dealers(Parcel in) {
        dealer = in.readString();
        dealer_address = in.readString();
        dealer_zipcode = in.readString();
        dealer_contact = in.readString();
        dealer_email = in.readString();
        dealer_lat = in.readString();
        dealer_lon = in.readString();
        dealer_phone = in.readString();
        dealer_mobile = in.readString();
        dealer_city = in.readString();
        dealer_sale = in.readString();
        distance = in.readString();
    }

    public static final Creator<dealers> CREATOR = new Creator<dealers>() {
        @Override
        public dealers createFromParcel(Parcel in) {
            return new dealers(in);
        }

        @Override
        public dealers[] newArray(int size) {
            return new dealers[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(Integer dealer_id) {
        this.dealer_id = dealer_id;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getDealer_address() {
        return dealer_address;
    }

    public void setDealer_address(String dealer_address) {
        this.dealer_address = dealer_address;
    }

    public String getDealer_zipcode() {
        return dealer_zipcode;
    }

    public void setDealer_zipcode(String dealer_zipcode) {
        this.dealer_zipcode = dealer_zipcode;
    }

    public String getDealer_contact() {
        return dealer_contact;
    }

    public void setDealer_contact(String dealer_contact) {
        this.dealer_contact = dealer_contact;
    }

    public String getDealer_email() {
        return dealer_email;
    }

    public void setDealer_email(String dealer_email) {
        this.dealer_email = dealer_email;
    }

    public String getDealer_lat() {
        return dealer_lat;
    }

    public void setDealer_lat(String dealer_lat) {
        this.dealer_lat = dealer_lat;
    }

    public String getDealer_lon() {
        return dealer_lon;
    }

    public void setDealer_lon(String dealer_lon) {
        this.dealer_lon = dealer_lon;
    }

    public String getDealer_phone() {
        return dealer_phone;
    }

    public void setDealer_phone(String dealer_phone) {
        this.dealer_phone = dealer_phone;
    }

    public String getDealer_mobile() {
        return dealer_mobile;
    }

    public void setDealer_mobile(String dealer_mobile) {
        this.dealer_mobile = dealer_mobile;
    }

    public String getDealer_city() {
        return dealer_city;
    }

    public void setDealer_city(String dealer_city) {
        this.dealer_city = dealer_city;
    }

    public String getDealer_sale() {
        return dealer_sale;
    }

    public void setDealer_sale(String dealer_sale) {
        this.dealer_sale = dealer_sale;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dealer);
        parcel.writeString(dealer_address);
        parcel.writeString(dealer_zipcode);
        parcel.writeString(dealer_contact);
        parcel.writeString(dealer_email);
        parcel.writeString(dealer_lat);
        parcel.writeString(dealer_lon);
        parcel.writeString(dealer_phone);
        parcel.writeString(dealer_mobile);
        parcel.writeString(dealer_city);
        parcel.writeString(dealer_sale);
        parcel.writeString(distance);
    }
}