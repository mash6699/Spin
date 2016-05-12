package mx.spin.mobile.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mx.spin.mobile.DrawerActivity;
import mx.spin.mobile.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class AnalizeFragment extends android.support.v4.app.Fragment {

    private static String TAG = AnalizeFragment.class.getName();

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;

    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_analize, container, false);
        ButterKnife.bind(this,rootView);

       /* txt_titleToolbar.setText(R.string.title_analisis);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
        toolbar.invalidate();*/

      /*  toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerActivity.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });*/


        return rootView;
    }

}
