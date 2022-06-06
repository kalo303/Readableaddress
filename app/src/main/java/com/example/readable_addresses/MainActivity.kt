package com.example.readable_addresses
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.location.LocationListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     val mapFragment = supportFragmentManager

         .findFragmentById(R.id.myMap) as SupportMapFragment
          mapFragment.getMapAsync(this)
    }

    private lateinit var GMap:GoogleMap
    var locationManager:LocationManager?=null
     var locationListener :LocationListener?=null


        override fun onMapReady(googleMap: GoogleMap)
        {GMap = googleMap
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    GMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16f))
                    GMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))}
            }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)}

            else{
                locationManager!!.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER,0,0f,locationListener as android.location.LocationListener)
//                locationManager!!.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER,0,0f,locationListener as LocationListener)

                //Click Listener
                val myListener = object :GoogleMap.OnMapClickListener{
                    override fun onMapClick(location: LatLng?) {
                        GMap.clear()
                    val geocoder= Geocoder(applicationContext, Locale.getDefault())

                        var address =""
                        try {
                            val addressList = geocoder.getFromLocation(
                                location!!.latitude, location.longitude, 1
                            )

                            if (addressList != null && addressList.size > 0) {
                                if (addressList[0].thoroughfare != null) {
                                    address += addressList[0].thoroughfare
                                    if (addressList[0].subThoroughfare != null) {
                                        address += addressList[0].subThoroughfare
                                    }
                                }
                            }
                        }
                        catch (e:Exception){
                            e.printStackTrace()
                        }
                        if (address.equals("")){
                            address="No address is available"}
                        GMap.addMarker(MarkerOptions().position(location!!).
                        title(address))
                        }
                    }
                    }
            }
        }








