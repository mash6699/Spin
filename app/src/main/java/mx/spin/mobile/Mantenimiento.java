package mx.spin.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.utils.constants.Constants;

/**
 * Created by miguelangel on 18/05/2016.
 */
public class Mantenimiento extends AppCompatActivity {

    private static String TAG = Mantenimiento.class.getName();
    private static SpingApplication spingApplication = SpingApplication.getInstance();
    private BoussinesSpin boussinesSpin;
    private Pool piscina;

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
    static double cloroLibre;
    static double cloraminas;
    static double bromo;
    static double dureza;
    static double std;


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mantenimiento);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boussinesSpin = new BoussinesSpin(this);

        txt_titleToolbar.setText(R.string.title_activity_mantenimiento);
        pool_name.setText(" " + spingApplication.getName());
        pool_date.setText(" " +spingApplication.getDate());

        int idPiscina = spingApplication.getIdPiscina();
        piscina = boussinesSpin.getPool(idPiscina);

        setValues();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    void setValues(){
        try{
            if(piscina != null){
                Log.d(TAG, "setValues");

                volumen     = Double.parseDouble(piscina.getPool_volume());
                um          = Integer.parseInt(piscina.getPool_um());

                ph           = Double.parseDouble(spingApplication.getFs_11());
                alcalinidad  = Double.parseDouble(spingApplication.getFs_12());

                dureza      = Double.parseDouble(spingApplication.getFs_13());
                std         = Double.parseDouble(spingApplication.getFs_15());

                cya         = Double.parseDouble(spingApplication.getSs_26());

                cloroLibre    = Double.parseDouble(spingApplication.getSs_22());
                cloraminas    = Double.parseDouble(spingApplication.getSs_23());
                bromo         = Double.parseDouble(spingApplication.getSs_27());

                turbidez    = Double.parseDouble(spingApplication.getSs_24());
                metales     = spingApplication.getSs_25();

                //ABIERTA = 1
                instalacion = Integer.parseInt(piscina.getPool_category());

            }
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
                    return "PRIMER AJUSTE";
                case 1:
                    return "SEGUNDO AJUSTE";
                case 2:
                    return "TERCER AJUSTE";
                case 3:
                    return  "CUARTO AJUSTE";
            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        private View rootView;

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
        @Bind(R.id.txt_metales_rec)
        TextView turbidez_rec;


        @Nullable
        @Bind(R.id.txt_dureza_con)
        TextView dure_con;
        @Nullable
        @Bind(R.id.txt_dureza_rec)
        TextView dure_rec;
        @Nullable
        @Bind(R.id.txt_std_con)
        TextView std_con;
        @Nullable
        @Bind(R.id.txt_std_rec)
        TextView std_rec;



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

            int position = getArguments().getInt(ARG_SECTION_NUMBER);

            switch (position){
                case 1:
                    viewPrimero.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    viewSegundo.setVisibility(View.VISIBLE);
                    turbidezRec();
                    break;
                case 3:
                    viewTercero.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    viewCuaro.setVisibility(View.VISIBLE);
                    durezaCondicional();
                    stdCondicional();
                    break;
            }

            return rootView;
        }

        protected void turbidezRec(){
            //Positivo
            if(turbidez < 0.5 && metales.equals("Negativo")){
                turbidez_rec.setText(getString(R.string.turb_rec_20));
            }else if(turbidez > 0.5d && metales.equals("Negativo")){
                turbidez_rec.setText(getString(R.string.turb_rec_21,turbidezCal(1)));
            }else if(turbidez > 0.5d && metales.equals("Positivo")){
                turbidez_rec.setText(getString(R.string.turb_rec_21,turbidezCal(1)));
            }else if(turbidez < 0.5 && metales.equals("Positivo")){
                turbidez_rec.setText(getString(R.string.turb_rec_22,turbidezCal(2)));
            }
        }


        //TODO CUARTO AJUSTE
       protected void durezaCondicional(){
            try{
                Log.d(TAG ,"durezaFA val [" + dureza + " ]" );
                if(dureza < 150d){
                    dure_con.setText(Html.fromHtml(getString(R.string.dureza_cond_40)));
                    dure_rec.setText(Html.fromHtml(getString(R.string.dureza_rec_40, dosificacionDur())));
                }else if(dureza >= 150d || dureza <= 250d){
                    dure_con.setText(Html.fromHtml(getString(R.string.dureza_cond_41)));
                    dure_rec.setText(Html.fromHtml(getString(R.string.dureza_rec_41)));
                }else if (dureza > 250d){
                    dure_con.setText(Html.fromHtml(getString(R.string.dureza_cond_42)));
                    dure_rec.setText(Html.fromHtml(getString(R.string.dureza_rec_42)));
                }
            }catch (Exception ex){
                Log.e(TAG, ex.getMessage());
                dure_con.setText("ups");
                dure_rec.setText("ups");
            }
        }

        protected void stdCondicional(){
          if(std >= 2500d){
              std_con.setText(getString(R.string.std_cond_43));
              std_rec.setText(getString(R.string.std_rec_43));
          }else if( std < 2500d){
              std_con.setText(getString(R.string.std_cond_44));
              std_rec.setText(getString(R.string.std_rec_44));
          }
        }


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


    }

}



