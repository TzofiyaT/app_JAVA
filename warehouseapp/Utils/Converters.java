package com.example.warehouseapp.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.example.warehouseapp.Entity.Enums;
import com.example.warehouseapp.Entity.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Converters {

    public static String parcelTypeToString(Enums.ParcelType p)
    {
        String result =p.name();
        return result;
    }

    public static Enums.ParcelType stringToParcelType(String s)
    {
        Enums.ParcelType result =Enums.ParcelType.valueOf(s);
        return  result;
    }

    public static LatLng getLocationFromAddress(Context context, String strAddress) {

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
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        return address;
        /*String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();*/
    }
}