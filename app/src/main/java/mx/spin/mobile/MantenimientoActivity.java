package mx.spin.mobile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;



import mx.spin.mobile.adapters.AdapterMantenimiento;
import mx.spin.mobile.entitys.InfoAnalisisPDF;
import mx.spin.mobile.entitys.ItemMantenimiento;
import mx.spin.mobile.singleton.SpingApplication;
/*import mx.spin.mobile.utils.MyPrintDocumentAdapter;*/

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MantenimientoActivity extends AppCompatActivity {

    private final static String TAG = MantenimientoActivity.class.getName();
    private SpingApplication spingApplication = SpingApplication.getInstance();

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;
    @Nullable
    @Bind(R.id.tv_pool_name)
    TextView pool_name;
    @Nullable
    @Bind(R.id.tv_pool_date)
    TextView pool_date;


    @Nullable
    @Bind(R.id.btnMantenimientoSave)
    Button btnMantenSaveDetails;
    @Nullable
    @Bind(R.id.btnMantenimientoSend)
    Button btnMantenSend;
    @Nullable
    @Bind(R.id.btnMantenimientoWhere)
    Button btnMantenWhereIBuy;

    @Nullable
    @Bind(R.id.listMantenimiento)
    ListView listQuimicos;

    AdapterMantenimiento adapterMantenimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_titleToolbar.setText(R.string.title_activity_mantenimiento);
        pool_name.setText(spingApplication.getName());
        pool_date.setText(spingApplication.getDate());

        adapterMantenimiento = new AdapterMantenimiento(MantenimientoActivity.this, R.layout.item_list_mantenimiento, getQuimicos());
        listQuimicos.setAdapter(adapterMantenimiento);
        setListViewHeightBasedOnChildren(listQuimicos);
        listQuimicos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        btnMantenSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MantenimientoActivity.this, SendAnalizeActivity.class));
            /*    Realm realm = Realm.getInstance(MantenimientoActivity.this);
                realm.beginTransaction();
                InfoAnalisisPDF doc = realm.where(InfoAnalisisPDF.class).findFirst();
                realm.commitTransaction();
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String filelocation=dir.getAbsolutePath()+"/"+doc.getNombre();
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("vnd.android.cursor.dir/email");
                Log.d("EmailAttach", filelocation);
                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+filelocation));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Analisis de piscina");
                startActivity(Intent.createChooser(emailIntent , "Enviar análisis..."));*/
            }
        });

        btnMantenWhereIBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(MantenimientoActivity.this, DrawerActivity.class).putExtra("showFragment", 2));
            }
        });

        btnMantenSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintManager printManager = (PrintManager) MantenimientoActivity.this.getSystemService(Context.PRINT_SERVICE);

                String jobName = " Document";
            /*    MyPrintDocumentAdapter printAdapter = new MyPrintDocumentAdapter(MantenimientoActivity.this);
                printAdapter.setTitulo("Nombre de la piscina");
                printAdapter.setTextoMantenimientoRutinario("Encontramos 1 problema en tu piscina y está \n causando Agua Corrosiva.\nEs necesario aplicar el siguiente \nquímico para que el agua de \ntu piscina esté en balance.");
                printManager.print(jobName,printAdapter ,null);*/

            }
        });

    }

    private ArrayList<ItemMantenimiento> getQuimicos() {
        ArrayList<ItemMantenimiento> item = new ArrayList<>();
        item.add(new ItemMantenimiento("Aplicación de Químico", "Problema 1: Alcalinidad fuera de Rango", "Clorizide 91", "Aplicar 1.3 litros de Clorizide 91. Los textos simulados son aquellos textos que, en ausencia de un texto definitivo, sirven como sustituto.", R.drawable.clorize_example));

        return item;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
