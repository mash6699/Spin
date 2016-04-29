package mx.spin.mobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


import mx.spin.mobile.entitys.ItemConcepts;
import mx.spin.mobile.fragments.DetalleConceptoFragment;
import mx.spin.mobile.utils.constants.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetalleConceptoActivity extends AppCompatActivity {

    private final static String TAG = DetalleConceptoActivity.class.getSimpleName();
    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;
    Fragment fragment;
    Bundle args;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_piscina);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragment = new DetalleConceptoFragment();
        args = getIntent().getExtras();
        if (args == null){
            args = new Bundle();
        }else{
            ItemConcepts concepts = (ItemConcepts) args.get(Constants.CONCEPTO);
            txt_titleToolbar.setText(concepts.getTitulo());
        }

        fragment.setArguments(args);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.simple_fragment, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        // ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.commit();

    }

}
