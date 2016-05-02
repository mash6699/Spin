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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.OnClick;
import mx.spin.mobile.common.SpinBusinnes;
import mx.spin.mobile.entitys.Usuario;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

public class EditProfileActivity extends AppCompatActivity {

    private static String TAG = EditProfileActivity.class.getName();
    private SpinBusinnes spinBusinnes;
    private Usuario usuario;

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;

    @Nullable
    @Bind(R.id.nombreUsuario)
    EditText nombre;
    @Nullable
    @Bind(R.id.telefonoUsuario)
    EditText telefono;

    @Nullable
    @Bind(R.id.imgProfileEditar)
    CircleImageView imgProfileUser;

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private Bitmap bitmap;
    private Integer flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        spinBusinnes = new SpinBusinnes();
        usuario = spinBusinnes.loadUser();

        txt_titleToolbar.setText(R.string.title_edit_profile);

        setUsuarioInView();

        imgProfileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"), SELECT_PICTURE);
            }
        });

    }

    void setUsuarioInView(){
       try {
           Log.d(TAG, "setUsuarioInView" );
           if (usuario != null) {
               nombre.setText(usuario.getNombre());
               telefono.setText(usuario.getTelefono());
               if (!usuario.getPhoto().equals("")){
                   imgProfileUser.setImageURI(Uri.parse(usuario.getPhoto()));
               }
           }
       }catch (Exception e){
           Log.e(TAG, e.getMessage());
       }
    }


    @Nullable
    @OnClick(R.id.btn_guardar)
    public void actualizarUsuario(View view){
        Log.d(TAG, "actualizarUsuario");
        Realm realm = Realm.getInstance(EditProfileActivity.this);
        realm.beginTransaction();
        Usuario usuario = realm.where(Usuario.class).findFirst();
        usuario.setNombre(nombre.getText().toString());
        if(flag == 1){
            usuario.setPhoto(selectedImagePath);
        }
        usuario.setTelefono(telefono.getText().toString());
        realm.commitTransaction();
        editarUsuario(usuario.getId(),nombre.getText().toString(), telefono.getText().toString());
    }



    @Nullable
    @OnClick(R.id.btn_cancelar)
    public void cancelar(View view){
        super.onBackPressed();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);

                Log.d("ImagePath", selectedImagePath);
                imgProfileUser.setImageURI(selectedImageUri);
            }
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            bitmap = imageBitmap;
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    private void editarUsuario(String id,final String nombre, final String telefono){
        NetConnection.editarUsuario(id,nombre,telefono,  new TextHttpResponseHandlerMessage() {
            @Override
            public void onStart() {
                super.onStart();
                showMessage(EditProfileActivity.this, "Enviando");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                Toast.makeText(EditProfileActivity.this, "Lo sentimos ocurrio un error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject sesion = jsonObject.optJSONObject("session");
                        Log.d("RegisterOK", responseString);
                        Realm realm = Realm.getInstance(EditProfileActivity.this);
                        realm.beginTransaction();
//                        Usuario user = realm.createObject(Usuario.class);
//                        user.setToken(sesion.optString("user_id"));
//                        user.setNombre(sesion.optString("name"));
//                        user.setEmail(sesion.optString("mail"));
//                        user.setTelefono(sesion.optString("phone"));
//                        user.setContrasena(contrasena);
//                        user.setPhoto(selectedImagePath);
//                        user.setFotoFile(Utils.convertBitmapToBytes(imageFile));
                        realm.commitTransaction();
                        startActivity(new Intent(EditProfileActivity.this, DrawerActivity.class));
                        finish();
                    } else {
                        Log.d("RegisterFail", responseString);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
       // super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
