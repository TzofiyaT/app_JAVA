package com.example.messangerusers.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;

import androidx.room.TypeConverter;

import com.example.messangerusers.Entities.Enums;
import com.example.messangerusers.Entities.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

//theses are converters:
public class Converters {

    // LatLng Converters //

    @TypeConverter
    public static LatLng stringToLatLng(String fromRoom) {
        if (fromRoom != "") {
            String[] stringLatlong = fromRoom.split(",");
            double latitude = Double.parseDouble(stringLatlong[0]);
            double longitude = Double.parseDouble(stringLatlong[1]);
            LatLng latLng = new LatLng();
            latLng.setLatitude(latitude);
            latLng.setLongitude(longitude);
            return latLng;
        }
        return null;
    }

    @TypeConverter
    public static String latLngToString(LatLng latLng) {
        if (latLng!= null){
            return Location.convert(latLng.getLatitude(), Location.FORMAT_DEGREES) + "," + Location.convert(latLng.getLongitude(), Location.FORMAT_DEGREES);
        }
        return "";
    }


    public static String getStringAddressFromLatLng(Context context, LatLng latLng)
    {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            // May throw an IOException
            addresses = geocoder.getFromLocation(latLng.getLatitude(), latLng.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses == null)
                return null;

        } catch (IOException ex) {

            ex.printStackTrace();
            return null;
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        return address;
        /*String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();*/
    }

    public static LatLng getLatLngFromString(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng LatLan= null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            LatLan= new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return LatLan;
    }

    // Enums.ParcelWeight Converters //

    //convert weight enum to string
    @TypeConverter
    public static String parcelWeightEnumToString(Enums.ParcelWeight p)
    {
        String result = p.name().toString();
        return  result;
    }

    //convert string to weight enum
    @TypeConverter
    public static Enums.ParcelWeight stringToParcelWeightEnum(String s)
    {
        Enums.ParcelWeight result =Enums.ParcelWeight.valueOf(s);
        return result;
    }



    // Enums.ParcelStatus Converters //

    //convert status enum to string
    @TypeConverter
    public  static String parcelStatusEnumToString(Enums.ParcelStatus p)
    {
        String result =p.name().toString();
        return  result;
    }

    //convert string to status enum
    @TypeConverter
    public  static Enums.ParcelStatus stringToParcelStatusEnum(String s)
    {
        Enums.ParcelStatus result = Enums.ParcelStatus.valueOf(s);
        return result;
    }



    // Enums.ParcelType Converters //

    //convert ParcelType enum to string
    @TypeConverter
    public static String parcelTypeEnumToString(Enums.ParcelType p)
    {
        String result =p.name().toString();
        return result;
    }

    //convert string to ParcelType enum
    @TypeConverter
    public static Enums.ParcelType stringToParcelTypeEnum(String s)
    {
        Enums.ParcelType result = Enums.ParcelType.valueOf(s);
        return result;
    }

    @TypeConverter
    public String listToString(List<String> list) {
        if(list == null)
            return "";
        String joined = TextUtils.join(",", list);
        return joined;
    }

    @TypeConverter
    public List<String> stringToList(String string) {
        if(string.equals(""))
            return null;
        List<String> myList = new ArrayList<String>(Arrays.asList(string.split(",")));
        return myList;
    }
}