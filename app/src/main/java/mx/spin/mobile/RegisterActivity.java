package mx.spin.mobile;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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


import mx.spin.mobile.common.SpinCommon;
import mx.spin.mobile.entitys.Estado;
import mx.spin.mobile.entitys.Pais;
import mx.spin.mobile.entitys.Usuario;
import mx.spin.mobile.network.NetConnection;
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
import io.realm.Realm;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = RegisterActivity.class.getName();
    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;

    private TextView txtYaTengoCuenta;
    private EditText nombre;
    private EditText email;
    private EditText contrasena;
    private EditText telefono;
    private EditText emailForget;

    private Spinner spinerPais;
    private Spinner spinerEstados;
    private CircleImageView imagePerfil;
    private Button btnRegistrate;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initComponents();


        if (NetConnection.isOnline(this,true)){
            obtenerPaises();
            obtenerEstados();
        }

        btnRegistrate = (Button) findViewById(R.id.btnRegistrate);
        btnRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SpinCommon spinCommon = new SpinCommon().getInstance(getApplicationContext());
                spinCommon.cleanUser();

                //cleanUser();
//                if (imageFile != null){
                if(!validateUserData()){
                    registrarUsuario(nombre.getText().toString(),email.getText().toString(),contrasena.getText().toString(),telefono.getText().toString(), Constants.DEFAULT_LOGIN,listadoPaises.get(paisSeleccionado).getPk()+"",listadoEstados.get(estadoSeleccionado).getPk()+"");
                }else{
                    Toast.makeText(RegisterActivity.this,"Ingresa la información requerida.",Toast.LENGTH_SHORT).show();
                }
                //                }else{
//                    Toast.makeText(RegisterActivity.this,"Debe subir una foto",Toast.LENGTH_SHORT).show();
//                }
            }
        });
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

    }

/*    private void cleanUser() {
        Realm realm = Realm.getInstance(RegisterActivity.this);
        realm.beginTransaction();
        RealmResults usuario = realm.where(Usuario.class).findAll();
        usuario.clear();
        realm.commitTransaction();
    }*/

    boolean validateUserData(){
        boolean estatus = false;
        View focusView = null;
        try{

            String nom = nombre.getText().toString().trim();
            String ema = email.getText().toString().trim();
            String pas = contrasena.getText().toString().trim();
            String tel = telefono.getText().toString().trim();
            String pai = listadoPaises.get(paisSeleccionado).getPk()+"";
            String edo = listadoEstados.get(estadoSeleccionado).getPk()+"";

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

    void initComponents(){
        txt_titleToolbar.setText(R.string.title_new_user);
        txtYaTengoCuenta = (TextView) findViewById(R.id.txtYaTengoCuenta);
        nombre          = (EditText) findViewById(R.id.nombreUsuario);
        email           = (EditText) findViewById(R.id.emailUsuario);
        contrasena      = (EditText) findViewById(R.id.contrasenaUsuario);
        telefono        = (EditText) findViewById(R.id.telefonoUsuario);
        emailForget        = (EditText) findViewById(R.id.correoCambiar);
        imagePerfil     = (CircleImageView) findViewById(R.id.imagePerfil);
        spinerEstados   = (Spinner) findViewById(R.id.spinnerEstado);
        spinerPais      = (Spinner) findViewById(R.id.spinnerPais);


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
                Toast.makeText(RegisterActivity.this,getResources().getString(R.string.msg_generic_error),Toast.LENGTH_SHORT).show();
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

                            //    spinerPais.setAdapter(UtilViews.getAdapterPHTitle(getApplicationContext(), adapterPaises));
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
                Toast.makeText(RegisterActivity.this,getResources().getString(R.string.msg_generic_error),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                Log.d("Estados",responseString);
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
    private void registrarUsuario(final String nombre, final String email, final String contrasena, final String telefono,String tipoLogin, String pais, String estado){
        NetConnection.registrarUsuario(nombre, email, contrasena, telefono, tipoLogin, pais, estado,new TextHttpResponseHandlerMessage() {
            @Override
            public void onStart() {
                super.onStart();
                showMessage(RegisterActivity.this,getResources().getString(R.string.msg_send));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                Toast.makeText(RegisterActivity.this,getResources().getString(R.string.msg_generic_error),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optBoolean("success")){
                        JSONObject sesion = jsonObject.optJSONObject("session");
                        Log.d("RegisterOK", responseString);
                        Realm realm = Realm.getInstance(RegisterActivity.this);
                        realm.beginTransaction();
                        Usuario user = realm.createObject(Usuario.class);
                        user.setToken(sesion.optString("id_user"));
                        user.setId(sesion.optString("id_user"));
                        user.setNombre(sesion.optString("name"));
                        user.setEmail(sesion.optString("mail"));
                        user.setTelefono(sesion.optString("phone"));
                        user.setPais(listadoPaises.get(paisSeleccionado).getNombre());
                        if (estadoSeleccionado != null){
                            user.setEstado(listadoEstados.get(estadoSeleccionado).getNombre());
                        }
                        user.setContrasena(contrasena);
                        if (selectedImagePath == null){
                            user.setPhoto("");
                        }else{
                            user.setPhoto(selectedImagePath);
                        }
//                        user.setFotoFile(Utils.convertBitmapToBytes(imageFile));
                        realm.commitTransaction();
                        startActivity(new Intent(RegisterActivity.this, DrawerActivity.class));
                        finish();
                    }else{
                        Log.d("RegisterFail",responseString);
                        Toast.makeText(RegisterActivity.this,getResources().getString(R.string.msg_generic_error), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
