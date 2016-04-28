package mx.spin.mobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;



import mx.spin.mobile.fragments.CambiarContrasenaFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CambiarContrasenaActivity extends AppCompatActivity {  // AppCompatActivity

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;

    private Fragment fragment;
    private Bundle args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_titleToolbar.setText(R.string.lbl_forget_password);

        fragment = new CambiarContrasenaFragment();
        args = getIntent().getExtras();
        if (args == null){
            args = new Bundle();
        }
        fragment.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.simple_fragment, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        // ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.commit();

    }

}
