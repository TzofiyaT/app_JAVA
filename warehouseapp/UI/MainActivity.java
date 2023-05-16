package com.example.warehouseapp.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.warehouseapp.Entity.Enums;
import com.example.warehouseapp.Entity.LatLng;
import com.example.warehouseapp.Entity.Parcel;
import com.example.warehouseapp.R;
import com.example.warehouseapp.Utils.Converters;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference myRef;
    private Button FinishButton;
    private Spinner spinner;
    EditText editName;
    EditText editTelephone;
    EditText editEmail;
    EditText editAddress;
    EditText editDate;
    Switch sw;
    RadioGroup Weight;
    Calendar c;
    DatePickerDialog dt;
    Button ddate;
    Parcel parcel;

    //LOCATION
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        //findViewById
        ddate = (Button) findViewById(R.id.ddate);
        editName = (EditText) findViewById(R.id.editName);
        editTelephone = (EditText) findViewById(R.id.editTelephone);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editDate = (EditText) findViewById(R.id.editDate);
        FinishButton = findViewById(R.id.FinishButton);

        FinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the parcel
                getParcel();

                if(parcel == null)
                    return;

                //upload to FireBase
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("parcels");
                Snackbar.make(v, "Save", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                DatabaseReference myRef = database.getReference("parcels");

                String pushId = myRef.push().getKey();
                parcel.setFireBasePushId(pushId);
                myRef.child(pushId).setValue(parcel);

                //close this activity
                finish();
            }
        });
        ddate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                dt = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                        editDate.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                    }
                }, year, month, day);
                dt.show();
            }
        });


        //spinner
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Package_Type, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void getParcel() {
        //radio group
        Weight = (RadioGroup) findViewById(R.id.radioWeight);
        String stringParcelWeight = ((RadioButton) findViewById(Weight.getCheckedRadioButtonId())).getText().toString();

        //switch
        sw = (Switch) findViewById(R.id.switch1);

        //check if EditTexts are empty

        boolean allFieldsAreFull = true;


        if(isEmpty(editName))
        {
            allFieldsAreFull = false;
            editName.setError("Please fill in the recipient name");
        }
        if(isEmpty(editEmail))
        {
            allFieldsAreFull = false;
            editEmail.setError("Please fill in the recipient email");
        }
        if(isEmpty(editTelephone))
        {
            allFieldsAreFull = false;
            editTelephone.setError("Please fill in the recipient's cellphone number");
        }
        if(isEmpty(editAddress))
        {
            allFieldsAreFull = false;
            editAddress.setError("Please enter the recipient's address");
        }
        if(isEmpty(editDate))
        {
            allFieldsAreFull = false;
            editDate.setError("Please fill in the parcel arrival date");
        }

        if(currentLocation == null)
        {
            allFieldsAreFull = false;
            Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        if(allFieldsAreFull == false)
        {
            parcel = null;
            return;
        }

        //get parcel data
        String recipientEmail = editEmail.getText().toString();
        String recipientPhone = editTelephone.getText().toString();
        boolean isFragile = sw.isChecked();
        Enums.ParcelWeight parcelWeight = Enums.ParcelWeight.valueOf(stringParcelWeight);
        LatLng warehouseLocation = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        String recipientName = editName.getText().toString();
        Enums.ParcelType parcelType = Converters.stringToParcelType(spinner.getSelectedItem().toString());
        String dateReceived = editDate.getText().toString();
        String uploadDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        LatLng recipientAddress = Converters.getLocationFromAddress(this, editAddress.getText().toString());
        if(recipientAddress == null)
        {
            editAddress.setError("Please enter a valid address");
            return;
        }

        parcel = new Parcel(recipientEmail,recipientPhone,parcelType,isFragile,parcelWeight,warehouseLocation,recipientName,recipientAddress,dateReceived,uploadDate);
    }

    private Boolean isEmpty(EditText etText)
    {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //Location Functions

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    currentLocation = location;
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            currentLocation = mLastLocation;
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

}