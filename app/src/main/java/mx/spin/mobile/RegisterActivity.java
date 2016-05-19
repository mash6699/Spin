package mx.spin.mobile;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.User;
import mx.spin.mobile.entitys.Estado;
import mx.spin.mobile.entitys.Pais;
import mx.spin.mobile.entitys.pojo.UsuarioReg;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.utils.SpinUtility;
import mx.spin.mobile.utils.UtilViews;
import mx.spin.mobile.utils.constants.Constants;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;
import mx.spin.mobile.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import mx.spin.mobile.utils.constants.JSKeys;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = RegisterActivity.class.getName();
    private BoussinesSpin boussinesSpin;
    private UsuarioReg usuarioReg = new UsuarioReg();
    private SpinUtility spinUtility;
    private UtilViews utilViews;

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;

    @Nullable
    @Bind(R.id.txtYaTengoCuenta)
    TextView txtYaTengoCuenta;
    @Nullable
    @Bind(R.id.nombreUsuario)
    EditText nombre;
    @Nullable
    @Bind(R.id.emailUsuario)
    EditText email;
    @Nullable
    @Bind(R.id.contrasenaUsuario)
    EditText contrasena;
    @Nullable
    @Bind(R.id.telefonoUsuario)
    EditText telefono;
    @Nullable
    @Bind(R.id.correoCambiar)
    EditText emailForget;

    @Nullable
    @Bind(R.id.spinnerPais)
    Spinner spinerPais;
    @Nullable
    @Bind(R.id.spinnerEstado)
    Spinner spinerEstados;
    @Nullable
    @Bind(R.id.imagePerfil)
    CircleImageView imagePerfil;
    @Nullable
    @Bind(R.id.btnRegistrate)
    Button btnRegistrate;

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private ArrayList<Pais> listadoPaises = new ArrayList<>();
    private ArrayList<Estado> listadoEstados = new ArrayList<>();
    private ArrayAdapter<String> adapterPaises;
    private ArrayAdapter<String> adapterEstados;
    private Integer paisSeleccionado = null;
    private Integer estadoSeleccionado = null;

    private Bitmap imageFile = null;

    @Override
    protected void onStart() {
        super.onStart();
        if (NetConnection.isOnline(this,true)){
            obtenerPaises();
            obtenerEstados();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinUtility = SpinUtility.getInstance();
        utilViews = new UtilViews().getInstance(getApplication());
        boussinesSpin = new BoussinesSpin(this);
        initSpinners();
        setActions();
    }


    void setActions(){
        Log.d(TAG, "setActions");

        spinerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paisSeleccionado = position;
                if (listadoPaises.get(position).getNombre().equals(Constants.KEY_MEX)) {
                    spinerEstados.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estadoSeleccionado = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imagePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);*/
                Toast.makeText(getApplication(),"WIP", Toast.LENGTH_LONG).show();
            }
        });


        txtYaTengoCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
                try {
                    finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        btnRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //cleanUser();
//                if (imageFile != null){
                if(!validateUserData()){
                    //registrarUsuario(nombre.getText().toString(),email.getText().toString(),contrasena.getText().toString(),telefono.getText().toString(), Constants.DEFAULT_LOGIN,listadoPaises.get(paisSeleccionado).getPk()+"",listadoEstados.get(estadoSeleccionado).getPk()+"");
                    registrarUsuario(usuarioReg);
                }else{
                 //   Toast.makeText(RegisterActivity.this,"Ingresa la información requerida.",Toast.LENGTH_SHORT).show();
                    utilViews.showToastInView("Ingresa la información requerida.");
                }
                //                }else{
//                    Toast.makeText(RegisterActivity.this,"Debe subir una foto",Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }


    boolean validateUserData(){
        boolean estatus = false;
        View focusView = null;

        try{
            String nom      = nombre.getText().toString().trim();
            String ema      = email.getText().toString().trim();
            String pas      = contrasena.getText().toString().trim();
            String tel      = telefono.getText().toString().trim();
            String idPais   = listadoPaises.get(paisSeleccionado).getPk()+"";
            String idEdo    = listadoEstados.get(estadoSeleccionado).getPk()+"";
            String pais     = listadoPaises.get(paisSeleccionado).getNombre();
            String edo      = listadoEstados.get(estadoSeleccionado).getNombre();
            String token    = spinUtility.getValueDataStorage(getApplicationContext(), SpinUtility.ANDROID_TOKEN);
            String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


            //TODO SET VALUES USER REG
            usuarioReg.setNombre(nom);
            usuarioReg.setEmail(ema);
            usuarioReg.setIdPais(idPais);
            usuarioReg.setNombrePais(pais);
            usuarioReg.setIdEstado(idEdo);

            usuarioReg.setTelefono(tel);
            usuarioReg.setToken(token);
            usuarioReg.setDiviceId(deviceId);

            if(listadoPaises.get(paisSeleccionado).getNombre().equals(Constants.KEY_MEX)){
                usuarioReg.setNombreEstado(edo);
            }else{
                usuarioReg.setNombreEstado(" ");
            }
            if(TextUtils.isEmpty(nom)){
                nombre.setError("Debes ingresar tu nombre");
                focusView = nombre;
                estatus = true;
            }
            if(TextUtils.isEmpty(ema) || !Utils.isEmailValid(ema)){
                email.setError("Debes ingresar un email válido");
                focusView = email;
                estatus = true;
            }

            if(TextUtils.isEmpty(pas) || !Utils.isPasswordValid(pas)){
                contrasena.setError("Debes ingresar una contraseña mayor a 4 caracteres");
                focusView = contrasena;
                estatus = true;
            }

            if(TextUtils.isEmpty(tel) || tel.length() <= 8){
                telefono.setError("Debes ingresar un teléfono válido");
                focusView = telefono;
                estatus = true;
            }

        }catch (Exception e){
            System.out.println("ERROR::: " + e.getMessage());
        }

        return estatus;
    }

    void initSpinners(){
        Log.d(TAG, "initSpinners");
        adapterPaises   = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapterEstados  = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        spinerPais.setAdapter(adapterPaises);
        spinerEstados.setAdapter(adapterEstados);
    }


    public void sendMain(View view){
        startActivity(new Intent(RegisterActivity.this, DrawerActivity.class));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Log.d("ImagePath",selectedImagePath);
                imagePerfil.setImageURI(selectedImageUri);
            }
//            Bitmap imageBitmap = (Bitmap) data.getDa;
//            imageFile = imageBitmap;
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void obtenerPaises(){
        NetConnection.obtenerPaises(new TextHttpResponseHandlerMessage() {
            @Override
            public void onStart() {
                super.onStart();
                showMessage(RegisterActivity.this,getString(R.string.msg_load));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                utilViews.showToastInView(getResources().getString(R.string.msg_generic_error));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    Boolean rc = jsonObject.optBoolean("success");
                    if (rc){
                        JSONArray listPaises = jsonObject.optJSONArray("countries");
                        for (int i = 0; i < listPaises.length(); i++) {
                            Pais p = new Pais(listPaises.optJSONObject(i));
                            listadoPaises.add(p);
                            adapterPaises.add(p.getNombre());
                            adapterPaises.notifyDataSetChanged();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void obtenerEstados(){
        NetConnection.obtenerEstados(new TextHttpResponseHandlerMessage() {
            @Override
            public void onStart() {
                super.onStart();
                showMessage(RegisterActivity.this,"Cargando");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                utilViews.showToastInView(getResources().getString(R.string.msg_generic_error));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                Log.d("Estados: ", " " +!responseString.isEmpty());
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    Boolean rc = jsonObject.optBoolean("success");
                    if (rc){
                        JSONArray listEstados = jsonObject.optJSONArray("countries");
                        for (int i = 0; i < listEstados.length(); i++) {
                            Estado e = new Estado(listEstados.optJSONObject(i));
                            listadoEstados.add(e);
                            adapterEstados.add(e.getNombre());
                            adapterEstados.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void registrarUsuario(UsuarioReg usuarioReg){
        NetConnection.registrarUsuario(usuarioReg,new TextHttpResponseHandlerMessage() {
            @Override
            public void onStart() {
                super.onStart();
                showMessage(RegisterActivity.this,getResources().getString(R.string.msg_send));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                utilViews.showToastInView(getResources().getString(R.string.msg_generic_error));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optBoolean(JSKeys.SUCCESS)){
                        JSONObject sesion = jsonObject.optJSONObject(JSKeys.SESSION);
                        Log.d("RegisterOK", responseString);

                        User mUser = new Gson().fromJson(sesion.toString(), User.class);
                        boussinesSpin.insertUser(mUser);

                        startActivity(new Intent(RegisterActivity.this, DrawerActivity.class));
                        finish();
                    }else{
                        String msj = jsonObject.get("msj").toString();
                        Log.d("RegisterFail",responseString);
                        utilViews.showToastInView(msj);
                    }
                } catch (JSONException e) {
                   Log.e(TAG, e.getMessage());
                }
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
