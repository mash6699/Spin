package mx.spin.mobile;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.utils.PermissionUtil;
import mx.spin.mobile.utils.UtilViews;
import mx.spin.mobile.utils.constants.Constants;

/**
 * Created by miguel_angel on 18/05/2016.
 */
public class MantenimientoActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static String TAG = MantenimientoActivity.class.getName();
    private static SpingApplication spingApplication = SpingApplication.getInstance();
    private BoussinesSpin boussinesSpin;
    private static final int REQUEST_PERMISSIONS = 16;
    private UtilViews utilViews;
    //  private Pool piscina;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    static double volumen;
    static int um;

    static double ph;
    static double alcalinidad;
    static double turbidez;
    static double cya;
    static String metales;
    static int instalacion;
    static double cloroTotal;
    static double cloroLibre;
    static double cloraminas;
    static double bromo;
    static double dureza;
    static double std;

    @Nullable
    @Bind(R.id.txt_name)
    TextView label_name;

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

    View mView;


    private static final String FILE_FOLDER = "SpinPDF";
    private static File file;
    private static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    //  private EditText _pdfBodyEDT;
    private boolean isPDFFromHTML = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mantenimiento);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boussinesSpin = new BoussinesSpin(this);
        utilViews = new UtilViews().getInstance(this);

        txt_titleToolbar.setText(R.string.title_activity_mantenimiento);
        pool_name.setText(" " + spingApplication.getName());
        pool_date.setText(" " +spingApplication.getDate());

        int idPiscina = spingApplication.getIdPiscina();
        //   piscina = boussinesSpin.getPool(idPiscina);



        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.guardar);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView =  view;
                requestMMPermissions();
            }
        });

        FloatingActionButton fabSend = (FloatingActionButton) findViewById(R.id.enviar);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Se enviara el pdf en un email", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mView =  view;

            }
        });

        FloatingActionButton fabFind = (FloatingActionButton) findViewById(R.id.buscar);
        fabFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MantenimientoActivity.this, DrawerActivity.class).putExtra("showFragment", 2));

            }
        });

        setFonts();
        setValues();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    void setFonts(){
        txt_titleToolbar.setTypeface(utilViews.setFontRegular());
        label_name.setTypeface(utilViews.setFontRegular());
        pool_name.setTypeface(utilViews.setFontNormal());
        pool_date.setTypeface(utilViews.setFontNormal());
    }

    private void requestMMPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            String storeWritePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
            java.util.List<String> permissions = new ArrayList<String>();
            int hasStorePermission = checkSelfPermission(storeWritePermission);

            if (hasStorePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(storeWritePermission);
            }

            if (!permissions.isEmpty()) {
                final String[] params = permissions.toArray(new String[permissions.size()]);
                //
                new AlertDialog.Builder(this).setMessage(Html.fromHtml("Para crear el PDF tienes que activar el almacenamiento de tu dispositivo.")).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(params, REQUEST_PERMISSIONS);
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setTitle(getString(R.string.app_name)).setCancelable(false).create().show();

            }else {
                createPdf();
            }


        } else {
            createPdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                boolean isGranted = PermissionUtil.verifyPermissions(grantResults);
                if (!isGranted) {
                    createPdf();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void setValues(){
        try{
            Log.d(TAG, "setValues");

            volumen     = Double.parseDouble(spingApplication.getVolumen());
            um          = Integer.parseInt(spingApplication.getUm());

            ph           = Double.parseDouble(spingApplication.getFs_11());
            alcalinidad  = Double.parseDouble(spingApplication.getFs_12());

            dureza      = Double.parseDouble(spingApplication.getFs_13());
            std         = Double.parseDouble(spingApplication.getFs_15());
            cya         = Double.parseDouble(spingApplication.getSs_26());

            turbidez    = Double.parseDouble(spingApplication.getSs_24());
            metales     = spingApplication.getSs_25();

            instalacion = Integer.parseInt(spingApplication.getInstalacion());

            //TECHADA = 1
            if(instalacion == Constants.PISCINA_TECHADA){
                bromo         = Double.parseDouble(spingApplication.getSs_27());
            }else{
                cloroTotal    = Double.parseDouble(spingApplication.getSs_21());
                cloroLibre    = Double.parseDouble(spingApplication.getSs_22());
                cloraminas    = Double.parseDouble(spingApplication.getSs_23());
            }

            //     }
        }catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.lbl_ajuste_primero);
                case 1:
                    return getResources().getString(R.string.lbl_ajuste_segundo);
                case 2:
                    return getResources().getString(R.string.lbl_ajuste_tercero);
                case 3:
                    return getResources().getString(R.string.lbl_ajuste_cuarto);
            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private View rootView;
        private UtilViews utilViews ;

        @Nullable
        @Bind(R.id.primero)
        View viewPrimero;

        @Nullable
        @Bind(R.id.segundo)
        View viewSegundo;
        @Nullable
        @Bind(R.id.tercero)
        View viewTercero;

        @Nullable
        @Bind(R.id.cuarto)
        View viewCuaro;


        @Nullable
        @Bind(R.id.iv_ph)
        ImageView iv_ph;
        @Nullable
        @Bind(R.id.title_ph)
        TextView title_ph;
        @Nullable
        @Bind(R.id.lbl_description_ph)
        TextView lbl_ph;


        @Nullable
        @Bind(R.id.iv_desinfectante)
        ImageView iv_desinfectante;
        @Nullable
        @Bind(R.id.title_desinfectante)
        TextView title_desinfectante;
        @Nullable
        @Bind(R.id.lbl_description_desinfectante)
        TextView lbl_desinfectante;
        @Nullable
        @Bind(R.id.cv_cloramidas)
        View cv_cloramidad;
        @Nullable
        @Bind(R.id.iv_desinfectante_cloramidas)
        ImageView iv_desinfectante_cloramidas;
        @Nullable
        @Bind(R.id.title_desinfectante_cloramidas)
        TextView title_desinfectante_cloramidas;
        @Nullable
        @Bind(R.id.lbl_description_desinfectante_cloramidas)
        TextView lbl_desinfectante_cloramidas;


        @Nullable
        @Bind(R.id.iv_estabilizador)
        ImageView iv_estabilizador;
        @Nullable
        @Bind(R.id.title_estabilizador)
        TextView title_estabilizador;
        @Nullable
        @Bind(R.id.lbl_description_estabilizador)
        TextView lbl_estabilizador;

        @Nullable
        @Bind(R.id.iv_shock)
        ImageView iv_shock;
        @Nullable
        @Bind(R.id. title_shockpreventivo)
        TextView title_shock;
        @Nullable
        @Bind(R.id.lbl_description_shockpreventivo)
        TextView lbl_shock;


        @Nullable
        @Bind(R.id.iv_turbidez)
        ImageView iv_turbidez;
        @Nullable
        @Bind(R.id.title_turbidez)
        TextView title_turbidez;
        @Nullable
        @Bind(R.id.lbl_description_turbidez)
        TextView lbl_turbidez;

        @Nullable
        @Bind(R.id.iv_dureza)
        ImageView iv_dureza;
        @Nullable
        @Bind(R.id.title_dureza)
        TextView title_dureza;
        @Nullable
        @Bind(R.id.lbl_description_dureza)
        TextView lbl_dureza;

        @Nullable
        @Bind(R.id.title_std)
        TextView title_std;
        @Nullable
        @Bind(R.id.lbl_description_std)
        TextView lbl_std;


        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.mantenimiento_content, container, false);
            ButterKnife.bind(this,rootView);
            utilViews = new UtilViews().getInstance(getActivity());
            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            setFontsResults();
            switch (position) {
                case 1:
                    viewPrimero.setVisibility(View.VISIBLE);
                    phRec();
                    break;
                case 2:
                    viewSegundo.setVisibility(View.VISIBLE);
                    turbidezRec();
                    break;
                case 3:
                    viewTercero.setVisibility(View.VISIBLE);
                    desinfectanteRec();
                    estabilizadorRec();
                    shockPreventivoRec();
                    break;
                case 4:
                    viewCuaro.setVisibility(View.VISIBLE);
                    durezaCondicional();
                    stdCondicional();
                    break;
            }

            return rootView;
        }

        void setFontsResults(){
            Log.d(TAG, "setFontsResults");

            title_ph.setTypeface(utilViews.setFontRegular());
            lbl_ph.setTypeface(utilViews.setFontNormal());

            title_desinfectante.setTypeface(utilViews.setFontRegular());
            lbl_desinfectante.setTypeface(utilViews.setFontNormal());
            title_desinfectante_cloramidas.setTypeface(utilViews.setFontRegular());
            lbl_desinfectante_cloramidas.setTypeface(utilViews.setFontNormal());

            title_estabilizador.setTypeface(utilViews.setFontRegular());
            lbl_estabilizador.setTypeface(utilViews.setFontNormal());

            title_shock.setTypeface(utilViews.setFontRegular());
            lbl_shock.setTypeface(utilViews.setFontNormal());

            title_turbidez.setTypeface(utilViews.setFontRegular());
            lbl_turbidez.setTypeface(utilViews.setFontNormal());

            title_dureza.setTypeface(utilViews.setFontRegular());
            lbl_dureza.setTypeface(utilViews.setFontNormal());

            title_std.setTypeface(utilViews.setFontRegular());
            lbl_std.setTypeface(utilViews.setFontNormal());
        }

        //TODO PRIMER AJUSTE
        protected  void phRec(){
            Log.d(TAG, "phRec");
            StringBuilder phTxt = new StringBuilder();

            if(ph < 7.4d){
                //  phTxt.append("<b>Estatus: </b>" + getString(R.string.ph_cond_10));
                phTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.ph_rec_10));

                if(alcalinidad > 120d){
                    phTxt.append("<br /><b> " + getString(R.string.lbl_paso_intermedio) +" </b><br />"  );

                }else if (alcalinidad >= 80d || alcalinidad <= 120d){
                    phTxt.append("<br /><b> " + getString(R.string.lbl_paso_intermedio) +" </b><br />"  );

                }else if (alcalinidad < 80d){
                    phTxt.append("<br /><b> " + getString(R.string.lbl_paso_intermedio) +" </b><br />"  );

                }

            }else if (ph >= 7.4d || ph <= 7.6d){
                // phTxt.append("<b>Estatus: </b>" + getString(R.string.ph_cond_11));
                phTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.ph_rec_11));

                if (alcalinidad > 120d){
                    phTxt.append("<br /><b> " + getString(R.string.lbl_paso_intermedio) +" </b><br />"  );

                }
            }else if(ph > 7.6d){
                //phTxt.append("<b>Estatus: </b>" + getString(R.string.ph_cond_12));
                phTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.ph_rec_12));

                if (alcalinidad >= 80d || alcalinidad <= 120d){
                    phTxt.append("<br /><b> " + getString(R.string.lbl_paso_intermedio) +" </b><br />"  );

                }else if (alcalinidad > 120d){
                    phTxt.append("<br /><b> " + getString(R.string.lbl_paso_intermedio) +" </b><br />"  );

                }else if(alcalinidad < 80d){
                    phTxt.append("<br /><b> " + getString(R.string.lbl_paso_intermedio) +" </b><br />"  );

                }
            }

            lbl_ph.setText(Html.fromHtml(phTxt.toString()));

        }

        //TODO SEGUNDO AJUSTE
        protected void turbidezRec(){
            Log.d(TAG, "turbidezRec");
            StringBuilder turbidezTxt = new StringBuilder();
            if(turbidez < 0.5 && metales.equals(getString(R.string.lbl_negativo))){
                turbidezTxt.append(("<b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.turb_rec_20)));
            }else if(turbidez > 0.5d && metales.equals(getString(R.string.lbl_negativo))){
                turbidezTxt.append("<b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.turb_rec_21,turbidezCal(1)));
                iv_turbidez.setVisibility(View.VISIBLE);
            }else if(turbidez > 0.5d && metales.equals(getString(R.string.lbl_positivo))){
                turbidezTxt.append("<b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.turb_rec_21,turbidezCal(1)));
                iv_turbidez.setVisibility(View.VISIBLE);
            }else if(turbidez < 0.5 && metales.equals(getString(R.string.lbl_positivo))){
                turbidezTxt.append("<b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.turb_rec_22,turbidezCal(2)));
                iv_turbidez.setVisibility(View.VISIBLE);
            }
            lbl_turbidez.setText(Html.fromHtml(turbidezTxt.toString()));
        }

        //TODO TERCER AJUSTE
        protected void desinfectanteRec(){
            StringBuilder desinfTxt = new StringBuilder();
            StringBuilder desinfCloramidasTxt = new StringBuilder();

            if(instalacion == Constants.PISCINA_ABIERTA){
                if(cloroLibre < 1d){
                    //desinfTxt.append("<b>Estatus: </b>" + getString(R.string.cloro_cond_30));
                    desinfTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.cloro_rec_30, desinfectanteCloro()));
                    iv_desinfectante.setVisibility(View.VISIBLE);
                    iv_desinfectante.setBackgroundResource(R.drawable.trizidecuatrokg);
                } else if(cloroLibre >= 1d || cloroLibre <= 5d){
                    //desinfTxt.append("<b>Estatus: </b>" + getString(R.string.cloro_cond_31));
                    desinfTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.cloro_rec_31));

                } else if(cloroLibre > 5d){
                    // desinfTxt.append("<b>Estatus: </b>" + getString(R.string.cloro_cond_32));
                    desinfTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.cloro_rec_32));
                    iv_desinfectante.setVisibility(View.VISIBLE);
                    iv_desinfectante.setBackgroundResource(R.drawable.neutraclorcincokg);
                }
                cv_cloramidad.setVisibility(View.VISIBLE);

                if(cloraminas < 2d){
                    //desinfCloramidasTxt.append("<b>Estatus: </b>" + getString(R.string.cloraminas_cond_33));
                    desinfCloramidasTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.cloraminas_rec_33));

                }else if(cloraminas >= 0.3d){
                    //desinfCloramidasTxt.append("<b>Estatus: </b>" + getString(R.string.cloraminas_cond_34));
                    desinfCloramidasTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.cloraminas_rec_34, desinfectanteCloraminas()));

                    iv_desinfectante_cloramidas.setVisibility(View.VISIBLE);
                    iv_desinfectante_cloramidas.setBackgroundResource(R.drawable.shockcorrectivounkg);
                }

                lbl_desinfectante_cloramidas.setText(Html.fromHtml(desinfCloramidasTxt.toString()));

            } else {

                if(bromo < 2d){
                    //desinfTxt.append("<b>Estatus: </b>" + getString(R.string.bromo_cond_30));
                    desinfTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.bromo_rec_30));
                    iv_desinfectante.setVisibility(View.VISIBLE);
                    iv_desinfectante.setBackgroundResource(R.drawable.clorizidebromadounkg);
                } else if( bromo >= 2 || bromo <= 6){
                    //desinfTxt.append("<b>Estatus: </b>" + getString(R.string.bromo_cond_31));
                    desinfTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.bromo_rec_31));

                } else if (bromo > 6d){
                    // desinfTxt.append("<b>Estatus: </b>" + getString(R.string.bromo_cond_32));
                    desinfTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.bromo_rec_32));
                    iv_desinfectante.setVisibility(View.VISIBLE);
                    iv_desinfectante.setBackgroundResource(R.drawable.clorizidebromadounkg);
                }

            }


            lbl_desinfectante.setText(Html.fromHtml(desinfTxt.toString()));
        }

        protected void estabilizadorRec(){
            StringBuilder estabTxt = new StringBuilder();
            //if(cya )  TRIZIDE Y NEUTRACLOR

            lbl_estabilizador.setText(Html.fromHtml(estabTxt.toString()));
        }
        protected void shockPreventivoRec(){
            StringBuilder shockTxt = new StringBuilder();
            double shockVal = calcularShock();

            if(shockVal <= 2d){
                // shockTxt.append("<b>Estatus: </b>" + getString(R.string.shock_cond_37));
                shockTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.shock_rec_37, shockVal));
                iv_shock.setVisibility(View.VISIBLE);
            }else if(shockVal >= 3d || shockVal <= 10){
                // shockTxt.append("<b>Estatus: </b>" + getString(R.string.shock_cond_38));
                shockTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.shock_rec_38));
            }else if(shockVal > 10){
                // shockTxt.append("<b>Estatus: </b>" + getString(R.string.shock_cond_39));
                shockTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.shock_rec_39));
            }

            lbl_shock.setText(Html.fromHtml(shockTxt.toString()));
        }

        //TODO FIN TERCER AJUSTE

        //TODO CUARTO AJUSTE
        protected void durezaCondicional(){
            try{
                StringBuilder durezaTxt = new StringBuilder();
                Log.d(TAG ,"durezaFA val [" + dureza + " ]" );
                if(dureza < 150d){
                    // durezaTxt.append("<b>Estatus: </b>" + getString(R.string.dureza_cond_40));
                    durezaTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>" + getString(R.string.dureza_rec_40, dosificacionDur()) );
                    iv_dureza.setVisibility(View.VISIBLE);
                }else if(dureza >= 150d || dureza <= 250d){
                    //  durezaTxt.append("<b>Estatus: </b>" + getString(R.string.dureza_cond_41));
                    durezaTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>" + getString(R.string.dureza_rec_41) );
                }else if (dureza > 250d){
                    // durezaTxt.append("<b>Estatus: </b>" + getString(R.string.dureza_cond_42));
                    durezaTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>" +getString(R.string.dureza_rec_42) );
                }

                lbl_dureza.setText(Html.fromHtml(durezaTxt.toString()));
            }catch (Exception ex){
                Log.e(TAG, ex.getMessage());
            }
        }

        protected void stdCondicional(){
            StringBuilder stdTxt = new StringBuilder();
            if(std >= 2500d){
                //stdTxt.append("<b>Estatus: </b>" + getString(R.string.std_cond_43));
                stdTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>" + getString(R.string.std_rec_43));
            }else if( std < 2500d){
                //stdTxt.append("<b>Estatus: </b>" + getString(R.string.std_cond_44));
                stdTxt.append("<br /><b> " + getString(R.string.lbl_recomendacion) +" </b>"  + getString(R.string.std_rec_44));
            }
            lbl_std.setText(Html.fromHtml(stdTxt.toString()));
        }
        //TODO FIN CUARTO AJUSTE

        //TODO CALCULO DOSIFICACION
        public String dosificacionDur(){
            //'=((15  * "VOLUMEN EN METROS CUBICOS") / 10) * ((150 - DUREZA ) / 10)
            double dosificacion = 0;
            double calcM3 = ((15 * volumen ) / 10);
            double calDure = ((150 - dureza) / 10);

            if(um == 1){
                dosificacion = calcM3 * calDure;
            }else{
                dosificacion = (calcM3 * calDure) / 28.35;
            }

            Log.d(TAG, "UM [ "+ um +" ] calcDosificacion = " + dosificacion);
            return String.format(Constants.TWO_DECIMAL,dosificacion);
        }

        public String turbidezCal(int type){
            double turb = 0;
            double turbCal = 0;
            if(type == 1){
                //= ((240 * "Volumen en metros cubicos"  ) /  10  )    /      1000 )
                turb = (((240 * volumen) / 10) / 1000);
            }else {
                turb = (((50 * volumen)/ 10) / 1000);
                // '= ((50 * "Volumen en metros cubicos"  ) /  10  )    /      1000 )
            }

            if(um == 2){
                //Galones   =   Litros   /    3.785
                turbCal = (turb/ 3.785);
            }else{
                turbCal = turb;
            }

            Log.d(TAG, "UM [ "+ um +" ] turbidezCal = " + turbCal);
            return String.format(Constants.TWO_DECIMAL, turbCal);
        }

        public Double calcularShock(){
            double result = 0;
            //TODO SI
            if(um == 2){
                result = (((15 * volumen) / 10 ) * ((150 - dureza)/10) );
            }else {
                result = (300 * volumen) / 10;
            }
            Log.d(TAG, "calcularShock: "  + result );
            return  result;
        }


        public Double desinfectanteCloro(){
            double result = 0;
            //TODO SI '=((16 * "VOLUMEN EN METROS CUBICOS") / 10) * (3 - "Cloro Libre")
            if(um == 2){
                result = (((16 * volumen) / 10 ) * (3 - cloroLibre) );
            }else {
                // '=((16 * "VOLUMEN EN METROS CUBICOS") / 10) * (3 - "Cloro Libre")
                result = (((16 * volumen) / 10) * (3 - cloroLibre) );
            }
            Log.d(TAG, "desinfectanteCloro: "  + result );
            return  result;
        }

        public Double desinfectanteCloraminas(){
            double result =   result = (((cloroTotal -cloroLibre) * 10 ) - cloroLibre);

            Log.d(TAG, "desinfectanteCloraminas: "  + result );
            return  result;
        }


    }



    private void getFile() {
        file = new File(FILE_PATH, FILE_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void createPdf()  {
        Log.d(TAG, "createPdf" );
        try {
            getFile();
            //Create time stamp
            Date date = new Date ();
            String timeStamp = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault()).format(date);
            String fileName = FILE_PATH + "/"+ FILE_FOLDER  +  "/"+ spingApplication.getName() +"_" + timeStamp + ".pdf";

            File myFile = new File(fileName);
            myFile.createNewFile();
            OutputStream output = new FileOutputStream(myFile);

            // Document document = new Document();
            HeaderTable event = new HeaderTable();

            Document document = new Document(PageSize.A4, 36, 36, 20 + event.getTableHeight(), 36);

            PdfWriter writer = PdfWriter.getInstance(document, output);
            // writer.setLinearPageMode();
            // writer.setFullCompression();

            writer.setPageEvent(event);
            // HeaderTable event = new HeaderTable();


              /* Create Set Font and its Size */
            Font titleFont= new Font(Font.FontFamily.HELVETICA, Font.BOLD);
            titleFont.setSize(16);

            Font fontContent= new Font(Font.FontFamily.HELVETICA, Font.NORMAL);
            fontContent.setSize(12);

            //TODO document header attributes
            document.addAuthor("Spin mobile");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("www.spingrupo.com");

            document.addTitle("Sping Reporte");
            /*document.setPageSize(PageSize.A4);
            document.setMargins(36, 36, 36, 36);
            document.setMarginMirroring(true);*/

            //TODO open document
            document.open();



            addTablePiscina(document, titleFont);
            addTableBalance(document, titleFont);
            addTableDesinfeccion(document, titleFont);
            addMantenimiento(document, titleFont);

            //Close the document
            document.close();

          /*  Snackbar.make( mView.getRootView(),"El PDF se creo correctamente se encuentra en la carpeta " + FILE_FOLDER, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/

            openPDF(fileName);



                           /* Inserting Image in PDF */
         /*   ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.setAlignment(Image.MIDDLE);
            //add image to document
            document.add(myImg);*/


            //      addContent(document);



        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            Log.e("PDF--->",  "exception", e);
        }
    }

    public void openPDF(String name)
    {
        // File pdfFile = new File(Environment.getExternalStorageDirectory() + "/t/" + name);  // -> filename = maven.pdf
        File pdfFile = new File(name);
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(MantenimientoActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }



    public class HeaderTable extends PdfPageEventHelper {
        protected PdfPTable table;
        protected float tableHeight;
        protected PdfPCell cell;
        protected Image img;
        public HeaderTable() {
            try{

                Drawable drawable = getResources().getDrawable(R.drawable.logo_ajustado);
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                cell = new PdfPCell(new Phrase("Resultado del Análisis"));
                img = Image.getInstance(stream.toByteArray());

                img.scaleToFit(30, 30);
                img.setAlignment(Chunk.ALIGN_MIDDLE);

                table = new PdfPTable(2);
                table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);


                table.setTotalWidth(523);

                table.setWidths(new int[]{ 1, 9 });

                table.setLockedWidth(true);

                table.addCell(new PdfPCell(img, true));
                table.addCell(cell);


                tableHeight = table.getTotalHeight();

            }catch (Exception ex){

            }
        }

        public float getTableHeight() {
            return tableHeight;
        }

        public void onEndPage(PdfWriter writer, Document document) {
            table.writeSelectedRows(0, -1,
                    document.left(),
                    document.top() + ((document.topMargin() + tableHeight) / 2),
                    writer.getDirectContent());
        }
    }


    public void addInfoPool(Document document, Font fontContent) throws DocumentException{
        Log.d(TAG, "addInfoPool");
        Paragraph iPiscina = new Paragraph(getResources().getString(R.string.lbl_nombre_piscina) +" : " + spingApplication.getName() +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_fecha_analisis)      + " : " + spingApplication.getDate() +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_volumen)             + " : " + spingApplication.getVolumen() +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_unidad_volumen)      + " : " + spingApplication.getUmVal() +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_piscina)             + " : " + spingApplication.gettPiscina()+" \n");
        iPiscina.add(getResources().getString(R.string.lbl_tipo_instalacion)    + " : " + spingApplication.getInstalacionVal() +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_tiempo_rotacion)     + " : " + spingApplication.gettRotacion() +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_velocidad_flujo)     + " : " + spingApplication.getvFlujo() +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_uso_piscina)         + " : " + spingApplication.getUsoPiscina() +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_equipo)              + " : " + "" +"\n");
        iPiscina.add("" +"" +"\n");
        iPiscina.add("" +"" +"\n");

        iPiscina.setAlignment(Paragraph.ALIGN_LEFT);
        iPiscina.setFont(fontContent);

        document.add(iPiscina);
    }

    private void addTablePiscina(Document document, Font titleFont) throws DocumentException {
        Log.d(TAG, "addTablePiscina");
//        Paragraph header = new Paragraph("Resultado del Análisis de la Piscina\n\n");
        Paragraph header = new Paragraph("Información de la Piscina\n\n");
        header.setAlignment(Paragraph.ALIGN_CENTER);
        header.setFont(titleFont);
        document.add(header);

        PdfPTable table = new PdfPTable(2);
        //   table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(getResources().getString(R.string.lbl_nombre_piscina) + ": ");
        table.addCell(spingApplication.getName());

        table.addCell(getResources().getString(R.string.lbl_fecha_analisis)+ ": ");
        table.addCell(spingApplication.getDate());

        table.addCell(getResources().getString(R.string.lbl_volumen) + ": ");
        table.addCell(spingApplication.getVolumen());

        table.addCell(getResources().getString(R.string.lbl_unidad_volumen)+ ": ");
        table.addCell(spingApplication.getUmVal());

        table.addCell(getResources().getString(R.string.lbl_piscina)  + ": ");
        table.addCell(spingApplication.gettPiscina());

        table.addCell(getResources().getString(R.string.lbl_tipo_instalacion)+ ": ");
        table.addCell(spingApplication.getInstalacionVal());

        table.addCell(getResources().getString(R.string.lbl_tiempo_rotacion)+ ": ");
        table.addCell(spingApplication.gettRotacion());

        table.addCell(getResources().getString(R.string.lbl_velocidad_flujo) + ": ");
        table.addCell(spingApplication.getvFlujo().trim());

        table.addCell(getResources().getString(R.string.lbl_uso_piscina)+ ": ");
        table.addCell(spingApplication.getUsoPiscina());

        table.addCell(getResources().getString(R.string.lbl_equipo)+ ": " );
        table.addCell("");

        document.add(table);

    }


    private void addTableBalance(Document document, Font titleFont) throws DocumentException {
        Log.d(TAG, "addTableBalance");
        Paragraph iBalance = new Paragraph("\nBalance\n\n");
        iBalance.setAlignment(Paragraph.ALIGN_CENTER);
        iBalance.setFont(titleFont);
        document.add(iBalance);

        PdfPTable table = new PdfPTable(3);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        PdfPCell c1 = new PdfPCell(new Phrase(""));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResources().getString(R.string.lbl_conactual)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setCellEvent(new DottedCell(PdfPCell.RIGHT | PdfPCell.BOTTOM));
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResources().getString(R.string.lbl_conideal)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setCellEvent(new DottedCell(PdfPCell.BOTTOM));
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell(getResources().getString(R.string.lbl_ph) + "\n" + spingApplication.getFres_11().toLowerCase());
        c1 = new PdfPCell(new Phrase(spingApplication.getFs_11()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setCellEvent(new DottedCell(PdfPCell.RIGHT));
        table.addCell(c1);
        table.addCell(getResources().getString(R.string.lbl_conideal_ph));

        table.addCell(getResources().getString(R.string.lbl_alcalinidad) + "\n" + spingApplication.getFres_12().toLowerCase());
        c1 = new PdfPCell(new Phrase(spingApplication.getFs_12()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setCellEvent(new DottedCell(PdfPCell.RIGHT ));
        table.addCell(c1);
        //table.addCell(spingApplication.getFs_12());
        table.addCell(getResources().getString(R.string.lbl_conideal_alcalinidad));

        table.addCell(getResources().getString(R.string.lbl_std) + "\n" + spingApplication.getFres_15().toLowerCase());
        c1 = new PdfPCell(new Phrase(spingApplication.getFs_15()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setCellEvent(new DottedCell(PdfPCell.RIGHT ));
        table.addCell(c1);
        //table.addCell(spingApplication.getFs_15());
        table.addCell(getResources().getString(R.string.lbl_conideal_std));

        table.addCell(getResources().getString(R.string.lbl_dureza) + "\n" + spingApplication.getFres_13().toLowerCase());
        c1 = new PdfPCell(new Phrase(spingApplication.getFs_13()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setCellEvent(new DottedCell(PdfPCell.RIGHT ));
        table.addCell(c1);
        //table.addCell(spingApplication.getFs_13());
        table.addCell(getResources().getString(R.string.lbl_conideal_dureza));

        table.addCell(getResources().getString(R.string.lbl_temperatura));
        c1 = new PdfPCell(new Phrase(spingApplication.getFs_14()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setCellEvent(new DottedCell(PdfPCell.RIGHT ));
        table.addCell(c1);
        //table.addCell(spingApplication.getFs_14());
        table.addCell(getResources().getString(R.string.lbl_conideal_temperatura));

        document.add(table);
    }

    private void addTableDesinfeccion(Document document, Font titleFont) throws DocumentException {
        Log.d(TAG, "addTableDesinfeccion");
        Paragraph iDesinfeccion = new Paragraph("\nDesinfección\n\n");
        iDesinfeccion.setAlignment(Paragraph.ALIGN_CENTER);
        iDesinfeccion.setFont(titleFont);
        document.add(iDesinfeccion);

        PdfPTable table = new PdfPTable(3);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        PdfPCell c1 = new PdfPCell(new Phrase(""));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResources().getString(R.string.lbl_conactual)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setCellEvent(new DottedCell(PdfPCell.RIGHT | PdfPCell.BOTTOM));
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResources().getString(R.string.lbl_conideal)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setCellEvent(new DottedCell(PdfPCell.BOTTOM));
        table.addCell(c1);
        table.setHeaderRows(1);

        if (spingApplication.getTipoPiscina() ==  Constants.PISCINA_ABIERTA ){
            //TODO CLORO TOTAL
            //TODO CLORMINAS
            table.addCell(getResources().getString(R.string.lbl_cloro_total) + "\n" + spingApplication.getSres_22().toLowerCase());
            c1 = new PdfPCell(new Phrase(spingApplication.getSs_22()));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorder(PdfPCell.NO_BORDER);
            c1.setCellEvent(new DottedCell(PdfPCell.RIGHT ));
            table.addCell(c1);
            // table.addCell(spingApplication.getSs_22());
            table.addCell(getResources().getString(R.string.lbl_conideal_cloro_total));

            table.addCell(getResources().getString(R.string.lbl_cloraminas) + "\n" + spingApplication.getSres_23().toLowerCase());
            c1 = new PdfPCell(new Phrase(spingApplication.getSs_23()));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorder(PdfPCell.NO_BORDER);
            c1.setCellEvent(new DottedCell(PdfPCell.RIGHT ));
            table.addCell(c1);
            // table.addCell(spingApplication.getSs_23());
            table.addCell(getResources().getString(R.string.lbl_conideal_cloramidas));
        }else {
            //TODO BROMO
            table.addCell(getResources().getString(R.string.lbl_bromo) + "\n" + spingApplication.getSres_27().toLowerCase());
            c1 = new PdfPCell(new Phrase(spingApplication.getSs_27()));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBorder(PdfPCell.NO_BORDER);
            c1.setCellEvent(new DottedCell(PdfPCell.RIGHT ));
            table.addCell(c1);
            // table.addCell(spingApplication.getSs_27());
            table.addCell(getResources().getString(R.string.lbl_conideal_bromo));
        }

        table.addCell(getResources().getString(R.string.lbl_turbidez) + "\n" + spingApplication.getSres_24().toLowerCase());
        c1 = new PdfPCell(new Phrase(spingApplication.getSs_24()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setCellEvent(new DottedCell(PdfPCell.RIGHT ));
        table.addCell(c1);
        //table.addCell(spingApplication.getSs_24());
        table.addCell(getResources().getString(R.string.lbl_conideal_turbidez));

        table.addCell(getResources().getString(R.string.lbl_metales)+ "\n" + spingApplication.getSres_25().toLowerCase());
        c1 = new PdfPCell(new Phrase(spingApplication.getSs_25()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setCellEvent(new DottedCell(PdfPCell.RIGHT ));
        table.addCell(c1);
        // table.addCell(spingApplication.getSs_25());
        table.addCell(getResources().getString(R.string.lbl_conideal_metales) );

        table.addCell(getResources().getString(R.string.lbl_cya) + "\n" + spingApplication.getSres_26().toLowerCase());
        c1 = new PdfPCell(new Phrase(spingApplication.getSs_26()));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setBorder(PdfPCell.NO_BORDER);
        c1.setCellEvent(new DottedCell(PdfPCell.RIGHT ));
        table.addCell(c1);
        //  table.addCell(spingApplication.getSs_26());
        table.addCell(getResources().getString(R.string.lbl_conideal_cya));

        document.add(table);
    }


    private void addMantenimiento(Document document, Font titleFont) throws DocumentException {
        Log.d(TAG,"addMantenimiento");
        Paragraph mantenimiento = new Paragraph("\nMantenimiento\n\n");
        mantenimiento.setAlignment(Paragraph.ALIGN_CENTER);
        mantenimiento.setFont(titleFont);
        document.add(mantenimiento);
    }

    class DottedCell implements PdfPCellEvent {
        private int border = 0;
        public DottedCell(int border) {
            this.border = border;
        }
        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            canvas.saveState();
            //    canvas.setLineDash(0, 1);
            if ((border & PdfPCell.TOP) == PdfPCell.TOP) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getTop());
            }
            if ((border & PdfPCell.BOTTOM) == PdfPCell.BOTTOM) {
                canvas.moveTo(position.getRight(), position.getBottom());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }
            if ((border & PdfPCell.RIGHT) == PdfPCell.RIGHT) {
                canvas.moveTo(position.getRight(), position.getTop());
                canvas.lineTo(position.getRight(), position.getBottom());
            }
            if ((border & PdfPCell.LEFT) == PdfPCell.LEFT) {
                canvas.moveTo(position.getLeft(), position.getTop());
                canvas.lineTo(position.getLeft(), position.getBottom());
            }
            canvas.stroke();
            canvas.restoreState();
        }
    }

}



