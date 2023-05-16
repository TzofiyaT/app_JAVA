package com.example.warehouseapp.Entity;

public class Enums {

    public enum ParcelStatus{
        sent,
        someoneOfferedToTake,
        inWay,
        received,
    }
    public enum ParcelType{
        envelope,
        small,
        large,
    }
    public enum ParcelWeight{
        under500gr,
        under1kg,
        under5Kg,
        under20Kg,
        Huge,
    }
}


