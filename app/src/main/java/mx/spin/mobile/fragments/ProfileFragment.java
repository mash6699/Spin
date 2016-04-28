package mx.spin.mobile.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import mx.spin.mobile.DrawerActivity;
import mx.spin.mobile.EditProfileActivity;
import mx.spin.mobile.LoginActivity;

import mx.spin.mobile.SpinApp;
import mx.spin.mobile.common.SpinCommon;
import mx.spin.mobile.entitys.Usuario;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import mx.spin.mobile.R;

public class ProfileFragment extends Fragment {


    private static String TAG = ProfileFragment.class.getName();

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;


    private  View rootView;
    private Button btnCerrarSesion;
    private Button btnEditarPerfil;
    private CircleImageView imgProfileUser;
    private TextView nombreUsuarioPerfil;
    private TextView emailUsuarioPerfil;
    private TextView telefonoUsuarioPerfil;
    private TextView cantPisicnas;
    private TextView pais;
    private View optionMenu;

    SpinCommon spinCommon;

    private Usuario usuario;

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this,rootView);
        setComponentsInView();

        spinCommon = new SpinCommon().getInstance(getActivity());
        usuario = spinCommon.getUsuario();
        setUsuarioInView();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerActivity.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });


     /*   Realm realm = Realm.getInstance(getActivity());
        realm.beginTransaction();
        Usuario usuario = realm.where(Usuario.class).findFirst();
        realm.commitTransaction();*/





        optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), optionMenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
              /*  TextView liveitem = (TextView)popupMenu.findItem(R.id.close);
                liveitem.setTextColor(Color.RED);*/

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        Toast.makeText(getActivity(),
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT).show();

                        switch (item.getItemId()) {
                            case R.id.edit:
                                startActivity(new Intent(getActivity(), EditProfileActivity.class));
                                break;
                            case R.id.close:
                                closeSession();
                                break;

                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        return rootView;

    }


    void setUsuarioInView(){

        SpinApp.initCargarImagen(getActivity());

        if (usuario.getPais() !="-1"){
            pais.setText(usuario.getPais());
        }
        nombreUsuarioPerfil.setText(usuario.getNombre());
        emailUsuarioPerfil.setText(usuario.getEmail());

        if (usuario.getCantPiscinas() > 0) {
            cantPisicnas.setText(getResources().getString(R.string.lbl_mis_piscinas) + usuario.getCantPiscinas());
        }
        if (usuario.getTelefono() != null) {
            telefonoUsuarioPerfil.setText(usuario.getTelefono());
        }
        if (!usuario.getPhoto().equals("")) {
            imgProfileUser.setImageURI(Uri.parse(usuario.getPhoto()));
        }


        if (usuario.getOrigenLogin() != 0) {
//            btnEditarPerfil.setVisibility(View.GONE);
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(usuario.getPhoto(), imgProfileUser);
        }
    }

    void closeSession(){


       /* Realm realm = Realm.getInstance(getActivity());
        realm.beginTransaction();
        RealmResults usuario = realm.where(Usuario.class).findAll();
        usuario.clear();
        realm.commitTransaction();*/

        spinCommon.cleanUser();

        Intent loginIntent = new Intent(getActivity() , LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
      /*  getActivity().finish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();*/
    }


    private void setComponentsInView() {

        optionMenu              =   rootView.findViewById(R.id.iv_option_menu);
        imgProfileUser          = (CircleImageView) rootView.findViewById(R.id.imgProfileUser);
        nombreUsuarioPerfil     = (TextView) rootView.findViewById(R.id.nombreUsuarioPerfil);
        emailUsuarioPerfil      = (TextView) rootView.findViewById(R.id.emailUsuarioPerfil);
        telefonoUsuarioPerfil   = (TextView) rootView.findViewById(R.id.telefonoUsuarioPerfil);
        cantPisicnas            = (TextView) rootView.findViewById(R.id.txtProfilePoolsRegistered);
        pais                    = (TextView) rootView.findViewById(R.id.paisProfile);

        txt_titleToolbar.setText(getResources().getString(R.string.title_profile));
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);


    }


}
