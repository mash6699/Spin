package mx.spin.mobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;


import mx.spin.mobile.adapters.AdapterBitacora;
import mx.spin.mobile.entitys.ItemBitacora;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BitacoraActivity extends AppCompatActivity {

    private static final String TAG = BitacoraActivity.class.getName();

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;

    @Nullable
    @Bind(R.id.listBitacora)
    ListView listBitacora;



    private AdapterBitacora adapterBitacora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitacora);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_titleToolbar.setText(R.string.title_activity_bitacora);
        adapterBitacora = new AdapterBitacora(BitacoraActivity.this, R.layout.item_bitacora, getBitacora());
        listBitacora.setAdapter(adapterBitacora);
    }

    private ArrayList<ItemBitacora> getBitacora() {
        ArrayList<ItemBitacora> item = new ArrayList<>();
        item.add(new ItemBitacora("20 oct 2015", "9:00 hrs", "Mantenimento\nCorrectivo"));
        item.add(new ItemBitacora("20 oct 2015", "9:00 hrs", "Mantenimento\nCorrectivo"));
        item.add(new ItemBitacora("20 oct 2015", "9:00 hrs", "Mantenimento\nCorrectivo"));
        item.add(new ItemBitacora("20 oct 2015", "9:00 hrs", "Mantenimento\nCorrectivo"));
        item.add(new ItemBitacora("20 oct 2015", "9:00 hrs", "Mantenimento\nCorrectivo"));
        return item;
    }

}