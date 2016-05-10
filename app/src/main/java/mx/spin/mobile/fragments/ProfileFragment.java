package mx.spin.mobile.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import butterknife.OnClick;
import mx.spin.mobile.DrawerActivity;
import mx.spin.mobile.EditProfileActivity;
import mx.spin.mobile.LoginActivity;

import mx.spin.mobile.SpinApp;
import mx.spin.mobile.common.SpinBusinnes;
import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.User;
import mx.spin.mobile.entitys.Usuario;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import mx.spin.mobile.R;

public class ProfileFragment extends Fragment {

    private static String TAG = ProfileFragment.class.getName();
   // private SpinBusinnes spinBusinnes;
    private User usuario;
    private View rootView;

    private BoussinesSpin boussinesSpin;

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;
    @Nullable
    @Bind(R.id.imgProfileUser)
    CircleImageView imgProfileUser;
    @Nullable
    @Bind(R.id.nombreUsuarioPerfil)
    TextView nombreUsuarioPerfil;
    @Nullable
    @Bind(R.id.emailUsuarioPerfil)
    TextView emailUsuarioPerfil;
    @Nullable
    @Bind(R.id.telefonoUsuarioPerfil)
    TextView telefonoUsuarioPerfil;
    @Nullable
    @Bind(R.id.txtProfilePoolsRegistered)
    TextView cantPisicnas;
    @Nullable
    @Bind(R.id.paisProfile)
    TextView pais;

    @Nullable
    @Bind(R.id.iv_option_menu)
    View optionMenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // spinBusinnes = new SpinBusinnes();
        boussinesSpin = new BoussinesSpin(getActivity());
        //usuario = spinBusinnes.loadUser();
        usuario = boussinesSpin.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,rootView);
        txt_titleToolbar.setText(getResources().getString(R.string.title_profile));
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerActivity.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        setUsuarioInView();
        return rootView;
    }

    @Nullable
    @OnClick(R.id.iv_option_menu)
    public void optionMenuClick(View view){
        PopupMenu popupMenu = new PopupMenu(getActivity(), optionMenu);
        popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick: " + item.getTitle());
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


    void setUsuarioInView(){
        Log.d(TAG, "setUsuarioInView");
        try{
            if(usuario != null){
                SpinApp.initCargarImagen(getActivity());
                nombreUsuarioPerfil.setText(usuario.getName());
                emailUsuarioPerfil.setText(usuario.getMail());

              //  if (usuario.getPais() !="-1"){
                if (!usuario.getCountry().isEmpty()){
                    pais.setText(usuario.getCountry());
                }
                if (usuario.getTotal_pools() > 0) {
                    cantPisicnas.setText(getResources().getString(R.string.lbl_mis_piscinas) + usuario.getTotal_pools());
                }
                if (usuario.getPhone() != null) {
                    telefonoUsuarioPerfil.setText(usuario.getPhone());
                }
                if (!usuario.getProfilePicture().equals("")) {
                    imgProfileUser.setImageURI(Uri.parse(usuario.getProfilePicture()));
                }
             /*   if (usuario.getOrigenLogin() != 0) {
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(usuario.getPhoto(), imgProfileUser);
                }*/
            }
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    void closeSession(){
        try {
          //  spinBusinnes.cleanUser();
            boussinesSpin.cleanDB();
            Intent loginIntent = new Intent(getActivity() , LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
