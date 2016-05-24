package mx.spin.mobile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.Toast;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.os.Environment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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



    private static final String FILE_FOLDER = "Skholingua";
    private static File file;
    private static final String filepath = Environment.getExternalStorageDirectory().getPath();
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

        setValues();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
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
        file = new File(filepath, FILE_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }

    }

@OnClick(R.id.button)
    public void onClickPdf(View view){
        createPdf();
    }

    private void createPdf()  {
        try {
            getFile();
            //Create time stamp
            Date date = new Date ();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date);
            File myFile = new File(file.getAbsolutePath()+ File.separator + timeStamp + ".pdf");
            myFile.createNewFile();
            OutputStream output = new FileOutputStream(myFile);

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, output);
            writer.setLinearPageMode();
            writer.setFullCompression();
            // document header attributes
            document.addAuthor("Skholingua");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("www.skholingua.com");
            document.addTitle("Skholingua");
            document.setPageSize(PageSize.A4);
            // left,right,top,bottom
            document.setMargins(36, 36, 36, 36);
            document.setMarginMirroring(true);
            // open document
            document.open();

            //Add content
       /*     if(isPDFFromHTML) {
                //A helper class for parsing XHTML/CSS or XML flow to PDF.
                XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                        new ByteArrayInputStream(readFromFile("index.html").getBytes("UTF-8")),
                        new ByteArrayInputStream(readFromFile("style.css").getBytes("UTF-8")));
            }
            else {*/
                /* Create Paragraph and Set Font */
                Paragraph p1 = new Paragraph("Skholingua Tutorial\nLearn how to create a PDF in android with image and dynamic text form User.");

                /* Create Set Font and its Size */
                Font paraFont= new Font(Font.FontFamily.HELVETICA);
                paraFont.setSize(16);
                p1.setAlignment(Paragraph.ALIGN_CENTER);
                p1.setFont(paraFont);

                //add paragraph to document
                document.add(p1);

                Paragraph p2 = new Paragraph("hola");//_pdfBodyEDT.getText().toString().trim()

                /* You can also SET FONT and SIZE like this */
                Font paraFont2= new Font(Font.FontFamily.COURIER,14.0f, Color.GREEN);
                p2.setAlignment(Paragraph.ALIGN_CENTER);
                p2.setFont(paraFont2);

                document.add(p2);

                /* Inserting Image in PDF */
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.mipmap.ic_launcher);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
                Image myImg = Image.getInstance(stream.toByteArray());
                myImg.setAlignment(Image.MIDDLE);

                //add image to document
                document.add(myImg);

                addContent(document);

                //set footer
               /* Phrase footerText = new Phrase("This is an example of a footer");
                HeaderFooter pdfFooter = new HeaderFooter(footerText, false);
                document.(pdfFooter);*/
//            }
//
            //Close the document
            document.close();
            Toast.makeText(this, "Pdf created successfully.", Toast.LENGTH_LONG).show();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            Log.e("PDF--->",  "exception", e);
        }
    }


    private static void addContent(Document document) throws DocumentException {
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                Font.BOLD);
        Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
                Font.BOLD);
        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 2);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);

    }

    private static void createTable(Section subCatPart)
            throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
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



