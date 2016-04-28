package mx.spin.mobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import mx.spin.mobile.R;

import mx.spin.mobile.adapters.AdapterMantenimiento;
import mx.spin.mobile.entitys.ItemMantenimiento;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SendAnalizeActivity extends AppCompatActivity {

    private static String TAG = RegisterActivity.class.getName();
    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;

    ListView listQuimicos;
    AdapterMantenimiento adapterMantenimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_analize);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listQuimicos = (ListView) findViewById(R.id.listMantenimiento);
        txt_titleToolbar.setText(R.string.title_activity_mantenimiento);

        adapterMantenimiento = new AdapterMantenimiento(SendAnalizeActivity.this, R.layout.item_list_mantenimiento, getQuimicos());
        listQuimicos.setAdapter(adapterMantenimiento);

    }

    private ArrayList<ItemMantenimiento> getQuimicos() {
        ArrayList<ItemMantenimiento> item = new ArrayList<>();
        item.add(new ItemMantenimiento("Aplicación de Químico", "Problema 1: Alcalinidad fuera de Rango", "Clorizide 91", "Aplicar 1.3 litros de Clorizide 91. Los textos simulados son aquellos textos que, en ausencia de un texto definitivo, sirven como sustituto.", R.drawable.clorize_example));
        item.add(new ItemMantenimiento("Aplicación de Químico", "Problema 1: Alcalinidad fuera de Rango", "Clorizide 91", "Aplicar 1.3 litros de Clorizide 91. Los textos simulados son aquellos textos que, en ausencia de un texto definitivo, sirven como sustituto.", R.drawable.clorize_example));
        item.add(new ItemMantenimiento("Aplicación de Químico", "Problema 1: Alcalinidad fuera de Rango", "Clorizide 91", "Aplicar 1.3 litros de Clorizide 91. Los textos simulados son aquellos textos que, en ausencia de un texto definitivo, sirven como sustituto.", R.drawable.clorize_example));

        return item;
    }

}
