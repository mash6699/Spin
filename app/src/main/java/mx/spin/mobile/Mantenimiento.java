package mx.spin.mobile;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
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
import mx.spin.mobile.utils.constants.Constants;

/**
 * Created by miguel_angel on 18/05/2016.
 */
public class Mantenimiento extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static String TAG = Mantenimiento.class.getName();
    private static SpingApplication spingApplication = SpingApplication.getInstance();
    private BoussinesSpin boussinesSpin;
    private static final int REQUEST_PERMISSIONS = 16;
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
                startActivity(new Intent(Mantenimiento.this, DrawerActivity.class).putExtra("showFragment", 2));

            }
        });


        setValues();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
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
            String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date);
            String fileName = FILE_PATH + "/"+ FILE_FOLDER  +  "/"+ timeStamp + ".pdf";

            File myFile = new File(fileName);
            myFile.createNewFile();
            OutputStream output = new FileOutputStream(myFile);

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, output);
            writer.setLinearPageMode();
            writer.setFullCompression();

              /* Create Set Font and its Size */
            Font titleFont= new Font(Font.FontFamily.HELVETICA, Font.BOLD);
            titleFont.setSize(16);

            Font fontContent= new Font(Font.FontFamily.HELVETICA);
            fontContent.setSize(12);

            //TODO document header attributes
            document.addAuthor("Spin mobile");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("www.spingrupo.com");

            document.addTitle("Sping Reporte");
            document.setPageSize(PageSize.A4);
            document.setMargins(36, 36, 36, 36);
            document.setMarginMirroring(true);

            //TODO open document
            document.open();


            headerDocument(document, titleFont);
            addInfoPool(document,fontContent);
            addTableBalance(document, titleFont);
            addTableDesinfeccion(document, titleFont);
            addMantenimiento(document, titleFont);



            //Close the document
            document.close();

          //  Toast.makeText(getApplicationContext(),  , Toast.LENGTH_LONG).show();

            Snackbar.make( mView.getRootView(),"El PDF se creo correctamente se encuentra en la carpeta " + FILE_FOLDER, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();



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


    public void headerDocument(Document document, Font titleFont) throws DocumentException{
        Log.d(TAG, "headerDocument");
        Paragraph header = new Paragraph("Resultado del Análisis de la Piscina\n\n");
        header.setAlignment(Paragraph.ALIGN_CENTER);
        header.setFont(titleFont);
        document.add(header);

    }

    public void addInfoPool(Document document, Font fontContent) throws DocumentException{
        Log.d(TAG, "addInfoPool");
        Paragraph iPiscina = new Paragraph(getResources().getString(R.string.lbl_nombre_piscina) +" : " + "Nueva" +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_fecha_analisis)      + " : " + new Date().getTime() +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_volumen)             + " : "  +"7000" +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_unidad_volumen)      + " : " +"m3" +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_piscina)             + " : " + "Piscina: \n");
        iPiscina.add(getResources().getString(R.string.lbl_tipo_instalacion)    + " : " +"" +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_tiempo_rotacion)     + " : " +"" +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_velocidad_flujo)     + " : " +"" +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_uso_piscina)         + " : " +"" +"\n");
        iPiscina.add(getResources().getString(R.string.lbl_equipo)              + " : " + "" +"\n");
        iPiscina.add("" +"" +"\n");
        iPiscina.add("" +"" +"\n");

        iPiscina.setAlignment(Paragraph.ALIGN_LEFT);
        iPiscina.setFont(fontContent);

        document.add(iPiscina);
    }


    private void addTableBalance(Document document, Font titleFont) throws DocumentException {
        Log.d(TAG, "addTableBalance");
        Paragraph iBalance = new Paragraph("Resultados Balance\n\n");
        iBalance.setAlignment(Paragraph.ALIGN_CENTER);
        iBalance.setFont(titleFont);
        document.add(iBalance);

        PdfPTable table = new PdfPTable(3);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell c1 = new PdfPCell(new Phrase(""));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResources().getString(R.string.lbl_conactual)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResources().getString(R.string.lbl_conideal)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell(getResources().getString(R.string.lbl_ph) + "\n" + "Estatus");
        table.addCell("1.1");
        table.addCell(getResources().getString(R.string.lbl_conideal_ph));

        table.addCell(getResources().getString(R.string.lbl_alcalinidad) + "\n" + "Estatus");
        table.addCell("2.2");
        table.addCell(getResources().getString(R.string.lbl_conideal_alcalinidad));

        table.addCell(getResources().getString(R.string.lbl_std) + "\n" + "Estatus");
        table.addCell("1.1");
        table.addCell(getResources().getString(R.string.lbl_conideal_std));

        table.addCell(getResources().getString(R.string.lbl_dureza) + "\n" + "Estatus");
        table.addCell("1.1");
        table.addCell(getResources().getString(R.string.lbl_conideal_dureza));

        table.addCell(getResources().getString(R.string.lbl_temperatura) + "\n" + "Estatus");
        table.addCell("1.1");
        table.addCell(getResources().getString(R.string.lbl_conideal_temperatura));

        document.add(table);
    }

    private void addTableDesinfeccion(Document document, Font titleFont) throws DocumentException {
        Log.d(TAG, "addTableDesinfeccion");
        Paragraph iDesinfeccion = new Paragraph("Resultados Desinfección\n\n");
        iDesinfeccion.setAlignment(Paragraph.ALIGN_CENTER);
        iDesinfeccion.setFont(titleFont);
        document.add(iDesinfeccion);

        PdfPTable table = new PdfPTable(3);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell c1 = new PdfPCell(new Phrase(""));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResources().getString(R.string.lbl_conactual)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(getResources().getString(R.string.lbl_conideal)));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        //TODO CLORO LIBRE
        //TODO CLORMINAS
        table.addCell(getResources().getString(R.string.lbl_cloro_total) + "\n" + "Estatus" );
        table.addCell("1.1");
        table.addCell(getResources().getString(R.string.lbl_conideal_cloro_total));

        table.addCell(getResources().getString(R.string.lbl_cloraminas) + "\n" + "Estatus");
        table.addCell("1.1");
        table.addCell(getResources().getString(R.string.lbl_conideal_cloramidas));

        //TODO BROMO
        table.addCell(getResources().getString(R.string.lbl_bromo) + "\n" + "Estatus");
        table.addCell("1.1");
        table.addCell(getResources().getString(R.string.lbl_conideal_bromo));


        table.addCell(getResources().getString(R.string.lbl_turbidez) + "\n" + "Estatus");
        table.addCell("1.1");
        table.addCell(getResources().getString(R.string.lbl_conideal_turbidez));

        table.addCell(getResources().getString(R.string.lbl_metales)+ "\n" + "Estatus");
        table.addCell("1.1");
        table.addCell(getResources().getString(R.string.lbl_conideal_metales) );

        table.addCell(getResources().getString(R.string.lbl_cya) + "\n" + "Estatus");
        table.addCell("1.1");
        table.addCell(getResources().getString(R.string.lbl_conideal_cya));

        document.add(table);
    }


    private void addMantenimiento(Document document, Font titleFont) throws DocumentException {
        Paragraph mantenimiento = new Paragraph("Mantenimiento\n\n");
        mantenimiento.setAlignment(Paragraph.ALIGN_CENTER);
        mantenimiento.setFont(titleFont);
        document.add(mantenimiento);
    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    /**
     * To read a file from assets folder
     * @param fileName
     */
    public String readFromFile(String fileName) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            // File myFile = new File(Environment.getExternalStorageDirectory()+ File.separator + fileName);
            fIn = getResources().getAssets()
                    .open(fileName, Context.MODE_PRIVATE);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }
}



