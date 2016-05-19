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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.OnClick;
import mx.spin.mobile.DrawerActivity;

import mx.spin.mobile.entitys.Tienda;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.utils.GeoLocalization;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import mx.spin.mobile.R;
import mx.spin.mobile.utils.UtilViews;
import mx.spin.mobile.utils.constants.Constants;
import mx.spin.mobile.utils.constants.JSKeys;

public class WhereBuyFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static String TAG = WhereBuyFragment.class.getName();

    private UtilViews utilViews;

    private GoogleMap map;
    private GeoLocalization geoLocalization;
    private LocationManager mLocManager;
    private Location mLastLocation;

    private ArrayList<LatLng> listadoTiendas = new ArrayList<>();

    private Boolean locationEnabled = false;

    private LatLng latLngSeleccionado = null;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100; // 5 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5; // 5 Segundos

    private GoogleApiClient mGoogleApiClient;

    @Nullable
    @Bind(R.id.dealderContent)
    View dealderContent;

    @Nullable
    @Bind(R.id.tv_dealderName)
    TextView dealderName;
    @Nullable
    @Bind(R.id.tv_dealderAddress)
    TextView dealderAddress;
    @Nullable
    @Bind(R.id.tv_dealderCity)
    TextView dealderCity;
    @Nullable
    @Bind(R.id.tv_dealderPhone)
    TextView dealderPhone;
    @Nullable
    @Bind(R.id.tv_dealderEmail)
    TextView dealderEmail;

    @Nullable
    @Bind(R.id.btnComollegar)
    Button btnComollegar;
    @Nullable
    @Bind(R.id.btnSendEmail)
    Button btnSendEmail;

    String email;
    List<Tienda> tiendaList = new ArrayList<>();

    private Integer REQUEST_CHECK_SETTINGS = 0;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_where_buy, container, false);
        ButterKnife.bind(this,rootView);

        utilViews = new UtilViews().getInstance(getContext());

        map = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

        //TODO SET FONTS
        dealderName.setTypeface(utilViews.setFontRegular());
        dealderAddress.setTypeface(utilViews.setFontNormal());
        dealderCity.setTypeface(utilViews.setFontNormal());
        dealderPhone.setTypeface(utilViews.setFontNormal());
        dealderEmail.setTypeface(utilViews.setFontNormal());

        mLocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


        if (map != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("MarkerClick:  ", marker.getTitle());
                if(!marker.getTitle().equals(Constants.MY_POSITION)){
                    latLngSeleccionado = marker.getPosition();
                    setDealderInfo(marker.getTitle());
                }else{
                    latLngSeleccionado = null;
                    ocultaDatos();
                }
                return false;
            }
        });

        return rootView;
    }

    @OnClick(R.id.btnSendEmail)
    public void sendMail(View view){
        sendEmail();
    }

    @OnClick(R.id.btnComollegar)
    public void getInfo(View view){
        if (latLngSeleccionado == null) {
            Toast.makeText(getActivity(), "Debe seleccionar una tienda en el mapa antes de comenzar el recorrido", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(Constants.MAPS_URL + latLngSeleccionado.latitude + "," + latLngSeleccionado.longitude + ""));
            startActivity(intent);
        }
    }


    void sendEmail(){
        try {
            if(email != null){
                String [] to = new String[]{email};
                String subject = "Solicitud de pedido";
                String message = "Estoy interesado en sus productos";
                Intent email = new Intent();

                email.setAction(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, to);
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);
                email.setType("text/plain");

                startActivity(Intent.createChooser(email, "Enviar email"));
                //Log.i(TAG, "finish");
                //  inish();
            }
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    void setDealderInfo(String name){
        Tienda datosTienda = getInfoTienda(name);
        try{
            if(datosTienda != null ){
                dealderContent.setVisibility(View.VISIBLE);
                dealderName.setText(" " + datosTienda.getNombre());
                dealderAddress.setText(" " + datosTienda.getDireccion());
                dealderCity.setText(" " + datosTienda.getCiudad());
                dealderPhone.setText(" " + datosTienda.getTelefono());
                dealderEmail.setText(" " + datosTienda.getEmail());
                email = datosTienda.getEmail();
            }else{
                email = null;
                ocultaDatos();
            }
        }catch (Exception ex){
            Log.e(TAG,ex.getMessage());
            email = null;
            ocultaDatos();
        }


    }

    void ocultaDatos(){
        email = null;
        dealderContent.setVisibility(View.GONE);
    }

    protected Tienda getInfoTienda(String name){
        Tienda mTienda = new Tienda();
        if(!tiendaList.isEmpty()) {
            Iterator<Tienda> iterator = tiendaList.iterator();
            while (iterator.hasNext()) {
                mTienda = iterator.next();
                if (mTienda.getNombre().equalsIgnoreCase(name)) {
                    System.out.println("encontro tienda");
                    break;
                }
            }
        }
        return mTienda;
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
            } else {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    LatLng posicion = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                    map.addMarker(new MarkerOptions().position(posicion).title("Aqui estoy"));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 16));
                    getDealders(posicion);
                } else {
                    Toast.makeText(getActivity(), "Localización vacía", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean checkLocation() {
        locationEnabled = true;
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
                        //  Log.d("JsonTiendas", responseString);
                        for (int i = 0; i < dealersArray.length(); i++) {

                            JSONObject tiendaJson = dealersArray.getJSONObject(i);

                            double latitude     = dealersArray.optJSONObject(i).getDouble(JSKeys.DEALDER_LAT);
                            double longitude    = dealersArray.optJSONObject(i).getDouble(JSKeys.DEALDER_LON);

                            LatLng latLng1 = new LatLng(latitude, longitude);
                            listadoTiendas.add(latLng1);

                           /* Realm realm = Realm.getInstance(getActivity());
                            realm.beginTransaction();
                            Tienda tienda = realm.createObject(Tienda.class);*/

                            Tienda tienda = new Tienda();

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

                           // realm.commitTransaction();

                            tiendaList.add(tienda);

                            map.addMarker(new MarkerOptions()
                                    .title(tienda.getNombre())
                                    .snippet(tienda.getDireccion())
                                    .position(latLng1));
                                   // .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
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

    protected void connectCliente(){
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        connectCliente();
    }

    @Override
    public void onStop() {
        if(mGoogleApiClient != null){
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //  outState.putSerializable("lista", (Serializable) tiendaList);
        super.onSaveInstanceState(outState);
    }
}
