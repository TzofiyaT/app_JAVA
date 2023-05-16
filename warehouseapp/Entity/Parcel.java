package com.example.warehouseapp.Entity;

import java.util.ArrayList;
import java.util.List;

public class Parcel {
    private Enums.ParcelType parcelType;
    private boolean isFragile;
    private Enums.ParcelWeight parcelWeight;
    private LatLng warehouseLocation;
    private String recipientName;
    private LatLng recipientAddress;
    private String recipientEmail;
    private String recipientPhone;
    private String dateReceived;
    private String shippingDate;
    private Enums.ParcelStatus parcelStatus;
    private String deliveryPersonName;
    private String fireBasePushId;
    private List<String> possibleDeliveryPersonsList;
    private String uploadDate;

    public Parcel()
    {
        warehouseLocation = null;
        recipientAddress = null;
        this.shippingDate = "";
        this.deliveryPersonName = "NO";
        this.fireBasePushId = "";
        this.possibleDeliveryPersonsList = new ArrayList<>();
        this.possibleDeliveryPersonsList.add("null");
    }

    public Parcel(String recipientEmail, String recipientPhone, Enums.ParcelType parcelType, boolean isFragile, Enums.ParcelWeight parcelWeight, LatLng warehouseLocation, String recipientName, LatLng recipientAddress, String dateReceived, String uploadDate) {
        this.recipientEmail = recipientEmail;
        this.recipientPhone = recipientPhone;
        this.parcelType = parcelType;
        this.isFragile = isFragile;
        this.parcelWeight = parcelWeight;
        this.warehouseLocation = warehouseLocation;
        this.recipientName = recipientName;
        this.recipientAddress = recipientAddress;
        this.dateReceived = dateReceived;
        this.shippingDate = "";
        this.parcelStatus = Enums.ParcelStatus.sent;
        this.deliveryPersonName = "NO";
        this.fireBasePushId = "";
        this.possibleDeliveryPersonsList = new ArrayList<>();
        this.possibleDeliveryPersonsList.add("null");
        this.uploadDate = uploadDate;
    }

    //Copy Constructor
    public Parcel(Parcel parcel) {
        this.parcelType = parcel.parcelType;
        this.isFragile = parcel.isFragile;
        this.parcelWeight = parcel.parcelWeight;
        this.warehouseLocation = parcel.warehouseLocation;
        this.recipientName = parcel.recipientName;
        this.recipientAddress = parcel.recipientAddress;
        this.recipientEmail = parcel.recipientEmail;
        this.recipientPhone = parcel.recipientPhone;
        this.dateReceived = parcel.dateReceived;
        this.shippingDate = parcel.shippingDate;
        this.parcelStatus = parcel.parcelStatus;
        this.deliveryPersonName = parcel.deliveryPersonName;
        this.fireBasePushId = parcel.fireBasePushId;
        this.possibleDeliveryPersonsList = parcel.possibleDeliveryPersonsList;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public Enums.ParcelType getParcelType() {
        return parcelType;
    }

    public void setParcelType(Enums.ParcelType parcelType) {
        this.parcelType = parcelType;
    }

    public boolean isFragile() {
        return isFragile;
    }

    public void setFragile(boolean fragile) {
        isFragile = fragile;
    }

    public Enums.ParcelWeight getParcelWeight() {
        return parcelWeight;
    }

    public void setParcelWeight(Enums.ParcelWeight parcelWeight) {
        this.parcelWeight = parcelWeight;
    }

    public LatLng getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(LatLng warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public LatLng getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(LatLng recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(String shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Enums.ParcelStatus getParcelStatus() {
        return parcelStatus;
    }

    public void setParcelStatus(Enums.ParcelStatus parcelStatus) {
        this.parcelStatus = parcelStatus;
    }

    public String getDeliveryPersonName() {
        return deliveryPersonName;
    }

    public void setDeliveryPersonName(String deliveryPersonName) {
        this.deliveryPersonName = deliveryPersonName;
    }


    @Override
    public String toString() {
        return "Parcel{" +
                "parcelType=" + parcelType +
                ", isFragile=" + isFragile +
                ", parcelWeight=" + parcelWeight +
                ", warehouseLocation=" + warehouseLocation +
                ", recipientName='" + recipientName + '\'' +
                ", recipientAddress='" + recipientAddress + '\'' +
                ", recipientEmail='" + recipientEmail + '\'' +
                ", recipientPhone='" + recipientPhone + '\'' +
                ", dateReceived='" + dateReceived + '\'' +
                ", shippingDate='" + shippingDate + '\'' +
                ", parcelStatus=" + parcelStatus +
                ", deliveryPersonName='" + deliveryPersonName + '\'' +
                '}';
    }

    public String getFireBasePushId() {
        return fireBasePushId;
    }

    public void setFireBasePushId(String fireBasePushId) {
        this.fireBasePushId = fireBasePushId;
    }

    public List<String> getPossibleDeliveryPersonsList() {
        return possibleDeliveryPersonsList;
    }

    public boolean addPossibleDeliveryPerson(String deliveryPerson) {
        if(this.possibleDeliveryPersonsList.contains(deliveryPerson))
            return false;

        this.possibleDeliveryPersonsList.add(deliveryPerson);
        return true;
    }

    public void setPossibleDeliveryPersonsList(List<String> possibleDeliveryPersonsList) {
        this.possibleDeliveryPersonsList = possibleDeliveryPersonsList;
    }

        public String getUploadDate() {
            return uploadDate;
        }

        public void setUploadDate(String uploadDate) {
            this.uploadDate = uploadDate;
        }
}
