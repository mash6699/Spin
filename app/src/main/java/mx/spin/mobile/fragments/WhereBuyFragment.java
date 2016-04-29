package mx.spin.mobile.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import mx.spin.mobile.DrawerActivity;

import mx.spin.mobile.entitys.Tienda;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.utils.GeoLocalization;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import mx.spin.mobile.R;
import mx.spin.mobile.utils.constants.JSKeys;

public class WhereBuyFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static String TAG = WhereBuyFragment.class.getName();

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;

    private GoogleMap mMapa;
    private GeoLocalization geoLocalization;
    private ArrayList<LatLng> listadoTiendas = new ArrayList<>();
    private Button btnComollegar;
    private LocationManager mLocManager;
    private Location mLastLocation;
    private Boolean locationEnabled = false;

    private LatLng latLngSeleccionado = null;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100; // 5 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5; // 5 Segundos

    private GoogleApiClient mGoogleApiClient;
    /* @Nullable
     @Bind(R.id.infoMarcador)*/
    TextView infoTienda;

    TextView dealderName;
    TextView dealderAddress;
    TextView dealderCity;
    TextView dealderPhone;
    TextView dealderEmail;
    /* @Nullable
     @Bind(R.id.btnComollegar)*/
    Button getBtnComollegar;
    /* @Nullable
     @Bind(R.id.btnSendEmail)*/
    Button btnSendEmail;


    List<Tienda> tiendaList = new ArrayList<>();

    private Integer REQUEST_CHECK_SETTINGS = 0;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_where_buy, container, false);
        ButterKnife.bind(this,rootView);

        //  listadoTiendas = new ArrayList<>();

        mMapa = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        btnComollegar = (Button) rootView.findViewById(R.id.btnComollegar);
        btnSendEmail = (Button) rootView.findViewById(R.id.btnSendEmail);
        infoTienda = (TextView) rootView.findViewById(R.id.infoMarcador);

        dealderName     = (TextView) rootView.findViewById(R.id.tv_dealderName);
        dealderAddress  = (TextView) rootView.findViewById(R.id.tv_dealderAddress);
        dealderCity     = (TextView) rootView.findViewById(R.id.tv_dealderCity);
        dealderPhone    = (TextView) rootView.findViewById(R.id.tv_dealderPhone);
        dealderEmail    = (TextView) rootView.findViewById(R.id.tv_dealderEmail);

        txt_titleToolbar.setText("¿" + getResources().getString(R.string.title_buy) +"?");
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);

        mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerActivity.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        btnComollegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLngSeleccionado == null) {
                    Toast.makeText(getActivity(), "Debe seleccionar una tienda en el mapa antes de comenzar el recorrido", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=" + latLngSeleccionado.latitude + "," + latLngSeleccionado.longitude + ""));
                    startActivity(intent);
                }
            }
        });

        mMapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("MarcadorSel", marker.toString());
                latLngSeleccionado = marker.getPosition();
                System.out.println("MARKER ID --- Z " + marker.getId());
                infoTienda.setText(marker.getSnippet());

                setDealderInfo(marker);

                return false;
            }
        });

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendEmailIntent = new Intent(Intent.ACTION_SENDTO);

                sendEmailIntent.setData(Uri.parse("mailto:"));
                sendEmailIntent.setType("text/plain");

              //  sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, "miguel.ash.14@gmail.com");
                sendEmailIntent.putExtra(Intent.EXTRA_CC, "miguel.ash.14@gmail.com");
                sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                sendEmailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                try {
                    // startActivity(Intent.createChooser(sendEmailIntent, "Send mail..."));
                    // finish();
                    // if (sendEmailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendEmailIntent);
                    //  }
                    Log.i(TAG, "finish");
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }



            }
        });


        return rootView;
    }


    void setDealderInfo(Marker marker){
        dealderName.setText("");
        dealderAddress.setText("");
        dealderCity.setText("");
        dealderPhone.setText("");
        dealderEmail.setText("");
    }
    /*
// TODO HACER ESTA FUNCIONALIDAD
    Tienda getInfoTienda(Marker marker){
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;

       // if()

    }*/

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
//                final LocationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        mGoogleApiClient.connect();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.

                        break;
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (!checkLocation()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Los servicios de localizacion estan desactivados, ¿desea activarlos?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            getActivity().startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //TODO
            } else {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    LatLng posicion = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
//                    LatLng posicion = new LatLng(19.39, -99.14);
                    mMapa.addMarker(new MarkerOptions().position(posicion).title("Aqui estoy"));
                    mMapa.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 16));
                    getDealders(posicion);
                } else {
                    Toast.makeText(getActivity(), "Localización vacía", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    public boolean checkLocation() {
        locationEnabled = true;
        //mLocManager.removeUpdates(this);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
            return false;
        }
        if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        } else if (mLocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            mLocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        } else {
            locationEnabled = false;
        }
        return locationEnabled;
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    private void getDealders(final LatLng latLng) {
        NetConnection.getTiendas(latLng.latitude, latLng.longitude, new TextHttpResponseHandlerMessage() {
            @Override
            public void onStart() {
                super.onStart();
                showMessage(getActivity(), "Cargando tiendas");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                Toast.makeText(getActivity(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                try {

                    JSONObject jsonObject = new JSONObject(responseString);
                    Boolean success = jsonObject.getBoolean(JSKeys.SUCCESS);

                    if (success) {
                        JSONArray dealersArray = jsonObject.getJSONArray(JSKeys.DEALDERS);
                        Log.d("JsonTiendas", responseString);
                        for (int i = 0; i < dealersArray.length(); i++) {

                            JSONObject tiendaJson = dealersArray.getJSONObject(i);

                            double latitude     = dealersArray.optJSONObject(i).getDouble(JSKeys.DEALDER_LAT);
                            double longitude    = dealersArray.optJSONObject(i).getDouble(JSKeys.DEALDER_LON);

                            LatLng latLng1 = new LatLng(latitude, longitude);
                            listadoTiendas.add(latLng1);

                            Realm realm = Realm.getInstance(getActivity());
                            realm.beginTransaction();

                            Tienda tienda = realm.createObject(Tienda.class);

                            tienda.setPk(tiendaJson.optInt(JSKeys.DEALDER_ID));
                            tienda.setNombre(tiendaJson.optString(JSKeys.DEALDER));
                            tienda.setDireccion(tiendaJson.optString(JSKeys.DEALDER_ADDRESS));
                            tienda.setCodigoPostal(tiendaJson.optString(JSKeys.DEALDER_ZIPCODE));
                            tienda.setContacto(tiendaJson.optString(JSKeys.DEALDER_CONTACT));
                            tienda.setEmail(tiendaJson.optString(JSKeys.DEALDER_EMAIL));
                            tienda.setLat(tiendaJson.optString(JSKeys.DEALDER_LAT));
                            tienda.setLon(tiendaJson.optString(JSKeys.DEALDER_LON));
                            tienda.setTelefono(tiendaJson.optString(JSKeys.DEALDER_PHONE));
                            tienda.setCiudad(tiendaJson.optString(JSKeys.DEALDER_CITY));
                            tienda.setDireccion(tiendaJson.optString(JSKeys.DEALDER_ADDRESS));

                            realm.commitTransaction();

                            tiendaList.add(tienda);

                            mMapa.addMarker(new MarkerOptions()
                                    .position(latLng1)
                                    .title(tienda.getNombre())
                                    .snippet(
                                            tienda.getDireccion() + " \n Tel: " + tienda.getTelefono() + "\n Email: " + tienda.getEmail()
                                         //   tiendaJson.optString("dealer_address") + " \n Tel: " + tiendaJson.optString("dealer_phone") + "\n Email: " + tiendaJson.optString("dealer_email")
                                    ));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
