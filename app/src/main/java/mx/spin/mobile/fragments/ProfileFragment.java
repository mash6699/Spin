package mx.spin.mobile.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import butterknife.OnClick;
import mx.spin.mobile.EditProfileActivity;
import mx.spin.mobile.LoginActivity;

import mx.spin.mobile.SpinApp;
import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import mx.spin.mobile.R;
import mx.spin.mobile.utils.UtilViews;

public class ProfileFragment extends Fragment {

    private static String TAG = ProfileFragment.class.getName();

    private User usuario;
    private View rootView;

    private BoussinesSpin boussinesSpin;
    private UtilViews utilViews;

    @Nullable
    @Bind(R.id.imgProfileUser)
    CircleImageView txt_image;
    @Nullable
    @Bind(R.id.nombreUsuarioPerfil)
    TextView txt_nombre;
    @Nullable
    @Bind(R.id.emailUsuarioPerfil)
    TextView txt_email;
    @Nullable
    @Bind(R.id.telefonoUsuarioPerfil)
    TextView txt_phone;
    @Nullable
    @Bind(R.id.txtProfilePoolsRegistered)
    TextView txt_totalPools;
    @Nullable
    @Bind(R.id.paisProfile)
    TextView txt_pais;


    @Nullable
    @Bind(R.id.btn_analizar)
    Button btn_analizar;


    @Nullable
    @Bind(R.id.iv_option_menu)
    View optionMenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boussinesSpin = new BoussinesSpin(getActivity());
        usuario = boussinesSpin.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this,rootView);
        setUsuarioInView();
        utilViews = new UtilViews().getInstance(getActivity());

        txt_nombre.setTypeface(utilViews.setFontRegular());
        txt_email.setTypeface(utilViews.setFontNormal());
        txt_phone.setTypeface(utilViews.setFontNormal());
        txt_pais.setTypeface(utilViews.setFontNormal());
        txt_totalPools.setTypeface(utilViews.setFontNormal());
        btn_analizar.setTypeface(utilViews.setFontRegular());

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
                txt_nombre.setText(usuario.getName());
                txt_email.setText(usuario.getMail());

                if (usuario.getCountry()!= null){
                    txt_pais.setText(usuario.getCountry());
                }else{
                    txt_pais.setVisibility(View.GONE);
                }
                if (usuario.getTotal_pools() > 0) {
                    txt_totalPools.setText(getResources().getString(R.string.lbl_mis_piscinas) + usuario.getTotal_pools());
                }
                if (usuario.getPhone() != null && !usuario.getPhone().equals("Sin número")) {
                    txt_phone.setText(usuario.getPhone());
                }else{
                    txt_phone.setVisibility(View.GONE);
                }
                if (!usuario.getProfilePicture().equals("")) {
                    txt_image.setImageURI(Uri.parse(usuario.getProfilePicture()));
                }
            }
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    void closeSession(){
        try {
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
